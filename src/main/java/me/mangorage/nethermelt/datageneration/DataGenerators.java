package me.mangorage.nethermelt.datageneration;

import me.mangorage.nethermelt.datageneration.localization.en_us;
import me.mangorage.nethermelt.NetherMelt;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;


@Mod.EventBusSubscriber(modid = NetherMelt.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)  {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper FileHelper = event.getExistingFileHelper();

        if (event.includeServer()) {
            generator.addProvider(new GenBlockTags(generator, FileHelper));
            generator.addProvider(new GenRecipes(generator));
            generator.addProvider(new GenLootTables(generator));
        }

        if (event.includeClient()) {
            generator.addProvider(new GenBlockStateProvider(generator, FileHelper));
            generator.addProvider(new GenItemModelProvider(generator, FileHelper));
            generator.addProvider(new en_us(generator));
        }
    }
}
