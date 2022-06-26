package me.mangorage.nethermelt.client;

import me.mangorage.nethermelt.NetherMelt;
import me.mangorage.nethermelt.blocks.FoamBlock;
import me.mangorage.nethermelt.client.render.FoamBlockBakedModel;
import me.mangorage.nethermelt.client.render.ModFallingBlockRenderer;
import me.mangorage.nethermelt.setup.Registry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;

import java.util.List;

public class ClientMinecraftEvents {

    public ClientMinecraftEvents() {
        ItemBlockRenderTypes.setRenderLayer(Registry.BLOCK_FOAM.get(), t -> true);
        EntityRenderers.register(Registry.ENTITY_FALLINGBLOCK.get(), ModFallingBlockRenderer::new);
    }

    @SubscribeEvent
    public void onHover(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() == Registry.ITEM_ROOT.get()) {
            ItemStack stack = event.getItemStack();

            if (stack.hasTag() && stack.getTag().contains("charges")) {
                int charges = stack.getTag().getInt("charges");

                List<Component> tooltip = event.getToolTip();

                tooltip.add(new TextComponent("Charges Remaining: ").withStyle(ChatFormatting.WHITE).append(new TextComponent("" + charges).withStyle(ChatFormatting.GOLD)));
            }

        }
    }
}
