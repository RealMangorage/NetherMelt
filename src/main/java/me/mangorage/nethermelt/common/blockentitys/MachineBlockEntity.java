package me.mangorage.nethermelt.common.blockentitys;

import me.mangorage.nethermelt.common.core.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MachineBlockEntity extends BlockEntity {
    public MachineBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(Registration.MACHINE_BLOCKENTITY.get(), pPos, pBlockState);
    }
}
