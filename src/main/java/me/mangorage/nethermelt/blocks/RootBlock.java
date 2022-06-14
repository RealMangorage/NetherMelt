package me.mangorage.nethermelt.blocks;

import me.mangorage.nethermelt.blockentitys.RootBlockEntity;
import me.mangorage.nethermelt.util.DefaultProperties;
import me.mangorage.nethermelt.util.Translatable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class RootBlock extends Block implements EntityBlock {
    public static BooleanProperty ACTIVATED = BooleanProperty.create("activated");


    public RootBlock() {
        super(DefaultProperties.BLOCK(Material.STONE).requiresCorrectToolForDrops().strength(100.0f).destroyTime(13.0f).sound(SoundType.NETHERRACK));
        registerDefaultState(this.defaultBlockState().setValue(ACTIVATED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> BUILDER) {
        BUILDER.add(ACTIVATED);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(ACTIVATED) ? 15 : 0;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {

        if (level.isClientSide)
            return InteractionResult.FAIL;



        if (!state.getValue(ACTIVATED).booleanValue() && player.getItemInHand(hand).getItem().equals(Items.FLINT_AND_STEEL)) {
            if (level.dimension() != Level.NETHER) {
                player.displayClientMessage(Translatable.ROOT_TOOLTIP_WRONG_DIMENSION.getComponent().withStyle(ChatFormatting.RED, ChatFormatting.BOLD), true);
                return InteractionResult.FAIL;
            }

            InteractionResult result = InteractionResult.CONSUME_PARTIAL;
            player.getItemInHand(hand).setDamageValue(1);
            level.setBlock(blockPos, state.setValue(ACTIVATED, true), Block.UPDATE_ALL); // logic continues on BE
            return result;
        }

        return InteractionResult.FAIL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> tBlockEntityType) {
        if (level.isClientSide()) {
            return null;
        }
        return (lvl, pos, blockState, t) -> {
            if (t instanceof RootBlockEntity BE) {
                BE.tick();
            }
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RootBlockEntity(pos, state);
    }
}
