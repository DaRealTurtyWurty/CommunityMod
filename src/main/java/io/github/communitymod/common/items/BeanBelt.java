package io.github.communitymod.common.items;

import java.util.List;

import javax.annotation.Nullable;

import io.github.communitymod.common.armor.BeanArmorMaterial;
import io.github.communitymod.network.ExplosionMessage;
import io.github.communitymod.network.PacketHandler;
import io.github.communitymod.util.MyColor;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Level;

public class BeanBelt extends ArmorItem {
    private final byte COOLDOWN_SECONDS = 3;
    private final float EXPLOSION_RADIUS = 2.0f;

    // TODO: this shouldn't be hard coded
    private final byte TPS = 20;
    private byte ticks = COOLDOWN_SECONDS * TPS;

    public BeanBelt(Properties properties) {
        super(BeanArmorMaterial.BEAN_ARMOR, EquipmentSlot.LEGS, properties);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        if (ticks < COOLDOWN_SECONDS * TPS) {
            ticks++;
        }

        if (player.isCrouching() && ticks == COOLDOWN_SECONDS * TPS) {
            ticks = 0;

            PacketHandler.INSTANCE.sendToServer(new ExplosionMessage(player.getX(), player.getY(), player.getZ(),
                    EXPLOSION_RADIUS, BlockInteraction.DESTROY));
        }

        super.onArmorTick(stack, world, player);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(new TextComponent(I18n.get("item.communitymod.bean_belt.tooltip", MyColor.RESET, MyColor.BOLD,
                MyColor.AQUA, MyColor.RED)));

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
