package io.github.communitymod.common.items;

import io.github.communitymod.core.init.SoundsInit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WhatSign extends Item {
    public WhatSign(final Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level level, final Player player,
            final InteractionHand hand) {
        player.getCooldowns().addCooldown(this, 180);
        player.playSound(SoundsInit.WHAT.get(), 1.0f, 1.0f);
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
