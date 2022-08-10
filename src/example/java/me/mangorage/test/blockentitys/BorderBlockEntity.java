package me.mangorage.test.blockentitys;

import me.mangorage.test.core.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BorderBlockEntity extends BlockEntity {
    public final EnergyStorage energyStorage = new EnergyStorage(10000, 100, 10000, 1000);
    public final LazyOptional<IEnergyStorage> lazyHandler = LazyOptional.of(() -> energyStorage);
    public final int powerPerHitpoint = 100;

    public BorderBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(Registration.BLOCK_ENTITY_BORDER.get(), pWorldPosition, pBlockState);
    }


    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return lazyHandler.cast();
        }

        return LazyOptional.empty();
    }

    private void killEntity(LivingEntity entity) {
        int usage = Math.round(entity.getHealth()) * powerPerHitpoint;

        if (energyStorage.extractEnergy(usage, true) == usage) { // Meaning we can extract a full usage!
            energyStorage.extractEnergy(usage, false);
            entity.kill();
            setChanged();
        }

    }

    public void entityStepped(Entity entity) {
        if (getLevel().isClientSide) return;
        if (entity instanceof LivingEntity LE) {
            if (LE instanceof Monster monster) {
                killEntity(monster);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("energy", energyStorage.serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        if (tag.contains("energy"))
            energyStorage.deserializeNBT(tag.get("energy"));
    }
}
