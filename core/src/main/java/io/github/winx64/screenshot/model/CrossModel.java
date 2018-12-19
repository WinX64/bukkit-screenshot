package io.github.winx64.screenshot.model;

import io.github.winx64.screenshot.api.util.Intersection;
import io.github.winx64.screenshot.api.util.MathUtil;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class CrossModel extends AbstractModel {

    private static final Vector NORMAL_ONE = new Vector(1, 0, 1).normalize();
    private static final Vector NORMAL_TWO = new Vector(-1, 0, 1).normalize();

    private static final Vector POINT_ONE = new Vector(1, 0, 0);
    private static final Vector POINT_TWO = new Vector(1, 0, 1);

    private CrossModel(int[][] texture, double transparencyFactor, double reflectionFactor,
                       boolean occluding) {
        super(texture, transparencyFactor, reflectionFactor, occluding);
    }

    @Override
    public Intersection intersect(Block block, Intersection currentIntersection) {
        Vector linePoint = currentIntersection.getPoint();
        Vector lineDirection = currentIntersection.getDirection();

        Vector blockPoint = block.getLocation().toVector();
        Vector planePoint = block.getLocation().add(0.5, 0, 0.5).toVector();

        double distance = Double.POSITIVE_INFINITY;
        int color = 0;
        Vector target = null;

        Vector intersectionOne = MathUtil.getLinePlaneIntersection(linePoint, lineDirection, planePoint, NORMAL_ONE,
                true);
        if (intersectionOne != null) {
            intersectionOne.subtract(blockPoint);
            if (isInsideBlock(intersectionOne)) {
                color = getColor(intersectionOne, POINT_ONE);
                distance = linePoint.distanceSquared(intersectionOne.add(blockPoint));
                target = intersectionOne;
            }
        }

        Vector intersectionTwo = MathUtil.getLinePlaneIntersection(linePoint, lineDirection, planePoint, NORMAL_TWO,
                true);
        if (intersectionTwo != null) {
            intersectionTwo.subtract(blockPoint);
            if (isInsideBlock(intersectionTwo)) {
                int colorTwo = getColor(intersectionTwo, POINT_TWO);
                double distanceTwo = linePoint.distanceSquared(intersectionTwo.add(blockPoint));
                if ((distanceTwo < distance && (colorTwo >> 24) != 0) || (color >> 24) == 0) {
                    target = intersectionTwo;
                    color = colorTwo;
                }
            }
        }

        if (target == null) {
            target = linePoint;
        }

        return Intersection.of(currentIntersection.getNormal(), target, lineDirection, color);
    }

    private boolean isInsideBlock(Vector vec) {
        return vec.getX() >= 0 && vec.getZ() < 1 && vec.getY() >= 0 && vec.getY() < 1 && vec.getZ() >= 0
                && vec.getZ() < 1;
    }

    private int getColor(Vector vec, Vector base) {
        double xOffset = Math.sqrt(Math.pow(vec.getX() - base.getX(), 2) + Math.pow(vec.getZ() - base.getZ(), 2));
        double yOffset = vec.getY();

        int pixelY = (int) Math.floor(yOffset * textureSize);
        int pixelX = (int) Math.floor(xOffset / Math.sqrt(2) * textureSize);

        return texture[pixelY][pixelX];
    }

    public static class CrossModelBuilder extends Builder {

        CrossModelBuilder(int[][] texture) {
            super(texture);
        }

        @Override
        public CrossModel build() {
            return new CrossModel(texture, transparencyFactor, reflectionFactor, occluding);
        }
    }
}
