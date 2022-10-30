package me.mangorage.nethermelt;

import me.mangorage.nethermelt.api.IResistantBlocksProvider;
import me.mangorage.nethermelt.common.compat.nethermelt.ResistantBlocksProvider;
import me.mangorage.nethermelt.common.compat.theoneprobe.TOPPlugin;
import me.mangorage.nethermelt.common.config.Config;
import me.mangorage.nethermelt.common.core.Constants;
import me.mangorage.nethermelt.common.core.Registration;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static me.mangorage.nethermelt.common.core.Constants.MODID;

//TODO: FIX DATA GENERATION
//TODO: ADD MORE TYPES OF BLOCKS!
@Mod(MODID)
public class NetherMelt {
    public static final Logger logger = LogManager.getLogger(NetherMelt.class);
    public static final List<Class<? extends Block>> RESISTANT_BLOCKS_CLASSZ = new ArrayList<>();
    public static final List<Block> RESISTANT_BLOCKS = new ArrayList<>();

    public static IEventBus ForgeEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    public static IEventBus MinecraftEventBus = MinecraftForge.EVENT_BUS;

    public static boolean isResistant(Block block) {
        if (RESISTANT_BLOCKS.contains(block))
            return true;

        return isResistant(block.getClass());
    }

    public static boolean isResistant(Class classz) {
        return RESISTANT_BLOCKS_CLASSZ.contains(classz);
    }



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

        InterModComms.sendTo(MODID, "getResistantBlocksProvider", ResistantBlocksProvider::new);

    }

    @SubscribeEvent
    public void onIMC(final InterModProcessEvent event) {
        event.getIMCStream().forEach(message -> {
            String modID = message.modId();
            String method = message.method();
            String sender = message.senderModId();
            Object object = message.messageSupplier().get();

            if (modID.equals(MODID) && method.equals("getResistantBlocksProvider")) {
                if (object instanceof IResistantBlocksProvider RBP) {
                    logger.info("Recieved IResistantBlocksProvider from {}", sender);

                    RBP.addBlockClassz(RESISTANT_BLOCKS_CLASSZ);
                    RBP.addBlocks(RESISTANT_BLOCKS);
                }
            }
        });
    }


}
