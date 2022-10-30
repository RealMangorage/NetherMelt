package me.mangorage.test.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class ExampleBlock extends Block {
    public ExampleBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL).dynamicShape().noOcclusion().friction(-100f).color(MaterialColor.COLOR_LIGHT_GREEN).lootFrom(() -> Blocks.STONE).speedFactor(100f));
    }

}
