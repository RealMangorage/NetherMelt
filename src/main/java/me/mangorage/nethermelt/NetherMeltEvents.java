package me.mangorage.nethermelt;

import me.mangorage.nethermelt.blockentitys.FoamBlockEntity;
import me.mangorage.nethermelt.util.FoamDeathType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NetherMeltEvents {

    @SubscribeEvent
    public void onDestroy(BlockEvent.BreakEvent event) {
        if (event.getWorld().isClientSide())
            return;

        ServerLevel level = (ServerLevel) event.getWorld();
        BlockPos pos = event.getPos();

        if (level.getBlockEntity(pos) instanceof FoamBlockEntity) {
            FoamBlockEntity BE = (FoamBlockEntity) level.getBlockEntity(pos);
            if (BE.getRoot() != null) {
                NetherMelt.getCore().Die(BE, FoamDeathType.DEFAULT);
            }
        }
    }
}
