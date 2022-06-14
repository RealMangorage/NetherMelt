package me.mangorage.nethermelt.datageneration;

import me.mangorage.nethermelt.blocks.FoamBlock;
import me.mangorage.nethermelt.NetherMelt;
import me.mangorage.nethermelt.setup.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Objects;

public class GenBlockStateProvider extends BlockStateProvider {
    public GenBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, NetherMelt.MOD_ID, exFileHelper);
    }


    public void createFoamBlock() {
        Block block = Registry.BLOCK_FOAM.get();
        ResourceLocation id = Objects.requireNonNull(block.getRegistryName());
        String namespace = id.getNamespace();
        String name = id.getPath();

        getVariantBuilder(block).forAllStates(state -> {
            int level = state.getValue(FoamBlock.STAGE);

            String modelName = name + "_" + level;
            ResourceLocation textureLocation = new ResourceLocation(namespace, ModelProvider.BLOCK_FOLDER + "/" + modelName);
            BlockModelBuilder model = models().cubeAll(modelName, textureLocation);
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
        createBlockModel(Registry.BLOCK_ROOT.get());
        createBlockModel(Registry.BLOCK_DEAD_ROOT.get());
        createBlockModel(Registry.BLOCK_DEAD_FOAM.get());
    }
}
