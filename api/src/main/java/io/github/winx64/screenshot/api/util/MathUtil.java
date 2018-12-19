package io.github.winx64.screenshot.api.util;

import org.bukkit.Color;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class MathUtil {

	private MathUtil() {}

	public static Vector yawPitchRotation(Vector base, double angleYaw, double anglePitch) {
		double oldX = base.getX();
		double oldY = base.getY();
		double oldZ = base.getZ();

		double sinOne = Math.sin(angleYaw);
		double sinTwo = Math.sin(anglePitch);
		double cosOne = Math.cos(angleYaw);
		double cosTwo = Math.cos(anglePitch);

		double newX = oldX * cosOne * cosTwo - oldY * cosOne * sinTwo - oldZ * sinOne;
		double newY = oldX * sinTwo + oldY * cosTwo;
		double newZ = oldX * sinOne * cosTwo - oldY * sinOne * sinTwo + oldZ * cosOne;

		return new Vector(newX, newY, newZ);
	}

	public static Vector doubleYawPitchRotation(Vector base, double firstYaw, double firstPitch, double secondYaw,
			double secondPitch) {
		return yawPitchRotation(yawPitchRotation(base, firstYaw, firstPitch), secondYaw, secondPitch);
	}

	public static Vector reflectVector(Vector linePoint, Vector lineDirection, Vector planePoint, Vector planeNormal) {
		return lineDirection.clone().subtract(planeNormal.clone().multiply(2 * lineDirection.dot(planeNormal)));
	}

	public static Vector toVector(BlockFace face) {
		return new Vector(face.getModX(), face.getModY(), face.getModZ());
	}

	public static int weightedColorSum(int rgbOne, int rgbTwo, double weightOne, double weightTwo) {
		Color colorOne = Color.fromRGB(rgbOne & 0xFFFFFF);
		Color colorTwo = Color.fromRGB(rgbTwo & 0xFFFFFF);

		double total = weightOne + weightTwo;
		int newRed = (int) ((colorOne.getRed() * weightOne + colorTwo.getRed() * weightTwo) / total);
		int newGreen = (int) ((colorOne.getGreen() * weightOne + colorTwo.getGreen() * weightTwo) / total);
		int newBlue = (int) ((colorOne.getBlue() * weightOne + colorTwo.getBlue() * weightTwo) / total);

		return Color.fromRGB(newRed, newGreen, newBlue).asRGB();
	}

	public static Vector getLinePlaneIntersection(Vector linePoint, Vector lineDirection, Vector planePoint,
			Vector planeNormal, boolean allowBackwards) {
		double d = planePoint.dot(planeNormal);
		double t = (d - planeNormal.dot(linePoint)) / planeNormal.dot(lineDirection);

		if (t < 0 && !allowBackwards) {
			return null;
		}

		double x = linePoint.getX() + lineDirection.getX() * t;
		double y = linePoint.getY() + lineDirection.getY() * t;
		double z = linePoint.getZ() + lineDirection.getZ() * t;

		return new Vector(x, y, z);
	}
}
