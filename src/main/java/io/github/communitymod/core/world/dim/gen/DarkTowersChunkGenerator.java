package io.github.communitymod.core.world.dim.gen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.IntStream;

public class DarkTowersChunkGenerator extends ChunkGenerator {

    private static final Codec<DimensionSettings> SETTINGS_CODEC = RecordCodecBuilder
            .create(instance -> instance
                    .group(Codec.STRING.fieldOf("tower_block").forGetter(DimensionSettings::getTowerBlock),
                            Codec.INT.fieldOf("min_height").forGetter(DimensionSettings::getMinHeight),
                            Codec.INT.fieldOf("max_height").forGetter(DimensionSettings::getMaxHeight))
                    .apply(instance, DimensionSettings::new));

    public static final Codec<DarkTowersChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(BiomeSource.CODEC.fieldOf("biome_source")
                    .forGetter(surfaceChunkGenerator -> surfaceChunkGenerator.biomeSource),
                    SETTINGS_CODEC.fieldOf("settings")
                            .forGetter(DarkTowersChunkGenerator::getDimensionSettings))
            .apply(instance, DarkTowersChunkGenerator::new));

    protected final long seed;

    protected final DimensionSettings settings;
    protected final PerlinNoise surfaceNoise;
    protected final WorldgenRandom worldRandom;

    public DarkTowersChunkGenerator(final BiomeSource biomeSource, final DimensionSettings settings) {
        this(biomeSource, settings, new Random().nextLong());
    }

    public DarkTowersChunkGenerator(final BiomeSource biomeSource, final DimensionSettings settings,
            final long seed) {
        super(biomeSource, new StructureSettings(false));
        this.settings = settings;
        this.seed = seed;
        this.worldRandom = new WorldgenRandom(this.seed);
        this.surfaceNoise = new PerlinNoise(this.worldRandom, IntStream.rangeClosed(-3, 0));
        this.worldRandom.consumeCount(2620);
    }

    @Override
    public void buildSurfaceAndBedrock(final WorldGenRegion region, final ChunkAccess chunkAccess) {
        final var mutable = new BlockPos.MutableBlockPos();

        for (var x = 0; x < 16; ++x) {
            for (var z = 0; z < 16; ++z) {

                // Bedrock
                chunkAccess.setBlockState(mutable.set(x, 0, z), Blocks.BEDROCK.defaultBlockState(), false);

                // TODO: Config values for all of those
                final var baseHeight = 32;
                final var maxHeightDifference = 16;
                final var defaultGapWidth = 4;
                final var gap = new Random().nextInt(defaultGapWidth) + 1 / 2;
                final var height = new Random(System.currentTimeMillis()).nextInt(maxHeightDifference)
                        + baseHeight;
                final var block = ForgeRegistries.BLOCKS
                        .getValue(new ResourceLocation(this.settings.towerBlock));
                final var towerBlockState = (block != null ? block : Blocks.STONE).defaultBlockState();

                for (var y = 0; y < height; y++) {
                    if (x >= 16 - gap * 2 || x <= gap * 2 || z >= 16 - gap * 2 || z <= gap * 2) {
                        chunkAccess.setBlockState(new BlockPos(x, 1, z), towerBlockState, false);
                    } else {
                        chunkAccess.setBlockState(new BlockPos(x, y, z), towerBlockState, false);
                    }
                }
            }
        }
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(final Executor executor,
            final StructureFeatureManager structureFeatureManager, final ChunkAccess chunkAccess) {
        return CompletableFuture.supplyAsync(() -> chunkAccess, executor);
    }

    @Override
    public NoiseColumn getBaseColumn(final int chunkX, final int chunkZ,
            final LevelHeightAccessor levelHeightAccessor) {
        return new NoiseColumn(0, new BlockState[0]);
    }

    @Override
    public int getBaseHeight(final int x, final int z, final Heightmap.Types types,
            final LevelHeightAccessor levelHeightAccessor) {
        return this.settings.maxHeight / 2;
    }

    public DimensionSettings getDimensionSettings() {
        return this.settings;
    }

    @Override
    public int getSeaLevel() {
        return this.settings.maxHeight;
    }

    @Override
    public ChunkGenerator withSeed(final long seed) {
        return this;
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    public record DimensionSettings(String towerBlock, int minHeight, int maxHeight) {
        public String getTowerBlock() {
            return this.towerBlock;
        }

        public int getMinHeight() {
            return this.minHeight;
        }

        public int getMaxHeight() {
            return this.maxHeight;
        }
    }
}
