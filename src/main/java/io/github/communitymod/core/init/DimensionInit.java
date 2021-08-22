package io.github.communitymod.core.init;

import io.github.communitymod.core.util.ModResourceLocation;
import io.github.communitymod.core.world.dim.gen.DarkTowersChunkGenerator;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class DimensionInit {

    public static final ResourceKey<Level> DARK_TOWERS_DIMENSION_LEVEL = ResourceKey
            .create(Registry.DIMENSION_REGISTRY, new ModResourceLocation("dark_towers"));
    public static final ResourceKey<DimensionType> DARK_TOWERS_DIMENSION_TYPE = ResourceKey
            .create(Registry.DIMENSION_TYPE_REGISTRY, new ModResourceLocation("dark_towers"));

    public static void setup() {
        Registry.register(Registry.CHUNK_GENERATOR, new ModResourceLocation("dark_towers_chunk_generator"),
                DarkTowersChunkGenerator.CODEC);
    }
}
