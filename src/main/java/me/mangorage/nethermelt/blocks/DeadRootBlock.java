package me.mangorage.nethermelt.blocks;

import me.mangorage.nethermelt.setup.Registry;
import me.mangorage.nethermelt.util.DefaultProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import java.util.Random;

public class DeadRootBlock extends Block {
    public DeadRootBlock() {
        super(DefaultProperties.BLOCK(Material.STONE).requiresCorrectToolForDrops().strength(100.0f).destroyTime(5.0f).sound(SoundType.NETHERRACK));
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 15;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        BlockPos belowPos = pos.below(1);

        if (level.getBlockState(belowPos) == Blocks.AIR.defaultBlockState()) {
            // Move self down!!
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            level.setBlock(belowPos, state, Block.UPDATE_ALL, 1);
            level.scheduleTick(belowPos, level.getBlockState(belowPos).getBlock(), 10);
        }
    }
}
