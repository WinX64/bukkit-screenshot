package io.github.winx64.screenshot.registry;

import io.github.winx64.screenshot.model.Model;
import io.github.winx64.screenshot.model.AbstractModel.Builder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DefaultModelRegistry implements ModelRegistry {

    private static final String IMAGE_RESOURCE = "terrain.png";
    private static final int TEXTURE_SIZE = 16;

    private final Map<Material, Map<BlockData, Model>> modelMap;
    private BufferedImage textures;

    public DefaultModelRegistry() {
        this.modelMap = new HashMap<>();
    }

    @Override
    public void initialize() {
        URL url = this.getClass().getClassLoader().getResource(IMAGE_RESOURCE);
        if (url == null) {
            throw new RuntimeException("Default resource \"terrain.png\" is missing");
        }
        try (InputStream input = url.openConnection().getInputStream()) {
            this.textures = ImageIO.read(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        registerModel(Material.GRASS_BLOCK, Builder.createMulti(textureIndex(0, 0), textureIndex(0, 3), textureIndex(0, 2)).build());
        registerModel(Material.STONE, Builder.createSimple(textureIndex(0, 1)).build());
        registerModel(Material.DIRT, Builder.createSimple(textureIndex(0, 2)).build());
        registerModel(Material.OAK_PLANKS, Builder.createSimple(textureIndex(0, 4)).build());
        registerModel(Material.SPRUCE_PLANKS,
                Builder.createSimple(textureIndex(0, 4)).build());
        registerModel(Material.BIRCH_PLANKS,
                Builder.createSimple(textureIndex(0, 4)).build());
        registerModel(Material.JUNGLE_PLANKS,
                Builder.createSimple(textureIndex(0, 4)).build());
        registerModel(Material.ACACIA_PLANKS,
                Builder.createSimple(textureIndex(0, 4)).build());
        registerModel(Material.DARK_OAK_PLANKS,
                Builder.createSimple(textureIndex(0, 4)).build());
        registerModel(Material.BRICK, Builder.createSimple(textureIndex(0, 7)).build());
        registerModel(Material.TNT, Builder.createMulti(textureIndex(0, 9),
                textureIndex(0, 8), textureIndex(0, 10)).build());
        registerModel(Material.WATER, Builder.createStatic(0xFF000000 | Color.fromRGB(0, 5, 60).asRGB())
                .transparency(0.60).reflection(0.1).occlusion().build());
        registerModel(Material.DIAMOND_BLOCK,
                Builder.createSimple(textureIndex(3, 3)).reflection(0.75).build());
        registerModel(Material.POPPY, Builder.createCross(textureIndex(0, 12)).build());
        registerModel(Material.DANDELION, Builder.createCross(textureIndex(0, 13)).build());
        registerModel(Material.OAK_SAPLING,
                Builder.createCross(textureIndex(0, 15)).build());
        registerModel(Material.SPRUCE_SAPLING,
                Builder.createCross(textureIndex(0, 15)).build());
        registerModel(Material.BIRCH_SAPLING,
                Builder.createCross(textureIndex(0, 15)).build());
        registerModel(Material.JUNGLE_SAPLING,
                Builder.createCross(textureIndex(0, 15)).build());
        registerModel(Material.ACACIA_SAPLING,
                Builder.createCross(textureIndex(0, 15)).build());
        registerModel(Material.DARK_OAK_SAPLING,
                Builder.createCross(textureIndex(0, 15)).build());

        registerModel(Material.COBBLESTONE,
                Builder.createSimple(textureIndex(1, 0)).build());
        registerModel(Material.BEDROCK, Builder.createSimple(textureIndex(1, 1)).build());
        registerModel(Material.SAND, Builder.createSimple(textureIndex(1, 2)).build());
        registerModel(Material.GRAVEL, Builder.createSimple(textureIndex(1, 3)).build());
        registerModel(Material.OAK_LOG, Builder.createMulti(textureIndex(1, 5),
                textureIndex(1, 4), textureIndex(1, 5)).build());
        registerModel(Material.SPRUCE_LOG, Builder.createMulti(textureIndex(1, 5),
                textureIndex(1, 4), textureIndex(1, 5)).build());
        registerModel(Material.BIRCH_LOG, Builder.createMulti(textureIndex(1, 5),
                textureIndex(1, 4), textureIndex(1, 5)).build());
        registerModel(Material.JUNGLE_LOG, Builder.createMulti(textureIndex(1, 5),
                textureIndex(1, 4), textureIndex(1, 5)).build());
        registerModel(Material.ACACIA_LOG, Builder.createMulti(textureIndex(1, 5),
                textureIndex(1, 4), textureIndex(1, 5)).build());
        registerModel(Material.DARK_OAK_LOG, Builder.createMulti(textureIndex(1, 5),
                textureIndex(1, 4), textureIndex(1, 5)).build());
        registerModel(Material.OAK_WOOD, Builder.createSimple(textureIndex(1, 4)).build());
        registerModel(Material.SPRUCE_WOOD,
                Builder.createSimple(textureIndex(1, 4)).build());
        registerModel(Material.BIRCH_WOOD, Builder.createSimple(textureIndex(1, 4)).build());
        registerModel(Material.JUNGLE_WOOD,
                Builder.createSimple(textureIndex(1, 4)).build());
        registerModel(Material.ACACIA_WOOD,
                Builder.createSimple(textureIndex(1, 4)).build());
        registerModel(Material.DARK_OAK_WOOD,
                Builder.createSimple(textureIndex(1, 4)).build());
        registerModel(Material.OAK_LEAVES, Builder.createSimple(textureIndex(1, 6)).build());
        registerModel(Material.SPRUCE_LEAVES,
                Builder.createSimple(textureIndex(1, 6)).build());
        registerModel(Material.BIRCH_LEAVES,
                Builder.createSimple(textureIndex(1, 6)).build());
        registerModel(Material.JUNGLE_LEAVES,
                Builder.createSimple(textureIndex(1, 6)).build());
        registerModel(Material.ACACIA_LEAVES,
                Builder.createSimple(textureIndex(1, 6)).build());
        registerModel(Material.DARK_OAK_LEAVES,
                Builder.createSimple(textureIndex(1, 6)).build());
        registerModel(Material.IRON_BLOCK,
                Builder.createMulti(textureIndex(1, 7),
                        textureIndex(2, 7), textureIndex(3, 7)).build());
        registerModel(Material.GOLD_BLOCK, Builder.createMulti(textureIndex(1, 8),
                textureIndex(2, 8), textureIndex(3, 8)).build());
        registerModel(Material.RED_MUSHROOM,
                Builder.createCross(textureIndex(1, 12)).build());
        registerModel(Material.BROWN_MUSHROOM,
                Builder.createCross(textureIndex(1, 13)).build());
        registerModel(Material.LAVA, Builder.createSimple(textureIndex(2, 14))
                .transparency(0.15).reflection(0.05).occlusion().build());

        registerModel(Material.GOLD_ORE, Builder.createSimple(textureIndex(2, 0)).build());
        registerModel(Material.IRON_ORE, Builder.createSimple(textureIndex(2, 1)).build());
        registerModel(Material.COAL_ORE, Builder.createSimple(textureIndex(2, 2)).build());

        registerModel(Material.GLASS,
                Builder.createSimple(textureIndex(3, 1)).occlusion().build());

        registerModel(Material.GRASS, Builder.createCross(textureIndex(5, 6)).build());
        registerModel(Material.SUGAR_CANE, Builder.createCross(textureIndex(5, 5)).build());
    }

    @Override
    public Model getModel(Material material, BlockData blockData) {
        return modelMap.computeIfAbsent(material, key -> new HashMap<>()).getOrDefault(blockData,
                blockData == null ? getDefaultModel()
                        : modelMap.get(material).getOrDefault(null, getDefaultModel()));
    }

    @Override
    public Model getDefaultModel() {
        return Builder.createStatic(Color.PURPLE.asRGB()).build();
    }

    private void registerModel(Material material, Model blockModel) {
        modelMap.computeIfAbsent(material, key -> new HashMap<>())
                .put(null, blockModel);
    }

    private int[][] textureIndex(int verticalIndex, int horizontalIndex) {
        int[][] texture = new int[TEXTURE_SIZE][TEXTURE_SIZE];

        int offsetY = verticalIndex * TEXTURE_SIZE + (TEXTURE_SIZE - 1);
        int offsetX = horizontalIndex * TEXTURE_SIZE;

        for (int pixelY = 0; pixelY < TEXTURE_SIZE; pixelY++) {
            for (int pixelX = 0; pixelX < TEXTURE_SIZE; pixelX++) {
                texture[pixelY][pixelX] = textures.getRGB(offsetX + pixelX, offsetY - pixelY);
            }
        }

        return texture;
    }
}
