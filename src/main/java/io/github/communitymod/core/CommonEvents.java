package io.github.communitymod.core;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.common.entities.BeanEntity;
import io.github.communitymod.core.init.DimensionInit;
import io.github.communitymod.core.init.EntityInit;
import io.github.communitymod.core.util.ModDataGeneration;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public final class CommonEvents {

	@EventBusSubscriber(modid = CommunityMod.MODID, bus = Bus.FORGE)
	public static final class ForgeEvents {
	}

	@EventBusSubscriber(modid = CommunityMod.MODID, bus = Bus.MOD)
	public static final class ModEvents {

		@SubscribeEvent
		public static void commonSetup(final FMLCommonSetupEvent event) {
			DimensionInit.setup();
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
				/* TODO: change textures path to /item and /block etc. else it won't work!
				generator.addProvider(
						new ModDataGeneration.ItemModelGen(generator, existingFileHelper));
				generator.addProvider(
						new ModDataGeneration.BlockStateGen(generator, existingFileHelper));
				 */
			}
		}

		@SubscribeEvent
		public static void entityAttributes(final EntityAttributeCreationEvent event) {
			event.put(EntityInit.BEAN_ENTITY.get(), BeanEntity.createAttributes().build());
		}
	}
}
