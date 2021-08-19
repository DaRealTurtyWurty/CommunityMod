package io.github.communitymod.core.init;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.common.armor.BeanArmorMaterial;
import io.github.communitymod.common.items.*;
import io.github.communitymod.core.util.enums.ModToolMaterials;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@EventBusSubscriber(modid = CommunityMod.MODID, bus = Bus.MOD)
public final class ItemInit {

	private static final Supplier<Item.Properties> DEFAULT = () -> new Item.Properties().tab(CommunityMod.TAB);

	static final Set<RegistryObject<Block>> BLOCK_ITEM_BLACKLIST = new HashSet<>();

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
			CommunityMod.MODID);
  
	public static final FoodProperties BEAN_SOUP_PROPERTIES = (new FoodProperties.Builder().nutrition(8).saturationMod(0.6F)
		.effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 7200, 0), 1.0F)
		.effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 100, 0), 0.5F)
		.alwaysEat().build()
	);
  
  public static final RegistryObject<Item> BEANS = ITEMS.register("beans",
			() -> new Item(new Item.Properties().tab(CommunityMod.TAB).stacksTo(69).fireResistant()));

	public static final RegistryObject<Item> BEAN_SOUP = ITEMS.register("bean_soup",
			() -> new BowlFoodItem(new Item.Properties().tab(CommunityMod.TAB).food(BEAN_SOUP_PROPERTIES).stacksTo(1).fireResistant()));
  
  public static final RegistryObject<Item> BEANS_SANDWICH = ITEMS.register("beans_sandwich",
			() -> new Item(new Item.Properties().tab(CommunityMod.TAB).food(new FoodProperties.Builder().saturationMod(35.6F).meat().nutrition(20).build())));

  public static final RegistryObject<Item> TOAST = ITEMS.register("toast",
			() -> new Item(new Item.Properties().tab(CommunityMod.TAB).food(new FoodProperties.Builder().saturationMod(15).meat().nutrition(30).build())));
	
	public static final RegistryObject<Item> MIGUEL_OF_FORTUNE = ITEMS.register("miguel_of_fortune", 
			() -> new MiguelItem(new Item.Properties().tab(CommunityMod.TAB)));
	
	public static final RegistryObject<Item> CHEESE_ITEM = ITEMS.register("cheese_item",
			() -> new Item(new Item.Properties()
					.tab(CommunityMod.TAB)
					.food(new FoodProperties.Builder()
					.nutrition(1000)
					.saturationMod(1000.0f)
					.effect(() -> new MobEffectInstance(MobEffects.LEVITATION, 60, 1), 1.0f)
					.effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 7200, 3), 0.5f)
					.effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 7200, 10), 0.25f)
					.build())));

	public static final RegistryObject<Item> SPECIAL_ITEM = ITEMS.register("special_item",
			() -> new SpecialItem(new Item.Properties().tab(CommunityMod.TAB)));

	public static final RegistryObject<Item> ORB_OF_INSANITY = ITEMS.register("orb_of_insanity",
			() -> new OrbOfInsanity(new Item.Properties().tab(CommunityMod.TAB).stacksTo(1).rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> BEAN_HAT = ITEMS.register("bean_hat", () ->
            new ArmorItem(
                BeanArmorMaterial.BEAN_ARMOR, 
                EquipmentSlot.HEAD,
                new Item.Properties().tab(CommunityMod.TAB)
            )
        );

    public static final RegistryObject<Item> BEAN_BELT = ITEMS.register("bean_belt", () ->
        new BeanBelt(
            new Item.Properties().tab(CommunityMod.TAB)
        )
    );
  
	public static final RegistryObject<Item> MUSIC_DISC_BEANAL = ITEMS.register("music_disc_beanal",
			() -> new RecordItem(5, ()-> SoundsInit.MUSIC_DISC_BEANAL.get(), new Item.Properties().tab(CommunityMod.TAB).stacksTo(1).rarity(Rarity.RARE)));
	
	public static final RegistryObject<Item> BEAN_SWORD = ITEMS.register("bean_sword",
			() -> new SwordItem(ModToolMaterials.BEAN, 8, -2.4f, new Item.Properties().defaultDurability(100).tab(CommunityMod.TAB)));

	public static final RegistryObject<QuiverItem> QUIVER = ITEMS.register("quiver",
			() -> new QuiverItem(DEFAULT.get()));

	@SubscribeEvent
	public static void registerBlockItems(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();
		BlockInit.BLOCKS.getEntries().stream().filter(object -> !BLOCK_ITEM_BLACKLIST.contains(object))
				.forEach(block -> {
					final var blockItem = new BlockItem(block.get(), new Item.Properties().tab(CommunityMod.TAB));
					blockItem.setRegistryName(block.getId());
					registry.register(blockItem);
				});
	}
}
