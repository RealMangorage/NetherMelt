package me.mangorage.nethermelt.config;

import me.mangorage.nethermelt.core.RegistryCollection;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.HashMap;

public class NetherMeltConfig {

    private static HashMap<String, HashMap<Type, ForgeConfigSpec.ConfigValue<?>>> CONFIGS = new HashMap<>();
    public static void registerCommonConfig(ForgeConfigSpec.Builder SERVER_BUILDER) {
        SERVER_BUILDER.comment("Settings for Nethermelt Mod").push("nethermelt");


        SERVER_BUILDER.push("default");
        /// Handle Default Values and use these for the configs below!
        SERVER_BUILDER.pop();



        RegistryCollection.getVariantIDs().forEach(type -> {
            RegistryCollection collection = RegistryCollection.getVariant(type);
            RegistryCollection.Properties data = collection.PROPERTIES;

            SERVER_BUILDER.push(data.getName());

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

    public static HashMap<Type, ForgeConfigSpec.ConfigValue<?>> getConfig(String type) {
        return CONFIGS.get(type);
    }


    public enum Type {
        RESISTANT,
        FALLING_BLOCKS,
        CHARGES,
        RANGE;
    }

}
