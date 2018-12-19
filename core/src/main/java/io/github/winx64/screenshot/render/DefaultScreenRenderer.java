package io.github.winx64.screenshot.render;

import io.github.winx64.screenshot.raytrace.Raytracer;
import io.github.winx64.screenshot.util.MathUtil;
import io.github.winx64.screenshot.raytrace.DefaultRaytracer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;

public class DefaultScreenRenderer implements Renderer {

    private static final double FOV_YAW_DEG = 53;
    private static final double FOV_PITCH_DEG = 23;

    private static final double FOV_YAW_RAD = Math.toRadians(FOV_YAW_DEG);
    private static final double FOV_PITCH_RAD = Math.toRadians(FOV_PITCH_DEG);

    private static final Vector BASE_VEC = new Vector(1, 0, 0);

    private final Raytracer raytracer;

    public DefaultScreenRenderer() {
        this.raytracer = new DefaultRaytracer();
    }

    @Override
    public BufferedImage render(Player player, Resolution resolution) {
        int width = resolution.getWidth();
        int height = resolution.getHeight();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] imageData = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        World world = player.getWorld();
        Vector linePoint = player.getEyeLocation().toVector();
        List<Vector> rayMap = buildRayMap(player, resolution);
        for (int i = 0; i < rayMap.size(); i++) {
            imageData[i] = raytracer.trace(world, linePoint, rayMap.get(i));
        }

        return image;
    }

    private List<Vector> buildRayMap(Player p, Resolution resolution) {
        Location eyeLocation = p.getEyeLocation();
        Vector lineDirection = eyeLocation.getDirection();

        double x = lineDirection.getX();
        double y = lineDirection.getY();
        double z = lineDirection.getZ();

        double angleYaw = Math.atan2(z, x);
        double anglePitch = Math.atan2(y, Math.sqrt(x * x + z * z));

        Vector lowerLeftCorner = MathUtil.doubleYawPitchRotation(BASE_VEC, -FOV_YAW_RAD, -FOV_PITCH_RAD, angleYaw, anglePitch);
        Vector upperLeftCorner = MathUtil.doubleYawPitchRotation(BASE_VEC, -FOV_YAW_RAD, FOV_PITCH_RAD, angleYaw, anglePitch);
        Vector lowerRightCorner = MathUtil.doubleYawPitchRotation(BASE_VEC, FOV_YAW_RAD, -FOV_PITCH_RAD, angleYaw, anglePitch);
        Vector upperRightCorner = MathUtil.doubleYawPitchRotation(BASE_VEC, FOV_YAW_RAD, FOV_PITCH_RAD, angleYaw, anglePitch);

        int width = resolution.getWidth();
        int height = resolution.getHeight();
        List<Vector> rayMap = new ArrayList<>(width * height);

        Vector leftFraction = upperLeftCorner.clone().subtract(lowerLeftCorner).multiply(1.0 / (height - 1));
        Vector rightFraction = upperRightCorner.clone().subtract(lowerRightCorner).multiply(1.0 / (height - 1));

        for (int pitch = 0; pitch < height; pitch++) {

            Vector leftPitch = upperLeftCorner.clone().subtract(leftFraction.clone().multiply(pitch));
            Vector rightPitch = upperRightCorner.clone().subtract(rightFraction.clone().multiply(pitch));
            Vector yawFraction = rightPitch.clone().subtract(leftPitch).multiply(1.0 / (width - 1));

            for (int yaw = 0; yaw < width; yaw++) {
                Vector ray = leftPitch.clone().add(yawFraction.clone().multiply(yaw)).normalize();
                rayMap.add(ray);
            }
        }

        return rayMap;
    }
}
