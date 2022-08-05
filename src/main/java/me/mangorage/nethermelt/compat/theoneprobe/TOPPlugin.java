package me.mangorage.nethermelt.compat.theoneprobe;

import mcjty.theoneprobe.api.*;
import me.mangorage.nethermelt.blockentitys.RootBlockEntity;
import me.mangorage.nethermelt.blocks.RootBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Function;
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
        final Block block = state.getBlock();

        if (block instanceof RootBlock) {
            BlockEntity blockEntity = level.getBlockEntity(data.getPos());

            if (blockEntity instanceof RootBlockEntity BE) {
                info.mcText(new TextComponent("Charges Remaining: ").withStyle(ChatFormatting.WHITE).append(new TextComponent("" + BE.getCharges()).withStyle(ChatFormatting.GOLD)));
            }
        }
    }
}
