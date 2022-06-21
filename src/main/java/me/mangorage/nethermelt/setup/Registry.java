package me.mangorage.nethermelt.setup;

import me.mangorage.nethermelt.blockentitys.FallingBlockEntity;
import me.mangorage.nethermelt.blockentitys.FoamBlockEntity;
import me.mangorage.nethermelt.blockentitys.RootBlockEntity;
import me.mangorage.nethermelt.blocks.*;
import me.mangorage.nethermelt.NetherMelt;
import me.mangorage.nethermelt.util.DefaultProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Registry {

    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, NetherMelt.MOD_ID);
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITYS = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, NetherMelt.MOD_ID);
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NetherMelt.MOD_ID);



    /** Blocks **/
    public static RegistryObject<RootBlock> BLOCK_ROOT = BLOCKS.register("root", () -> new RootBlock());
    public static RegistryObject<DeadRootBlock> BLOCK_DEAD_ROOT = BLOCKS.register("deadroot", () -> new DeadRootBlock());
    public static RegistryObject<FoamBlock> BLOCK_FOAM = BLOCKS.register("foam", () -> new FoamBlock());
    public static RegistryObject<DeadFoamBlock> BLOCK_DEAD_FOAM = BLOCKS.register("deadfoam", () -> new DeadFoamBlock());
    public static RegistryObject<FallingBlock> BLOCK_FALLING = BLOCKS.register("falling", () -> new FallingBlock());


    /** Block Entitys **/
    public static RegistryObject<BlockEntityType> BLOCKENTITY_ROOT = BLOCK_ENTITYS.register("root", () -> BlockEntityType.Builder.of(RootBlockEntity::new, BLOCK_ROOT.get()).build(null));
    public static RegistryObject<BlockEntityType> BLOCKENTITY_FOAM = BLOCK_ENTITYS.register("foam", () -> BlockEntityType.Builder.of(FoamBlockEntity::new, BLOCK_FOAM.get()).build(null));
    public static RegistryObject<BlockEntityType> BLOCKENTITY_FALLING = BLOCK_ENTITYS.register("falling", () -> BlockEntityType.Builder.of(FallingBlockEntity::new, BLOCK_FALLING.get()).build(null));


    /** Items **/
    public static RegistryObject<Item> ITEM_ROOT = ITEMS.register("root", () -> new BlockItem(BLOCK_ROOT.get(), DefaultProperties.ITEM().stacksTo(64)));
    public static RegistryObject<Item> ITEM_DEAD_ROOT = ITEMS.register("deadroot", () -> new BlockItem(BLOCK_DEAD_ROOT.get(), DefaultProperties.ITEM().stacksTo(64)));
    public static RegistryObject<Item> ITEM_FOAM = ITEMS.register("foam", () -> new BlockItem(BLOCK_FOAM.get(), DefaultProperties.ITEM().stacksTo(64)));
    public static RegistryObject<Item> ITEM_DEAD_FOAM = ITEMS.register("deadfoam", () -> new BlockItem(BLOCK_DEAD_FOAM.get(), DefaultProperties.ITEM().stacksTo(64)));

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        BLOCK_ENTITYS.register(bus);
        ITEMS.register(bus);
    }

}
