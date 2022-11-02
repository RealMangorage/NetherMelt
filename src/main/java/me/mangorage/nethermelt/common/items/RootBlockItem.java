package me.mangorage.nethermelt.common.items;

import me.mangorage.nethermelt.NetherMelt;
import me.mangorage.nethermelt.common.blocks.RootBlock;
import me.mangorage.nethermelt.common.core.Registration;
import me.mangorage.nethermelt.common.core.RegistryCollection;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RootBlockItem extends BlockItem {
    private final String type;

    public RootBlockItem(RootBlock block) {
        super(block, RegistryCollection.getVariant(block.getType()).PROPERTIES.isModLoaded() ? Registration.PROPERTIES_ITEM.get().stacksTo(64).tab(NetherMelt.CreativeTab) : Registration.PROPERTIES_ITEM.get().stacksTo(64));
        type = block.getType();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> Tooltip, TooltipFlag flag) {
        if (stack.hasTag() && stack.getTag().contains("charges")) {
            int charges = stack.getTag().getInt("charges");
            Tooltip.add(Component.literal("Charges Remaining: ").withStyle(ChatFormatting.WHITE).append(Component.literal("" + charges).withStyle(ChatFormatting.GOLD)));
        }

        super.appendHoverText(stack, level, Tooltip, flag);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pPos, Level pLevel, @Nullable Player pPlayer, ItemStack pStack, BlockState pState) {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);

        if (stack != null) {
            int charges = ((RootBlock) getBlock()).getConfig().getDefaultCharges();

            if (stack.hasTag() && !stack.getTag().contains("charges")) {
                CompoundTag tag = stack.getTag();
                tag.putInt("charges", charges);
            } else if (!stack.hasTag()) {
                CompoundTag tag = new CompoundTag();
                stack.setTag(tag);
                tag.putInt("charges", charges);
            }
        }

        return super.use(level, player, hand);
    }

}
