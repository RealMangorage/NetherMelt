package me.mangorage.nethermelt.config;

import me.mangorage.nethermelt.core.RootType;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class NetherMeltConfig {

    private static HashMap<RootType, HashMap<Type, ForgeConfigSpec.ConfigValue<?>>> CONFIGS = new HashMap<>();
    public static void registerCommonConfig(ForgeConfigSpec.Builder SERVER_BUILDER) {
        SERVER_BUILDER.comment("Settings for Nethermelt Mod").push("nethermelt");


        SERVER_BUILDER.push("default");
        /// Handle Default Values and use these for the configs below!
        SERVER_BUILDER.pop();



        Arrays.stream(RootType.values()).sequential().forEach(type -> {
            if (type == RootType.UNKNOWN) return; // We dont need to do it for this lmao, its UNKNOWN for a reason

            SERVER_BUILDER.push(type.getName());
            HashMap<Type, ForgeConfigSpec.ConfigValue<?>> CONFIG = new HashMap<>();


            CONFIG.put(
                    Type.RESISTANT,
                    SERVER_BUILDER.define("RESISTANT", new ArrayList())
            );
            CONFIG.put(
                    Type.FALLING_BLOCKS,
                    SERVER_BUILDER.define("FALLING_BLOCKS", new ArrayList())
            );
            CONFIG.put(
                    Type.CHARGES,
                    SERVER_BUILDER.define("CHARGES", 10)
            );
            CONFIG.put(
                    Type.RANGE,
                    SERVER_BUILDER.define("RANGE", 10)
            );


            CONFIGS.put(type, CONFIG);
            SERVER_BUILDER.pop(); // Continue to next type


        });


    }

    public static HashMap<Type, ForgeConfigSpec.ConfigValue<?>> getConfig(RootType type) {
        return CONFIGS.get(type);
    }


    public enum Type {
        RESISTANT,
        FALLING_BLOCKS,
        CHARGES,
        RANGE;
    }

}
