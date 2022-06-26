package me.mangorage.nethermelt;

import me.mangorage.nethermelt.client.ClientForgeEvents;
import me.mangorage.nethermelt.client.ClientMinecraftEvents;
import me.mangorage.nethermelt.compat.theoneprobe.TOPPlugin;
import me.mangorage.nethermelt.config.Config;
import me.mangorage.nethermelt.datageneration.DataGenerators;
import me.mangorage.nethermelt.client.render.ModFallingBlockRenderer;
import me.mangorage.nethermelt.setup.Registry;
import me.mangorage.nethermelt.util.Core;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(NetherMelt.MOD_ID)
public class NetherMelt {
    private static final Logger LOGGER = LogManager.getLogger("nethermelt");
    private static final Core core = new Core();
    public static final String MOD_ID = "nethermelt";

    public static IEventBus ForgeEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    public static IEventBus MinecraftEventBus = MinecraftForge.EVENT_BUS;

    public static CreativeModeTab CreativeTab = new CreativeModeTab("nethermelt") {
        @Override
        public ItemStack makeIcon() {
            return Registry.ITEM_ROOT.get().getDefaultInstance();
        }
    };


    public NetherMelt() {
        Registry.init();

        ForgeEventBus.addListener(DataGenerators::gatherData);
        ForgeEventBus.register(new NetherMeltForgeEvents());

    }

    public static Core getCore() {
        return core;
    }


}
