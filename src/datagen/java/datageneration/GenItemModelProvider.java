package datageneration;

import me.mangorage.nethermelt.core.Registration;
import me.mangorage.nethermelt.core.RegistryCollection;
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
        RegistryCollection.getVariantIDs().forEach(variant -> {
            RegistryCollection collection = RegistryCollection.getVariant(variant);
            String name = collection.COLLECTION_DATA.getName();

            BlockItem(collection.BLOCK_ROOT.get(), name + "root");
            BlockItem(collection.BLOCK_DEAD_ROOT.get(), name + "deadroot");
            BlockItem(collection.BLOCK_FOAM.get(), name + "foam");
            BlockItem(collection.BLOCK_DEAD_FOAM.get(), name + "deadfoam");
        });

        basicItem(Registration.ITEM_TRIGGER_REMOTE.get());
    }
}
