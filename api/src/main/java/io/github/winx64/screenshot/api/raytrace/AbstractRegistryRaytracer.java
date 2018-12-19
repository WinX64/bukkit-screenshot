package io.github.winx64.screenshot.api.raytrace;

import io.github.winx64.screenshot.api.registry.ModelRegistry;

public abstract class AbstractRegistryRaytracer implements RegistryRaytracer {

    protected final ModelRegistry textureRegistry;

    public AbstractRegistryRaytracer(ModelRegistry textureRegistry) {
        this.textureRegistry = textureRegistry;
    }

    @Override
    public ModelRegistry getTextureRegistry() {
        return textureRegistry;
    }
}
