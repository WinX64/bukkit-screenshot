package io.github.winx64.screenshot.registry;

import io.github.winx64.screenshot.model.Model;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

public interface ModelRegistry {

    void initialize();

    default Model getModel(Block block) {
        return getModel(block.getType(), block.getBlockData());
    }

    default Model getModel(Material material) {
        return getModel(material, null);
    }

    Model getModel(Material material, BlockData blockData);

    Model getDefaultModel();
}
