package me.mangorage.nethermelt.util;

import me.mangorage.nethermelt.NetherMelt;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class DefaultProperties {
    public static Item.Properties ITEM() {
        return new Item.Properties().tab(NetherMelt.CreativeTab);
    }

    public static BlockBehaviour.Properties BLOCK(Material material) {
        return BlockBehaviour.Properties.of(material);
    }

    public static BlockBehaviour.Properties BLOCK() {
        return BLOCK(Material.STONE);
    }
}
