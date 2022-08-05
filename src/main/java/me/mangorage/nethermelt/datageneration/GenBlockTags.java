package me.mangorage.nethermelt.datageneration;

import me.mangorage.nethermelt.NetherMelt;
import me.mangorage.nethermelt.core.Registration;
import me.mangorage.nethermelt.core.RootType;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import static me.mangorage.nethermelt.core.Constants.MODID;

public class GenBlockTags extends BlockTagsProvider {
    public GenBlockTags(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, MODID, existingFileHelper);
    }


    @Override
    protected void addTags() {
        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(RootType.NETHER.getLiveVariantBlock())
                .add(Registration.BLOCK_DEAD_ROOT.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(RootType.NETHER.getLiveVariantBlock())
                .add(Registration.BLOCK_DEAD_ROOT.get());
    }

    @Override
    public String getName() {
        return "Nether Melt Block Tags";
    }
}
