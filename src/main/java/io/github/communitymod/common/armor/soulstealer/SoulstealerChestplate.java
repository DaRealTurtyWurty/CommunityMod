package io.github.communitymod.common.armor.soulstealer;

import io.github.communitymod.capabilities.playerskills.CapabilityPlayerSkills;
import io.github.communitymod.capabilities.playerskills.DefaultPlayerSkills;
import io.github.communitymod.core.util.ColorConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class SoulstealerChestplate extends ArmorItem {
    private static int souls;
    private static int scale;

    public SoulstealerChestplate(ArmorMaterial material, EquipmentSlot slot, Properties properties) {
        super(material, slot, properties);
    }

    @Override
    public void appendHoverText(final ItemStack stack, @Nullable final Level level, final List<Component> tooltip, final TooltipFlag flag) {
        tooltip.add(new TextComponent(ColorConstants.AQUA + ColorConstants.BOLD + "This armor is infused with the souls of the living..."));
        tooltip.add(new TextComponent(ColorConstants.DARK_RED + ColorConstants.BOLD + "Use it wisely."));
        tooltip.add(new TextComponent(ColorConstants.GOLD + "Piece Passive: Nuncomplete"));
        tooltip.add(new TextComponent(ColorConstants.GRAY + "If the wearer does not have a full set of this armor,"));
        tooltip.add(new TextComponent(ColorConstants.GRAY + "Gain " + ColorConstants.DARK_RED + "+10 Strength" + ColorConstants.GRAY + " and " + ColorConstants.WHITE + "+3 Speed" + ColorConstants.GRAY + " per piece"));
        tooltip.add(new TextComponent(ColorConstants.GRAY + "but lose " + ColorConstants.RED + "-0.2% Max Health" + ColorConstants.GRAY + " every " + ColorConstants.AQUA + "5" + ColorConstants.GRAY + " seconds."));
        tooltip.add(new TextComponent(""));
        tooltip.add(new TextComponent(ColorConstants.GOLD + "Full set Passive: Bloodlust"));
        tooltip.add(new TextComponent(ColorConstants.GRAY + "Every 10 seconds, lose " + ColorConstants.RED + "-1 Max Health" + ColorConstants.GRAY + " in return of " + ColorConstants.GREEN + "+5 Defense" + ColorConstants.GRAY + " and " + ColorConstants.DARK_RED + "+1 Strength"));
        tooltip.add(new TextComponent(ColorConstants.GRAY + "Every kill gain " + ColorConstants.WHITE + "5 seconds of Speed 3" + ColorConstants.GRAY + ", " + ColorConstants.DARK_RED + "3 seconds of Strength 5 " + ColorConstants.GRAY + "and " + ColorConstants.GOLD + "+1 Absorption Heart."));
        tooltip.add(new TextComponent(""));
        tooltip.add(new TextComponent(ColorConstants.GOLD + "Full set ability: Powersteal " + ColorConstants.YELLOW + ColorConstants.BOLD + "RCONTROL" + ColorConstants.RESET + ColorConstants.GRAY + " (300s Cooldown)"));
        tooltip.add(new TextComponent(ColorConstants.GRAY + "Consumes all of your hunger, and deals " + ColorConstants.RED + (scale) + "%" + ColorConstants.GRAY + " of their max health as damage."));
        tooltip.add(new TextComponent(ColorConstants.GRAY + "Heal the user " + ColorConstants.RED + (scale) + "%" + ColorConstants.GRAY + " of the total stolen health."));
        tooltip.add(new TextComponent(ColorConstants.GRAY + "Apply all potion effects used to the user with " + ColorConstants.RED + (scale) + "%" + ColorConstants.GRAY + " of their duration."));
        super.appendHoverText(stack, level, tooltip, flag);
    }

    @Override
    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity entity, int p_41407_, boolean p_41408_) {
        super.inventoryTick(p_41404_, p_41405_, entity, p_41407_, p_41408_);
        if (entity instanceof Player player) {
            player.getCapability(CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY).ifPresent(skills -> {
                souls = ((DefaultPlayerSkills) skills).soulCount;
                int scaledSouls = Math.min(souls, 500);
                scale = Math.round(10f + (scaledSouls / 100f) * 60f);
            });
        }
    }
}
