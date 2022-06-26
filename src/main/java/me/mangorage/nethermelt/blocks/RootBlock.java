package me.mangorage.nethermelt.blocks;

import me.mangorage.nethermelt.blockentitys.RootBlockEntity;
import me.mangorage.nethermelt.setup.Registry;
import me.mangorage.nethermelt.util.DefaultProperties;
import me.mangorage.nethermelt.util.Translatable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import org.stringtemplate.v4.ST;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RootBlock extends Block implements EntityBlock {
    public static BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    public RootBlock() {
        super(DefaultProperties.BLOCK(Material.STONE).requiresCorrectToolForDrops().strength(100.0f).destroyTime(13.0f).sound(SoundType.NETHERRACK));
        registerDefaultState(this.defaultBlockState().setValue(ACTIVATED, false));
    }

    private ItemStack getItem(int Charges) {
        ItemStack stack = Registry.ITEM_ROOT.get().getDefaultInstance();
        CompoundTag tag = new CompoundTag();
        tag.putInt("charges", Charges);
        stack.setTag(tag);

        if (Charges == 0) {
            return Registry.ITEM_DEAD_ROOT.get().getDefaultInstance();
        }

        return stack;
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
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {

        if (level.isClientSide)
            return InteractionResult.FAIL;

        if (!state.getValue(ACTIVATED).booleanValue() && player.getItemInHand(hand).getItem().equals(Items.FLINT_AND_STEEL)) {
            if (level.dimension() != Level.NETHER) {
                player.displayClientMessage(Translatable.ROOT_TOOLTIP_WRONG_DIMENSION.getComponent().withStyle(Style -> {
                    Style = Style.withColor(ChatFormatting.DARK_RED);
                    Style = Style.withBold(true);
                    return Style;
                }), true);
                return InteractionResult.FAIL;
            }

            ItemStack stack = player.getItemInHand(hand);

            if (!player.isCreative())
                stack.setDamageValue(stack.getDamageValue() - 1);

            int ChargesLeft = ((RootBlockEntity) level.getBlockEntity(blockPos)).getCharges();
            int ChargesRemaining = ChargesLeft > 0 ? ChargesLeft - 1 : 0;

            level.setBlock(blockPos, state.setValue(ACTIVATED, true), Block.UPDATE_ALL);

            if (level.getBlockEntity(blockPos) instanceof RootBlockEntity BE)
                BE.setCharges(ChargesRemaining);

            return InteractionResult.CONSUME_PARTIAL;
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


    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable LivingEntity placer, ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("charges")) {
            int Charges = stack.getTag().getInt("charges");

            if (level.getBlockEntity(pos) instanceof RootBlockEntity BE)
                BE.setCharges(Charges);
        }

    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootContext.Builder pBuilder) {
        List<ItemStack> items = new ArrayList<>();


        BlockEntity blockentity = pBuilder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockentity instanceof RootBlockEntity) {
            RootBlockEntity root = (RootBlockEntity) blockentity;
            int charges = root.getCharges();

            items.add(getItem(charges));
        }

        return items;
    }

}
