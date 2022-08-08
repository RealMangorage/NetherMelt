package me.mangorage.nethermelt;

import me.mangorage.nethermelt.compat.theoneprobe.TOPPlugin;
import me.mangorage.nethermelt.config.Config;
import me.mangorage.nethermelt.core.Registration;
import me.mangorage.nethermelt.core.Constants;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//TODO: FIX DATA GENERATION
//TODO: ADD MORE TYPES OF BLOCKS!
@Mod(Constants.MODID)
public class NetherMelt {
    public static final Logger logger = LogManager.getLogger(NetherMelt.class);

    public static IEventBus ForgeEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    public static IEventBus MinecraftEventBus = MinecraftForge.EVENT_BUS;

    public static CreativeModeTab CreativeTab = new CreativeModeTab("nethermelt") {
        @Override
        public ItemStack makeIcon() {
            return Registration.NETHER.ITEM_ROOT.get().getDefaultInstance();
        }
    };

    public NetherMelt() {
        Registration.init();

        ForgeEventBus.register(this);
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {
        Config.register();

        if (ModList.get().isLoaded("theoneprobe")) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", TOPPlugin::new);
        }

    }


}
