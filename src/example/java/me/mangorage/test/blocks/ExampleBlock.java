package me.mangorage.test.blocks;

import com.google.common.collect.ImmutableMap;
import me.mangorage.nethermelt.api.IResistant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;

import java.util.function.Function;

public class ExampleBlock extends Block implements IResistant {
    public ExampleBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL).dynamicShape().noOcclusion().friction(-100f).color(MaterialColor.COLOR_LIGHT_GREEN).lootFrom(() -> Blocks.STONE).speedFactor(100f));
    }

}
