package io.github.communitymod.common.items;

import java.util.List;

import javax.annotation.Nullable;

import io.github.communitymod.common.armor.BeanArmorMaterial;
import io.github.communitymod.network.ExplosionMessage;
import io.github.communitymod.network.PacketHandler;
import io.github.communitymod.util.MyColor;
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

public class BeanBelt extends ArmorItem {
	private static final byte COOLDOWN = 3 * SharedConstants.TICKS_PER_SECOND;
	private byte ticks = COOLDOWN;

	public BeanBelt(Properties properties) {
		super(BeanArmorMaterial.BEAN_ARMOR, EquipmentSlot.LEGS, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(new TextComponent(I18n.get("item.communitymod.bean_belt.tooltip", MyColor.RESET, MyColor.BOLD,
				MyColor.AQUA, MyColor.RED)));

		super.appendHoverText(stack, level, tooltip, flag);
	}

	@Override
	public void onArmorTick(ItemStack stack, Level world, Player player) {
		if (this.ticks < COOLDOWN) {
			this.ticks++;
		}

		if (player.isCrouching() && this.ticks == COOLDOWN) {
			this.ticks = 0;

			PacketHandler.INSTANCE.sendToServer(
					new ExplosionMessage(player.getX(), player.getY(), player.getZ(), 2.0f, BlockInteraction.DESTROY));
		}

		super.onArmorTick(stack, world, player);
	}
}
