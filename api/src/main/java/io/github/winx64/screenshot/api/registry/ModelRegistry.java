package io.github.winx64.screenshot.api.registry;

import io.github.winx64.screenshot.api.model.Model;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

public interface ModelRegistry {

    void initialize();

    Model getModel(Block block);

    Model getModel(Material material);

    Model getModel(Material material, BlockData blockData);

    Model getDefaultModel();
}
