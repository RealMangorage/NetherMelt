package me.mangorage.nethermelt.common.blocks;

import me.mangorage.nethermelt.common.blockentitys.FoamBlockEntity;
import me.mangorage.nethermelt.common.core.ITickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static me.mangorage.nethermelt.common.core.Constants.BlockStateProperties.STAGE;
import static me.mangorage.nethermelt.common.core.Constants.BlockStateProperties.VISIBLE;

public class FoamBlock extends Block implements EntityBlock {
    private static int[] LIGHT_LEVELS = {2, 6, 8, 15};

    private String variantType;

    public FoamBlock(String variantType) {
        super(BlockBehaviour.Properties.copy(Blocks.SPONGE).lightLevel(state -> {return LIGHT_LEVELS[state.getValue(STAGE)-1];}));
        this.variantType = variantType;
        registerDefaultState(defaultBlockState().setValue(VISIBLE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> BUILDER) {
        BUILDER.add(STAGE, VISIBLE);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return ITickable::tick;
    }

    @Override
    public void tick(BlockState state, @NotNull ServerLevel level, BlockPos pos, RandomSource random) {
        for (Direction direction : Direction.values()) {
            BlockPos nPos = pos.relative(direction, 1);
            BlockState cState = level.getBlockState(nPos);

            FluidState fluidState = cState.getFluidState();

            if (!fluidState.isEmpty())
                level.setBlock(nPos, Blocks.COBBLESTONE.defaultBlockState(), Block.UPDATE_ALL);
        }
        level.scheduleTick(pos, this, 1);
    }

    @Override
    public void onPlace(BlockState pState, Level level, BlockPos pos, BlockState pOldState, boolean pIsMoving) {
        if (level.isClientSide)
            return;
        level.scheduleTick(pos, this, 1);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pNewState.getBlock() instanceof FoamBlock) return;

        if (pLevel.getBlockEntity(pPos) instanceof FoamBlockEntity FBE) {
            if (FBE.getRoot() != null && !FBE.interupted) {
                FBE.getRoot().getCore().Die(FBE);
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FoamBlockEntity(pos, state);
    }
    @Override
    public RenderShape getRenderShape(BlockState state) {
        if (state.getValue(VISIBLE))
            return RenderShape.MODEL;

        return RenderShape.INVISIBLE;
    }

    public String getVariantType() {
        return variantType;
    }

}
