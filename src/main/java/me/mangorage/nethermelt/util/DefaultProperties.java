package me.mangorage.nethermelt.util;

import me.mangorage.nethermelt.NetherMelt;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class DefaultProperties {

    public static final Item.Properties ITEM = new Item.Properties().tab(NetherMelt.CreativeTab);
    public static final BlockBehaviour.Properties BLOCK = BlockBehaviour.Properties.of(Material.STONE);


}
