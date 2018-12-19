package io.github.winx64.screenshot.model;

import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import io.github.winx64.screenshot.util.Intersection;

public class MultiModel extends SimpleModel {

	private final int[][] topTexture;
	private final int[][] bottomTexture;

	private MultiModel(int[][] topTexture, int[][] sideTexture, int[][] bottomTexture,
					   double transparencyFactor, double reflectionFactor, boolean occluding) {
		super(sideTexture, transparencyFactor, reflectionFactor, occluding);

		this.topTexture = topTexture;
		this.bottomTexture = bottomTexture;
	}

	@Override
	public Intersection intersect(Block block, Intersection currentIntersection) {
		if (!currentIntersection.getNormal().equals(UP) && !currentIntersection.getNormal().equals(DOWN)) {
			return super.intersect(block, currentIntersection);
		}

		Vector normal = currentIntersection.getNormal();
		Vector point = currentIntersection.getPoint();
		Vector direction = currentIntersection.getDirection();

		double yOffset = point.getX() - (int) point.getX();
		double xOffset = point.getZ() - (int) point.getZ();

		int pixelY = (int) Math.floor((yOffset < 0 ? yOffset + 1 : yOffset) * textureSize);
		int pixelX = (int) Math.floor((xOffset < 0 ? xOffset + 1 : xOffset) * textureSize);

		if (normal.equals(UP)) {
			return Intersection.of(normal, point, direction, topTexture[pixelY][pixelX]);
		} else {
			return Intersection.of(normal, point, direction, bottomTexture[pixelY][pixelX]);
		}
	}

	public static class MultiModelBuilder extends SimpleModelBuilder {

		private final int[][] topTexture;
		private final int[][] bottomTexture;

		MultiModelBuilder(int[][] topTexture, int[][] sideTexture, int[][] bottomTexture) {
			super(sideTexture);

			this.topTexture = topTexture;
			this.bottomTexture = bottomTexture;
		}

		@Override
		public MultiModel build() {
			return new MultiModel(topTexture, texture, bottomTexture, transparencyFactor,
					reflectionFactor, occluding);
		}
	}
}
