package datageneration;

import datageneration.localization.en_us;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import static me.mangorage.nethermelt.common.core.Constants.MOD_ID;


@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)  {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper FileHelper = event.getExistingFileHelper();
        boolean client = event.includeClient();
        boolean server = event.includeServer();
        boolean dev = event.includeDev();

        generator.addProvider(server, new GenBlockTags(generator, FileHelper));
        generator.addProvider(server, new GenRecipes(generator));
        generator.addProvider(server, new GenLootTables(generator));


        generator.addProvider(client, new GenBlockStateProvider(generator, FileHelper));
        generator.addProvider(client, new GenItemModelProvider(generator, FileHelper));
        generator.addProvider(client, new en_us(generator));

    }
}
