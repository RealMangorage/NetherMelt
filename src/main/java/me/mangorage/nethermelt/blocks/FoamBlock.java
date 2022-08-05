package me.mangorage.nethermelt.blocks;

import me.mangorage.nethermelt.api.IResistant;
import me.mangorage.nethermelt.blockentitys.FoamBlockEntity;
import me.mangorage.nethermelt.core.Registration;
import net.minecraft.core.BlockPos;
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

import javax.annotation.Nullable;

import static me.mangorage.nethermelt.core.Constants.BlockStateProperties.STAGE;
import static me.mangorage.nethermelt.core.Constants.BlockStateProperties.VISIBLE;

public class FoamBlock extends Block implements EntityBlock, IResistant {
    private static int[] LIGHT_LEVELS = {2, 6, 8, 15};

    public FoamBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.GLASS).lightLevel(state -> {return LIGHT_LEVELS[state.getValue(STAGE)-1];}));
        registerDefaultState(defaultBlockState().setValue(VISIBLE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> BUILDER) {
        BUILDER.add(STAGE, VISIBLE);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> tBlockEntityType) {
        if (level.isClientSide()) {
            return null;
        }

        return (lvl, pos, blockState, t) -> {
            if (t instanceof FoamBlockEntity BE) {
                BE.serverTick();
            }
        };
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pNewState.getBlock().equals(Registration.BLOCK_FOAM.get())) return;

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

}
