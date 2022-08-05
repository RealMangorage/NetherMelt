package me.mangorage.nethermelt.datageneration;

import me.mangorage.nethermelt.core.Registration;
import me.mangorage.nethermelt.core.RootType;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import java.util.Objects;

import static me.mangorage.nethermelt.core.Constants.BlockStateProperties.STAGE;
import static me.mangorage.nethermelt.core.Constants.MODID;

public class GenBlockStateProvider extends BlockStateProvider {
    public GenBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, MODID, exFileHelper);
    }



    public void createFoamBlock() {
        Block block = Registration.BLOCK_FOAM.get();
        ResourceLocation id = Objects.requireNonNull(block.getRegistryName());
        String namespace = id.getNamespace();



        getVariantBuilder(Registration.BLOCK_FOAM.get()).forAllStates(state -> {
            int Stage = state.getValue(STAGE);

            ResourceLocation North = new ResourceLocation(namespace, ModelProvider.BLOCK_FOLDER + "/foam/stages/" + Stage + "/face1");
            ResourceLocation South = new ResourceLocation(namespace, ModelProvider.BLOCK_FOLDER + "/foam/stages/" + Stage + "/face4");;
            ResourceLocation East = new ResourceLocation(namespace, ModelProvider.BLOCK_FOLDER + "/foam/stages/" + Stage + "/face3");;
            ResourceLocation West = new ResourceLocation(namespace, ModelProvider.BLOCK_FOLDER + "/foam/stages/" + Stage + "/face2");;
            ResourceLocation Up = new ResourceLocation(namespace, ModelProvider.BLOCK_FOLDER + "/foam/stages/" + Stage + "/face5");
            ResourceLocation Down = new ResourceLocation(namespace, ModelProvider.BLOCK_FOLDER + "/foam/stages/" + Stage + "/face6");

            BlockModelBuilder model = models().cube("foam_" + Stage, Down, Up, North, South, East, West);
            model.texture("particle", new ResourceLocation("minecraft", "block/netherrack"));

            return ConfiguredModel.builder().modelFile(model).build();
        });
    }


    public void createBlockModel(Block block) {
        ResourceLocation id = Objects.requireNonNull(block.getRegistryName());
        String namespace = id.getNamespace();
        String name = id.getPath();

        getVariantBuilder(block).forAllStates(state -> {
            String modelName = name;
            ResourceLocation textureLocation = new ResourceLocation(namespace, ModelProvider.BLOCK_FOLDER + "/" + modelName);
            BlockModelBuilder model = models().cubeAll(modelName, textureLocation);
            return ConfiguredModel.builder().modelFile(model).build();
        });
    }



    @Override
    protected void registerStatesAndModels() {
        createFoamBlock();
        createBlockModel(RootType.NETHER.getLiveVariantBlock());
        createBlockModel(Registration.BLOCK_DEAD_ROOT.get());
        createBlockModel(Registration.BLOCK_DEAD_FOAM.get());
    }
}
