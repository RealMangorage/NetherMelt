package me.mangorage.nethermelt.common.core;

import me.mangorage.nethermelt.api.IResistantBlocksProvider;
import me.mangorage.nethermelt.common.util.ResistantArrayList;
import net.minecraft.world.level.block.Block;
import org.checkerframework.checker.units.qual.A;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.Set;

public class ResistantHandler {
    private static ArrayList<Block> BLOCKS = new ResistantArrayList();
    private static ArrayList<Class<? extends Block>> BLOCKS_CLASS = new ResistantArrayList<>();


    public static void handle(IResistantBlocksProvider resistantBlocksProvider) {
        resistantBlocksProvider.addBlocks(BLOCKS);
        resistantBlocksProvider.addBlockClassz(BLOCKS_CLASS);
    }

    public static boolean isResistant(Class<? extends Block> classz) {
        return BLOCKS_CLASS.contains(classz);
    }

    public static boolean isResistant(Block block) {
        return BLOCKS_CLASS.contains(block) ? true : isResistant(block.getClass());
    }
}
