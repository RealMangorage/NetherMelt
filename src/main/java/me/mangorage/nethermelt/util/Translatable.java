package me.mangorage.nethermelt.util;

import net.minecraft.network.chat.TranslatableComponent;

import java.awt.*;

public class Translatable {
    private String key;
    private TranslatableComponent component;

    public static final Translatable
            ROOT_TOOLTIP_WRONG_DIMENSION = new Translatable("tooltip.nethermelt.root.error.incorrectdimension");


    Translatable(String key) {
        this.key = key;
        this.component = new TranslatableComponent(key);
    }

    public TranslatableComponent getComponent() {
        return component;
    }

    public String getKey() {
        return key;
    }

}
