package io.github.communitymod.common.items;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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
