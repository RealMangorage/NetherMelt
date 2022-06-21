package me.mangorage.nethermelt.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;

public class NetherMeltConfig {

    public static ForgeConfigSpec.ConfigValue<ArrayList<String>> RESISTANT;
    public static ForgeConfigSpec.ConfigValue<ArrayList<String>> FALLING_BLOCKS;

    public static void registerCommonConfig(ForgeConfigSpec.Builder COMMON_BUILDER) {
        COMMON_BUILDER.comment("Settings for Nethermelt Mod").push("nethermelt");

        RESISTANT = COMMON_BUILDER
                .comment("List of Tags and Blocks that cant be Melted by Nether Foam Source!")
                .comment("Use 'tag:forge:ores' for Tags")
                .comment("Use 'block:minecraft:stone' for Blocks")
                .define("RESISTANT", getDefaultConfigs(ConfigType.RESISTANT));
        FALLING_BLOCKS = COMMON_BUILDER
                .comment("List of Tags and Blocks defining what can can fall to the floor after Nether Foam Source Dies")
                .comment("Use 'tag:forge:ores' for Tags")
                .comment("Use 'block:minecraft:stone' for Blocks")
                .define("FALLING_BLOCKS", getDefaultConfigs(ConfigType.FALLING));


        COMMON_BUILDER.pop();
    }



    private static ArrayList<String> getDefaultConfigs(ConfigType type) {
        ArrayList<String> result = new ArrayList<>();

        switch (type) {
            case RESISTANT -> {
                result.add("tag:forge:ores");
                result.add("block:minecraft:air");
                result.add("block:minecraft:cave_air");
                result.add("block:minecraft:void_air");
            }
            case FALLING -> {

            }

        }
        return result;
    }

    public enum ConfigType {
        RESISTANT,
        FALLING
    }


}
