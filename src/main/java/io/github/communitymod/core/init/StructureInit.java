package io.github.communitymod.core.init;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.core.world.structures.bean.BeanStructure;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class StructureInit {

	public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister
			.create(ForgeRegistries.STRUCTURE_FEATURES, CommunityMod.MODID);

	public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> BEAN_STRUCTURE = STRUCTURES
			.register("bean_structure", () -> new BeanStructure(NoneFeatureConfiguration.CODEC));

	public static <F extends StructureFeature<?>> void setupMapSpacingAndLand(F structure,
			StructureFeatureConfiguration structureSeparationSettings, boolean transformSurroundingLand) {
		/*
		 * We need to add our structures into the map in Structure class alongside
		 * vanilla structures or else it will cause errors.
		 *
		 * If the registration is setup properly for the structure, getRegistryName()
		 * should never return null.
		 */
		StructureFeature.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

		/*
		 * Whether surrounding land will be modified automatically to conform to the
		 * bottom of the structure. Basically, it adds land at the base of the structure
		 * like it does for Villages and Outposts. Doesn't work well on structure that
		 * have pieces stacked vertically or change in heights.
		 *
		 * Note: The air space this method will create will be filled with water if the
		 * structure is below sealevel. This means this is best for structure above
		 * sealevel so keep that in mind.
		 *
		 * NOISE_AFFECTING_FEATURES requires AccessTransformer (See
		 * resources/META-INF/accesstransformer.cfg)
		 */
		if (transformSurroundingLand) {
			StructureFeature.NOISE_AFFECTING_FEATURES = ImmutableList.<StructureFeature<?>>builder()
					.addAll(StructureFeature.NOISE_AFFECTING_FEATURES).add(structure).build();
		}

		/*
		 * This is the map that holds the default spacing of all structures. Always add
		 * your structure to here so that other mods can utilize it if needed.
		 *
		 * However, while it does propagate the spacing to some correct dimensions from
		 * this map, it seems it doesn't always work for code made dimensions as they
		 * read from this list beforehand.
		 *
		 * Instead, we will use the WorldEvent.Load event in StructureTutorialMain to
		 * add the structure spacing from this list into that dimension or to do
		 * dimension blacklisting properly. We also use our entry in
		 * DimensionStructuresSettings.DEFAULTS in WorldEvent.Load as well.
		 *
		 * DEFAULTS requires AccessTransformer (See
		 * resources/META-INF/accesstransformer.cfg)
		 */
		StructureSettings.DEFAULTS = ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder()
				.putAll(StructureSettings.DEFAULTS).put(structure, structureSeparationSettings).build();

		/*
		 * There are very few mods that relies on seeing your structure in the noise
		 * settings registry before the world is made.
		 *
		 * You may see some mods add their spacings to
		 * DimensionSettings.BUILTIN_OVERWORLD instead of the NOISE_GENERATOR_SETTINGS
		 * loop below but that field only applies for the default overworld and won't
		 * add to other worldtypes or dimensions (like amplified or Nether). So yeah,
		 * don't do DimensionSettings.BUILTIN_OVERWORLD. Use the
		 * NOISE_GENERATOR_SETTINGS loop below instead if you must.
		 */
		BuiltinRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
			final var structureMap = settings.getValue().structureSettings().structureConfig();

			/*
			 * Pre-caution in case a mod makes the structure map immutable like datapacks
			 * do. I take no chances myself. You never know what another mods does...
			 *
			 * structureConfig requires AccessTransformer (See
			 * resources/META-INF/accesstransformer.cfg)
			 */
			if (structureMap instanceof ImmutableMap) {
				final Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(structureMap);
				tempMap.put(structure, structureSeparationSettings);
				settings.getValue().structureSettings().structureConfig = tempMap;
			} else {
				structureMap.put(structure, structureSeparationSettings);
			}
		});
	}

	public static void setupStructures() {
		setupMapSpacingAndLand(BEAN_STRUCTURE.get(), /* The instance of the structure */
				new StructureFeatureConfiguration(15, /* average distance apart in chunks between spawn attempts */
						10, /*
							 * minimum distance apart in chunks between spawn attempts. MUST BE LESS THAN
							 * ABOVE VALUE
							 */
						694201337), /*
									 * this modifies the seed of the structure so no two structures always spawn
									 * over each-other. Make this large and unique.
									 */
				true);

		// Add more structures here and so on
	}

}
