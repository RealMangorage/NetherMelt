package me.mangorage.nethermelt.blocks;

import me.mangorage.nethermelt.util.DefaultProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class DeadFoamBlock extends Block {
    public DeadFoamBlock() {
        super(DefaultProperties.BLOCK(Material.SPONGE));
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 10;
    }
}
