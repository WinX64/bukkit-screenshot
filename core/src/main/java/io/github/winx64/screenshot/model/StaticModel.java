package io.github.winx64.screenshot.model;

import io.github.winx64.screenshot.util.Intersection;
import io.github.winx64.screenshot.model.AbstractModel.Builder;
import org.bukkit.block.Block;

public class StaticModel implements Model {

    private final int color;
    private final double transparencyFactor;
    private final double reflectionFactor;
    private final boolean occluding;

    private StaticModel(int color, double transparencyFactor, double reflectionFactor, boolean occluding) {
        this.color = color;
        this.transparencyFactor = transparencyFactor;
        this.reflectionFactor = reflectionFactor;
        this.occluding = occluding;
    }

    @Override
    public Intersection intersect(Block block, Intersection currentIntersection) {
        return Intersection.of(currentIntersection.getNormal(), currentIntersection.getPoint(),
                currentIntersection.getDirection(), color);
    }

    @Override
    public double getTransparencyFactor() {
        return transparencyFactor;
    }

    @Override
    public double getReflectionFactor() {
        return reflectionFactor;
    }

    @Override
    public boolean isOccluding() {
        return occluding;
    }

    public static class StaticModelBuilder extends Builder {

        private final int color;

        StaticModelBuilder(int color) {
            super(new int[1][1]);

            this.color = color;
        }

        @Override
        public StaticModel build() {
            return new StaticModel(color, transparencyFactor, reflectionFactor, occluding);
        }
    }
}
