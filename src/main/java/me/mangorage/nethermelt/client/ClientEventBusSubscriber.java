package me.mangorage.nethermelt.client;

import me.mangorage.nethermelt.client.render.FoamBlockRenderer;
import me.mangorage.nethermelt.client.render.ModFallingBlockRenderer;
import me.mangorage.nethermelt.core.Registration;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;

import static me.mangorage.nethermelt.core.Constants.MODID;


@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void setup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(Registration.BLOCK_FOAM.get(), RenderType.cutout());
        ItemBlockRenderTypes.setFancy(true);

    }
    @SubscribeEvent
    public static void onEntityRendererEvent(EntityRenderersEvent.RegisterRenderers event) {
        LogManager.getLogger().fatal("Registering ENTITY RENDER'S");
        event.registerBlockEntityRenderer((BlockEntityType) Registration.BLOCKENTITY_FOAM.get(), new FoamBlockRenderer.Provider());
        event.registerEntityRenderer(Registration.ENTITY_FALLINGBLOCK.get(), new ModFallingBlockRenderer.Provider());
    }


}
