package io.github.winx64.screenshot.api.model;

import org.bukkit.block.Block;

import io.github.winx64.screenshot.api.util.Intersection;

public interface Model {

	Intersection intersect(Block block, Intersection currentIntersection);

	double getTransparencyFactor();

	double getReflectionFactor();

	boolean isOccluding();
}
