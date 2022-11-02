package datageneration;

import me.mangorage.nethermelt.common.core.Registration;
import me.mangorage.nethermelt.common.core.RegistryCollection;
import me.mangorage.nethermelt.common.core.UpdateUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Objects;

import static me.mangorage.nethermelt.common.core.Constants.MODID;
public class GenItemModelProvider extends ItemModelProvider {
    public GenItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, MODID, existingFileHelper);
    }

    public void BlockItem(String variant, Block block, String modelName) {
        ResourceLocation id = Objects.requireNonNull(UpdateUtils.getRegistryName((block)));
        String namespace = id.getNamespace();

        ResourceLocation textureLocation = new ResourceLocation(namespace, ModelProvider.BLOCK_FOLDER + "/variants/" + variant + "/" + modelName);

        cubeAll(UpdateUtils.getRegistryName((block.asItem())).getPath(), textureLocation);
    }
    @Override
    protected void registerModels() {
        RegistryCollection.getVariantIDs().forEach(variant -> {
            RegistryCollection collection = RegistryCollection.getVariant(variant);
            String name = collection.PROPERTIES.getName();

            BlockItem(variant, collection.BLOCK_FOAM.get(), "foam");
            if (variant.equals("nether")) {
                BlockItem(variant, collection.BLOCK_ROOT.get(), "root");
                BlockItem(variant, collection.BLOCK_DEAD_ROOT.get(), "deadroot");
                BlockItem(variant, collection.BLOCK_DEAD_FOAM.get(), "deadfoam");
            }
        });

        basicItem(Registration.ITEM_TRIGGER_REMOTE.get());
    }
}
