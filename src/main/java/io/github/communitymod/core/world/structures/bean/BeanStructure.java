package io.github.communitymod.core.world.structures.bean;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.IglooPieces;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class BeanStructure extends StructureFeature<NoneFeatureConfiguration> {
    public BeanStructure(Codec<NoneFeatureConfiguration> p_66121_) {
        super(p_66121_);
    }

    public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return BeanStructure.Start::new;
    }

    public static class Start extends StructureStart<NoneFeatureConfiguration> {
        public Start(StructureFeature<NoneFeatureConfiguration> p_159888_, ChunkPos p_159889_, int p_159890_, long p_159891_) {
            super(p_159888_, p_159889_, p_159890_, p_159891_);
        }

        public void generatePieces(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, StructureManager structureManager, ChunkPos chunkPos, Biome biome, NoneFeatureConfiguration noneFeatureConfiguration, LevelHeightAccessor levelHeightAccessor) {
            int x = chunkPos.x;
            int z = chunkPos.z;
            int y = chunkGenerator.getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor);
            BlockPos blockPos = new BlockPos(x, y, z);
            Rotation rotation = Rotation.getRandom(this.random);
            IglooPieces.addPieces(structureManager, blockPos, rotation, this, this.random);
        }
    }
}