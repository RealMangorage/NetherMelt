package me.mangorage.nethermelt.blocks;

import me.mangorage.nethermelt.blockentitys.FoamBlockEntity;
import me.mangorage.nethermelt.blockentitys.RootBlockEntity;
import me.mangorage.nethermelt.util.DefaultProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.commands.SetBlockCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;
import java.util.Random;

public class FoamBlock extends Block implements EntityBlock {
    public static int MAX_STAGES = 4;
    public static IntegerProperty STAGES = IntegerProperty.create("stage", 1, MAX_STAGES);


    public FoamBlock() {
        super(DefaultProperties.BLOCK.noOcclusion());
    }


    @Override
    public void tick(BlockState state, ServerLevel serverLevel, BlockPos blockPos, Random random) {
        if (state.getValue(STAGES) < 4) {
            serverLevel.setBlock(blockPos, state.cycle(STAGES), Block.UPDATE_ALL);

            FoamBlockEntity BE = (FoamBlockEntity) serverLevel.getBlockEntity(blockPos);
            BE.playSound();

            serverLevel.scheduleTick(blockPos, serverLevel.getBlockState(blockPos).getBlock(), 20);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> BUILDER) {
        BUILDER.add(STAGES);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> tBlockEntityType) {
        if (level.isClientSide()) {
            return null;
        }
        return (lvl, pos, blockState, t) -> {
            if (t instanceof FoamBlockEntity BE) {
                BE.tick();
            }
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FoamBlockEntity(pos, state);
    }
}
