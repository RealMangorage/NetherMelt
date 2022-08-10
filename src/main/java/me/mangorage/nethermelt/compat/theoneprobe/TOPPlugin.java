package me.mangorage.nethermelt.compat.theoneprobe;

import mcjty.theoneprobe.api.*;
import me.mangorage.nethermelt.blockentitys.RootBlockEntity;
import me.mangorage.nethermelt.blocks.RootBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

import static me.mangorage.nethermelt.core.Constants.BlockStateProperties.ACTIVATED;
import static me.mangorage.nethermelt.core.Constants.MODID;

public class TOPPlugin implements Function<ITheOneProbe, Void>, IProbeInfoProvider {

    @Override
    public Void apply(ITheOneProbe top) {
        top.registerProvider(this);
        return null;
    }

    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(MODID + ":top_support");
    }

    @Override
    public void addProbeInfo(ProbeMode Mode, IProbeInfo info, Player player, Level level, BlockState state, IProbeHitData data) {
        if (state.getBlock() instanceof RootBlock && level.getBlockEntity(data.getPos()) instanceof RootBlockEntity RBE) {
            info.mcText(new TextComponent("Activated: ").withStyle(ChatFormatting.WHITE).append(new TextComponent(state.getValue(ACTIVATED) + "").withStyle(ChatFormatting.GOLD)));
            info.mcText(new TextComponent("Charges Remaining: ").withStyle(ChatFormatting.WHITE).append(new TextComponent("" + RBE.getCharges()).withStyle(ChatFormatting.GOLD)));
            if (Mode == ProbeMode.DEBUG) {
                info.mcText(new TextComponent("Foam: " + RBE.getCore().getFoamCount()));
                info.mcText(new TextComponent("Range: " + RBE.getCore().getRange()));
                info.progress(100, 100, info.defaultProgressStyle());
            }
        }
    }
}
