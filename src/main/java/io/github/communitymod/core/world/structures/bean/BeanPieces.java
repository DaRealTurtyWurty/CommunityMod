package io.github.communitymod.core.world.structures.bean;

import io.github.communitymod.CommunityMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.Random;

public class BeanPieces {
    public static final StructurePieceType BEAN_TYPE = BeanPiece::new;

    static final ResourceLocation STRUCTURE_LOCATION = new ResourceLocation(
            CommunityMod.MODID + ":bean_structure");

    public static void addPieces(final StructureManager structureManager, final BlockPos blockPos,
            final Rotation rotation, final StructurePieceAccessor accessor) {
        accessor.addPiece(new BeanPiece(structureManager, STRUCTURE_LOCATION, blockPos, rotation, 0));
    }

    public static class BeanPiece extends TemplateStructurePiece {

        public BeanPiece(final ServerLevel serverLevel, final CompoundTag compoundTag) {
            super(BEAN_TYPE, compoundTag, serverLevel,
                    p_162451_ -> makeSettings(Rotation.valueOf(compoundTag.getString("Rot"))));
        }

        public BeanPiece(final StructureManager structureManager, final ResourceLocation location,
                final BlockPos blockPos, final Rotation rotation, final int index) {
            super(BEAN_TYPE, index, structureManager, location, location.toString(), makeSettings(rotation),
                    blockPos);
        }

        private static StructurePlaceSettings makeSettings(final Rotation rot) {
            return new StructurePlaceSettings().setRotation(rot).setMirror(Mirror.NONE)
                    .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }

        @Override
        protected void addAdditionalSaveData(final ServerLevel level, final CompoundTag nbt) {
            super.addAdditionalSaveData(level, nbt);
            nbt.putString("Rot", this.placeSettings.getRotation().name());
        }

        @Override
        protected void handleDataMarker(final String name, final BlockPos blockPos,
                final ServerLevelAccessor serverLevelAccessor, final Random random,
                final BoundingBox boundingBox) {
        }
    }
}
