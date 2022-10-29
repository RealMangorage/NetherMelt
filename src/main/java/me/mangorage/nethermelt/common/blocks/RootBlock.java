package me.mangorage.nethermelt.common.blocks;

import me.mangorage.nethermelt.api.IResistant;
import me.mangorage.nethermelt.common.blockentitys.RootBlockEntity;
import me.mangorage.nethermelt.api.ITickable;
import me.mangorage.nethermelt.common.config.NetherMeltConfig;
import me.mangorage.nethermelt.common.core.RegistryCollection;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeConfigSpec;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static me.mangorage.nethermelt.common.core.Constants.BlockStateProperties.ACTIVATED;
import static me.mangorage.nethermelt.common.core.Constants.Translatable.ROOT_TOOLTIP_WRONG_DIMENSION;

public class RootBlock extends Block implements EntityBlock, IResistant {
    private final String type;
    private final Config config;

    public RootBlock(String type) {
        super(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(100.0f).destroyTime(13.0f).sound(SoundType.NETHERRACK).lightLevel(state -> {
            return state.getValue(ACTIVATED) ? 15 : 0;
        }));
        registerDefaultState(this.defaultBlockState().setValue(ACTIVATED, false));
        this.type = type;
        this.config = new Config(type);
    }

    public Config getConfig() {return config;}
    public String getType() {return type;}

    private ItemStack getItem(int Charges) {
        ItemStack stack = RegistryCollection.getVariant(getType()).ITEM_ROOT.get().getDefaultInstance();
        CompoundTag tag = new CompoundTag();
        tag.putInt("charges", Charges);
        stack.setTag(tag);

        if (Charges <= 0) {
            return RegistryCollection.getVariant(getType()).ITEM_DEAD_ROOT.get().getDefaultInstance();
        }

        return stack;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pNewState.getBlock() instanceof RootBlock) return;

        if (pLevel.getBlockEntity(pPos) instanceof RootBlockEntity RBE)
            RBE.getCore().killAllFoam();

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> BUILDER) {BUILDER.add(ACTIVATED);}

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {

        if (level.isClientSide)
            return InteractionResult.FAIL;

        if (!state.getValue(ACTIVATED).booleanValue() && player.getItemInHand(hand).getItem().equals(Items.FLINT_AND_STEEL)) {
            if (!RegistryCollection.getVariant(getType()).PROPERTIES.getDimensions().contains(level.dimension())) {
                player.displayClientMessage(new TranslatableComponent(ROOT_TOOLTIP_WRONG_DIMENSION.getKey(), "Nether Placeholder", "DIM Placeholder").withStyle(Style -> {
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
        return state.getValue(ACTIVATED) ? ITickable::tick : null;
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
    public class Config {
        private HashMap<NetherMeltConfig.Type, ForgeConfigSpec.ConfigValue<?>> config;

        private String type;
        private List<Block> RESISTANT_BLOCKS = new ArrayList<>(); // Blocks that cant be absorbed by foam blocks
        private List<Block> FALLING_BLOCKS = new ArrayList<>(); // Blocks that can Fall!


        private void load() {
            config = NetherMeltConfig.getConfig(this.type);
        }
        public Integer getDefaultCharges() {
            if (config == null) load();
            ForgeConfigSpec.ConfigValue<Integer> CHARGES = ((ForgeConfigSpec.ConfigValue<Integer>) config.get(NetherMeltConfig.Type.CHARGES));
            return CHARGES.get();
        }

        public Integer getDefaultRange() {
            if (config == null) load();
            ForgeConfigSpec.ConfigValue<Integer> RANGE = ((ForgeConfigSpec.ConfigValue<Integer>) config.get(NetherMeltConfig.Type.RANGE));
            return RANGE.get();
        }

        public List<Block> getDefaultResistantBlocks() {
            if (config == null) load();
            return RESISTANT_BLOCKS;
        }

        public List<Block> getDefaultFallingBlocks() {
            if (config == null) load();
            return FALLING_BLOCKS;
        }


        private Config(String type) {
            this.type = type;
            load();
        }
    }
}
