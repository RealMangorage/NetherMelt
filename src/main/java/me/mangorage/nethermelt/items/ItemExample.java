package me.mangorage.nethermelt.items;

import me.mangorage.nethermelt.NetherMelt;
import me.mangorage.nethermelt.util.DefaultProperties;
import net.minecraft.world.item.Item;

public class ItemExample extends Item {
    public ItemExample() {
        super(DefaultProperties.ITEM.stacksTo(1));
    }
}
