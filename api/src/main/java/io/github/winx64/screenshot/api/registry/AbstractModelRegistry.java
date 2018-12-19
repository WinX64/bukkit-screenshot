package io.github.winx64.screenshot.api.registry;

import io.github.winx64.screenshot.api.model.Model;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractModelRegistry implements ModelRegistry {

    protected final Map<Material, Map<BlockData, Model>> modelMap;

    protected AbstractModelRegistry(Map<Material, Map<BlockData, Model>> modelMap) {
        this.modelMap = modelMap;
    }

    @Override
    public Model getModel(Block block) {
        return getModel(block.getType(), block.getBlockData());
    }

    @Override
    public Model getModel(Material material) {
        return getModel(material, null);
    }

    @Override
    public Model getModel(Material material, BlockData blockData) {
        return modelMap.getOrDefault(material, new HashMap<>()).get(blockData);
    }
}
