package io.github.communitymod.core.world.structures.tent;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class TentStructure extends StructureFeature<NoneFeatureConfiguration> {
    public TentStructure(Codec<NoneFeatureConfiguration> p_66121_) {
        super(p_66121_);
    }

    public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return TentStructure.Start::new;
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    public StructureStart<?> generate(RegistryAccess registryAccess, ChunkGenerator p_160466_, BiomeSource biomeSource, StructureManager p_160468_, long p_160469_, ChunkPos p_160470_, Biome biome, int p_160472_, WorldgenRandom p_160473_, StructureFeatureConfiguration p_160474_, NoneFeatureConfiguration p_160475_, LevelHeightAccessor p_160476_) {

        //var biomeRegistry = registryAccess.ownedRegistryOrThrow(Registry.BIOME_REGISTRY);
        //var key = ResourceKey.create(Registry.BIOME_REGISTRY, biomeRegistry.getKey(biome));
        if (biome.getBiomeCategory() == Biome.BiomeCategory.OCEAN) {
            return StructureStart.INVALID_START;
        }
        ;

        return super.generate(registryAccess, p_160466_, biomeSource, p_160468_, p_160469_, p_160470_, biome, p_160472_, p_160473_, p_160474_, p_160475_, p_160476_);
    }

    @Override
    protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long p_160457_, WorldgenRandom p_160458_, ChunkPos chunkPos, Biome biome, ChunkPos p_160461_, NoneFeatureConfiguration noneFeatureConfiguration, LevelHeightAccessor levelHeightAccessor) {

        BlockPos centerOfChunk = new BlockPos(chunkPos.x * 16, 0, chunkPos.z * 16);
        int landHeight = chunkGenerator.getFirstOccupiedHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor);
        NoiseColumn column = chunkGenerator.getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ(), levelHeightAccessor);
        BlockState topBlock = column.getBlockState(centerOfChunk.above(landHeight));
        return topBlock.getFluidState().isEmpty();
    }

    public static class Start extends StructureStart<NoneFeatureConfiguration> {

        public Start(StructureFeature<NoneFeatureConfiguration> p_159888_, ChunkPos p_159889_, int p_159890_, long p_159891_) {
            super(p_159888_, p_159889_, p_159890_, p_159891_);
        }

        public void generatePieces(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, StructureManager structureManager, ChunkPos chunkPos, Biome biome, NoneFeatureConfiguration noneFeatureConfiguration, LevelHeightAccessor levelHeightAccessor) {
            int x = chunkPos.getMinBlockX();
            int z = chunkPos.getMinBlockZ();
            int y = chunkGenerator.getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor);
            BlockPos blockPos = new BlockPos(x, y, z);
            Rotation rotation = Rotation.getRandom(this.random);
            TentPieces.addPieces(structureManager, blockPos, rotation, this, this.random);
        }
    }
}
