package io.github.winx64.screenshot.raytrace;

import io.github.winx64.screenshot.api.model.Model;
import io.github.winx64.screenshot.api.raytrace.AbstractRegistryRaytracer;
import io.github.winx64.screenshot.api.util.Intersection;
import io.github.winx64.screenshot.api.util.MathUtil;
import io.github.winx64.screenshot.registry.DefaultModelRegistry;
import io.github.winx64.screenshot.util.BlockRaytracer;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

public class DefaultRaytracer extends AbstractRegistryRaytracer {

    private static final int MAX_DISTANCE = 300;
    private static final int REFLECTION_DEPTH = 10;

    private Block reflectedBlock;

    public DefaultRaytracer() {
        super(new DefaultModelRegistry());
        this.textureRegistry.initialize();

        this.reflectedBlock = null;
    }

    @Override
    public int trace(World world, Vector point, Vector direction) {
        return trace(world, point, direction, REFLECTION_DEPTH);
    }

    private int trace(World world, Vector point, Vector direction, int reflectionDepth) {
        Location loc = point.toLocation(world);
        loc.setDirection(direction);
        BlockRaytracer iterator = new BlockRaytracer(loc);
        int baseColor = Color.fromRGB(65, 89, 252).asRGB();
        Vector finalIntersection = null;

        int reflectionColor = 0;
        double reflectionFactor = 0;
        boolean reflected = false;

        Vector transparencyStart = null;
        int transparencyColor = 0;
        double transparencyFactor = 0;

        Material occlusionMaterial = null;
        BlockData occlusionData = null;

        for (int i = 0; i < MAX_DISTANCE; i++) {
            if (!iterator.hasNext()) {
                break;
            }

            Block block = iterator.next();
            if (block == null) {
                continue;
            }

            if (reflectedBlock != null && reflectedBlock.equals(block)) {
                continue;
            }
            reflectedBlock = null;

            Material material = block.getType();
            if (material == Material.AIR) {
                occlusionMaterial = null;
                occlusionData = null;
                continue;
            }

            Model textureModel = textureRegistry.getModel(block);
            Intersection currentIntersection = Intersection.of(MathUtil.toVector(iterator.getIntersectionFace()),
                    i == 0 ? point : iterator.getIntersectionPoint(), direction);
            Intersection newIntersection = textureModel.intersect(block, currentIntersection);

            if (newIntersection == null) {
                continue;
            }

            int color = newIntersection.getColor();

            if (!reflected && textureModel.getReflectionFactor() > 0 && reflectionDepth > 0 && (color >> 24) != 0) {
                reflectedBlock = block;
                reflectionColor = trace(world, newIntersection.getPoint(), MathUtil.reflectVector(point, direction,
                        newIntersection.getPoint(), newIntersection.getNormal()), reflectionDepth - 1);
                reflectionFactor = textureModel.getReflectionFactor();
                reflected = true;
            }

            if (transparencyStart == null && textureModel.getTransparencyFactor() > 0) {
                transparencyStart = newIntersection.getPoint();
                transparencyColor = newIntersection.getColor();
                transparencyFactor = textureModel.getTransparencyFactor();
            }

            if (textureModel.isOccluding()) {
                BlockData data = block.getBlockData();

                if (material == occlusionMaterial && data.equals(occlusionData)) {
                    continue;
                }

                occlusionMaterial = material;
                occlusionData = data;
            } else {
                occlusionMaterial = null;
                occlusionData = null;
            }

            if (transparencyStart != null && textureModel.getTransparencyFactor() > 0) {
                continue;
            }

            if ((color >> 24) == 0) {
                continue;
            }

            baseColor = color;
            finalIntersection = newIntersection.getPoint();
            break;
        }

        if (transparencyStart != null) {
            baseColor = MathUtil.weightedColorSum(baseColor, transparencyColor, transparencyFactor, (1
                    - transparencyFactor)
                    * (1 + transparencyStart.distance(finalIntersection == null ? transparencyStart : finalIntersection)
                    / 5.0));
        }
        if (reflected) {
            baseColor = MathUtil.weightedColorSum(baseColor, reflectionColor, 1 - reflectionFactor, reflectionFactor);
        }

        return baseColor & 0xFFFFFF;
    }
}
