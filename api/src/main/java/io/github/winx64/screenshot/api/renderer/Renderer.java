package io.github.winx64.screenshot.api.renderer;

import org.bukkit.entity.Player;

import java.awt.image.BufferedImage;

public interface Renderer {

    BufferedImage render(Player player, Resolution resolution);
}
