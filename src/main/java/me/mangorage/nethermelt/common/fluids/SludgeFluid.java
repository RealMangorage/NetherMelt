package me.mangorage.nethermelt.common.fluids;

import me.mangorage.nethermelt.common.core.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.WaterFluid;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidType;

import static net.minecraft.world.level.material.FlowingFluid.LEVEL;

public class SludgeFluid extends FlowingFluid {

    @Override
    public FluidType getFluidType() {
        return new FluidType(FluidType.Properties.create().rarity(Rarity.RARE).canHydrate(false).canSwim(false).canExtinguish(false).viscosity(1000).temperature(4000));
    }

    @Override
    public Fluid getFlowing() {
        return Registration.SLUDGE_FLOWING.get();
    }

    @Override
    public Fluid getSource() {
        return Registration.SLUDGE.get();
    }

    @Override
    protected boolean canConvertToSource() {
        return false;
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {

    }

    @Override
    protected int getSlopeFindDistance(LevelReader pLevel) {
        return 0;
    }

    @Override
    protected int getDropOff(LevelReader pLevel) {
        return 0;
    }

    @Override
    public Item getBucket() {
        return Registration.SLUDGE_BUCKET_ITEM.get();
    }

    @Override
    protected boolean canBeReplacedWith(FluidState pState, BlockGetter pLevel, BlockPos pPos, Fluid pFluid, Direction pDirection) {
        return false;
    }

    @Override
    public int getTickDelay(LevelReader pLevel) {
        return 0;
    }

    @Override
    protected float getExplosionResistance() {
        return 50;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState pState) {
        return Registration.SLUDGE_BLOCK.get().defaultBlockState().setValue(LiquidBlock.LEVEL, Integer.valueOf(getLegacyLevel(pState)));
    }

    @Override
    public boolean isSource(FluidState pState) {
        return false;
    }

    @Override
    public int getAmount(FluidState pState) {
        return 0;
    }

    public static class Flowing extends SludgeFluid {
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> pBuilder) {
            super.createFluidStateDefinition(pBuilder);
            pBuilder.add(LEVEL);
        }

        public int getAmount(FluidState pState) {
            return pState.getValue(LEVEL);
        }

        public boolean isSource(FluidState pState) {
            return false;
        }
    }

    public static class Source extends SludgeFluid {
        public int getAmount(FluidState pState) {
            return 8;
        }

        public boolean isSource(FluidState pState) {
            return true;
        }
    }
}
