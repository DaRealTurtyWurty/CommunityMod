package io.github.communitymod.core;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.capabilities.entitylevel.CapabilityMobLevel;
import io.github.communitymod.capabilities.entitylevel.DefaultMobLevel;
import io.github.communitymod.capabilities.playerskills.CapabilityPlayerSkills;
import io.github.communitymod.capabilities.playerskills.DefaultPlayerSkills;
import io.github.communitymod.common.items.Scythe;
import io.github.communitymod.common.items.Soul;
import io.github.communitymod.core.init.BlockInit;
import io.github.communitymod.core.init.EnchantmentInit;
import io.github.communitymod.core.init.ItemInit;
import io.github.communitymod.core.util.ColorConstants;
import io.github.communitymod.core.util.OtherUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
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

    public static int ticksPassed = 0;

    public static boolean showLevelCombat = false;
    public static boolean showLevelMining = false;
    public static boolean showLevelForaging = false;
    public static boolean showLevelFarming = false;
    public static int displayLevelCombat;
    public static int displayLevelMining;
    public static int displayLevelForaging;
    public static int displayLevelFarming;
    public static int miningBonus;
    public static int farmingBonus;
    public static int foragingBonus;

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
                DefaultPlayerSkills actualSkills = (DefaultPlayerSkills) skills;
                float actualAmount = event.getAmount();
                event.setAmount(actualAmount * (actualSkills.strength / 100f) * (1f + displayLevelCombat * 0.04f) * 5f);

                if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SOUL_BOOST.get(), attacker1.getMainHandItem()) > 0) {
                    event.setAmount(event.getAmount() * (1f + (actualSkills.soulCount / 0.1f)));
                }

                ArmorStand dmgTag = new ArmorStand(world, event.getEntity().position().x + (0.5f - Math.random()), event.getEntity().position().y + 0.5 + (0.5f - Math.random()), event.getEntity().position().z + (0.5f - Math.random()));
                dmgTag.setCustomName(Component.nullToEmpty(ColorConstants.YELLOW + Math.round(event.getAmount())));
                dmgTag.setCustomNameVisible(true);
                dmgTag.setInvulnerable(true);
                dmgTag.noPhysics = true;
                dmgTag.setInvisible(true);
                world.addFreshEntity(dmgTag);
            });
        }

        if (attacker instanceof Mob attacker2) {
            attacker2.getCapability(CapabilityMobLevel.MOB_LEVEL_CAPABILITY).ifPresent(mobLevel -> {
                DefaultMobLevel actualMobLevel = (DefaultMobLevel) mobLevel;
                float actualAmount = event.getAmount();
                event.setAmount(actualAmount * (((((1f + (actualMobLevel.mobLevel - 1f) / 4f) - 1f) * 50f) + 100f) / 100f) * (1f + (1f + (actualMobLevel.mobLevel - 1f) / 4f) * 0.04f));

                ArmorStand dmgTag = new ArmorStand(world, event.getEntity().position().x + (0.5f - Math.random()), event.getEntity().position().y + 0.5 + (0.5f - Math.random()), event.getEntity().position().z + (0.5f - Math.random()));
                dmgTag.setCustomName(Component.nullToEmpty(ColorConstants.YELLOW + Math.round(event.getAmount())));
                dmgTag.setCustomNameVisible(true);
                dmgTag.setInvulnerable(true);
                dmgTag.noPhysics = true;
                dmgTag.setInvisible(true);
                world.addFreshEntity(dmgTag);
            });
        }

        if (target instanceof Player) {
            target.getCapability(CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY).ifPresent(skills -> {
                DefaultPlayerSkills actualSkills = (DefaultPlayerSkills) skills;
                if (event.getSource() == DamageSource.FALL) {

                    ArmorStand dmgTag = new ArmorStand(world, event.getEntity().position().x + (0.5f - Math.random()), event.getEntity().position().y + 0.5 + (0.5f - Math.random()), event.getEntity().position().z + (0.5f - Math.random()));
                    dmgTag.setCustomName(Component.nullToEmpty(ColorConstants.YELLOW + (Math.round(event.getAmount() * 5))));
                    dmgTag.setCustomNameVisible(true);
                    dmgTag.setInvulnerable(true);
                    dmgTag.noPhysics = true;
                    dmgTag.setInvisible(true);
                    world.addFreshEntity(dmgTag);
                    event.setAmount(event.getAmount() * (target.getMaxHealth() / actualSkills.maxHealth) * 5f);

                } else if (event.getSource().isBypassArmor()) {
                    event.setAmount(event.getAmount() * (target.getMaxHealth() / actualSkills.maxHealth));
                } else {
                    event.setAmount((event.getAmount() * (1 - (actualSkills.defense / (actualSkills.defense + 100f)))) * (target.getMaxHealth() / actualSkills.maxHealth));
                }
            });
        }

        if (target instanceof Mob) {
            target.getCapability(CapabilityMobLevel.MOB_LEVEL_CAPABILITY).ifPresent(mobLevel -> {
                DefaultMobLevel actualMobLevel = (DefaultMobLevel) mobLevel;
                if (event.getSource() == DamageSource.FALL) {
                    event.setAmount(event.getAmount() / (1f + ((actualMobLevel.mobLevel - 1f) / 4f)) * 5f);
                    ArmorStand dmgTag = new ArmorStand(world, event.getEntity().position().x + (0.5f - Math.random()), event.getEntity().position().y + 0.5 + (0.5f - Math.random()), event.getEntity().position().z + (0.5f - Math.random()));
                    dmgTag.setCustomName(Component.nullToEmpty(ColorConstants.YELLOW + (Math.round(event.getAmount()))));
                    dmgTag.setCustomNameVisible(true);
                    dmgTag.setInvulnerable(true);
                    dmgTag.noPhysics = true;
                    dmgTag.setInvisible(true);
                    world.addFreshEntity(dmgTag);
                } else if (event.getSource().isBypassArmor()) {
                    event.setAmount(event.getAmount() / (1f + ((actualMobLevel.mobLevel - 1f) / 4f)) * 5f);
                } else {
                    event.setAmount(event.getAmount() * (1 - (targetTotalDefense.get() / (targetTotalDefense.get() + 100f))) / (1f + ((actualMobLevel.mobLevel - 1f) / 4f)));
                }
            });
        }

    }

    @SubscribeEvent
    public static void entityUpdate(final LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            Level level = event.getEntity().level;
            skillShowTimer -= 1;
            skillShowText = skillShowTimer < 1 ? "" : skillShowText;
            ticksPassed += 1;
            player.getCapability(CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY).ifPresent(skills -> {
                DefaultPlayerSkills actualSkills = (DefaultPlayerSkills) skills;
                if (showLevelCombat) {
                    OtherUtils.sendChat(player, ColorConstants.DARK_AQUA + ColorConstants.BOLD + "========================================");
                    OtherUtils.sendChat(player, ColorConstants.AQUA + "Combat " + ColorConstants.DARK_GRAY + (displayLevelCombat - 1) + " \u2192 " + displayLevelCombat);
                    OtherUtils.sendChat(player, ColorConstants.GOLD + "REWARDS");
                    OtherUtils.sendChat(player, ColorConstants.GOLD + "   - Deal " + ColorConstants.DARK_GRAY + "+" + ((displayLevelCombat - 1) * 4) + "%" + ColorConstants.YELLOW + " \u2192 " + ColorConstants.GOLD + "+" + (displayLevelCombat * 4) + "% More damage to mobs.");
                    OtherUtils.sendChat(player, ColorConstants.DARK_AQUA + ColorConstants.BOLD + "========================================");
                    player.playSound(SoundEvents.PLAYER_LEVELUP, 1f, 0.5f);
                    showLevelCombat = false;
                }
                if (showLevelMining) {
                    OtherUtils.sendChat(player, ColorConstants.DARK_AQUA + ColorConstants.BOLD + "========================================");
                    OtherUtils.sendChat(player, ColorConstants.AQUA + "Mining " + ColorConstants.DARK_GRAY + (displayLevelMining - 1) + " \u2192 " + displayLevelMining);
                    OtherUtils.sendChat(player, ColorConstants.GOLD + "REWARDS");
                    OtherUtils.sendChat(player, ColorConstants.GOLD + "   - Gain " + ColorConstants.DARK_GRAY + "+" + ((displayLevelMining - 1) * 2) + ColorConstants.DARK_GREEN + " \u2192 " + ColorConstants.GREEN + "+" + (displayLevelMining * 2) + " Defense.");
                    OtherUtils.sendChat(player, ColorConstants.DARK_AQUA + ColorConstants.BOLD + "========================================");
                    player.playSound(SoundEvents.PLAYER_LEVELUP, 1f, 0.5f);
                    showLevelMining = false;
                }
                if (showLevelFarming) {
                    OtherUtils.sendChat(player, ColorConstants.DARK_AQUA + ColorConstants.BOLD + "========================================");
                    OtherUtils.sendChat(player, ColorConstants.AQUA + "Farming " + ColorConstants.DARK_GRAY + (displayLevelFarming - 1) + " \u2192 " + displayLevelFarming);
                    OtherUtils.sendChat(player, ColorConstants.GOLD + "REWARDS");
                    OtherUtils.sendChat(player, ColorConstants.GOLD + "   - Gain " + ColorConstants.DARK_GRAY + "+" + ((displayLevelFarming - 1) * 3) + ColorConstants.DARK_RED + " \u2192 " + ColorConstants.RED + "+" + (displayLevelFarming * 3) + " Max Health.");
                    OtherUtils.sendChat(player, ColorConstants.DARK_AQUA + ColorConstants.BOLD + "========================================");
                    player.playSound(SoundEvents.PLAYER_LEVELUP, 1f, 0.5f);
                    showLevelFarming = false;
                }
                if (showLevelForaging) {
                    OtherUtils.sendChat(player, ColorConstants.DARK_AQUA + ColorConstants.BOLD + "========================================");
                    OtherUtils.sendChat(player, ColorConstants.AQUA + "Foraging " + ColorConstants.DARK_GRAY + (displayLevelForaging - 1) + " \u2192 " + displayLevelForaging);
                    OtherUtils.sendChat(player, ColorConstants.GOLD + "REWARDS");
                    OtherUtils.sendChat(player, ColorConstants.GOLD + "   - Gain " + ColorConstants.DARK_GRAY + "+" + ((displayLevelForaging - 1) * 2) + ColorConstants.DARK_RED + " \u2192 " + ColorConstants.RED + "+" + (displayLevelForaging * 4) + " Strength.");
                    OtherUtils.sendChat(player, ColorConstants.DARK_AQUA + ColorConstants.BOLD + "========================================");
                    player.playSound(SoundEvents.PLAYER_LEVELUP, 1f, 0.5f);
                    showLevelForaging = false;
                }

                if (actualSkills.miningLvl > 0) {
                    miningBonus = actualSkills.miningLvl * 2;
                }
                if (actualSkills.farmingLvl > 0) {
                    farmingBonus = actualSkills.farmingLvl * 3;
                }
                if (actualSkills.foragingLvl > 0) {
                    foragingBonus = actualSkills.foragingLvl * 2;
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
                if (player.getInventory().countItem(ItemInit.BEAN_ARTIFACT.get()) > 0 && actualSkills.farmingLvl >= 7) {
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
                } else if (player.getInventory().countItem(ItemInit.BEAN_RING.get()) > 0 && actualSkills.farmingLvl >= 6) {
                    speedAdder += ((float) Math.floor(beanCount / 96d));
                    strengthAdder += ((int) Math.round(Math.floor(beanCount / 96d)));
                    int rnd = level.random.nextInt(2000) + 1;
                    if (rnd == 50) {
                        ItemEntity beanItem = new ItemEntity(level, player.position().x, player.position().y, player.position().z, new ItemStack(ItemInit.BEANS.get()));
                        level.addFreshEntity(beanItem);
                    }
                } else if (player.getInventory().countItem(ItemInit.BEAN_TALISMAN.get()) > 0 && actualSkills.farmingLvl >= 5) {
                    speedAdder += ((float) Math.floor(beanCount / 128d));
                }

                strengthAdder += foragingBonus;
                defenseAdder += miningBonus;
                maxHealthAdder += farmingBonus;

                if (ticksPassed % 100 == 0 && actualSkills.soulCount > 500) {
                    player.hurt(DamageSource.GENERIC, player.getMaxHealth() / 100f);
                    OtherUtils.sendChat(player, ColorConstants.DARK_RED + "The " + ColorConstants.AQUA + "souls" + ColorConstants.DARK_RED + " start to bite...");
                }

                String color;
                if (player.isFullyFrozen()) {
                    color = ColorConstants.AQUA;
                } else if (player.hasEffect(MobEffects.WITHER)) {
                    color = ColorConstants.DARK_GRAY;
                } else if (player.hasEffect(MobEffects.ABSORPTION)) {
                    color = ColorConstants.GOLD;
                } else {
                    color = ColorConstants.RED;
                }

                player.displayClientMessage(Component.nullToEmpty(
                        color + actualSkills.health + "/" + actualSkills.maxHealth + " Health" + "  " +
                                ((actualSkills.defense != 0) ? (ColorConstants.GREEN + (actualSkills.defense) + " Defense" + "  ") : ("")) +
                                (skillShowTimer > 0 ? skillShowText : "")), true);

                int headDefense;
                int chestDefense;
                int legsDefense;
                int feetDefense;
                int totalAddedProtection = 0;

                if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ArmorItem head) {
                    headDefense = head.getDefense();
                    if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION, player.getItemBySlot(EquipmentSlot.HEAD)) > 0) {
                        totalAddedProtection += EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION, player.getItemBySlot(EquipmentSlot.HEAD)) * 5;
                    }
                    if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SOUL_BOOST.get(), player.getItemBySlot(EquipmentSlot.HEAD)) > 0) {
                        totalAddedProtection += actualSkills.soulCount;
                    }
                } else {
                    headDefense = 0;
                }

                if (player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ArmorItem chest) {
                    chestDefense = chest.getDefense();
                    if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION, player.getItemBySlot(EquipmentSlot.CHEST)) > 0) {
                        totalAddedProtection += EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION, player.getItemBySlot(EquipmentSlot.CHEST)) * 5;
                    }
                    if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SOUL_BOOST.get(), player.getItemBySlot(EquipmentSlot.CHEST)) > 0) {
                        totalAddedProtection += actualSkills.soulCount;
                    }
                } else {
                    chestDefense = 0;
                }

                if (player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ArmorItem legs) {
                    legsDefense = legs.getDefense();
                    if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION, player.getItemBySlot(EquipmentSlot.LEGS)) > 0) {
                        totalAddedProtection += EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION, player.getItemBySlot(EquipmentSlot.LEGS)) * 5;
                    }
                    if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SOUL_BOOST.get(), player.getItemBySlot(EquipmentSlot.LEGS)) > 0) {
                        totalAddedProtection += actualSkills.soulCount;
                    }
                } else {
                    legsDefense = 0;
                }

                if (player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ArmorItem feet) {
                    feetDefense = feet.getDefense();
                    if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION, player.getItemBySlot(EquipmentSlot.FEET)) > 0) {
                        totalAddedProtection += EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION, player.getItemBySlot(EquipmentSlot.FEET)) * 5;
                    }
                    if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SOUL_BOOST.get(), player.getItemBySlot(EquipmentSlot.FEET)) > 0) {
                        totalAddedProtection += actualSkills.soulCount;
                    }
                } else {
                    feetDefense = 0;
                }

                actualSkills.defense = (((headDefense + chestDefense + legsDefense + feetDefense) * 15) + defenseAdder + totalAddedProtection);
                actualSkills.strength = (strengthAdder);
                actualSkills.maxHealth = (maxHealthAdder + (event.getEntityLiving().getMaxHealth() - 20));
                actualSkills.health = (Math.round(((event.getEntityLiving().getHealth() + event.getEntityLiving().getAbsorptionAmount()) / event.getEntityLiving().getMaxHealth()) * actualSkills.maxHealth));
                actualSkills.speed = (Math.max(speedAdder, 0));
                //debug only
                //actualSkills.setSpeed(player.experienceLevel);

                Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(actualSkills.speed / 1000D);

            });
        } else if (event.getEntityLiving() instanceof Mob) {
            LivingEntity entity = event.getEntityLiving();
            entity.getCapability(CapabilityMobLevel.MOB_LEVEL_CAPABILITY).ifPresent(mobLevel -> {
                DefaultMobLevel actualMobLevel = (DefaultMobLevel) mobLevel;
                entity.setCustomNameVisible(true);
                entity.setCustomName(Component.nullToEmpty(ColorConstants.GRAY + "[Lv." + ColorConstants.RED + (actualMobLevel.mobLevel) + ColorConstants.GRAY + "] " +
                        entity.getType().getDescription().getString() +
                        " " + ColorConstants.RED + Math.round((entity.getHealth() + entity.getAbsorptionAmount()) * 5f * (1f + (actualMobLevel.mobLevel - 1f) / 4f)) + "/" + Math.round(entity.getMaxHealth() * 5f * (1f + (actualMobLevel.mobLevel - 1f) / 4f))));
            });
        }
    }

    @SubscribeEvent
    public static void soulManagement(final LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            if (event.getEntityLiving() instanceof Mob target) {
                if (player.getMainHandItem().getItem() instanceof Scythe scythe) {
                    player.getCapability(CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY).ifPresent(skills ->
                            target.getCapability(CapabilityMobLevel.MOB_LEVEL_CAPABILITY).ifPresent(mobLevel -> {
                                DefaultPlayerSkills actualSkills = (DefaultPlayerSkills) skills;
                                DefaultMobLevel actualMobLevel = (DefaultMobLevel) mobLevel;
                                float chance = 1f;
                                chance *= actualMobLevel.mobLevel;
                                chance *= (1f + ((actualSkills.combatLvl - 1f) / 4f));
                                if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SOUL_BOOST.get(), player.getMainHandItem()) > 0) {
                                    chance *= (1f + (actualSkills.soulCount / 4f));
                                }
                                int rnd1 = player.level.random.nextInt(2000) + 1;
                                //if (chance >= rnd1){
                                OtherUtils.sendChat(player, ColorConstants.AQUA + "You found a Soul!");
                                player.playSound(SoundEvents.ZOMBIE_CONVERTED_TO_DROWNED, 1f, .8f);
                                if (!player.getInventory().add(new ItemStack(ItemInit.SOUL.get()))) {
                                    ItemEntity item = new ItemEntity(player.level, player.position().x, player.position().y, player.position().z, new ItemStack(ItemInit.SOUL.get()));
                                    player.level.addFreshEntity(item);
                                }
                                int rnd2 = player.level.random.nextInt(4) + 1;
                                if (rnd2 == 1) {
                                    actualSkills.soulCount += 1;
                                    OtherUtils.sendChat(player, ColorConstants.AQUA + "The soul merges into your blood...");
                                    player.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE, 1f, .8f);
                                }
                                //}
                            }));
                }
            }
        }

        if (event.getEntityLiving() instanceof Player target) {
            if (event.getSource().getEntity() instanceof Mob killer) {
                if (killer instanceof EnderMan | killer instanceof Shulker | killer instanceof Endermite) {
                    killer.hurt(DamageSource.GENERIC, killer.getHealth() / 2f);
                } else if (killer instanceof EnderDragon) {
                    killer.hurt(DamageSource.GENERIC, killer.getHealth() / 10f);
                } else if (killer instanceof Piglin | killer instanceof ZombifiedPiglin | killer instanceof WitherSkeleton | killer instanceof WitherBoss | killer instanceof Blaze | killer instanceof Ghast) {
                    killer.heal(killer.getHealth() / 5f);
                } else {
                    killer.kill();
                }
            } else if (event.getSource().getEntity() instanceof Player killer) {
                killer.getCapability(CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY).ifPresent(skills1 -> {
                    killer.getCapability(CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY).ifPresent(skills2 -> {
                        DefaultPlayerSkills actualSkills1 = (DefaultPlayerSkills) skills1;
                        DefaultPlayerSkills actualSkills2 = (DefaultPlayerSkills) skills2;
                        actualSkills1.soulCount += Math.round(actualSkills2.soulCount / 2f);
                    });
                });
            }
        }
    }

    @SubscribeEvent
    public static void playerHeal(final LivingHealEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            player.getCapability(CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY).ifPresent(skills -> {
                DefaultPlayerSkills actualSkills = (DefaultPlayerSkills) skills;
                event.setAmount((event.getAmount() / actualSkills.maxHealth) * 100);
            });
        }
    }

    @SubscribeEvent
    public static void infuseSouls(final AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof TieredItem left) {
            if (event.getRight().getItem() instanceof Soul right && event.getRight().getCount() == 32) {
                var output = new ItemStack(left);
                output.enchant(EnchantmentInit.SOUL_BOOST.get(), 1);
                event.setOutput(output);
                event.setCost(5);
            }
        }
    }
}
