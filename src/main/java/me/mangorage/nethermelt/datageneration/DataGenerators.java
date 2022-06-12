package me.mangorage.nethermelt.datageneration;

import me.mangorage.nethermelt.datageneration.localization.en_us;
import me.mangorage.nethermelt.datageneration.localization.LanguageType;
import me.mangorage.nethermelt.datageneration.tags.GenBlockTags;
import me.mangorage.nethermelt.NetherMelt;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;


@Mod.EventBusSubscriber(modid = NetherMelt.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)  {
        DataGenerator generator = event.getGenerator();

        if (event.includeServer()) {
            GenBlockTags blockTags = new GenBlockTags(generator, event.getExistingFileHelper());
            generator.addProvider(blockTags);

            GenRecipes GR = new GenRecipes(generator);
            generator.addProvider(GR);
        }

        if (event.includeClient()) {
            GenBlockStateProvider BSP = new GenBlockStateProvider(generator, event.getExistingFileHelper());
            generator.addProvider(BSP);

            GenItemModelProvider IMP = new GenItemModelProvider(generator, event.getExistingFileHelper());
            generator.addProvider(IMP);

            generator.addProvider(new en_us(generator));
        }
    }
}
