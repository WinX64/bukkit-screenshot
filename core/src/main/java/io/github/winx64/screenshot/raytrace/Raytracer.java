package io.github.winx64.screenshot.raytrace;

import org.bukkit.World;
import org.bukkit.util.Vector;

public interface Raytracer {

	int trace(World world, Vector point, Vector direction);
}
