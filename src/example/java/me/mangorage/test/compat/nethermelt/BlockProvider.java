package me.mangorage.test.compat.nethermelt;

import me.mangorage.nethermelt.api.IResistantBlocksProvider;
import me.mangorage.test.core.Registration;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class BlockProvider implements IResistantBlocksProvider {
    @Override
    public void addBlocks(List<Block> blockList) {
        blockList.add(Registration.BLOCK_EXAMPLE.get());
    }

    @Override
    public void addBlockClassz(List<Class<? extends Block>> blockClasszList) {

    }
}
