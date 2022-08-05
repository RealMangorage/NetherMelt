package me.mangorage.nethermelt.core;

import me.mangorage.nethermelt.blocks.RootBlock;
import me.mangorage.nethermelt.items.RootBlockItem;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public enum RootType {
    NETHER("netherroot", "minecraft", Level.NETHER, "Nether Foam Source", "The Nether"),
    OVERWORLD("overworldroot", "minecraft", Level.OVERWORLD, "Overworld Foam Source", "The Overworld"),
    UNKNOWN();

    private final String name;
    private final String MODID;
    private final ResourceKey<Level> level;
    private final String BlockDisplayText;
    private final String DimDisplayText;

    RootType(String name, String MODID, ResourceKey<Level> level, String BlockDisplayText, String DimDisplayText) {
        this.name = name;
        this.MODID = MODID;
        this.level = level;
        this.BlockDisplayText = BlockDisplayText;
        this.DimDisplayText = DimDisplayText;
    }

    RootType() {
        this.name = null;
        this.MODID = null;
        this.level = null;
        this.BlockDisplayText = null;
        this.DimDisplayText = null;
    }

    public String getName() {
        return name;
    }
    public String getModID() { return MODID; }
    public ResourceKey<Level> getLevel() { return level; };

    public String getBlockDisplayText() {return BlockDisplayText;};
    public String getDimDisplayText() {return DimDisplayText;}

    public RootBlockItem getLiveVariantItem() {
        return switch (this) {
            case NETHER -> Registration.ITEM_ROOT_NETHER.get();
            case OVERWORLD -> Registration.ITEM_ROOT_OVERWORLD.get();
            case UNKNOWN -> null;
        };
    }

    public RootBlock getLiveVariantBlock() {
        return switch (this) {
            case NETHER -> Registration.BLOCK_ROOT_NETHER.get();
            case OVERWORLD -> Registration.BLOCK_ROOT_OVERWORLD.get();
            case UNKNOWN -> null;
        };
    }

    public static RootType fromName(String Name) {
        for (RootType type : RootType.values()) {
            if (type.getName().equals(Name)) {
                return type;
            }
        }

        return UNKNOWN;
    }
}
