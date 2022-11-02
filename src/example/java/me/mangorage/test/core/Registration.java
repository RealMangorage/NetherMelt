package me.mangorage.test.core;

import me.mangorage.test.Test;
import me.mangorage.test.blockentitys.BorderBlockEntity;
import me.mangorage.test.blocks.BorderBlock;
import me.mangorage.test.blocks.ExampleBlock;
import net.minecraft.Util;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static me.mangorage.test.Test.creativeTab;
import static me.mangorage.test.core.Constants.MODID;

public class Registration {


    // Registries
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITYS = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);


    // Blocks
    public static final RegistryObject<ExampleBlock> BLOCK_EXAMPLE = BLOCKS.register("example", () -> new ExampleBlock());
    public static final RegistryObject<BorderBlock> BLOCK_BORDER = BLOCKS.register("border", () -> new BorderBlock());


    // Block Entity's
    public static final RegistryObject<BlockEntityType<BorderBlockEntity>> BLOCK_ENTITY_BORDER = BLOCK_ENTITYS.register("border", () -> BlockEntityType.Builder.of(BorderBlockEntity::new,
            BLOCK_BORDER.get()
    ).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "oil")));

    // Items
    public static final RegistryObject<BlockItem> ITEM_EXAMPLE = ITEMS.register("example", () -> new BlockItem(BLOCK_EXAMPLE.get(), new Item.Properties().tab(creativeTab)));
    public static final RegistryObject<BlockItem> ITEM_BORDER = ITEMS.register("border", () -> new BlockItem(BLOCK_BORDER.get(), new Item.Properties().tab(creativeTab)));

    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        BLOCK_ENTITYS.register(bus);
        ITEMS.register(bus);
    }
}
