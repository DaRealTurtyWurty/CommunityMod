package io.github.communitymod.common.items;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.List;

/**
 * The SpecialItem from TurtyWurty's Forge tutorial.
 *
 * I added it because ???
 */
public class SpecialItem extends Item {
    public SpecialItem(final Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(final ItemStack stack, @Nullable final Level level,
            final List<Component> tooltip, final TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(),
                GLFW.GLFW_KEY_LEFT_SHIFT)) {
            tooltip.add(new TextComponent("Advanced Tooltip"));
        } else {
            tooltip.add(new TextComponent("Hold \u00A7eSHIFT \u00A77for more information."));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level level, final Player player,
            final InteractionHand hand) {
        player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 5));
        final var zombie = new Zombie(level);
        zombie.setPos(player.getX(), player.getY(), player.getZ());
        level.addFreshEntity(zombie);
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
