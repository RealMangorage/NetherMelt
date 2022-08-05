
package me.mangorage.nethermelt.core;

import me.mangorage.nethermelt.blockentitys.FoamBlockEntity;
import me.mangorage.nethermelt.blockentitys.RootBlockEntity;
import me.mangorage.nethermelt.blocks.*;
import me.mangorage.nethermelt.NetherMelt;
import me.mangorage.nethermelt.entities.ModFallingBlockEntity;
import me.mangorage.nethermelt.items.RootBlockItem;
import me.mangorage.nethermelt.items.TriggerRemoteItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
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


    //================================================
    // PROPERTIES
    //================================================
    public static Supplier<Item.Properties> PROPERTIES_ITEM = () -> new Item.Properties()
            .tab(NetherMelt.CreativeTab);


    //================================================
    // BLOCKS
    //================================================
    public static RegistryObject<RootBlock> BLOCK_ROOT_NETHER = BLOCKS.register(RootType.NETHER.getName(), () -> new RootBlock(RootType.NETHER));
    public static RegistryObject<RootBlock> BLOCK_ROOT_OVERWORLD = BLOCKS.register(RootType.OVERWORLD.getName(), () -> new RootBlock(RootType.OVERWORLD));
    public static RegistryObject<FoamBlock> BLOCK_FOAM = BLOCKS.register("foam", () -> new FoamBlock());
    public static RegistryObject<Block> BLOCK_DEAD_ROOT = BLOCKS.register("deadroot", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(100.0f).destroyTime(5.0f).sound(SoundType.NETHERRACK).lightLevel(state -> 15)));
    public static RegistryObject<Block> BLOCK_DEAD_FOAM = BLOCKS.register("deadfoam", () -> new Block(BlockBehaviour.Properties.of(Material.SPONGE).lightLevel(state -> 10)));




    //================================================
    // ITEMS
    //================================================

    public static RegistryObject<RootBlockItem> ITEM_ROOT_NETHER = ITEMS.register(RootType.NETHER.getName(), () -> new RootBlockItem(BLOCK_ROOT_NETHER.get()));
    public static RegistryObject<RootBlockItem> ITEM_ROOT_OVERWORLD = ITEMS.register(RootType.OVERWORLD.getName(), () -> new RootBlockItem(BLOCK_ROOT_OVERWORLD.get()));
    public static RegistryObject<BlockItem> ITEM_DEAD_ROOT = ITEMS.register("deadroot", () -> new BlockItem(BLOCK_DEAD_ROOT.get(), PROPERTIES_ITEM.get().stacksTo(64)));
    public static RegistryObject<BlockItem> ITEM_FOAM = ITEMS.register("foam", () -> new BlockItem(BLOCK_FOAM.get(), PROPERTIES_ITEM.get().stacksTo(64)));
    public static RegistryObject<BlockItem> ITEM_DEAD_FOAM = ITEMS.register("deadfoam", () -> new BlockItem(BLOCK_DEAD_FOAM.get(), PROPERTIES_ITEM.get().stacksTo(64)));
    public static RegistryObject<Item> ITEM_TRIGGER_REMOTE = ITEMS.register("remote", () -> new TriggerRemoteItem());



    //================================================
    // Entities
    //================================================
    public static RegistryObject<EntityType<ModFallingBlockEntity>> ENTITY_FALLINGBLOCK = ENTITYS.register("fallingblock", () -> EntityType.Builder.of(ModFallingBlockEntity::new, MobCategory.MISC).build("fallingblock"));


    //================================================
    // Block Entities
    //================================================
    public static RegistryObject<BlockEntityType<FoamBlockEntity>> BLOCKENTITY_FOAM = BLOCK_ENTITYS.register("foam", () -> BlockEntityType.Builder.of(FoamBlockEntity::new, BLOCK_FOAM.get()).build(null));
    public static RegistryObject<BlockEntityType<RootBlockEntity>> BLOCKENTITY_ROOT = BLOCK_ENTITYS.register("root", () -> BlockEntityType.Builder.of(RootBlockEntity::new,
            BLOCK_ROOT_NETHER.get(),
            BLOCK_ROOT_OVERWORLD.get()
    ).build(null));


    //================================================
    // INIT!
    //================================================
    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        BLOCK_ENTITYS.register(bus);
        ITEMS.register(bus);
        ENTITYS.register(bus);
    }

}