package io.github.communitymod.core.init;

import io.github.communitymod.CommunityMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.Item;
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

@EventBusSubscriber(modid = CommunityMod.MODID, bus = Bus.MOD)
public final class ItemInit {

	static final Set<RegistryObject<Block>> BLOCK_ITEM_BLACKLIST = new HashSet<>();

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
			CommunityMod.MODID);

	public static final RegistryObject<Item> BEANS = ITEMS.register("beans",
			() -> new Item(new Item.Properties().tab(CommunityMod.TAB).stacksTo(69).fireResistant()));

	public static final RegistryObject<Item> LONEWOLF_USER = ITEMS.register("lonewolf_user",
			() -> new Item(new Item.Properties().tab(CommunityMod.TAB)));
  
	public static final FoodProperties BEAN_SOUP_PROPERTIES = (new FoodProperties.Builder().nutrition(8).saturationMod(0.6F)
		.effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 7200, 0), 1.0F)
		.effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 100, 0), 0.5F)
		.alwaysEat().build()
	);

	public static final RegistryObject<Item> BEAN_SOUP = ITEMS.register("bean_soup",
			() -> new BowlFoodItem(new Item.Properties().tab(CommunityMod.TAB).food(BEAN_SOUP_PROPERTIES).stacksTo(1).fireResistant()));
  
  public static final RegistryObject<Item> BEANS_SANDWICH = ITEMS.register("beans_sandwich",
			() -> new Item(new Item.Properties().tab(CommunityMod.TAB).food(new FoodProperties.Builder().saturationMod(35.6F).meat().nutrition(20).build())));

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
