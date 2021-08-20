package io.github.communitymod.core;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.capabilities.entitylevel.CapabilityMobLevel;
import io.github.communitymod.capabilities.playerskills.CapabilityPlayerSkills;
import io.github.communitymod.core.init.BlockInit;
import io.github.communitymod.core.init.ItemInit;
import io.github.communitymod.core.util.OtherUtils;
import io.github.communitymod.util.MyColor;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Mod.EventBusSubscriber(modid = CommunityMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GameplayEvents {

    public static int skillShowTimer = 0;
    public static String skillShowText = "";
    public static boolean showLevelCombat = false;
    public static boolean showLevelMining = false;
    public static boolean showLevelForaging = false;
    public static boolean showLevelFarming = false;
    public static int displayLevelCombat;
    public static int displayLevelMining;
    public static int displayLevelForaging;
    public static int displayLevelFarming;
    private static int miningBonus;
    private static int farmingBonus;
    private static int foragingBonus;

    @SubscribeEvent
    public static void onLivingEntityHurt(final LivingHurtEvent event) {
        Level world = event.getEntityLiving().level;
        LivingEntity target = event.getEntityLiving();
        AtomicInteger targetTotalDefense = new AtomicInteger(0);
        if (target != null) {
            targetTotalDefense.updateAndGet(v -> target.getArmorValue());
        }

        LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
        AtomicInteger attackerTotalDefense = new AtomicInteger(0);
        if (attacker != null) {
            attackerTotalDefense.updateAndGet(v -> attacker.getArmorValue());
        }

        if (attacker instanceof Player attacker1) {
            attacker1.getCapability(CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY).ifPresent(skills -> {
                float actualAmount = event.getAmount();
                event.setAmount(actualAmount * (skills.getStrength() / 100f) * (1f + displayLevelCombat * 0.04f) * 5f);

                ArmorStand dmgTag = new ArmorStand(world, event.getEntity().position().x + (0.5f - Math.random()), event.getEntity().position().y + 0.5 + (0.5f - Math.random()), event.getEntity().position().z + (0.5f - Math.random()));
                dmgTag.setCustomName(Component.nullToEmpty(MyColor.YELLOW + Math.round(event.getAmount())));
                dmgTag.setCustomNameVisible(true);
                dmgTag.setInvulnerable(true);
                dmgTag.noPhysics = true;
                dmgTag.setInvisible(true);
                world.addFreshEntity(dmgTag);
            });
        }

        if (attacker instanceof Mob attacker2) {
            attacker2.getCapability(CapabilityMobLevel.MOB_LEVEL_CAPABILITY).ifPresent(mobLevel -> {
                float actualAmount = event.getAmount();
                event.setAmount(actualAmount * (((((mobLevel.getMobLevel() / 4f) - 1f) * 50f) + 100f) / 100f) * (1f + (mobLevel.getMobLevel() / 4f) * 0.04f) * 5f);

                ArmorStand dmgTag = new ArmorStand(world, event.getEntity().position().x + (0.5f - Math.random()), event.getEntity().position().y + 0.5 + (0.5f - Math.random()), event.getEntity().position().z + (0.5f - Math.random()));
                dmgTag.setCustomName(Component.nullToEmpty(MyColor.YELLOW + Math.round(event.getAmount())));
                dmgTag.setCustomNameVisible(true);
                dmgTag.setInvulnerable(true);
                dmgTag.noPhysics = true;
                dmgTag.setInvisible(true);
                world.addFreshEntity(dmgTag);
            });
        }

        if (target instanceof Player) {
            target.getCapability(CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY).ifPresent(skills -> {
                if (event.getSource() == DamageSource.FALL) {

                    ArmorStand dmgTag = new ArmorStand(world, event.getEntity().position().x + (0.5f - Math.random()), event.getEntity().position().y + 0.5 + (0.5f - Math.random()), event.getEntity().position().z + (0.5f - Math.random()));
                    dmgTag.setCustomName(Component.nullToEmpty(MyColor.YELLOW + (Math.round(event.getAmount()) * 5f)));
                    dmgTag.setCustomNameVisible(true);
                    dmgTag.setInvulnerable(true);
                    dmgTag.noPhysics = true;
                    dmgTag.setInvisible(true);
                    world.addFreshEntity(dmgTag);
                    event.setAmount(event.getAmount() * (target.getMaxHealth() / skills.getMaxHealth()));

                } else if (event.getSource().isBypassArmor()) {
                    event.setAmount(event.getAmount() * (target.getMaxHealth() / skills.getMaxHealth()));
                } else {
                    event.setAmount((event.getAmount() * (1 - (skills.getDefense() / (skills.getDefense() + 100f)))) * (target.getMaxHealth() / skills.getMaxHealth()));
                }
            });
        }

        if (target instanceof Mob) {
            target.getCapability(CapabilityMobLevel.MOB_LEVEL_CAPABILITY).ifPresent(skills -> {
                if (event.getSource() == DamageSource.FALL) {
                    ArmorStand dmgTag = new ArmorStand(world, event.getEntity().position().x + (0.5f - Math.random()), event.getEntity().position().y + 0.5 + (0.5f - Math.random()), event.getEntity().position().z + (0.5f - Math.random()));
                    dmgTag.setCustomName(Component.nullToEmpty(MyColor.YELLOW + (Math.round(event.getAmount() * 5f))));
                    dmgTag.setCustomNameVisible(true);
                    dmgTag.setInvulnerable(true);
                    dmgTag.noPhysics = true;
                    dmgTag.setInvisible(true);
                    world.addFreshEntity(dmgTag);
                    event.setAmount((event.getAmount() * (target.getMaxHealth() / (skills.getMobLevel() * 2.5f * target.getMaxHealth()))));
                } else if (event.getSource().isBypassArmor()) {
                    event.setAmount(event.getAmount() * (target.getMaxHealth() / (skills.getMobLevel() * 2.5f * target.getMaxHealth())));
                } else {
                    event.setAmount(event.getAmount() * (1 - (targetTotalDefense.get() / (targetTotalDefense.get() + 100f))) * (target.getMaxHealth() / (skills.getMobLevel() * 2.5f * target.getMaxHealth())));
                }
            });
        }

    }

    @SubscribeEvent
    public static void playerUpdate(final LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            Level level = event.getEntity().level;
            skillShowTimer -= 1;
            skillShowText = skillShowTimer < 1 ? "" : skillShowText;
            player.getCapability(CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY).ifPresent(skills -> {
                if (showLevelCombat) {
                    OtherUtils.SendChat(player, MyColor.DARK_AQUA + MyColor.BOLD + "========================================");
                    OtherUtils.SendChat(player, MyColor.AQUA + "Combat " + MyColor.DARK_GRAY + (displayLevelCombat - 1) + " \u2192 " + displayLevelCombat);
                    OtherUtils.SendChat(player, MyColor.GOLD + "REWARDS");
                    OtherUtils.SendChat(player, MyColor.GOLD + "   - Deal " + MyColor.DARK_GRAY + "+" + ((displayLevelCombat - 1) * 4) + "%" + MyColor.YELLOW + " \u2192 " + MyColor.GOLD + "+" + (displayLevelCombat * 4) + "% More damage to mobs.");
                    OtherUtils.SendChat(player, MyColor.DARK_AQUA + MyColor.BOLD + "========================================");
                    player.playSound(SoundEvents.PLAYER_LEVELUP, 1f, 0.5f);
                    showLevelCombat = false;
                }
                if (showLevelMining) {
                    OtherUtils.SendChat(player, MyColor.DARK_AQUA + MyColor.BOLD + "========================================");
                    OtherUtils.SendChat(player, MyColor.AQUA + "Mining " + MyColor.DARK_GRAY + (displayLevelMining - 1) + " \u2192 " + displayLevelMining);
                    OtherUtils.SendChat(player, MyColor.GOLD + "REWARDS");
                    OtherUtils.SendChat(player, MyColor.GOLD + "   - Gain " + MyColor.DARK_GRAY + "+" + ((displayLevelMining - 1) * 2) + MyColor.DARK_GREEN + " \u2192 " + MyColor.GREEN + "+" + (displayLevelMining * 2) + " Defense.");
                    OtherUtils.SendChat(player, MyColor.DARK_AQUA + MyColor.BOLD + "========================================");
                    player.playSound(SoundEvents.PLAYER_LEVELUP, 1f, 0.5f);
                    showLevelMining = false;
                }
                if (showLevelFarming) {
                    OtherUtils.SendChat(player, MyColor.DARK_AQUA + MyColor.BOLD + "========================================");
                    OtherUtils.SendChat(player, MyColor.AQUA + "Farming " + MyColor.DARK_GRAY + (displayLevelFarming - 1) + " \u2192 " + displayLevelFarming);
                    OtherUtils.SendChat(player, MyColor.GOLD + "REWARDS");
                    OtherUtils.SendChat(player, MyColor.GOLD + "   - Gain " + MyColor.DARK_GRAY + "+" + ((displayLevelFarming - 1) * 3) + MyColor.DARK_RED + " \u2192 " + MyColor.RED + "+" + (displayLevelFarming * 3) + " Max Health.");
                    OtherUtils.SendChat(player, MyColor.DARK_AQUA + MyColor.BOLD + "========================================");
                    player.playSound(SoundEvents.PLAYER_LEVELUP, 1f, 0.5f);
                    showLevelFarming = false;
                }
                if (showLevelForaging) {
                    OtherUtils.SendChat(player, MyColor.DARK_AQUA + MyColor.BOLD + "========================================");
                    OtherUtils.SendChat(player, MyColor.AQUA + "Foraging " + MyColor.DARK_GRAY + (displayLevelForaging - 1) + " \u2192 " + displayLevelForaging);
                    OtherUtils.SendChat(player, MyColor.GOLD + "REWARDS");
                    OtherUtils.SendChat(player, MyColor.GOLD + "   - Gain " + MyColor.DARK_GRAY + "+" + ((displayLevelForaging - 1) * 2) + MyColor.DARK_RED + " \u2192 " + MyColor.RED + "+" + (displayLevelForaging * 4) + " Strength.");
                    OtherUtils.SendChat(player, MyColor.DARK_AQUA + MyColor.BOLD + "========================================");
                    player.playSound(SoundEvents.PLAYER_LEVELUP, 1f, 0.5f);
                    showLevelForaging = false;
                }

                if (skills.getMiningLvl() > 0) {
                    miningBonus = skills.getMiningLvl() * 2;
                }
                if (skills.getFarmingLvl() > 0) {
                    farmingBonus = skills.getFarmingLvl() * 3;
                }
                if (skills.getForagingLvl() > 0) {
                    foragingBonus = skills.getForagingLvl() * 2;
                }

                int strengthAdder = 100;
                int speedAdder = 100;
                int maxHealthAdder = 100;
                int defenseAdder = 0;

                strengthAdder += player.experienceLevel / 5f;
                speedAdder += player.experienceLevel / 10f;
                maxHealthAdder += player.experienceLevel / 10f;
                defenseAdder += player.experienceLevel / 50f;

                int beanCount = player.getInventory().countItem(ItemInit.BEANS.get()) + player.getInventory().countItem(new BlockItem(BlockInit.BEAN_BLOCK.get(), new Item.Properties().tab(CommunityMod.TAB))) * 9;
                if (player.getInventory().countItem(ItemInit.BEAN_ARTIFACT.get()) > 0 && skills.getFarmingLvl() >= 7) {
                    speedAdder += ((float) Math.floor(beanCount / 64d));
                    strengthAdder += ((int) Math.round(Math.floor(beanCount / 64d)));
                    maxHealthAdder += ((float) Math.floor(beanCount / 64d));
                    int rnd1 = level.random.nextInt(1000) + 1;
                    if (rnd1 == 50) {
                        ItemEntity beanItem = new ItemEntity(level, player.position().x, player.position().y, player.position().z, new ItemStack(ItemInit.BEANS.get()));
                        level.addFreshEntity(beanItem);
                    }
                    int rnd = level.random.nextInt(2000) + 1;
                    if (rnd == 50) {
                        final var blockItem = new BlockItem(BlockInit.BEAN_BLOCK.get(), new Item.Properties().tab(CommunityMod.TAB));
                        blockItem.setRegistryName(BlockInit.BEAN_BLOCK.getId());
                        ItemEntity beanItem = new ItemEntity(level, player.position().x, player.position().y, player.position().z, new ItemStack(blockItem));
                        level.addFreshEntity(beanItem);
                    }
                } else if (player.getInventory().countItem(ItemInit.BEAN_RING.get()) > 0 && skills.getFarmingLvl() >= 6) {
                    speedAdder += ((float) Math.floor(beanCount / 96d));
                    strengthAdder += ((int) Math.round(Math.floor(beanCount / 96d)));
                    int rnd = level.random.nextInt(2000) + 1;
                    if (rnd == 50) {
                        ItemEntity beanItem = new ItemEntity(level, player.position().x, player.position().y, player.position().z, new ItemStack(ItemInit.BEANS.get()));
                        level.addFreshEntity(beanItem);
                    }
                } else if (player.getInventory().countItem(ItemInit.BEAN_TALISMAN.get()) > 0 && skills.getFarmingLvl() >= 5) {
                    speedAdder += ((float) Math.floor(beanCount / 128d));
                }

                strengthAdder += foragingBonus;
                defenseAdder += miningBonus;
                maxHealthAdder += farmingBonus;

                String color;
                if (player.isFullyFrozen()) {
                    color = MyColor.AQUA;
                } else if (player.hasEffect(MobEffects.WITHER)) {
                    color = MyColor.DARK_GRAY;
                } else if (player.hasEffect(MobEffects.ABSORPTION)) {
                    color = MyColor.GOLD;
                } else {
                    color = MyColor.RED;
                }

                player.displayClientMessage(Component.nullToEmpty(
                        color + skills.getHealth() + "/" + skills.getMaxHealth() + " Health" + "  " +
                                ((skills.getDefense() != 0) ? (MyColor.GREEN + (skills.getDefense() * 5) + " Defense" + "  ") : ("")) +
                                (skillShowTimer > 0 ? skillShowText : "")), true);

                int headDefense;
                int chestDefense;
                int legsDefense;
                int feetDefense;

                if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ArmorItem head) {
                    headDefense = head.getDefense();
                } else {
                    headDefense = 0;
                }

                if (player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ArmorItem chest) {
                    chestDefense = chest.getDefense();
                } else {
                    chestDefense = 0;
                }

                if (player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ArmorItem legs) {
                    legsDefense = legs.getDefense();
                } else {
                    legsDefense = 0;
                }

                if (player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ArmorItem feet) {
                    feetDefense = feet.getDefense();
                } else {
                    feetDefense = 0;
                }

                skills.setDefense(((headDefense + chestDefense + legsDefense + feetDefense) * 5) + defenseAdder);
                skills.setStrength(strengthAdder);
                skills.setMaxHealth(maxHealthAdder);
                skills.setHealth(Math.round(((event.getEntityLiving().getHealth() + event.getEntityLiving().getAbsorptionAmount()) / event.getEntityLiving().getMaxHealth()) * skills.getMaxHealth()));
                skills.setSpeed(Math.max(speedAdder, 0));
                //debug only
                //skills.setSpeed(player.experienceLevel);

                Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(skills.getSpeed() / 1000D);

            });
        } else if (event.getEntityLiving() instanceof Mob) {
            LivingEntity entity = event.getEntityLiving();
            entity.getCapability(CapabilityMobLevel.MOB_LEVEL_CAPABILITY).ifPresent(mobLevel -> {
                entity.setCustomNameVisible(true);
                entity.setCustomName(Component.nullToEmpty(MyColor.GRAY + "[Lv." + MyColor.RED + (mobLevel.getMobLevel()) + MyColor.GRAY + "] " +
                        entity.getType().getDescription().getString() +
                        " " + MyColor.RED + Math.round((entity.getHealth() + entity.getAbsorptionAmount()) * 5 * (mobLevel.getMobLevel() / 4f)) + "/" + Math.round(entity.getMaxHealth() * 5 * (mobLevel.getMobLevel() / 4f))));
            });
        }
    }

    @SubscribeEvent
    public static void PlayerHeal(final LivingHealEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            player.getCapability(CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY).ifPresent(
                    skills -> event.setAmount((event.getAmount() / skills.getMaxHealth()) * 100));
        }
    }
}
