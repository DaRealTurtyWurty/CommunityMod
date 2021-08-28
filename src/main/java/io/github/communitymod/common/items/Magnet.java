package io.github.communitymod.common.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.github.communitymod.core.util.NBTUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class Magnet extends Item {

    public Magnet(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
        if (entity instanceof Player && NBTUtils.getBoolean(stack, "Enabled")) {
            Player player = (Player) entity;
            ArrayList<ItemEntity> rangeItems = (ArrayList<ItemEntity>) level.getEntitiesOfClass(ItemEntity.class,
                    entity.getBoundingBox().inflate(7.0));
            for (ItemEntity item : rangeItems) {
                if (NBTUtils.getBoolean(item.getItem(), "IsNotMagnetable") == true)
                    continue;

                if (item.getThrower() != null && item.getThrower().equals(entity.getUUID()) && item.hasPickUpDelay())
                    continue;

                if (!level.isClientSide()) {
                    item.setNoPickUpDelay();
                    int damage = item.getItem().getCount();
                    Boolean shouldDamage = true;
                    if (item.distanceTo(entity) < 1.5F)
                        shouldDamage = false;
                    item.setPos(entity.getX(), entity.getY(), entity.getZ());
                    if (shouldDamage == true)
                        if (!player.isCreative())
                            stack.hurt(damage, new Random(1), (ServerPlayer) player);
                }
            }

            List<ExperienceOrb> xp = level.getEntitiesOfClass(ExperienceOrb.class,
                    entity.getBoundingBox().inflate(7.0));
            for (ExperienceOrb orb : xp) {
                if (!level.isClientSide()) {
                    orb.setPos(entity.getX(), entity.getY(), entity.getZ());
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents,
            TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent(
                "Teleports items and experience orbs (in a range of 7 blocks) to the player"));
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        NBTUtils.flipBoolean(stack, "Enabled");
        NBTUtils.flipBoolean(stack, "IsNotMagnetable");
        return super.use(pLevel, pPlayer, pUsedHand);
    }
    
    @Override
    public boolean isFoil(ItemStack pStack) {
        return NBTUtils.getBoolean(pStack, "Enabled");
    }
    
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {

        if (enchantment.isAllowedOnBooks()) {
            return false;
        }

        if (enchantment.isCompatibleWith(Enchantments.MENDING)) {
            return false;
        }

        return false;
    }
}
