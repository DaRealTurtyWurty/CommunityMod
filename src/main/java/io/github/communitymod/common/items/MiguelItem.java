package io.github.communitymod.common.items;

import java.util.Random;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;

public class MiguelItem extends Item {

	public MiguelItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
		
		ItemCooldowns itemCoolDowns = context.getPlayer().getCooldowns();
		
		if(!context.getLevel().isClientSide())
		{
			if(!itemCoolDowns.isOnCooldown(stack.getItem()))
			{
				Random rn = new Random();
				int i = rn.nextInt(100);
				if(i == 1)
				{
					context.getPlayer().spawnAtLocation(Items.DIAMOND);
				}
				else if(i > 90)
				{
					context.getPlayer().spawnAtLocation(Items.IRON_INGOT);
				}
				else {
					context.getPlayer().spawnAtLocation(Items.DIRT);
				}
				itemCoolDowns.addCooldown(stack.getItem(), 200);
			}
			
		}
		
		return InteractionResult.SUCCESS;
	}

}
