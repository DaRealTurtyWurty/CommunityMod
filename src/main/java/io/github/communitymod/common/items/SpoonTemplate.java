package io.github.communitymod.common.items;

import io.github.communitymod.core.init.SoundsInit;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class SpoonTemplate extends ShovelItem {

    public SpoonTemplate(Tier tier, float p_43115_, float p_43116_, Properties properties) {
        super(tier, p_43115_, p_43116_, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        if (Screen.hasShiftDown()) {
            tooltip.add(new TextComponent(I18n.get("tooltip.communitymod.spoonShovel.separator")));
            tooltip.add(new TextComponent(I18n.get("tooltip.communitymod.spoonShovel.text0")));
            tooltip.add(new TextComponent(I18n.get("tooltip.communitymod.spoonShovel.separator")));
            tooltip.add(new TextComponent(I18n.get("tooltip.communitymod.spoonShovel.text1")));
            tooltip.add(new TextComponent(I18n.get("tooltip.communitymod.spoonShovel.separator")));
            tooltip.add(new TextComponent(I18n.get("tooltip.communitymod.spoonShovel.text2")));
            tooltip.add(new TextComponent(I18n.get("tooltip.communitymod.spoonShovel.text3")));
            tooltip.add(new TextComponent(I18n.get("tooltip.communitymod.spoonShovel.text4")));
            tooltip.add(new TextComponent(I18n.get("tooltip.communitymod.spoonShovel.separator")));
        } else {
            tooltip.add(new TextComponent(I18n.get("tooltip.communitymod.spoonShovel.text5")));
            tooltip.add(new TextComponent(""));
            tooltip.add(new TextComponent(I18n.get("tooltip.communitymod.spoonShovel.text6") + (getMaxDamage(stack) - getDamage(stack)) + I18n.get("tooltip.communitymod.spoonShovel.text7") + getMaxDamage(stack)));
        }
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        player.playSound(SoundsInit.BONK.get(), 1.0f, 1.0f);
        FoodData food = player.getFoodData();
        int missingHunger = 20 - food.getFoodLevel();
        float missingSaturation = 20 - food.getSaturationLevel();
        if (missingSaturation > 0.0f) {
            if (missingHunger > 0) {
                player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() + 1);
            } else {
                player.getFoodData().setSaturation(player.getFoodData().getSaturationLevel() + 0.5f);
            }
        }
        return super.onLeftClickEntity(stack, player, entity);
    }
}
