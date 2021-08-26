package io.github.communitymod.common.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.core.init.ItemInit;
import io.github.communitymod.core.init.RecipeInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ProcessingToolRecipe implements IShapedRecipe<Inventory> {

	public static final Serializer SERIALIZER = new Serializer();

	private final Ingredient input;
	private final ItemStack output;
	private final ResourceLocation id;

	public ProcessingToolRecipe(ResourceLocation id, Ingredient input, ItemStack output) {
		this.id = id;
		this.input = input;
		this.output = output;
	}

	@Override
	public boolean matches(Inventory inv, net.minecraft.world.level.Level levelIn) {
		return this.input.test(inv.getItem(0));
	}

	@Override
	public ItemStack assemble(Inventory inv) {
		return this.output.copy();
	}

	@Override
	public ItemStack getResultItem() {
		return this.output;
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return RecipeInit.PROCESSING_TOOL_RECIPE;
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(ItemInit.PROCESSING_TOOL.get());
	}

	public boolean isValid(ItemStack input) {
		return this.input.test(input);
	}

	@Override
	public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
		return true;
	}

	private static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>>
			implements RecipeSerializer<ProcessingToolRecipe> {

		public Serializer() {
			this.setRegistryName(CommunityMod.MODID, "processing_tool_recipe");
		}

		@Override
		public ProcessingToolRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			final JsonElement inputEl = GsonHelper.isArrayNode(json, "input") ? GsonHelper.getAsJsonArray(json, "input")
					: GsonHelper.getAsJsonObject(json, "input");
			final Ingredient input = Ingredient.fromJson(inputEl);
			final ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));

			return new ProcessingToolRecipe(recipeId, input, output);
		}

		@Override
		public ProcessingToolRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			final Ingredient input = Ingredient.fromNetwork(buffer);
			final ItemStack output = buffer.readItem();
			return new ProcessingToolRecipe(recipeId, input, output);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, ProcessingToolRecipe recipe) {
			recipe.input.toNetwork(buffer);
			buffer.writeItem(recipe.output);
		}
	}

	@Override
	public int getRecipeWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRecipeHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

}
