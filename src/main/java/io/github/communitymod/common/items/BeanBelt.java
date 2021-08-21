package io.github.communitymod.common.items;

import io.github.communitymod.core.network.ExplosionMessage;
import io.github.communitymod.core.network.PacketHandler;
import io.github.communitymod.core.util.BeanArmorMaterial;
import io.github.communitymod.core.util.ColorConstants;
import net.minecraft.SharedConstants;
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

import javax.annotation.Nullable;
import java.util.List;

public class BeanBelt extends ArmorItem {
    private static final byte COOLDOWN = 3 * SharedConstants.TICKS_PER_SECOND;
    private byte ticks = COOLDOWN;

    public BeanBelt(final Properties properties) {
        super(BeanArmorMaterial.BEAN_ARMOR, EquipmentSlot.LEGS, properties);
    }

    @Override
    public void appendHoverText(final ItemStack stack, @Nullable final Level level,
            final List<Component> tooltip, final TooltipFlag flag) {
        tooltip.add(new TextComponent(I18n.get("item.communitymod.bean_belt.tooltip", ColorConstants.RESET,
                ColorConstants.BOLD, ColorConstants.AQUA, ColorConstants.RED)));

        super.appendHoverText(stack, level, tooltip, flag);
    }

    @Override
    public void onArmorTick(final ItemStack stack, final Level world, final Player player) {
        if (this.ticks < COOLDOWN) {
            this.ticks++;
        }

        if (player.isCrouching() && this.ticks == COOLDOWN) {
            this.ticks = 0;

            PacketHandler.INSTANCE.sendToServer(new ExplosionMessage(player.getX(), player.getY(),
                    player.getZ(), 2.0f, BlockInteraction.DESTROY));
        }

        super.onArmorTick(stack, world, player);
    }
}
