package datageneration;

import me.mangorage.nethermelt.core.RegistryCollection;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import static me.mangorage.nethermelt.core.Constants.MODID;

public class GenBlockTags extends BlockTagsProvider {
    public GenBlockTags(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, MODID, existingFileHelper);
    }


    @Override
    protected void addTags() {

        RegistryCollection.getVariantIDs().forEach(variant -> {
            RegistryCollection collection = RegistryCollection.getVariant(variant);

            tag(BlockTags.NEEDS_DIAMOND_TOOL).add(collection.BLOCK_ROOT.get(), collection.BLOCK_DEAD_ROOT.get());
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(collection.BLOCK_ROOT.get(), collection.BLOCK_DEAD_ROOT.get());
        });
    }

    @Override
    public String getName() {
        return "Nether Melt Block Tags";
    }
}
