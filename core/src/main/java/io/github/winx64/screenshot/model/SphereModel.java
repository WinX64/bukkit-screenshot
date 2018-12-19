package io.github.winx64.screenshot.model;

import io.github.winx64.screenshot.api.model.Model;
import io.github.winx64.screenshot.api.util.Intersection;
import io.github.winx64.screenshot.api.util.MathUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class SphereModel extends AbstractModel {

    private final double radius;
    private final Vector offset;

    private SphereModel(int[][] texture, double transparencyFactor, double reflectionFactor,
                        boolean occluding, double radius, Vector offset) {
        super(texture, transparencyFactor, reflectionFactor, occluding);

        this.radius = radius;
        this.offset = offset;
    }

    @Override
    public Intersection intersect(Block block, Intersection currentIntersection) {
        Vector linePoint = currentIntersection.getPoint();
        Vector lineDirection = currentIntersection.getDirection();
        Vector blockPoint = block.getLocation().toVector();
        Vector centerPoint = block.getLocation().add(0.5, 0.5, 0.5).add(offset).toVector();

        double a = lineDirection.dot(lineDirection);
        double b = 2 * (linePoint.dot(lineDirection) - centerPoint.dot(lineDirection));
        double c = linePoint.dot(linePoint) - 2 * centerPoint.dot(linePoint) + centerPoint.dot(centerPoint)
                - Math.pow(radius, 2);

        double delta = Math.pow(b, 2) - 4 * a * c;
        if (delta < 0) {
            return Intersection.of(currentIntersection.getNormal(), linePoint, lineDirection);
        }

        double dist = linePoint.distance(centerPoint);
        double minDist = dist - radius;
        double maxDist = dist + radius;

        if (delta == 0) {
            double t = -b / (2 * a);
            Vector intersection = lineDirection.clone().add(lineDirection.clone().multiply(t));
            if (!isInsideBlock(blockPoint, intersection)) {
                return currentIntersection;
            }
            double currentDist = intersection.distance(linePoint);
            double factor = (currentDist - minDist) / (maxDist - minDist);
            Vector normal = intersection.clone().subtract(centerPoint).normalize();
            return Intersection.of(normal, intersection, lineDirection, getColor(centerPoint, intersection, factor));
        }

        double deltaSqrt = Math.sqrt(delta);

        double tOne = (-b + deltaSqrt) / (2 * a);
        double tTwo = (-b - deltaSqrt) / (2 * a);

        Vector intersectionOne = linePoint.clone().add(lineDirection.clone().multiply(tOne));
        Vector intersectionTwo = linePoint.clone().add(lineDirection.clone().multiply(tTwo));

        boolean first = intersectionOne.distanceSquared(linePoint) < intersectionTwo.distanceSquared(linePoint);
        double currentDist = (first ? intersectionOne : intersectionTwo).distance(linePoint);
        double factor = (currentDist - minDist) / (maxDist - minDist);
        if (first && isInsideBlock(blockPoint, intersectionOne)) {
            Vector normal = intersectionOne.clone().subtract(centerPoint).normalize();
            return Intersection.of(normal, intersectionOne, lineDirection,
                    getColor(centerPoint, intersectionOne, factor));
        } else if (isInsideBlock(blockPoint, intersectionTwo)) {
            Vector normal = intersectionTwo.clone().subtract(centerPoint).normalize();
            return Intersection.of(normal, intersectionTwo, lineDirection,
                    getColor(centerPoint, intersectionTwo, factor));
        } else {
            return currentIntersection;
        }
    }

    private int getColor(Vector base, Vector intersection, double factor) {
        Location loc = base.toLocation(null);
        loc.setDirection(intersection.clone().subtract(base).normalize());

        double perimeter = Math.round(2 * Math.PI * radius);
        double yawDiv = 360 / perimeter;
        double pitchDiv = 180 / perimeter;

        int pixelX = (int) ((loc.getYaw() % yawDiv) / (yawDiv / textureSize));
        int pixelY = (int) (((loc.getPitch() + 90) % pitchDiv) / (pitchDiv / textureSize));

        return 0xFF000000 | MathUtil.weightedColorSum(texture[pixelY][pixelX], 0, 1 - factor, factor);
    }

    private boolean isInsideBlock(Vector blockPoint, Vector intersection) {
        intersection = intersection.clone().subtract(blockPoint);

        return intersection.getX() >= 0 && intersection.getX() < 1 && intersection.getY() >= 0
                && intersection.getY() < 1 && intersection.getZ() >= 0 && intersection.getZ() < 1;
    }

    public static class SphereModelBuilder extends Builder {

        private double radius;
        private Vector offset;

        SphereModelBuilder(int[][] texture) {
            super(texture);

            this.radius = 0.5;
            this.offset = new Vector();
        }

        public SphereModelBuilder radius(double radius) {
            this.radius = radius;
            return this;
        }

        public SphereModelBuilder offset(Vector offset) {
            this.offset = offset.clone();
            return this;
        }

        @Override
        public Model build() {
            return new SphereModel(texture, transparencyFactor, reflectionFactor, occluding, radius,
                    offset);
        }
    }
}
