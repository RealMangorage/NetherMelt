package me.mangorage.nethermelt.datageneration;

import me.mangorage.nethermelt.NetherMelt;
import me.mangorage.nethermelt.setup.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Objects;

public class GenItemModelProvider extends ItemModelProvider {
    public GenItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, NetherMelt.MOD_ID, existingFileHelper);
    }

    public void BlockItem(Block block, String modelName) {
        ResourceLocation id = Objects.requireNonNull(block.getRegistryName());
        String namespace = id.getNamespace();

        ResourceLocation textureLocation = new ResourceLocation(namespace, ModelProvider.BLOCK_FOLDER + "/" + modelName);

        cubeAll(block.asItem().getRegistryName().getPath(), textureLocation);
    }
    @Override
    protected void registerModels() {
        BlockItem(Registry.BLOCK_ROOT.get(), "root");
        BlockItem(Registry.BLOCK_DEAD_ROOT.get(), "deadroot");
        BlockItem(Registry.BLOCK_FOAM.get(), "foam_1");
        BlockItem(Registry.BLOCK_DEAD_FOAM.get(), "deadfoam");
        basicItem(Registry.ITEM_TRIGGER_REMOTE.get());
    }
}
