package io.github.communitymod.core;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.common.entities.BeanEntity;
import io.github.communitymod.common.entities.GooseEntity;
import io.github.communitymod.core.init.DimensionInit;
import io.github.communitymod.core.init.EntityInit;
import io.github.communitymod.core.init.StructureInit;
import io.github.communitymod.core.world.structures.ConfiguredStructures;
import io.github.communitymod.core.world.structures.bean.BeanPieces;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public final class CommonEvents {

	@EventBusSubscriber(modid = CommunityMod.MODID, bus = Bus.FORGE)
	public static final class ForgeEvents {

		@SuppressWarnings("resource")
		@SubscribeEvent
		public static void addDimensionalSpacing(final WorldEvent.Load event) {
			if (event.getWorld()instanceof final ServerLevel serverLevel) {
				final Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(
						serverLevel.getChunkSource().generator.getSettings().structureConfig());
				tempMap.putIfAbsent(StructureInit.BEAN_STRUCTURE.get(),
						StructureSettings.DEFAULTS.get(StructureInit.BEAN_STRUCTURE.get()));
				serverLevel.getChunkSource().generator.getSettings().structureConfig = tempMap;
			}
		}

		@SubscribeEvent
		public static void biomeModification(final BiomeLoadingEvent event) {
			event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURE_TEST_STRUCTURE);
		}
	}

	@EventBusSubscriber(modid = CommunityMod.MODID, bus = Bus.MOD)
	public static final class ModEvents {

		@SubscribeEvent
		public static void commonSetup(final FMLCommonSetupEvent event) {
			DimensionInit.setup();

			// Structures
			register(BeanPieces.BEAN_TYPE, "bean_structure");
			StructureInit.setupStructures();
			ConfiguredStructures.registerConfiguredStructures();
		}

		@SubscribeEvent
		public static void entityAttributes(final EntityAttributeCreationEvent event) {
			event.put(EntityInit.BEAN_ENTITY.get(), BeanEntity.createAttributes().build());
			event.put(EntityInit.GOOSE_ENTITY.get(), GooseEntity.createAttributes().build());
		}

		private static StructurePieceType register(StructurePieceType structurePiece, String key) {
			return Registry.register(Registry.STRUCTURE_PIECE, key.toLowerCase(Locale.ROOT), structurePiece);
		}
	}
}