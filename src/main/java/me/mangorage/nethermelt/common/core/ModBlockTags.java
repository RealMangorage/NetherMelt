package me.mangorage.nethermelt.common.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import static me.mangorage.nethermelt.common.core.Constants.MOD_ID;

public class ModBlockTags {
    public static final TagKey<Block> CAN_CORRODE = BlockTags.create(new ResourceLocation(MOD_ID, "can_corrode"));
    public static final TagKey<Block> CAN_FALL = BlockTags.create(new ResourceLocation(MOD_ID, "can_fall"));

}
