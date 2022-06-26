package me.mangorage.nethermelt.items;

import me.mangorage.nethermelt.NetherMelt;
import me.mangorage.nethermelt.blockentitys.RootBlockEntity;
import me.mangorage.nethermelt.blocks.RootBlock;
import me.mangorage.nethermelt.util.DefaultProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public class TriggerRemoteItem extends Item {
    public TriggerRemoteItem() {
        super(DefaultProperties.ITEM().tab(NetherMelt.CreativeTab));
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if (level.isClientSide)
            return InteractionResult.PASS;
        if (!stack.hasTag())
            stack.setTag(new CompoundTag());

        if (player.isCrouching()) {
            if (level.getBlockState(pos).getBlock() instanceof RootBlock) {
                CompoundTag tag = stack.getTag();
                if (!tag.contains("roots"))
                    tag.put("roots", new CompoundTag());
                CompoundTag rootstag = tag.getCompound("roots");
                rootstag.put("" + rootstag.size() + 1, NbtUtils.writeBlockPos(pos));
            }
        } else {
            CompoundTag tag = stack.getTag();

            if (tag.contains("roots")) {
                CompoundTag roots = tag.getCompound("roots");
                roots.getAllKeys().forEach(Key -> {

                    BlockPos rootpos = NbtUtils.readBlockPos(roots.getCompound(Key));

                    if (level.getBlockState(rootpos).getBlock() instanceof RootBlock) {
                        level.setBlock(rootpos, level.getBlockState(rootpos).setValue(RootBlock.ACTIVATED, true), Block.UPDATE_ALL);
                    }

                });
            }

        }


        return InteractionResult.PASS;
    }


}
