package me.mangorage.nethermelt.common.core;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class Constants {
    public final static String MODID = "nethermelt";
    public final static Integer MAX_STAGES = 3;


    public class BlockStateProperties {
        public final static IntegerProperty STAGE = IntegerProperty.create("stage", 1, MAX_STAGES);
        public final static BooleanProperty VISIBLE = BooleanProperty.create("visible");
        public final static BooleanProperty ACTIVATED = BooleanProperty.create("activated");
    }

    public static class Translatable {
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
}
