package io.github.winx64.screenshot.model;

import io.github.winx64.screenshot.api.model.Model;
import io.github.winx64.screenshot.api.util.Intersection;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class SimpleModel extends AbstractModel {

    static final Vector UP = new Vector(0, 1, 0);
    static final Vector DOWN = new Vector(0, -1, 0);
    private static final Vector NORTH = new Vector(0, 0, -1);
    private static final Vector SOUTH = new Vector(0, 0, 1);
    private static final Vector EAST = new Vector(1, 0, 0);
    private static final Vector WEST = new Vector(-1, 0, 0);

    SimpleModel(int[][] texture, double transparencyFactor, double reflectionFactor,
                boolean occluding) {
        super(texture, transparencyFactor, reflectionFactor, occluding);
    }

    @Override
    public Intersection intersect(Block block, Intersection currentIntersection) {
        double yOffset;
        double xOffset;

        Vector normal = currentIntersection.getNormal();
        Vector point = currentIntersection.getPoint();
        Vector direction = currentIntersection.getDirection();

        if (normal.equals(NORTH) || normal.equals(SOUTH)) {
            yOffset = point.getY() - (int) point.getY();
            xOffset = point.getX() - (int) point.getX();
        } else if (normal.equals(EAST) || normal.equals(WEST)) {
            yOffset = point.getY() - (int) point.getY();
            xOffset = point.getZ() - (int) point.getZ();
        } else {
            yOffset = point.getX() - (int) point.getX();
            xOffset = point.getZ() - (int) point.getZ();
        }

        int pixelY = (int) Math.floor((yOffset < 0 ? yOffset + 1 : yOffset) * textureSize);
        int pixelX = (int) Math.floor((xOffset < 0 ? xOffset + 1 : xOffset) * textureSize);

        return Intersection.of(normal, point, direction, texture[pixelY][pixelX]);
    }

    public static class SimpleModelBuilder extends Builder {

        protected SimpleModelBuilder(int[][] texture) {
            super(texture);
        }

        @Override
        public Model build() {
            return new SimpleModel(texture, transparencyFactor, reflectionFactor, occluding);
        }
    }
}
