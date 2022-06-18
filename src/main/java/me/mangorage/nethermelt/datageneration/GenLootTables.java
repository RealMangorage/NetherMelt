package me.mangorage.nethermelt.datageneration;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import me.mangorage.nethermelt.setup.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GenLootTables extends LootTableProvider {
    public GenLootTables(DataGenerator pGenerator) {
        super(pGenerator);
    }


    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return ImmutableList.of(Pair.of(ModBlockLootTable::new, LootContextParamSets.BLOCK));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {

    }

    @Override
    public String getName() {
        return "NetherMelt Loot Table";
    }

    private static class ModBlockLootTable extends BlockLoot {
        @Override
        protected void addTables() {
            dropSelf(Registry.BLOCK_ROOT.get());
            dropSelf(Registry.BLOCK_DEAD_ROOT.get());
            dropSelf(Registry.BLOCK_DEAD_FOAM.get());
            dropOther(Registry.BLOCK_FOAM.get(), Registry.ITEM_DEAD_FOAM.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return Registry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
        }
    }
}
