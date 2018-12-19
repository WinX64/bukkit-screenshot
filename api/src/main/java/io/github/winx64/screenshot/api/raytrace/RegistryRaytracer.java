package io.github.winx64.screenshot.api.raytrace;

import io.github.winx64.screenshot.api.registry.ModelRegistry;

public interface RegistryRaytracer extends Raytracer{

    ModelRegistry getTextureRegistry();
}
