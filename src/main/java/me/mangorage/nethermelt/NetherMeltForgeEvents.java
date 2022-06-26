package me.mangorage.nethermelt;

import me.mangorage.nethermelt.client.ClientForgeEvents;
import me.mangorage.nethermelt.client.ClientMinecraftEvents;
import me.mangorage.nethermelt.client.render.ModFallingBlockRenderer;
import me.mangorage.nethermelt.compat.theoneprobe.TOPPlugin;
import me.mangorage.nethermelt.config.Config;
import me.mangorage.nethermelt.setup.Registry;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class NetherMeltForgeEvents {
    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        NetherMelt.MinecraftEventBus.register(new NetherMeltMinecraftEvents());
        Config.register();

        if (ModList.get().isLoaded("theoneprobe")) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", TOPPlugin::new);
        }

    }
    @SubscribeEvent
    public void setupClient(final FMLClientSetupEvent event) {
        NetherMelt.MinecraftEventBus.register(new ClientMinecraftEvents());
        NetherMelt.ForgeEventBus.register(new ClientForgeEvents());
    }
}
