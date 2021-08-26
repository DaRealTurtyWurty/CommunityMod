package io.github.communitymod.common.items;

import java.util.List;

import io.github.communitymod.common.entities.Meatball;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MeatballWand extends Item {

    public MeatballWand(Properties properties) {
        super(properties);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        double charge = stack.getOrCreateTag().getInt("charge");
        return charge / 100;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getDurabilityForDisplay(stack) != 0;
    }

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents,
            TooltipFlag pIsAdvanced) {

        pTooltipComponents.add(new TextComponent("F L A V O U R"));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (stack.getOrCreateTag().getInt("charge") <= 100) {

            player.getCooldowns().addCooldown(this, 10);

            Meatball proj = new Meatball(world, player);

            proj.setIDFlags(stack.getOrCreateTag().getString("upgrades"));

            Vec3 vec = player.getLookAngle();

            proj.setDeltaMovement(vec.multiply(1.5, 1.5, 1.5));

            proj.setPos(player.getX(), player.getEyeY(), player.getZ());

            world.addFreshEntity(proj);

            if (!player.isCreative()) {
                stack.getOrCreateTag().putInt("charge", stack.getOrCreateTag().getInt("charge") + 2);
            }

            return InteractionResultHolder.success(stack);

        }

        return InteractionResultHolder.pass(stack);

    }

}
