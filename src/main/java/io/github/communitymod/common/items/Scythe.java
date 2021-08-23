package io.github.communitymod.common.items;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.communitymod.capabilities.playerskills.CapabilityPlayerSkills;
import io.github.communitymod.capabilities.playerskills.DefaultPlayerSkills;
import io.github.communitymod.core.init.EnchantmentInit;
import io.github.communitymod.core.util.ColorConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.List;

public class Scythe extends SwordItem {
    private static int souls;

    public Scythe(Tier tier, int damage, float attackSpeed, Properties properties) {
        super(tier, damage, attackSpeed, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> text, TooltipFlag tooltipFlag) {
        text.add(new TextComponent(ColorConstants.GRAY + "Use this item to hunt for " + ColorConstants.AQUA + "souls" + ColorConstants.GRAY + "!"));
        if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SOUL_BOOST.get(), stack) > 0) {
            text.add(new TextComponent(ColorConstants.AQUA + "Soul Boost bonus: Collecting souls 2x more common!"));
        }
        if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
            text.add(new TextComponent("Your Entitybound souls: " + souls));
        } else {
            text.add(new TextComponent("Hold \u00A7eSHIFT \u00A77for more information."));
        }
        super.appendHoverText(stack, level, text, tooltipFlag);
    }

    @Override
    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity entity, int p_41407_, boolean p_41408_) {
        super.inventoryTick(p_41404_, p_41405_, entity, p_41407_, p_41408_);
        if (entity instanceof Player player) {
            player.getCapability(CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY).ifPresent(skills ->
                    souls = ((DefaultPlayerSkills) skills).soulCount);
        }
    }
}
