package me.mangorage.nethermelt.datageneration.tags;

import me.mangorage.nethermelt.NetherMelt;
import me.mangorage.nethermelt.setup.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class GenBlockTags extends BlockTagsProvider {
    public GenBlockTags(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, NetherMelt.MOD_ID, existingFileHelper);
    }


    @Override
    protected void addTags() {
        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(Registry.BLOCK_ROOT.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(Registry.BLOCK_ROOT.get());
    }

    @Override
    public String getName() {
        return "Nether Melt Block Tags";
    }
}
