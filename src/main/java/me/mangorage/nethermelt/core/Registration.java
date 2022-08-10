
package me.mangorage.nethermelt.core;

import com.refinedmods.refinedstorage.RSBlocks;
import me.mangorage.nethermelt.blockentitys.FoamBlockEntity;
import me.mangorage.nethermelt.blockentitys.RootBlockEntity;
import me.mangorage.nethermelt.NetherMelt;
import me.mangorage.nethermelt.blocks.FoamBlock;
import me.mangorage.nethermelt.blocks.RootBlock;
import me.mangorage.nethermelt.entities.ModFallingBlockEntity;
import me.mangorage.nethermelt.items.TriggerRemoteItem;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Supplier;

import static me.mangorage.nethermelt.core.Constants.MODID;

public class Registration {

    //================================================
    // DeferredRegisterys!
    //================================================
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITYS = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static DeferredRegister<EntityType<?>> ENTITYS = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);
    public static FoamBlock[] test;

    //================================================
    // PROPERTIES
    //================================================
    public static Supplier<Item.Properties> PROPERTIES_ITEM = () -> new Item.Properties()
            .tab(NetherMelt.CreativeTab);


    //================================================
    // BLOCKS & ITEMS
    //================================================
    public static RegistryCollection NETHER = RegistryCollection.create(new RegistryCollection.Properties("nether")
            .name("nether")
            .modID("minecraft")
            .addDimensions(Level.NETHER)
            .setDefaultAbsorbing(Blocks.NETHERRACK.defaultBlockState()));

    public static RegistryCollection OVERWORLD = RegistryCollection.create(new RegistryCollection.Properties("overworld")
            .name("overworld")
            .modID("minecraft")
            .addDimensions(Level.OVERWORLD)
            .setDefaultAbsorbing(Blocks.GRASS_BLOCK.defaultBlockState()));


    public static RegistryObject<Item> ITEM_TRIGGER_REMOTE = ITEMS.register("remote", () -> new TriggerRemoteItem());



    //================================================
    // Entities
    //================================================
    public static RegistryObject<EntityType<ModFallingBlockEntity>> ENTITY_FALLINGBLOCK = ENTITYS.register("fallingblock", () -> EntityType.Builder.of(ModFallingBlockEntity::new, MobCategory.MISC).build("fallingblock"));


    //================================================
    // Block Entities
    //================================================
    public static RegistryObject<BlockEntityType<FoamBlockEntity>> BLOCKENTITY_FOAM = BLOCK_ENTITYS.register("foam", () -> BlockEntityType.Builder.of(FoamBlockEntity::new,
            NETHER.BLOCK_FOAM.get(),
            OVERWORLD.BLOCK_FOAM.get()
    ).build(null));
    public static RegistryObject<BlockEntityType<RootBlockEntity>> BLOCKENTITY_ROOT = BLOCK_ENTITYS.register("root", () -> BlockEntityType.Builder.of(RootBlockEntity::new,
            NETHER.BLOCK_ROOT.get(),
            OVERWORLD.BLOCK_ROOT.get()
    ).build(null));


    //================================================
    // INIT!
    //================================================
    public static void init(Block... test) {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITYS.register(bus);
        ENTITYS.register(bus);
    }

}