package me.mangorage.nethermelt.common.blocks;

import net.minecraft.world.level.block.Block;

public class VariantBlock extends Block {
    private final String variantID;


    public VariantBlock(Properties pProperties, String variantID) {
        super(pProperties);
        this.variantID = variantID;
    }

    public String getVariantID() {
        return variantID;
    }
}
