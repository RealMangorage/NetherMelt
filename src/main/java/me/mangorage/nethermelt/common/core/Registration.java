
package me.mangorage.nethermelt.common.core;

import me.mangorage.nethermelt.common.blockentitys.FoamBlockEntity;
import me.mangorage.nethermelt.common.blockentitys.RootBlockEntity;
import me.mangorage.nethermelt.NetherMelt;
import me.mangorage.nethermelt.common.entities.ModFallingBlockEntity;
import me.mangorage.nethermelt.common.items.RootChargeItem;
import me.mangorage.nethermelt.common.items.TriggerRemoteItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Supplier;

import static me.mangorage.nethermelt.common.core.Constants.MODID;

public class Registration {

    //================================================
    // Deferred Registerys!
    //================================================
    public final static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public final static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITYS = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);
    public final static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public final static DeferredRegister<EntityType<?>> ENTITYS = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    //================================================
    // PROPERTIES
    //================================================
    public final static Supplier<Item.Properties> PROPERTIES_ITEM = () -> new Item.Properties()
            .tab(NetherMelt.CreativeTab);


    //================================================
    // BLOCKS & ITEMS
    //================================================
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

    public final static RegistryObject<Item> ITEM_TRIGGER_REMOTE = ITEMS.register("remote", () -> new TriggerRemoteItem());

    public final static RegistryObject<Item> ITEM_ROOT_CHARGE = ITEMS.register("rootcharge", () -> new RootChargeItem(PROPERTIES_ITEM.get()));


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


    //================================================
    // INIT!
    //================================================
    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITYS.register(bus);
        ENTITYS.register(bus);
    }

}