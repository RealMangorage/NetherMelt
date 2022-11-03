package me.mangorage.nethermelt.common.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import static me.mangorage.nethermelt.common.core.Constants.MODID;

public class ModBlockTags {
    public static final TagKey<Block> CAN_CORRODE = BlockTags.create(new ResourceLocation(MODID, "can_corrode"));


}
