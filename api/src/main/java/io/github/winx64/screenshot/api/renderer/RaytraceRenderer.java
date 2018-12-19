package io.github.winx64.screenshot.api.renderer;

import io.github.winx64.screenshot.api.raytrace.Raytracer;

public interface RaytraceRenderer extends Renderer {

    Raytracer getRaytracer();
}
