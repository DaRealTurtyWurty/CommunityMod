package io.github.communitymod.common.items;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;

import java.util.Random;

public class MiguelItem extends Item {

    public MiguelItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {

        final var itemCoolDowns = context.getPlayer().getCooldowns();

        if (!context.getLevel().isClientSide() && !itemCoolDowns.isOnCooldown(stack.getItem())) {
            final var rn = new Random();
            final var i = rn.nextInt(100);
            if (i == 1) {
                context.getPlayer().spawnAtLocation(Items.DIAMOND);
            } else if (i > 90) {
                context.getPlayer().spawnAtLocation(Items.IRON_INGOT);
            } else {
                context.getPlayer().spawnAtLocation(Items.DIRT);
            }
            itemCoolDowns.addCooldown(stack.getItem(), 200);
        }

        return InteractionResult.SUCCESS;
    }

}
