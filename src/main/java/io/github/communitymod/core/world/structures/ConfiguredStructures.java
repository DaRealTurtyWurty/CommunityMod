package io.github.communitymod.core.world.structures;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.core.init.StructureInit;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;

public class ConfiguredStructures {

    public static final ConfiguredStructureFeature<?, ?> CONFIGURE_TENT_STRUCTURE = StructureInit.TENT_STRUCTURE.get().configured(FeatureConfiguration.NONE);


    public static void registerConfiguredStructures() {
        final var registry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;

        Registry.register(registry, new ResourceLocation(CommunityMod.MODID, "configured_tent"), CONFIGURE_TENT_STRUCTURE);

        /*
         * Ok so, this part may be hard to grasp but basically, just add your structure
         * to this to prevent any sort of crash or issue with other mod's custom
         * ChunkGenerators. If they use FlatGenerationSettings.STRUCTURE_FEATURES in it
         * and you don't add your structure to it, the game could crash later when you
         * attempt to add the StructureSeparationSettings to the dimension.
         *
         * (It would also crash with superflat worldtype if you omit the below line and
         * attempt to add the structure's StructureSeparationSettings to the world)
         *
         * Note: If you want your structure to spawn in superflat, remove the
         * FlatChunkGenerator check in StructureTutorialMain.addDimensionalSpacing and
         * then create a superflat world, exit it, and re-enter it and your structures
         * will be spawning. I could not figure out why it needs the restart but
         * honestly, superflat is really buggy and shouldn't be your main focus in my
         * opinion.
         *
         * Requires AccessTransformer ( see resources/META-INF/accesstransformer.cfg )
         */
        FlatLevelGeneratorSettings.STRUCTURE_FEATURES.put(StructureInit.TENT_STRUCTURE.get(), CONFIGURE_TENT_STRUCTURE);
    }
}
