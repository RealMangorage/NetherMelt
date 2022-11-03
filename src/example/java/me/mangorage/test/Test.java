package me.mangorage.test;

import me.mangorage.test.core.Constants;
import me.mangorage.test.core.Registration;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Constants.MODID)
public class Test {
    public static final Logger logger = LogManager.getLogger(Test.class);

    public static IEventBus ForgeEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    public static IEventBus MinecraftEventBus = MinecraftForge.EVENT_BUS;

    public static final CreativeModeTab creativeTab = new CreativeModeTab("example") {
        @Override
        public ItemStack makeIcon() {
            return Items.PAPER.getDefaultInstance();
        }
    }.hideScroll();

    public Test() {
        Registration.init();

        ForgeEventBus.register(this);
    }
}