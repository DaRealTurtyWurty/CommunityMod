package io.github.communitymod.common.items;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PowerBean extends Item {

    public PowerBean(Properties properties) {
        super(properties);
	}
   
    @Override
    public InteractionResultHolder<ItemStack> use (final Level level, final Player player, final InteractionHand hand) {
    	if (level.isClientSide()) return InteractionResultHolder.success(player.getItemInHand(hand));
    	final Random rand = ThreadLocalRandom.current();
    	final var randNum = rand.nextInt(3);
    	switch (randNum) {
    	case 0:
    		player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 0, true, true));
    		break;
    	case 1:
    		player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 1, true, true));
    		break;
    	case 2:
    		player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 2, true, true));
    		break;
    	default:
    		break;
    	}
        player.getCooldowns().addCooldown(player.getItemInHand(hand).getItem(), 2400);
    	if (!player.isCreative()) {
            player.getItemInHand(hand).shrink(1);
        }
    	return InteractionResultHolder.success(player.getItemInHand(hand));
    }
    
    @Override
    public boolean isFoil(ItemStack pStack) {
    	return true;
    }
}