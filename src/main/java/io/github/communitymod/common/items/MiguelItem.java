package io.github.communitymod.common.items;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;

public class MiguelItem extends Item {

    public MiguelItem(final Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(final ItemStack stack, final UseOnContext context) {

        final var itemCoolDowns = context.getPlayer().getCooldowns();

        if (!context.getLevel().isClientSide() && !itemCoolDowns.isOnCooldown(stack.getItem())) {
            final var randNumb = context.getPlayer().getRandom().nextInt(100);
            if (randNumb == 1) {
                context.getPlayer().spawnAtLocation(Items.DIAMOND);
            } else if (randNumb > 90) {
                context.getPlayer().spawnAtLocation(Items.IRON_INGOT);
            } else {
                context.getPlayer().spawnAtLocation(Items.DIRT);
            }
            itemCoolDowns.addCooldown(stack.getItem(), 200);
        }

        return InteractionResult.SUCCESS;
    }

}
