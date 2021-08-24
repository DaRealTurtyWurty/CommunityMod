package io.github.communitymod.common.items;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.communitymod.capabilities.playerskills.CapabilityPlayerSkills;
import io.github.communitymod.capabilities.playerskills.DefaultPlayerSkills;
import io.github.communitymod.core.util.ColorConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.List;

public class Soul extends Item {
    private static int souls;

    public Soul(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(final ItemStack stack, @Nullable final Level level, final List<Component> tooltip, final TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(new TextComponent(ColorConstants.RED + "A soul of a once living entity."));
        tooltip.add(new TextComponent(ColorConstants.RED + "Obtained using " + ColorConstants.DARK_RED + "the Scythe" + ColorConstants.RED + "."));
        if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
            tooltip.add(new TextComponent("Your Entitybound souls: " + souls));
        } else {
            tooltip.add(new TextComponent("Hold \u00A7eSHIFT \u00A77for more information."));
        }
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
