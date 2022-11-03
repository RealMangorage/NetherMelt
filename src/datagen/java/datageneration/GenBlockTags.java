package datageneration;

import me.mangorage.nethermelt.common.core.ModBlockTags;
import me.mangorage.nethermelt.common.core.RegistryCollection;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import static me.mangorage.nethermelt.common.core.Constants.MODID;

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
            tag(ModBlockTags.CAN_CORRODE).add(Blocks.STONE);
        });
    }

    @Override
    public String getName() {
        return "Nether Melt Block Tags";
    }
}
