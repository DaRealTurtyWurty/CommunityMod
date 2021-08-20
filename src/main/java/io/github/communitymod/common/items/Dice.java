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
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Dice extends Item {
    public Dice(final Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level level, final Player player,
            final InteractionHand hand) {
        if (level.isClientSide())
            return InteractionResultHolder.success(player.getItemInHand(hand));
        final Random rand = ThreadLocalRandom.current();
        var message = "If you receive this message, then AAAAAAAAAAAAAAAA";
        final var randomNumb = rand.nextInt(6) + 1;
        player.sendMessage(new TextComponent("You Roll " + randomNumb),
                player.getUUID());
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
