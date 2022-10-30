package me.mangorage.nethermelt.common.compat.nethermelt;

import me.mangorage.nethermelt.api.IResistantBlocksProvider;
import me.mangorage.nethermelt.common.blocks.FoamBlock;
import me.mangorage.nethermelt.common.blocks.RootBlock;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class ResistantBlocksProvider implements IResistantBlocksProvider {

    @Override
    public void addBlockClassz(List<Class<? extends Block>> blockClasszList) {
        blockClasszList.add(RootBlock.class);
        blockClasszList.add(FoamBlock.class);
    }
}
