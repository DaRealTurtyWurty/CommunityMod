package io.github.communitymod.common.items;

import java.util.List;

import io.github.communitymod.common.recipes.ProcessingToolRecipe;
import io.github.communitymod.core.init.RecipeInit;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class ProcessingTool extends Item {

    public ProcessingTool(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player player, InteractionHand pUsedHand) {
        ItemStack tool = player.getItemInHand(pUsedHand);
        ItemStack offHandItem = player.getOffhandItem();
        for (final Recipe<?> recipe : pLevel.getRecipeManager().getAllRecipesFor(RecipeInit.PROCESSING_TOOL_RECIPE)) {
            final ProcessingToolRecipe processingToolRecipe = (ProcessingToolRecipe) recipe;
            if (processingToolRecipe.isValid(offHandItem)) {
                player.drop(new ItemStack(recipe.getResultItem().getItem(), recipe.getResultItem().getCount()), false);
                offHandItem.shrink(1);
                tool.setDamageValue(tool.getDamageValue() + 1);
                if (tool.getDamageValue() >= tool.getMaxDamage())
                    tool.shrink(1);
            }
        }

        return super.use(pLevel, player, pUsedHand);
    }

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents,
            TooltipFlag pIsAdvanced) {
        pTooltipComponents
                .add(new TextComponent("Right-click the tool with an item in your off hand to drop another item."));
        pTooltipComponents.add(new TextComponent(
                "Exmples of recipe formats can be found in the Community Mod jar, in data/communitymod/recipes/processing_tool."));
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {

        if (enchantment.isAllowedOnBooks()) {
            return false;
        }

        if (enchantment.isCompatibleWith(Enchantments.MENDING)) {
            return false;
        }

        return false;
    }

}
