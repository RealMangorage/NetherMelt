
package me.mangorage.nethermelt.common.core;

import me.mangorage.nethermelt.NetherMeltCore;
import me.mangorage.nethermelt.common.blockentitys.FoamBlockEntity;
import me.mangorage.nethermelt.common.blockentitys.MachineBlockEntity;
import me.mangorage.nethermelt.common.blockentitys.RootBlockEntity;
import me.mangorage.nethermelt.common.blocks.MachineBlock;
import me.mangorage.nethermelt.common.container.MachineContainer;
import me.mangorage.nethermelt.common.entities.ModFallingBlockEntity;
import me.mangorage.nethermelt.common.fluids.SludgeFluid;
import me.mangorage.nethermelt.common.items.RootChargeItem;
import me.mangorage.nethermelt.common.items.TriggerRemoteItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static me.mangorage.nethermelt.common.core.Constants.MOD_ID;

public class Registration {

    //================================================
    // Deferred Registerys!
    //================================================
    public final static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public final static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITYS = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MOD_ID);
    public final static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public final static DeferredRegister<EntityType<?>> ENTITYS = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MOD_ID);
    public final static DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MOD_ID);
    public final static DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, MOD_ID);
    public final static DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, MOD_ID);

    //================================================
    // PROPERTIES
    //================================================
    public final static Supplier<Item.Properties> PROPERTIES_ITEM = () -> new Item.Properties()
            .tab(NetherMeltCore.CreativeTab);


    //================================================
    // BLOCKS & ITEMS
    //================================================

    public final static RegistryObject<Block> SLUDGE_BLOCK = BLOCKS.register("sludge", () -> new LiquidBlock(() -> Registration.SLUDGE.get(), BlockBehaviour.Properties.of(Material.WATER).noLootTable()));

    public final static RegistryCollection NETHER = RegistryCollection.create(new RegistryCollection.Properties("nether")
            .name("nether")
            .modID("minecraft")
            .addDimensions(Level.NETHER)
            .setDefaultAbsorbing(Blocks.NETHERRACK.defaultBlockState())
            .build());

    /**
    public static RegistryCollection OVERWORLD = RegistryCollection.create(new RegistryCollection.Properties("overworld")
            .name("overworld")
            .modID("minecraft")
            .addDimensions(Level.OVERWORLD)
            .setDefaultAbsorbing(Blocks.STONE.defaultBlockState()));

     **/

    public final static RegistryObject<Block> MACHINE_BLOCK = BLOCKS.register("machineblock", () -> new MachineBlock());
    public final static RegistryObject<BlockItem> MACHINE_BLOCK_ITEM = ITEMS.register("machineblock", () -> new BlockItem(MACHINE_BLOCK.get(), PROPERTIES_ITEM.get()));

    public final static RegistryObject<Item> ITEM_TRIGGER_REMOTE = ITEMS.register("remote", () -> new TriggerRemoteItem());

    public final static RegistryObject<Item> ITEM_ROOT_CHARGE = ITEMS.register("rootcharge", () -> new RootChargeItem(PROPERTIES_ITEM.get()));

    public final static RegistryObject<BucketItem> SLUDGE_BUCKET_ITEM = ITEMS.register("sludgebucket", () -> new BucketItem(() -> Registration.SLUDGE.get(), PROPERTIES_ITEM.get()));

    //================================================
    // Entities
    //================================================
    public final static RegistryObject<EntityType<ModFallingBlockEntity>> ENTITY_FALLINGBLOCK = ENTITYS.register("fallingblock", () -> EntityType.Builder.of(ModFallingBlockEntity::new, MobCategory.MISC).build("fallingblock"));


    //================================================
    // Block Entities
    //================================================
    public final static RegistryObject<BlockEntityType<FoamBlockEntity>> BLOCKENTITY_FOAM = BLOCK_ENTITYS.register("foam", () -> BlockEntityType.Builder.of(FoamBlockEntity::new,
            NETHER.BLOCK_FOAM.get()
    ).build(null));
    public final static RegistryObject<BlockEntityType<RootBlockEntity>> BLOCKENTITY_ROOT = BLOCK_ENTITYS.register("root", () -> BlockEntityType.Builder.of(RootBlockEntity::new,
            NETHER.BLOCK_ROOT.get()
    ).build(null));

    public final static RegistryObject<BlockEntityType<MachineBlockEntity>> MACHINE_BLOCKENTITY = BLOCK_ENTITYS.register("machine", () -> BlockEntityType.Builder.of(MachineBlockEntity::new,
            MACHINE_BLOCK.get()
    ).build(null));

    //================================================
    // Menu's
    //================================================

    public final static RegistryObject<MenuType<MachineContainer>> MACHINE_CONTAINER = CONTAINERS.register("menu", () -> IForgeMenuType.create((windowId, inv, data) -> new MachineContainer(windowId, data.readBlockPos(), inv, inv.player)));



    //================================================
    // Fluids's
    //================================================
    public final static RegistryObject<FluidType> SLUDGE_TYPE = FLUID_TYPES.register("sludge", () -> new FluidType(FluidType.Properties.create().temperature(5000).viscosity(5000).density(100).canSwim(false).canExtinguish(false).canHydrate(false).canPushEntity(true).canDrown(true).rarity(Rarity.RARE).canConvertToSource(false).supportsBoating(true)));
    public final static RegistryObject<FlowingFluid> SLUDGE = FLUIDS.register("sludge", () -> new SludgeFluid.Source());
    public final static RegistryObject<FlowingFluid> SLUDGE_FLOWING = FLUIDS.register("sludge_flowing", () -> new SludgeFluid.Flowing());


    //================================================
    // INIT!
    //================================================
    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITYS.register(bus);
        ENTITYS.register(bus);
        CONTAINERS.register(bus);
        FLUID_TYPES.register(bus);
        FLUIDS.register(bus);
    }

}