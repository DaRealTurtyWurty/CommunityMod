package io.github.communitymod.common.recipes;

import io.github.communitymod.CommunityMod;
import net.minecraft.world.item.crafting.RecipeType;

public class ProcessingToolRecipeType implements RecipeType<ProcessingToolRecipe> {

	@Override
	public String toString() {
		return CommunityMod.MODID + ":ore_processing_tool_recipe";
	}

}
