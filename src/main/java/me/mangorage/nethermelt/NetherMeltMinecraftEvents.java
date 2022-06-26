package me.mangorage.nethermelt;

import me.mangorage.nethermelt.blockentitys.FoamBlockEntity;
import me.mangorage.nethermelt.commands.ModCommands;
import me.mangorage.nethermelt.util.Core;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;

public class NetherMeltMinecraftEvents {

    @SubscribeEvent
    public void onDestroy(BlockEvent.BreakEvent event) {
        if (event.getWorld().isClientSide())
            return;

        ServerLevel level = (ServerLevel) event.getWorld();
        BlockPos pos = event.getPos();

        if (level.getBlockEntity(pos) instanceof FoamBlockEntity) {
            FoamBlockEntity BE = (FoamBlockEntity) level.getBlockEntity(pos);
            if (BE.getRoot() != null) {
                NetherMelt.getCore().Die(BE, Core.FoamDeathType.DEFAULT);
            }
        }
    }

    @SubscribeEvent
    public void serverLoad(RegisterCommandsEvent event) {
        LogManager.getLogger().error("Loading Command!");
        ModCommands.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void WorldLoad(WorldEvent.Load event) {
        if (!NetherMelt.getCore().isLoaded()) {
            NetherMelt.getCore().init();
        }
    }

    @SubscribeEvent
    public void WorldUnload(WorldEvent.Unload event) {
        if (NetherMelt.getCore().isLoaded()) {
            NetherMelt.getCore().unLoad();
        }
    }
}
