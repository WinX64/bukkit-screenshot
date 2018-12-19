package io.github.winx64.screenshot.model;

import io.github.winx64.screenshot.api.model.Model;
import io.github.winx64.screenshot.api.util.Intersection;
import io.github.winx64.screenshot.api.util.MathUtil;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class OctahedronModel extends AbstractModel {

    private static final double RADIUS = 0.5;

    private static final Vector[] NORMALS = new Vector[]{new Vector(-1, -1, -1), new Vector(-1, -1, 1),
            new Vector(-1, 1, -1), new Vector(-1, 1, 1), new Vector(1, -1, -1), new Vector(1, -1, 1),
            new Vector(1, 1, -1), new Vector(1, 1, 1)};

    private OctahedronModel(int[][] texture, double transparencyFactor, double reflectionFactor,
                            boolean occluding) {
        super(texture, transparencyFactor, reflectionFactor, occluding);
    }

    @Override
    public Intersection intersect(Block block, Intersection currentIntersection) {
        Vector linePoint = currentIntersection.getPoint();
        Vector lineDirection = currentIntersection.getDirection();
        Vector blockPoint = block.getLocation().toVector();
        Vector centerPoint = blockPoint.clone().add(new Vector(0.5, 0.5, 0.5));

        Vector lastIntersection = null;
        double lastDistance = Double.POSITIVE_INFINITY;
        for (int i = 0; i < 8; i++) {
            Vector planePoint = new Vector(i < 4 ? -0.5 : 0.5, 0, 0).add(centerPoint);
            Vector planeNormal = NORMALS[i];

            Vector intersection = MathUtil.getLinePlaneIntersection(linePoint, lineDirection, planePoint, planeNormal,
                    false);
            if (intersection == null) {
                continue;
            }

            if (!isInsideBlock(blockPoint, planeNormal, intersection)) {
                continue;
            }

            double distance = intersection.distance(linePoint);
            if (distance < lastDistance) {
                lastIntersection = intersection;
                lastDistance = distance;
            }
        }

        if (lastIntersection == null) {
            return currentIntersection;
        }

        double dist = linePoint.distance(centerPoint);
        double minDist = dist - RADIUS;
        double maxDist = dist + RADIUS;
        double factor = (lastDistance - minDist) / (maxDist - minDist);

        double yOffset = lastIntersection.getX() - (int) lastIntersection.getX();
        double xOffset = lastIntersection.getZ() - (int) lastIntersection.getZ();

        int pixelY = (int) Math.floor((yOffset < 0 ? yOffset + 1 : yOffset) * textureSize);
        int pixelX = (int) Math.floor((xOffset < 0 ? xOffset + 1 : xOffset) * textureSize);

        return Intersection.of(currentIntersection.getNormal(), lastIntersection, lineDirection,
                0xFF000000 | MathUtil.weightedColorSum(texture[pixelY][pixelX], 0, 1 - factor, factor));
    }

    private boolean isInsideBlock(Vector blockPoint, Vector planeNormal, Vector intersection) {
        intersection = intersection.clone().subtract(blockPoint);

        if (intersection.getX() < 0 || intersection.getX() >= 1 || intersection.getY() < 0 || intersection.getY() >= 1
                || intersection.getZ() < 0 || intersection.getZ() >= 1) {
            return false;
        }

        boolean posX = planeNormal.getX() >= 0;
        boolean posY = planeNormal.getY() >= 0;
        boolean posZ = planeNormal.getZ() >= 0;

        boolean blockX = intersection.getX() >= 0.5;
        boolean blockY = intersection.getY() >= 0.5;
        boolean blockZ = intersection.getZ() >= 0.5;

        return posX == blockX && posY == blockY && posZ == blockZ;
    }

    public static class OctahedronModelBuilder extends Builder {

        OctahedronModelBuilder(int[][] texture) {
            super(texture);
        }

        @Override
        public Model build() {
            return new OctahedronModel(texture, transparencyFactor, reflectionFactor, occluding);
        }
    }
}
