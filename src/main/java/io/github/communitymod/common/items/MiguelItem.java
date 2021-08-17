package io.github.communitymod.common.items;

import java.util.Random;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class MiguelItem extends Item {

	public MiguelItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		ItemCooldowns itemCoolDowns = pPlayer.getCooldowns();
		
		if(!pLevel.isClientSide())
		{
			if(!itemCoolDowns.isOnCooldown(pPlayer.getItemInHand(pHand).getItem()))
			{
				Random rn = new Random();
				int i = rn.nextInt(100);
				if(i == 1)
				{
					pPlayer.spawnAtLocation(Items.DIAMOND);
				}
				else if(i > 90)
				{
					pPlayer.spawnAtLocation(Items.IRON_INGOT);
				}
				else {
					pPlayer.spawnAtLocation(Items.DIRT);
				}
				itemCoolDowns.addCooldown(pPlayer.getItemInHand(pHand).getItem(), 200);
			}
		}
		
		return super.use(pLevel, pPlayer, pHand);
	}

}
