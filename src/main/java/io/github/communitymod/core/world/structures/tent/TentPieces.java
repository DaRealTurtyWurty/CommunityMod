package io.github.communitymod.core.world.structures.tent;

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

import java.util.ArrayList;
import java.util.Random;

public class TentPieces {

    static final ArrayList<ResourceLocation> STRUCTURE_LOCATIONS = new ArrayList<>();

    public static final StructurePieceType TENT_TYPE = TentPiece::new;


    static {
        for(int i = 0; i < 10; i++){
            STRUCTURE_LOCATIONS.add(new ResourceLocation(CommunityMod.MODID, "tent_structure_" + i));
        }
    }

    public static void addPieces(StructureManager structureManager, BlockPos blockPos, Rotation rotation, StructurePieceAccessor p_162438_, Random p_162439_) {
        ResourceLocation location = STRUCTURE_LOCATIONS.get(new Random().nextInt(STRUCTURE_LOCATIONS.size()));
        p_162438_.addPiece(new TentPiece(structureManager, location, blockPos, rotation, 0));
    }

    public static class TentPiece extends TemplateStructurePiece {

        public TentPiece(StructureManager structureManager, ResourceLocation location, BlockPos blockPos, Rotation rotation, int p_71248_) {
            super(TENT_TYPE, 0, structureManager, location, location.toString(), makeSettings(rotation), blockPos);
        }

        public TentPiece(ServerLevel serverLevel, CompoundTag compoundTag) {
            super(TENT_TYPE, compoundTag, serverLevel, (p_162451_) -> makeSettings(Rotation.valueOf(compoundTag.getString("Rot"))));
        }

        private static StructurePlaceSettings makeSettings(Rotation p_162447_) {
            return (new StructurePlaceSettings()).setRotation(p_162447_).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }


        protected void addAdditionalSaveData(ServerLevel p_162444_, CompoundTag p_162445_) {
            super.addAdditionalSaveData(p_162444_, p_162445_);
            p_162445_.putString("Rot", this.placeSettings.getRotation().name());
        }

        @Override
        protected void handleDataMarker(String s, BlockPos blockPos, ServerLevelAccessor serverLevelAccessor, Random random, BoundingBox boundingBox) {

        }

    }
}
