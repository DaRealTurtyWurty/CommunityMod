package io.github.communitymod.core;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.capabilities.entitylevel.CapabilityMobLevel;
import io.github.communitymod.capabilities.playerskills.CapabilityPlayerSkills;
import io.github.communitymod.client.util.ColorText;
import io.github.communitymod.common.entities.BeanEntity;
import io.github.communitymod.core.init.*;
import io.github.communitymod.core.util.ModDataGeneration;
import io.github.communitymod.core.util.OtherUtils;
import io.github.communitymod.core.world.structures.ConfiguredStructures;
import io.github.communitymod.core.world.structures.bean.BeanPieces;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public final class CommonEvents {

    @EventBusSubscriber(modid = CommunityMod.MODID, bus = Bus.FORGE)
    public static final class ForgeEvents {

        public static int skillShowTimer = 0;
        public static String skillShowText = "";
        private static int prevLevelCombat = 0;
        private static int prevLevelMining = 0;
        private static int prevLevelForaging = 0;
        private static int prevLevelFarming = 0;

        @SubscribeEvent
        public static void biomeModification(final BiomeLoadingEvent event) {
            event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURE_TEST_STRUCTURE);
        }

        @SubscribeEvent
        public static void addDimensionalSpacing(final WorldEvent.Load event) {
            if (event.getWorld() instanceof ServerLevel serverLevel) {
                Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(serverLevel.getChunkSource().generator.getSettings().structureConfig());
                tempMap.putIfAbsent(StructureInit.BEAN_STRUCTURE.get(), StructureSettings.DEFAULTS.get(StructureInit.BEAN_STRUCTURE.get()));
                serverLevel.getChunkSource().generator.getSettings().structureConfig = tempMap;
            }
        }

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
                    event.setAmount(actualAmount * (skills.getStrength() / 100f) * (1f + skills.getCombatLvl() * 0.04f) * 5f);

                    ArmorStand dmgTag = new ArmorStand(world, event.getEntity().position().x + (0.5f - Math.random()), event.getEntity().position().y + 0.5 + (0.5f - Math.random()), event.getEntity().position().z + (0.5f - Math.random()));
                    dmgTag.setCustomName(Component.nullToEmpty(ColorText.YELLOW.toString() + Math.round(event.getAmount())));
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
                    event.setAmount(actualAmount * (((((mobLevel.getMobLevel() / 2f) - 1f) * 50f) + 100f) / 100f) * (1f + (mobLevel.getMobLevel() / 4f) * 0.04f) * 5f);

                    ArmorStand dmgTag = new ArmorStand(world, event.getEntity().position().x + (0.5f - Math.random()), event.getEntity().position().y + 0.5 + (0.5f - Math.random()), event.getEntity().position().z + (0.5f - Math.random()));
                    dmgTag.setCustomName(Component.nullToEmpty(ColorText.YELLOW.toString() + Math.round(event.getAmount())));
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
                        dmgTag.setCustomName(Component.nullToEmpty(ColorText.YELLOW.toString() + (Math.round(event.getAmount()) * 5f)));
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
                        dmgTag.setCustomName(Component.nullToEmpty(ColorText.YELLOW.toString() + (Math.round(event.getAmount() * 5f))));
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
                    //@todo fix skills spamming chat (anyone test that this is still a thing?)
                    if (!(prevLevelCombat == skills.getCombatLvl())) {
                        OtherUtils.SendChat(player, ColorText.BOLD.toString() + ColorText.DARK_AQUA + "========================================");
                        OtherUtils.SendChat(player, ColorText.AQUA + "Combat " + ColorText.DARK_GRAY + prevLevelCombat + " -> " + skills.getCombatLvl());
                        OtherUtils.SendChat(player, ColorText.GOLD + "REWARDS");
                        OtherUtils.SendChat(player, ColorText.GOLD + "   - Deal " + ColorText.DARK_GRAY + "+" + (prevLevelCombat * 4) + "%" + ColorText.YELLOW + " -> " + ColorText.GOLD + "+" + (skills.getCombatLvl() * 4) + "% More damage to mobs.");
                        OtherUtils.SendChat(player, ColorText.BOLD.toString() + ColorText.DARK_AQUA + "========================================");
                    }
                    prevLevelCombat = skills.getCombatLvl();
                    if (!(prevLevelMining == skills.getMiningLvl())) {
                        OtherUtils.SendChat(player, ColorText.BOLD.toString() + ColorText.DARK_AQUA + "========================================");
                        OtherUtils.SendChat(player, ColorText.AQUA + "Mining " + ColorText.DARK_GRAY + prevLevelMining + " -> " + skills.getMiningLvl());
                        OtherUtils.SendChat(player, ColorText.GOLD + "REWARDS");
                        OtherUtils.SendChat(player, ColorText.GOLD + "   - Gain " + ColorText.DARK_GRAY + "+" + (prevLevelMining * 2) + ColorText.DARK_GREEN + " -> " + ColorText.GREEN + "+" + (skills.getMiningLvl() * 2) + " Defense.");
                        OtherUtils.SendChat(player, ColorText.BOLD.toString() + ColorText.DARK_AQUA + "========================================");
                    }
                    prevLevelMining = skills.getMiningLvl();
                    if (!(prevLevelFarming == skills.getFarmingLvl())) {
                        OtherUtils.SendChat(player, ColorText.BOLD.toString() + ColorText.DARK_AQUA + "========================================");
                        OtherUtils.SendChat(player, ColorText.AQUA + "Farming " + ColorText.DARK_GRAY + prevLevelFarming + " -> " + skills.getFarmingLvl());
                        OtherUtils.SendChat(player, ColorText.GOLD + "REWARDS");
                        OtherUtils.SendChat(player, ColorText.GOLD + "   - Gain " + ColorText.DARK_GRAY + "+" + (prevLevelFarming * 3) + ColorText.DARK_RED + " -> " + ColorText.RED + "+" + (skills.getFarmingLvl() * 3) + " Max Health.");
                        OtherUtils.SendChat(player, ColorText.BOLD.toString() + ColorText.DARK_AQUA + "========================================");
                    }
                    prevLevelFarming = skills.getFarmingLvl();
                    if (!(prevLevelForaging == skills.getForagingLvl())) {
                        OtherUtils.SendChat(player, ColorText.BOLD.toString() + ColorText.DARK_AQUA + "========================================");
                        OtherUtils.SendChat(player, ColorText.AQUA + "Foraging " + ColorText.DARK_GRAY + prevLevelForaging + " -> " + skills.getForagingLvl());
                        OtherUtils.SendChat(player, ColorText.GOLD + "REWARDS");
                        OtherUtils.SendChat(player, ColorText.GOLD + "   - Gain " + ColorText.DARK_GRAY + "+" + (prevLevelForaging * 2) + ColorText.DARK_RED + " \u8594 " + ColorText.RED + "+" + (skills.getForagingLvl() * 4) + " Strength.");
                        OtherUtils.SendChat(player, ColorText.BOLD.toString() + ColorText.DARK_AQUA + "========================================");
                    }
                    prevLevelForaging = skills.getForagingLvl();

                    int strengthAdder = 100;
                    int speedAdder = 100;
                    int maxHealthAdder = 100;
                    int defenseAdder = 0;

                    strengthAdder += player.experienceLevel / 5f;
                    speedAdder += player.experienceLevel / 10f;
                    maxHealthAdder += player.experienceLevel / 10f;
                    defenseAdder += player.experienceLevel / 50f;

                    int beanCount = player.getInventory().countItem(ItemInit.BEANS.get()) + player.getInventory().countItem(new BlockItem(BlockInit.BEAN_BLOCK.get(), new Item.Properties().tab(CommunityMod.TAB))) * 9;
                    if (player.getInventory().countItem(ItemInit.BEAN_ARTIFACT.get()) > 0) {
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
                    } else if (player.getInventory().countItem(ItemInit.BEAN_RING.get()) > 0) {
                        speedAdder += ((float) Math.floor(beanCount / 96d));
                        strengthAdder += ((int) Math.round(Math.floor(beanCount / 96d)));
                        int rnd = level.random.nextInt(2000) + 1;
                        if (rnd == 50) {
                            ItemEntity beanItem = new ItemEntity(level, player.position().x, player.position().y, player.position().z, new ItemStack(ItemInit.BEANS.get()));
                            level.addFreshEntity(beanItem);
                        }
                    } else if (player.getInventory().countItem(ItemInit.BEAN_TALISMAN.get()) > 0) {
                        speedAdder += ((float) Math.floor(beanCount / 128d));
                    }

                    ColorText color;
                    if (player.isFullyFrozen()) {
                        color = ColorText.AQUA;
                    } else if (player.hasEffect(MobEffects.WITHER)) {
                        color = ColorText.DARK_GRAY;
                    } else if (player.hasEffect(MobEffects.ABSORPTION)) {
                        color = ColorText.GOLD;
                    } else {
                        color = ColorText.RED;
                    }

                    player.displayClientMessage(Component.nullToEmpty(
                            color.toString() + skills.getHealth() + "/" + skills.getMaxHealth() + " Health" + "  " +
                                    ((skills.getDefense() != 0) ? (ColorText.GREEN.toString() + (skills.getDefense() * 5) + " Defense" + "  ") : ("")) +
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

                    skills.setDefense(((headDefense + chestDefense + legsDefense + feetDefense) * 5) + (skills.getMiningXp() * 2) + defenseAdder);
                    skills.setStrength(strengthAdder + (skills.getForagingLvl() * 2));
                    skills.setMaxHealth(maxHealthAdder + (skills.getFarmingLvl() * 3));
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
                    entity.setCustomName(Component.nullToEmpty(ColorText.GRAY + "[Lv." + ColorText.RED + (mobLevel.getMobLevel()) + ColorText.GRAY + "] " +
                            entity.getType().getDescription().getString() +
                            " " + ColorText.RED + Math.round((entity.getHealth() + entity.getAbsorptionAmount()) * 5 * (mobLevel.getMobLevel() / 2f)) + "/" + Math.round(entity.getMaxHealth() * 5 * (mobLevel.getMobLevel() / 2f))));
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

        @SubscribeEvent
        public static void registerCommands(final RegisterCommandsEvent event) {
            CommandInit.registerCommands(event);
        }
    }

    @EventBusSubscriber(modid = CommunityMod.MODID, bus = Bus.MOD)
    public static final class ModEvents {

        @SubscribeEvent
        public static void commonSetup(final FMLCommonSetupEvent event) {
            DimensionInit.setup();

            //Structures
            register(BeanPieces.BEAN_TYPE, "bean_structure");
            StructureInit.setupStructures();
            ConfiguredStructures.registerConfiguredStructures();

            //Capabilities
            CapabilityPlayerSkills.register();
            CapabilityMobLevel.register();
        }

        @SubscribeEvent
        public static void gatherData(final GatherDataEvent event) {
            DataGenerator generator = event.getGenerator();
            ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

            if (event.includeServer()) {
                //TODO: recipes, loot tables, tags
            }
            if (event.includeClient()) {
                generator.addProvider(new ModDataGeneration.LanguageGen(generator, "en_us"));
            }
        }

        @SubscribeEvent
        public static void entityAttributes(final EntityAttributeCreationEvent event) {
            event.put(EntityInit.BEAN_ENTITY.get(), BeanEntity.createAttributes().build());
        }

        private static StructurePieceType register(StructurePieceType structurePiece, String key) {
            return Registry.register(Registry.STRUCTURE_PIECE, key.toLowerCase(Locale.ROOT), structurePiece);
        }
    }
}
