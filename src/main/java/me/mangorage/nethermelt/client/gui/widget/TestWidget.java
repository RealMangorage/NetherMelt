package me.mangorage.nethermelt.client.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;

import java.awt.*;

public class TestWidget implements Widget {
    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        Font fr = Minecraft.getInstance().font;
        GuiComponent.drawString(pPoseStack, fr, "Text", pMouseX, pMouseY, Color.BLUE.getRGB());
    }
}
