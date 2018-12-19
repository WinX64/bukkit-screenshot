package io.github.winx64.screenshot.api.renderer;

import io.github.winx64.screenshot.api.raytrace.Raytracer;

public abstract class AbstractRaytraceRenderer implements RaytraceRenderer {

    protected final Raytracer raytracer;

    public AbstractRaytraceRenderer(Raytracer raytracer) {
        this.raytracer = raytracer;
    }

    @Override
    public Raytracer getRaytracer() {
        return raytracer;
    }
}
