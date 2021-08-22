package io.github.communitymod.common.items.bean_accessories;

import io.github.communitymod.core.util.ColorConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class BeanTalisman extends Item {
    public BeanTalisman(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> text, TooltipFlag p_41424_) {
        text.add(new TextComponent(ColorConstants.GRAY + "Gain " + ColorConstants.WHITE + "+1 Speed" + ColorConstants.GRAY + " for every " + ColorConstants.GOLD + "128 beans" + ColorConstants.GRAY + " in your inventory."));
        text.add(new TextComponent(ColorConstants.GRAY + "Note: " + ColorConstants.GOLD + "1 Block of Beans" + ColorConstants.GRAY + " = " + ColorConstants.GOLD + "9 Beans"));
        text.add(new TextComponent(""));
        text.add(new TextComponent(ColorConstants.RED + "Requires " + ColorConstants.GOLD + "Farming 5"));
        super.appendHoverText(p_41421_, p_41422_, text, p_41424_);
    }
}
