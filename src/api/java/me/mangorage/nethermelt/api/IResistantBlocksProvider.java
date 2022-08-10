package me.mangorage.nethermelt.api;

import net.minecraft.world.level.block.Block;

import java.util.List;

public interface IResistantBlocksProvider {
    default void addBlocks(List<Block> blockList) {}
    default void addBlockClassz(List<Class<? extends Block>> blockClasszList) {}
}
