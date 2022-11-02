package me.mangorage.nethermelt.api;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.InterModComms;

import java.util.List;
import java.util.function.Supplier;

import static me.mangorage.nethermelt.api.Constants.IMCMethods.getResistantBlockProvider;
import static me.mangorage.nethermelt.api.Constants.MODID;

public interface IResistantBlocksProvider {
    default void addBlocks(List<Block> blockList) {}
    default void addBlockClassz(List<Class<? extends Block>> blockClasszList) {}

    default void sendIMC() {
        InterModComms.sendTo(MODID, getResistantBlockProvider, () -> this);
    }

    static void sendIMC(Supplier<IResistantBlocksProvider> provider) {
        InterModComms.sendTo(MODID, getResistantBlockProvider, provider);
    }
}
