package me.mangorage.nethermelt.datageneration;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

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
        /**
        ShapedRecipeBuilder.shaped(Registration.ITEM_ROOT.get())
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
                .unlockedBy("nethermelt:root", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.ITEM_ROOT.get()))
                .save(writer, "root"); // Add data to builder
        ShapedRecipeBuilder.shaped(Registration.ITEM_ROOT.get())
                .pattern("abc") // Create recipe pattern
                .pattern("def")
                .pattern("ghi")
                .define('a', Items.SLIME_BLOCK)
                .define('b', Items.MILK_BUCKET)
                .define('c', Items.NETHERRACK)
                .define('d', Items.WATER_BUCKET)
                .define('e', Registration.ITEM_DEAD_ROOT.get())
                .define('f', Items.LAVA_BUCKET)
                .define('g', Items.GUNPOWDER)
                .define('h', Items.REDSTONE_BLOCK)
                .define('i', Items.GLOWSTONE)
                .unlockedBy("nethermelt:root", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.ITEM_ROOT.get()))
                .save(writer, "root2"); // Add data to builder
         **/
    }

}
