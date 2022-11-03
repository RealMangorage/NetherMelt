package me.mangorage.nethermelt.common.config;

import me.mangorage.nethermelt.common.core.RegistryCollection;
import net.minecraftforge.common.ForgeConfigSpec;
import java.util.HashMap;

public class NetherMeltConfig {

    private static HashMap<String, HashMap<Type, ForgeConfigSpec.ConfigValue<?>>> CONFIGS = new HashMap<>();
    public static void registerCommonConfig(ForgeConfigSpec.Builder SERVER_BUILDER) {
        SERVER_BUILDER.comment("Settings for Nethermelt Mod").push("nethermelt");


        SERVER_BUILDER.push("default");
        /// Handle Default Values and use these for the configs below!
        SERVER_BUILDER.pop();



        RegistryCollection.getVariantIDs().forEach(variantID -> {
            RegistryCollection collection = RegistryCollection.getVariant(variantID);
            RegistryCollection.Properties data = collection.PROPERTIES;

            SERVER_BUILDER.push(data.getName());

            HashMap<Type, ForgeConfigSpec.ConfigValue<?>> CONFIG = new HashMap<>();

            CONFIG.put(
                    Type.DEFAULT_CHARGES,
                    SERVER_BUILDER.define("DEFAULT_CHARGES", 10)
            );

            CONFIG.put(
                    Type.MAX_CHARGES,
                    SERVER_BUILDER.define("MAX_CHARGES", 100)
            );

            CONFIG.put(
                    Type.RANGE,
                    SERVER_BUILDER.define("RANGE", 10)
            );


            CONFIGS.put(variantID, CONFIG);
            SERVER_BUILDER.pop(); // Continue to next type

            RegistryCollection.getVariant(variantID).BLOCK_ROOT.get().getConfig().load();
        });


    }

    public static HashMap<Type, ForgeConfigSpec.ConfigValue<?>> getConfig(String variantID) {
        return CONFIGS.get(variantID);
    }


    public enum Type {
        DEFAULT_CHARGES,
        MAX_CHARGES,
        RANGE
    }

}
