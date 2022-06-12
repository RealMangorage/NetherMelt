package me.mangorage.nethermelt.datageneration;

import me.mangorage.nethermelt.NetherMelt;
import me.mangorage.nethermelt.setup.Registry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class GenRecipes extends RecipeProvider {
    public GenRecipes(DataGenerator generator) {
        super(generator);
    }


    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> writer) {
        generateRootRecipe(writer);
    }

    private void generateRootRecipe(Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(Registry.ITEM_ROOT.get())
                .pattern("abc") // Create recipe pattern
                .pattern("def")
                .pattern("ghi")
                .define('a', Items.SLIME_BLOCK)
                .define('b', Items.MILK_BUCKET)
                .define('c', Items.NETHERRACK)
                .define('d', Items.WATER_BUCKET)
                .define('e', Items.NETHER_STAR)
                .define('f', Items.LAVA_BUCKET)
                .define('g', Items.GUNPOWDER)
                .define('h', Items.REDSTONE_BLOCK)
                .define('i', Items.GLOWSTONE)
                .unlockedBy("nethermelt:root", InventoryChangeTrigger.TriggerInstance.hasItems(Registry.ITEM_ROOT.get()))
                .save(writer, "root"); // Add data to builder
        ShapedRecipeBuilder.shaped(Registry.ITEM_ROOT.get())
                .pattern("abc") // Create recipe pattern
                .pattern("def")
                .pattern("ghi")
                .define('a', Items.SLIME_BLOCK)
                .define('b', Items.MILK_BUCKET)
                .define('c', Items.NETHERRACK)
                .define('d', Items.WATER_BUCKET)
                .define('e', Registry.ITEM_ROOT.get())
                .define('f', Items.LAVA_BUCKET)
                .define('g', Items.GUNPOWDER)
                .define('h', Items.REDSTONE_BLOCK)
                .define('i', Items.GLOWSTONE)
                .unlockedBy("nethermelt:root", InventoryChangeTrigger.TriggerInstance.hasItems(Registry.ITEM_ROOT.get()))
                .save(writer, "root2"); // Add data to builder
    }

}
