package me.mangorage.nethermelt.common.blockentitys;

import me.mangorage.nethermelt.common.core.ITickable;
import me.mangorage.nethermelt.common.core.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MachineBlockEntity extends BlockEntity implements ITickable.Server {

    private final ItemHandler itemHandler = new ItemHandler();
    private final FluidHandler fluidHandler = new FluidHandler(1000);
    private final EnergyHandler energyHandler = new EnergyHandler(1000);

    private final LazyOptional<IItemHandler> ITEM_HANDLER = LazyOptional.of(() -> itemHandler);
    private final LazyOptional<IFluidHandler> FLUID_HANDLER = LazyOptional.of(() -> fluidHandler);
    private final LazyOptional<IEnergyStorage> ENERGY_HANDLER = LazyOptional.of(() -> energyHandler);


    private int ticks = 0;

    public MachineBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(Registration.MACHINE_BLOCKENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void serverTick() {
        ticks++;
        if (ticks % 100 == 0) {
            // Turn one Degraded Foam into sludge if we have more than 500 Energy Units
            if (energyHandler.removeEnergy(500, true) && itemHandler.removeItem(1, true) && fluidHandler.fill(new FluidStack(Registration.SLUDGE.get(), 10), IFluidHandler.FluidAction.SIMULATE) == 10) {
                energyHandler.removeEnergy(500, false);
                itemHandler.removeItem(1, false);
                fluidHandler.fill(new FluidStack(Registration.SLUDGE.get(), 10), IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return ITEM_HANDLER.cast();

        if (cap == ForgeCapabilities.ENERGY)
            return ENERGY_HANDLER.cast();

        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return FLUID_HANDLER.cast();

        return super.getCapability(cap, side);
    }

    private class ItemHandler extends ItemStackHandler {
        ItemHandler() {
            super(1);
        }

        private boolean removeItem(int amount, boolean simulate) {
            if (getStackInSlot(1).getCount() >= amount) {
                if (!simulate) {
                    getStackInSlot(1).shrink(amount);
                    onContentsChanged(1);
                }
                return true;
            }
            return false;
        }


        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return super.extractItem(slot, 0, simulate);
        }
    }

    private class EnergyHandler extends EnergyStorage {

        public EnergyHandler(int capacity) {
            super(capacity);
        }

        private boolean removeEnergy(int amount, boolean simulate) {
            if (energy >= amount) {
                if (!simulate)
                    energy -= amount;
                return true;
            }
            return false;
        }

        @Override
        public boolean canExtract() {
            return false;
        }


    }

    private class FluidHandler extends FluidTank {

        public FluidHandler(int capacity) {
            super(capacity);
            setFluid(new FluidStack(Registration.SLUDGE.get(), 0));
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid().isSame(Registration.SLUDGE.get());
        }
    }
}
