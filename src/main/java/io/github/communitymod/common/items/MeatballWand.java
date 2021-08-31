package io.github.communitymod.common.items;

import java.util.List;

import io.github.communitymod.common.entities.Meatball;
import io.github.communitymod.core.util.MeatballTypes;
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

        for (String key : pStack.getOrCreateTag().getAllKeys()) {
            if (MeatballTypes.containsValue(key)) {
                String lower = key.toLowerCase().substring(1);
                String upper = key.substring(0, 1);
                pTooltipComponents.add(new TextComponent(upper + lower));
            }
        }

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (stack.getOrCreateTag().getInt("charge") <= 100) {

            player.getCooldowns().addCooldown(this, stack.getOrCreateTag().getBoolean("FASTRELOAD") ? 10 : 20);

            Meatball proj = new Meatball(world, player);

            Vec3 vec = player.getLookAngle();

            float f = stack.getOrCreateTag().getBoolean("SPEED") ? 3.0f : 1.5f;

            proj.setDeltaMovement(vec.multiply(f, f, f));

            proj.setPos(player.getX(), player.getEyeY(), player.getZ());

            String id = "";

            for (int i = 0; i < 16; i++) {

                if (stack.getOrCreateTag().contains(MeatballTypes.get(i).name())) {
                    id = id.concat("1");
                } else
                    id = id.concat("0");
            }

            proj.setIDFlags(id);

            world.addFreshEntity(proj);

            if (!player.isCreative()) {

                stack.getOrCreateTag().putInt("charge", stack.getOrCreateTag().getInt("charge")
                        + (stack.getOrCreateTag().getBoolean("GENTILE") ? 1 : 2));

            }

            return InteractionResultHolder.success(stack);

        }

        return InteractionResultHolder.pass(stack);

    }

}
