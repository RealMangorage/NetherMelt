package me.mangorage.nethermelt.client;

import me.mangorage.nethermelt.client.render.FoamBlockRenderer;
import me.mangorage.nethermelt.client.render.ModFallingBlockRenderer;
import me.mangorage.nethermelt.client.gui.screen.MachineScreen;
import me.mangorage.nethermelt.common.core.Registration;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static me.mangorage.nethermelt.common.core.Constants.MOD_ID;


@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void setup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(Registration.MACHINE_CONTAINER.get(), MachineScreen::new);
        });
    }
    @SubscribeEvent
    public static void onEntityRendererEvent(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer((BlockEntityType) Registration.BLOCKENTITY_FOAM.get(), new FoamBlockRenderer.Provider());
        event.registerEntityRenderer(Registration.ENTITY_FALLINGBLOCK.get(), new ModFallingBlockRenderer.Provider());
    }


}
