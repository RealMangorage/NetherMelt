package me.mangorage.nethermelt;

import me.mangorage.nethermelt.common.compat.theoneprobe.TOPPlugin;
import me.mangorage.nethermelt.common.config.Config;
import me.mangorage.nethermelt.common.core.Registration;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static me.mangorage.nethermelt.common.core.Constants.MODID;

@Mod(MODID)
public class NetherMeltCore {
    public static final Logger logger = LogManager.getLogger(NetherMeltCore.class);
    public static IEventBus ForgeEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    public static IEventBus MinecraftEventBus = MinecraftForge.EVENT_BUS;
    public static CreativeModeTab CreativeTab = new CreativeModeTab("nethermelt") {
        @Override
        public ItemStack makeIcon() {
            return Registration.NETHER.ITEM_ROOT.get().getDefaultInstance();
        }
    };

    public NetherMeltCore() {
        Registration.init();

        ForgeEventBus.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void setup(final FMLCommonSetupEvent event) {
        Config.register();

        if (ModList.get().isLoaded("theoneprobe")) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", TOPPlugin::new);
        }
    }
}
