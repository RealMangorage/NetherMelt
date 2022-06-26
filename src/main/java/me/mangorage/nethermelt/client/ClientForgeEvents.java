package me.mangorage.nethermelt.client;

import me.mangorage.nethermelt.NetherMelt;
import me.mangorage.nethermelt.blocks.FoamBlock;
import me.mangorage.nethermelt.client.render.FoamBlockBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;

public class ClientForgeEvents {
    @SubscribeEvent
    public void onModelBake(ModelBakeEvent e) {
        LogManager.getLogger().info("Loading Baked Models!");

        FoamBlock.STAGE.getPossibleValues().forEach(stage -> {
            var rl = new ModelResourceLocation(new ResourceLocation(NetherMelt.MOD_ID, "foam"), "stage=" + stage);
            e.getModelRegistry().put(rl, new FoamBlockBakedModel(e.getModelManager().getModel(rl)));
        });

    }
}
