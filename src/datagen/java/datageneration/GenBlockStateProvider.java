package datageneration;

import me.mangorage.nethermelt.common.core.Constants;
import me.mangorage.nethermelt.common.core.RegistryCollection;
import me.mangorage.nethermelt.common.core.UpdateUtils;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import java.util.Objects;

import static me.mangorage.nethermelt.common.core.Constants.BlockStateProperties.STAGE;
import static me.mangorage.nethermelt.common.core.Constants.MODID;

public class GenBlockStateProvider extends BlockStateProvider {
    public GenBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, MODID, exFileHelper);
    }



    public void createFoamBlock(Block block) {
        ResourceLocation id = Objects.requireNonNull(UpdateUtils.getRegistryName(block));
        String namespace = id.getNamespace();

        getVariantBuilder(block).forAllStates(state -> {
            int Stage = state.getValue(STAGE);

            ResourceLocation North = new ResourceLocation(namespace, ModelProvider.BLOCK_FOLDER + "/foam/stages/" + Stage + "/face1");
            ResourceLocation South = new ResourceLocation(namespace, ModelProvider.BLOCK_FOLDER + "/foam/stages/" + Stage + "/face4");
            ResourceLocation East = new ResourceLocation(namespace, ModelProvider.BLOCK_FOLDER + "/foam/stages/" + Stage + "/face3");
            ResourceLocation West = new ResourceLocation(namespace, ModelProvider.BLOCK_FOLDER + "/foam/stages/" + Stage + "/face2");
            ResourceLocation Up = new ResourceLocation(namespace, ModelProvider.BLOCK_FOLDER + "/foam/stages/" + Stage + "/face5");
            ResourceLocation Down = new ResourceLocation(namespace, ModelProvider.BLOCK_FOLDER + "/foam/stages/" + Stage + "/face6");

            BlockModelBuilder model = models().cube("foam_" + Stage, Down, Up, North, South, East, West);
            model.texture("particle", new ResourceLocation("minecraft", "block/netherrack"));
            model.renderType(mcLoc("cutout"));

            return ConfiguredModel.builder().modelFile(model).build();
        });
    }


    public void createBlockModel(String variant, Block block, String type) {
        ResourceLocation id = Objects.requireNonNull(UpdateUtils.getRegistryName(block));
        String namespace = id.getNamespace();
        String modelName = id.getPath();

        try {
            getVariantBuilder(block).forAllStates(state -> {
                if (state.hasProperty(Constants.BlockStateProperties.ACTIVATED) && state.getValue(Constants.BlockStateProperties.ACTIVATED)) {
                    ResourceLocation textureLocation = new ResourceLocation(namespace, ModelProvider.BLOCK_FOLDER + "/variants/" + variant + "/activated_" + type);
                    BlockModelBuilder model = models().cubeAll("activated_" + modelName, textureLocation);
                    return ConfiguredModel.builder().modelFile(model).build();
                }

                ResourceLocation textureLocation = new ResourceLocation(namespace, ModelProvider.BLOCK_FOLDER + "/variants/" + variant + "/" + type);
                BlockModelBuilder model = models().cubeAll(modelName, textureLocation);
                return ConfiguredModel.builder().modelFile(model).build();
            });
        } catch (Exception e) {
            // Whatever !
        }
    }



    @Override
    protected void registerStatesAndModels() {
        RegistryCollection.getVariantIDs().forEach(variant -> {
            RegistryCollection collection = RegistryCollection.getVariant(variant);
            createFoamBlock(collection.BLOCK_FOAM.get());
            if (variant.equals("nether")) {
                createBlockModel(variant, collection.BLOCK_ROOT.get(), "root");
                createBlockModel(variant, collection.BLOCK_DEAD_FOAM.get(), "deadfoam");
                createBlockModel(variant, collection.BLOCK_DEAD_ROOT.get(), "deadroot");
            }
        });
    }
}
