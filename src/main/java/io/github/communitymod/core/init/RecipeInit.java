package io.github.communitymod.core.init;

import io.github.communitymod.common.recipes.ProcessingToolRecipe;
import io.github.communitymod.common.recipes.ProcessingToolRecipeType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.event.RegistryEvent.Register;

public class RecipeInit {

	public static final RecipeType<ProcessingToolRecipe> PROCESSING_TOOL_RECIPE = new ProcessingToolRecipeType();
	
	public static void registerRecipes(Register<RecipeSerializer<?>> event) {
		registerRecipe(event, PROCESSING_TOOL_RECIPE, ProcessingToolRecipe.SERIALIZER);
	}
	
	private static void registerRecipe(Register<RecipeSerializer<?>> event, RecipeType<?> type,
			RecipeSerializer<?> serializer) {
		Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(type.toString()), type);
		event.getRegistry().register(serializer);
	}
	
}
