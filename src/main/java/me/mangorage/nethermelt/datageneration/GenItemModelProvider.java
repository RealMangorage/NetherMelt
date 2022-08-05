package me.mangorage.nethermelt.datageneration;

import me.mangorage.nethermelt.core.Registration;
import me.mangorage.nethermelt.core.RootType;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Objects;

import static me.mangorage.nethermelt.core.Constants.MODID;

public class GenItemModelProvider extends ItemModelProvider {
    public GenItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, MODID, existingFileHelper);
    }

    public void BlockItem(Block block, String modelName) {
        ResourceLocation id = Objects.requireNonNull(block.getRegistryName());
        String namespace = id.getNamespace();

        ResourceLocation textureLocation = new ResourceLocation(namespace, ModelProvider.BLOCK_FOLDER + "/" + modelName);

        cubeAll(block.asItem().getRegistryName().getPath(), textureLocation);
    }
    @Override
    protected void registerModels() {
        BlockItem(RootType.NETHER.getLiveVariantBlock(), RootType.NETHER.getName());

        BlockItem(Registration.BLOCK_DEAD_ROOT.get(), "deadroot");
        BlockItem(Registration.BLOCK_FOAM.get(), "foam_1");
        BlockItem(Registration.BLOCK_DEAD_FOAM.get(), "deadfoam");
        basicItem(Registration.ITEM_TRIGGER_REMOTE.get());
    }
}
