package io.github.communitymod.core.init;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.common.items.BeanBelt;
import io.github.communitymod.common.items.Dice;
import io.github.communitymod.common.items.MiguelItem;
import io.github.communitymod.common.items.OrbOfInsanity;
import io.github.communitymod.common.items.Scythe;
import io.github.communitymod.common.items.Soul;
import io.github.communitymod.common.items.SpecialItem;
import io.github.communitymod.common.items.SpoonTemplate;
import io.github.communitymod.common.items.WhatSign;
import io.github.communitymod.common.items.bean_accessories.BeanArtifact;
import io.github.communitymod.common.items.bean_accessories.BeanRing;
import io.github.communitymod.common.items.bean_accessories.BeanTalisman;
import io.github.communitymod.core.util.BeanArmorMaterial;
import io.github.communitymod.core.util.ModToolMaterials;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

@EventBusSubscriber(modid = CommunityMod.MODID, bus = Bus.MOD)
public final class ItemInit {

    static final Set<RegistryObject<Block>> BLOCK_ITEM_BLACKLIST = new HashSet<>();

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            CommunityMod.MODID);

    public static final FoodProperties BEAN_SOUP_PROPERTIES = new FoodProperties.Builder().nutrition(8)
            .saturationMod(0.6F)
            .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 7200, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 100, 0), 0.5F).alwaysEat().build();

    public static final RegistryObject<Item> APPLE_INGOT = ITEMS.register("apple_ingot",
            () -> new Item(new Item.Properties()
                    .tab(CommunityMod.TAB)
                    .food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationMod(10f)
                            .build())));

    public static final RegistryObject<Item> BEAN_SOUP = ITEMS.register("bean_soup",
            () -> new BowlFoodItem(new Item.Properties().tab(CommunityMod.TAB).food(BEAN_SOUP_PROPERTIES)
                    .stacksTo(1).fireResistant()));

    public static final RegistryObject<Item> BEAN_SWORD = ITEMS.register("bean_sword",
            () -> new SwordItem(ModToolMaterials.BEAN, 8, -2.4f,
                    new Item.Properties().defaultDurability(100).tab(CommunityMod.TAB)));

    public static final RegistryObject<Item> BEANS = ITEMS.register("beans",
            () -> new Item(new Item.Properties().tab(CommunityMod.TAB).stacksTo(69).fireResistant()));

    public static final RegistryObject<Item> BEANS_SANDWICH = ITEMS.register("beans_sandwich",
            () -> new Item(new Item.Properties().tab(CommunityMod.TAB)
                    .food(new FoodProperties.Builder().saturationMod(35.6F).meat().nutrition(20).build())));

    public static final RegistryObject<Item> CHEESE_ITEM = ITEMS.register("cheese_item",
            () -> new Item(new Item.Properties().tab(CommunityMod.TAB)
                    .food(new FoodProperties.Builder().nutrition(10).saturationMod(1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.LEVITATION, 60, 1), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 7200, 3), 0.5f)
                            .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 7200, 10), 0.25f)
                            .build())));
    
    //Burger stuff
    public static final RegistryObject<Item> CHEESEBURGER = ITEMS.register("cheeseburger",
    		() -> new Item(new Item.Properties().tab(CommunityMod.TAB)
    				.food(new FoodProperties.Builder().nutrition(100).saturationMod(100.0f)
    						.effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 120, 2), 1.0f)
    						.effect(() -> new MobEffectInstance(MobEffects.POISON, 40, 2), 1.0f)
    						.build())));
    public static final RegistryObject<Item> BURGER_TOPBUN = ITEMS.register("burger_topbun", () -> new Item(new Item.Properties().tab(CommunityMod.TAB)));
    public static final RegistryObject<Item> BURGER_BOTTOMBUN = ITEMS.register("burger_bottombun", () -> new Item(new Item.Properties().tab(CommunityMod.TAB)));
    public static final RegistryObject<Item> BURGER_CHEESE = ITEMS.register("burger_cheese", () -> new Item(new Item.Properties().tab(CommunityMod.TAB)));
    public static final RegistryObject<Item> BURGER_LETTUCE = ITEMS.register("burger_lettuce", () -> new Item(new Item.Properties().tab(CommunityMod.TAB)));
    public static final RegistryObject<Item> BURGER_TOMATO = ITEMS.register("burger_tomato", () -> new Item(new Item.Properties().tab(CommunityMod.TAB)));
    public static final RegistryObject<Item> BURGER_MEAT = ITEMS.register("burger_meat", () -> new Item(new Item.Properties().tab(CommunityMod.TAB)));
    public static final RegistryObject<Item> BURGER_INGREDIENTS = ITEMS.register("burger_ingredients", () -> new Item(new Item.Properties().tab(CommunityMod.TAB)));
    //End burger stuff

    public static final RegistryObject<Item> MIGUEL_OF_FORTUNE = ITEMS.register("miguel_of_fortune",
            () -> new MiguelItem(new Item.Properties().tab(CommunityMod.TAB)));

    public static final RegistryObject<Item> ORB_OF_INSANITY = ITEMS.register("orb_of_insanity",
            () -> new OrbOfInsanity(
                    new Item.Properties().tab(CommunityMod.TAB).stacksTo(1).rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> BEAN_HAT = ITEMS.register("bean_hat",
            () -> new ArmorItem(BeanArmorMaterial.BEAN_ARMOR, EquipmentSlot.HEAD,
                    new Item.Properties().tab(CommunityMod.TAB)));

    public static final RegistryObject<Item> BEAN_BELT = ITEMS.register("bean_belt",
            () -> new BeanBelt(new Item.Properties().tab(CommunityMod.TAB)));

    public static final RegistryObject<Item> MUSIC_DISC_BEANAL = ITEMS.register("music_disc_beanal",
            () -> new RecordItem(5, SoundsInit.MUSIC_DISC_BEANAL,
                    new Item.Properties().tab(CommunityMod.TAB).stacksTo(1).rarity(Rarity.RARE)));

    public static final RegistryObject<Item> MUSIC_DISC_CHEESE = ITEMS.register("music_disc_cheese",
            () -> new RecordItem(15, SoundsInit.MUSIC_DISC_CHEESE,
                    new Item.Properties().tab(CommunityMod.TAB).stacksTo(1).rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> MUSIC_DISC_SOVIET = ITEMS.register("music_disc_soviet",
            () -> new RecordItem(15, SoundsInit.MUSIC_DISC_SOVIET,
                    new Item.Properties().tab(CommunityMod.TAB).stacksTo(1)));

    public static final RegistryObject<Item> MUSIC_DISC_DICE = ITEMS.register("music_disc_dice",
            () -> new RecordItem(15, SoundsInit.MUSIC_DISC_DICE,
                    new Item.Properties().tab(CommunityMod.TAB).stacksTo(1)));

    public static final RegistryObject<Item> SPECIAL_ITEM = ITEMS.register("special_item",
            () -> new SpecialItem(new Item.Properties().tab(CommunityMod.TAB)));

    public static final RegistryObject<Item> BASIC_DIE = ITEMS.register("basic_die",
            () -> new Dice(new Item.Properties().tab(CommunityMod.TAB)));

    public static final RegistryObject<Item> TOAST = ITEMS.register("toast",
            () -> new Item(new Item.Properties().tab(CommunityMod.TAB)
                    .food(new FoodProperties.Builder().saturationMod(15).meat().nutrition(30).build())));

    public static final RegistryObject<Item> WHAT = ITEMS.register("what",
            () -> new WhatSign(new Item.Properties().tab(CommunityMod.TAB)));

    public static final RegistryObject<Item> ETERNIUM_CRYSTAL = ITEMS.register("eternium_crystal",
            () -> new Item(new Item.Properties().tab(CommunityMod.TAB).stacksTo(420).fireResistant()));

    public static final RegistryObject<Item> REALZ_INGOT = ITEMS.register("realz_ingot",
            () -> new Item(new Item.Properties().tab(CommunityMod.TAB).stacksTo(69420).fireResistant()));

    public static final RegistryObject<Item> REALZ_APPLE = ITEMS.register("realz_apple",
            () -> new Item(new Item.Properties().tab(CommunityMod.TAB)
                    .food(new FoodProperties.Builder().nutrition(1000).saturationMod(1000.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 7200, 3), 0.5f)
                            .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 7200, 10), 0.25f)
                            .build())));

    //Spoons
    public static final RegistryObject<Item> WOODEN_SPOON = ITEMS.register("wooden_spoon",
        () -> new SpoonTemplate(
            Tiers.WOOD, 5.0f, -2.0f,
            new Item.Properties().tab(CommunityMod.TAB).durability(256).defaultDurability(256)));
    public static final RegistryObject<Item> STONE_SPOON = ITEMS.register("stone_spoon",
        () -> new SpoonTemplate(
            Tiers.STONE, 5.0f, -2.0f,
            new Item.Properties().tab(CommunityMod.TAB).durability(512).defaultDurability(512)));
    public static final RegistryObject<Item> GOLDEN_SPOON = ITEMS.register("golden_spoon",
        () -> new SpoonTemplate(
            Tiers.GOLD, 5.0f, -2.0f,
            new Item.Properties().tab(CommunityMod.TAB).durability(128).defaultDurability(128)));
    public static final RegistryObject<Item> IRON_SPOON = ITEMS.register("iron_spoon",
        () -> new SpoonTemplate(
            Tiers.IRON, 5.0f, -2.0f,
                new Item.Properties().tab(CommunityMod.TAB).durability(1024).defaultDurability(1024)));
    public static final RegistryObject<Item> DIAMOND_SPOON = ITEMS.register("diamond_spoon",
            () -> new SpoonTemplate(
                    Tiers.DIAMOND, 5.0f, -2.0f,
                    new Item.Properties().tab(CommunityMod.TAB).durability(2048).defaultDurability(2048)));
    public static final RegistryObject<Item> NETHERITE_SPOON = ITEMS.register("netherite_spoon",
            () -> new SpoonTemplate(
                    Tiers.NETHERITE, 5.0f, -2.0f,
                    new Item.Properties().tab(CommunityMod.TAB).durability(4096).defaultDurability(4096)));

    public static final RegistryObject<Item> BEAN_TALISMAN = ITEMS.register("bean_talisman",
            () -> new BeanTalisman(new Item.Properties().tab(CommunityMod.TAB).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> BEAN_RING = ITEMS.register("bean_ring",
            () -> new BeanRing(new Item.Properties().tab(CommunityMod.TAB).stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> BEAN_ARTIFACT = ITEMS.register("bean_artifact",
            () -> new BeanArtifact(new Item.Properties().tab(CommunityMod.TAB).stacksTo(1).rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> SOUL = ITEMS.register("soul",
            () -> new Soul(new Item.Properties().tab(CommunityMod.TAB).stacksTo(32).rarity(Rarity.RARE)));

    public static final RegistryObject<Item> SCYTHE = ITEMS.register("scythe",
            () -> new Scythe(ModToolMaterials.SCYTHE_SOURCE, 10, -2f, new Item.Properties().tab(CommunityMod.TAB).rarity(Rarity.RARE)));

    @SubscribeEvent
    public static void registerBlockItems(final RegistryEvent.Register<Item> event) {
        final var registry = event.getRegistry();
        BlockInit.BLOCKS.getEntries().stream().filter(object -> !BLOCK_ITEM_BLACKLIST.contains(object))
                .forEach(block -> {
                    final var blockItem = new BlockItem(block.get(),
                            new Item.Properties().tab(CommunityMod.TAB));
                    blockItem.setRegistryName(block.getId());
                    registry.register(blockItem);
                });
    }
}
