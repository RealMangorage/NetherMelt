package me.mangorage.nethermelt.core;

import me.mangorage.nethermelt.blocks.FoamBlock;
import me.mangorage.nethermelt.blocks.RootBlock;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;

import static me.mangorage.nethermelt.core.Registration.BLOCKS;
import static me.mangorage.nethermelt.core.Registration.ITEMS;

//TODO: Rename this class to a better name!
public class RegistryCollection {
    public static HashMap<String, RegistryCollection> REGISTRED = new HashMap<>();

    public static RegistryCollection getVariant(String ID) {
        return REGISTRED.get(ID);
    }

    public static Set<String> getVariantIDs() {
        return REGISTRED.keySet();
    }

    public final Properties PROPERTIES;
    public final RegistryObject<RootBlock> BLOCK_ROOT;
    public final RegistryObject<Block> BLOCK_DEAD_ROOT;
    public final RegistryObject<FoamBlock> BLOCK_FOAM;
    public final RegistryObject<Block> BLOCK_DEAD_FOAM;

    public final RegistryObject<Item> ITEM_ROOT;
    public final RegistryObject<Item> ITEM_DEAD_ROOT;
    public final RegistryObject<Item> ITEM_FOAM;
    public final RegistryObject<Item> ITEM_DEAD_FOAM;


    private RegistryCollection(Properties data) {
        // Flags
        this.PROPERTIES = data;
        boolean isModLoaded = data.isModLoaded();
        String name = data.getName();
        String ID = data.getID();

        // Blocks
        this.BLOCK_ROOT = BLOCKS.register(name + "root", () -> new RootBlock(ID));
        this.BLOCK_DEAD_ROOT = BLOCKS.register(name + "deadroot", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(100.0f).destroyTime(5.0f).sound(SoundType.NETHERRACK).lightLevel(state -> 15)));
        this.BLOCK_FOAM = BLOCKS.register(name + "foam", () -> new FoamBlock(ID));
        this.BLOCK_DEAD_FOAM = BLOCKS.register(name + "deadfoam", () -> new Block(BlockBehaviour.Properties.of(Material.SPONGE).lightLevel(state -> 10)));

        // Items
        this.ITEM_ROOT = ITEMS.register(name + "root", () -> new BlockItem(BLOCK_ROOT.get(), isModLoaded ? Registration.PROPERTIES_ITEM.get() : new Item.Properties()));
        this.ITEM_DEAD_ROOT = ITEMS.register(name + "deadroot", () -> new BlockItem(BLOCK_DEAD_ROOT.get(), isModLoaded ? Registration.PROPERTIES_ITEM.get() : new Item.Properties()));
        this.ITEM_FOAM = ITEMS.register(name + "foam", () -> new BlockItem(BLOCK_FOAM.get(), isModLoaded ? Registration.PROPERTIES_ITEM.get() : new Item.Properties()));
        this.ITEM_DEAD_FOAM = ITEMS.register(name + "deadfoam", () -> new BlockItem(BLOCK_DEAD_FOAM.get(), isModLoaded ? Registration.PROPERTIES_ITEM.get() : new Item.Properties()));
    }

    public static RegistryCollection create(Properties data) {
        RegistryCollection collection = new RegistryCollection(data);
        REGISTRED.put(data.getID(), collection);
        return collection;
    }

    public static class Properties {

        private final List<ResourceKey<Level>> dimensions = new ArrayList<>();
        private final String ID;

        private String MODID;
        private String Name;
        private BlockState defaultAbsorbing = Blocks.AIR.defaultBlockState();

        public Properties(String ID) {
            this.ID = ID;
        }

        public Properties modID(String modID) {
            this.MODID = modID;
            return this;
        }

        public Properties name(String name) {
            this.Name = name;
            return this;
        }

        public Properties addDimensions(ResourceKey<Level> dimension) {
            this.dimensions.add(dimension);
            return this;
        }

        public Properties setDefaultAbsorbing(BlockState blockState) {
            this.defaultAbsorbing = blockState;
            return this;
        }


        public String getModID() {
            return MODID;
        }

        public String getID() {
            return ID;
        }

        public String getName() {
            return Name;
        }

        public List<ResourceKey<Level>> getDimensions() {
            return dimensions;
        }

        public BlockState getDefaultAbsorbing() {
            return defaultAbsorbing;
        }

        public boolean isModLoaded() {
            return ModList.get().isLoaded(getModID());
        }



    }
}
