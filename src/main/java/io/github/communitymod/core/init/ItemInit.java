package io.github.communitymod.core.init;

import io.github.communitymod.CommunityMod;
import net.minecraft.world.item.BlockItem;
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
