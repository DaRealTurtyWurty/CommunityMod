package io.github.communitymod;

import io.github.communitymod.core.init.BlockEntityInit;
import io.github.communitymod.core.init.BlockInit;
import io.github.communitymod.core.init.EntityInit;
import io.github.communitymod.core.init.ItemInit;
import io.github.communitymod.core.init.SoundsInit;
import io.github.communitymod.network.PacketHandler;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CommunityMod.MODID)
public class CommunityMod {

	public static final String MODID = "communitymod";

	public static final CreativeModeTab TAB = new CreativeModeTab(MODID) {

		@Override
		public ItemStack makeIcon() {
			return ItemInit.BEANS.get().getDefaultInstance();
		}
	};

	public CommunityMod() {
		final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		ItemInit.ITEMS.register(bus);
		BlockInit.BLOCKS.register(bus);
		BlockInit.toBlacklist();
		BlockEntityInit.BLOCK_ENTITIES.register(bus);
		EntityInit.ENTITIES.register(bus);
		EntityInit.registerSpawnEggs();
		SoundsInit.SOUNDS.register(bus);
		SoundsInit.registerSounds();
        
        PacketHandler.init();
	}
}
