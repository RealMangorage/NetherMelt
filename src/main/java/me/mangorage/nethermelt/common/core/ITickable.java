package me.mangorage.nethermelt.common.core;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface ITickable {
    default void tick() {};

    static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        if (blockEntity instanceof ITickable T) {
            T.tick();
            return;
        }

        if (level.isClientSide()) {
            if (blockEntity instanceof ITickable.Client T) {
                T.clientTick();
                return;
            }
        } else {
            if (blockEntity instanceof ITickable.Server T) {
                T.serverTick();
                return;
            }
        }
    }

    interface Server {
        default void serverTick() {};
    }

    interface Client {
        default void clientTick() {};
    }
}
