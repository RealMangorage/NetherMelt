package me.mangorage.nethermelt;

import com.google.common.collect.ImmutableList;
import me.mangorage.nethermelt.blocks.FoamBlock;
import me.mangorage.nethermelt.config.Config;
import me.mangorage.nethermelt.datageneration.DataGenerators;
import me.mangorage.nethermelt.render.FallingBlockBakedModel;
import me.mangorage.nethermelt.render.FoamBlockBakedModel;
import me.mangorage.nethermelt.setup.Registry;
import me.mangorage.nethermelt.util.Core;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(NetherMelt.MOD_ID)
public class NetherMelt {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger("nethermelt");
    private static final Core core = new Core();
    public static final String MOD_ID = "nethermelt";

    IEventBus ForgeEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    IEventBus MinecraftEventBus = MinecraftForge.EVENT_BUS;



    public static CreativeModeTab CreativeTab = new CreativeModeTab("nethermelt") {
        @Override
        public ItemStack makeIcon() {
            return Registry.ITEM_ROOT.get().getDefaultInstance();
        }
    };


    public NetherMelt() {
        Registry.register(ForgeEventBus);

        ForgeEventBus.addListener(this::setup);
        ForgeEventBus.addListener(DataGenerators::gatherData);
        ForgeEventBus.addListener(this::onModelBake);
        ForgeEventBus.addListener(this::setupClient);

        ForgeEventBus.addGenericListener(Block.class, this::MappingFix);

        MinecraftEventBus.register(this);
        MinecraftEventBus.register(new NetherMeltEvents());

        Config.register();

    }

    public static Core getCore() {
        return core;
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        core.init();
    }

    public void setupClient(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(Registry.BLOCK_FOAM.get(), t -> true);
        ItemBlockRenderTypes.setRenderLayer(Registry.BLOCK_FALLING.get(), t -> true);
    }

    public void onModelBake(ModelBakeEvent e) {
        LogManager.getLogger().info("Loading Baked Models!");

        FoamBlock.STAGE.getPossibleValues().forEach(stage -> {
            var rl = new ModelResourceLocation(new ResourceLocation(NetherMelt.MOD_ID, "foam"), "stage=" + stage);
            e.getModelRegistry().put(rl, new FoamBlockBakedModel(e.getModelManager().getModel(rl)));
        });

        var rl = new ModelResourceLocation(new ResourceLocation(NetherMelt.MOD_ID, "falling"), "");
        e.getModelRegistry().put(rl, new FallingBlockBakedModel(e.getModelManager().getModel(rl)));

    }

    public void MappingFix(RegistryEvent.MissingMappings<Block> event) {
        ImmutableList a = event.getMappings(MOD_ID);

        LogManager.getLogger().error("Error! AJHSASO");

        a.forEach(mapping -> {

            RegistryEvent.MissingMappings.Mapping<Block> b = (RegistryEvent.MissingMappings.Mapping<Block>) mapping;

            b.remap(Registry.BLOCK_ROOT.get());
            b.remap(Registry.BLOCK_DEAD_ROOT.get());
            b.remap(Registry.BLOCK_FOAM.get());
            b.remap(Registry.BLOCK_DEAD_FOAM.get());
        });

    }



}
