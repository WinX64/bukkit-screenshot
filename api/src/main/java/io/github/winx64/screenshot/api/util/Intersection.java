package io.github.winx64.screenshot.api.util;

import org.bukkit.util.Vector;

public final class Intersection {

	private final Vector normal;
	private final Vector point;
	private final Vector direction;
	private final int color;

	private Intersection(Vector normal, Vector point, Vector direction, int color) {
		this.normal = normal;
		this.point = point;
		this.direction = direction;
		this.color = color;
	}

	public Vector getNormal() {
		return normal;
	}

	public Vector getPoint() {
		return point;
	}

	public Vector getDirection() {
		return direction;
	}

	public int getColor() {
		return color;
	}

	public static Intersection of(Vector normal, Vector point, Vector direction) {
		return of(normal, point, direction, 0);
	}

	public static Intersection of(Vector normal, Vector point, Vector direction, int color) {
		return new Intersection(normal, point, direction, color);
	}
}
