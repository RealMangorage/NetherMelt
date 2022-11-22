package me.mangorage.nethermelt.common.items;

import me.mangorage.nethermelt.NetherMeltCore;
import me.mangorage.nethermelt.common.blockentitys.RootBlockEntity;
import me.mangorage.nethermelt.common.blocks.RootBlock;
import me.mangorage.nethermelt.common.blocks.VariantBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class RootChargeItem extends Item {
    public RootChargeItem(Properties pProperties) {
        super(pProperties.stacksTo(200000));
    }


    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        if (level.isClientSide())
            return InteractionResult.sidedSuccess(true);


        if (level.getBlockEntity(pos) instanceof RootBlockEntity RBE) {
            RBE.addCharge(1);
            pContext.getItemInHand().shrink(1);
            level.playSound(pContext.getPlayer(), pos, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 1.0F, 0.0F);
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else if(level.getBlockState(pos).getBlock() instanceof VariantBlock VB) {
            RootBlock.setBlock(VB.getVariantID(), level, pos, 1);
        }

        return InteractionResult.FAIL;
    }
}
