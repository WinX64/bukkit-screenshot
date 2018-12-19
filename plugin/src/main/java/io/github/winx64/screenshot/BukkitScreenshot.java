package io.github.winx64.screenshot;

import io.github.winx64.screenshot.api.renderer.Renderer;
import io.github.winx64.screenshot.api.renderer.Resolution;
import io.github.winx64.screenshot.api.renderer.Resolution.AspectRatio;
import io.github.winx64.screenshot.api.renderer.Resolution.Pixels;
import io.github.winx64.screenshot.render.DefaultScreenRenderer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.regex.Pattern;

public final class BukkitScreenshot extends JavaPlugin implements Listener {

    private static final Pattern INVALID_FILE_NAME = Pattern.compile("[^a-zA-Z0-9_]");

    private final Renderer screenRenderer;

    public BukkitScreenshot() {
        this.screenRenderer = new DefaultScreenRenderer();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only a player may use this command!");
            return true;
        }

        Player player = (Player) sender;
        String fileName;
        Pixels pixels = Pixels._1080P;
        AspectRatio aspectRatio = AspectRatio._16_9;

        if (args.length > 0) {
            if (INVALID_FILE_NAME.matcher(fileName = args[0]).find()) {
                player.sendMessage(ChatColor.RED + "Enter a file name containing only alphanumeric characters and underscore");
                return true;
            }
        } else {
            player.sendMessage(ChatColor.RED + "Syntax: /screenshot <file name> [pixels] [aspect ratio]");
            return true;
        }

        if (args.length > 1) {
            pixels = Pixels.getPixels(args[1]);
            if (pixels == null) {
                player.sendMessage(ChatColor.RED + "Invalid option \"" + args[1] + "\"");
                player.sendMessage(ChatColor.YELLOW + "Available options: " + ChatColor.GRAY + String.join(", ", Pixels.getAllAliases()));
                return true;
            }
        }

        if (args.length > 2) {
            aspectRatio = AspectRatio.getAspectRatio(args[2]);
            if (aspectRatio == null) {
                player.sendMessage(ChatColor.RED + "Invalid option \"" + args[2] + "\"");
                player.sendMessage(ChatColor.YELLOW + "Available options: " + ChatColor.GRAY + String.join(", ", AspectRatio.getAllAliases()));
                return true;
            }
        }

        Resolution resolution = new Resolution(pixels, aspectRatio);
        player.sendMessage(ChatColor.RED + "Starting rendering process, this may take a while...");
        BufferedImage image = screenRenderer.render(player, resolution);

        player.sendMessage(ChatColor.GREEN + "Rendering complete, saving image...");
        File file = new File(getDataFolder(), fileName + ".png");
        try {
            getDataFolder().mkdir();
            ImageIO.write(image, "png", file);
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "An error occurred while trying to save the image file. Check the console for more information");
            return true;
        }
        player.sendMessage(ChatColor.GREEN + "Image saved to the file \"" + file.getPath() + "\" with success");

        return true;
    }
}
