package io.github.winx64.screenshot.model;

import com.google.common.base.Preconditions;
import io.github.winx64.screenshot.api.model.Model;
import io.github.winx64.screenshot.model.CrossModel.CrossModelBuilder;
import io.github.winx64.screenshot.model.MultiModel.MultiModelBuilder;
import io.github.winx64.screenshot.model.OctahedronModel.OctahedronModelBuilder;
import io.github.winx64.screenshot.model.SimpleModel.SimpleModelBuilder;
import io.github.winx64.screenshot.model.SphereModel.SphereModelBuilder;
import io.github.winx64.screenshot.model.StaticModel.StaticModelBuilder;

public abstract class AbstractModel implements Model {

    final int textureSize;
    final int[][] texture;

    private final double transparencyFactor;
    private final double reflectionFactor;
    private final boolean occluding;

    AbstractModel(int[][] texture, double transparencyFactor, double reflectionFactor,
                  boolean occluding) {
        Preconditions.checkNotNull(texture);
        Preconditions.checkArgument(texture.length > 0, "texture cannot be empty");
        Preconditions.checkArgument(texture.length == texture[0].length, "texture must be a square array");

        this.textureSize = texture.length;
        this.texture = texture;

        this.transparencyFactor = transparencyFactor;
        this.reflectionFactor = reflectionFactor;
        this.occluding = occluding;
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

    public static abstract class Builder {

        final int[][] texture;

        double transparencyFactor;
        double reflectionFactor;
        boolean occluding;

        Builder(int[][] texture) {
            this.texture = texture;

            this.transparencyFactor = 0;
            this.reflectionFactor = 0;
            this.occluding = false;
        }

        public static SimpleModelBuilder createSimple(int[][] texture) {
            return new SimpleModelBuilder(texture);
        }

        public static MultiModelBuilder createMulti(int[][] topTexture, int[][] sideTexture,
                                                    int[][] bottomTexture) {
            return new MultiModelBuilder(topTexture, sideTexture, bottomTexture);
        }

        public static StaticModelBuilder createStatic(int color) {
            return new StaticModelBuilder(color);
        }

        public static CrossModelBuilder createCross(int[][] texture) {
            return new CrossModelBuilder(texture);
        }

        public static SphereModelBuilder createSphere(int[][] texture) {
            return new SphereModelBuilder(texture);
        }

        public static OctahedronModelBuilder createOctahedron(int[][] texture) {
            return new OctahedronModelBuilder(texture);
        }

        public Builder transparency(double transparencyFactor) {
            this.transparencyFactor = transparencyFactor;
            return this;
        }

        public Builder reflection(double reflectionFactor) {
            this.reflectionFactor = reflectionFactor;
            return this;
        }

        public Builder occlusion() {
            this.occluding = true;
            return this;
        }

        public abstract Model build();
    }
}
