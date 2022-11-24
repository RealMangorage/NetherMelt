package me.mangorage.nethermelt.common.compat.theoneprobe;

import mcjty.theoneprobe.api.*;
import me.mangorage.nethermelt.common.blockentitys.RootBlockEntity;
import me.mangorage.nethermelt.common.blocks.RootBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

import static me.mangorage.nethermelt.common.core.Constants.BlockStateProperties.ACTIVATED;
import static me.mangorage.nethermelt.common.core.Constants.MOD_ID;

public class TOPPlugin implements Function<ITheOneProbe, Void>, IProbeInfoProvider {

    @Override
    public Void apply(ITheOneProbe top) {
        top.registerProvider(this);
        return null;
    }

    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(MOD_ID + ":top_support");
    }

    @Override
    public void addProbeInfo(ProbeMode Mode, IProbeInfo info, Player player, Level level, BlockState state, IProbeHitData data) {
        if (state.getBlock() instanceof RootBlock && level.getBlockEntity(data.getPos()) instanceof RootBlockEntity RBE) {
            info.mcText(Component.literal("Activated: ").withStyle(ChatFormatting.WHITE).append(Component.literal(state.getValue(ACTIVATED) + "").withStyle(ChatFormatting.GOLD)));
            info.mcText(Component.literal("Charges Remaining: ").withStyle(ChatFormatting.WHITE).append(Component.literal("" + RBE.getCharges()).withStyle(ChatFormatting.GOLD)));
            if (Mode == ProbeMode.DEBUG) {
                info.mcText(Component.literal("Foam: " + RBE.getCore().getFoamCount()));
                info.mcText(Component.literal("Range: " + RBE.getCore().getRange()));
                info.progress(100, 100, info.defaultProgressStyle());
            }
        }
    }
}
