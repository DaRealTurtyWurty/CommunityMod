package io.github.communitymod.client.entity.goose;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.common.entities.GooseEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GooseRenderer<T extends GooseEntity> extends MobRenderer<T, GooseModel<T>> {

    private static final ResourceLocation ENTITY_TEXTURE = new ResourceLocation(CommunityMod.MODID,
            "textures/entities/goose.png");

    public static final ModelLayerLocation GOOSE_LAYER = new ModelLayerLocation(
            new ResourceLocation(CommunityMod.MODID, "goose"), "main");

    public GooseRenderer(final Context context) {
        super(context, new GooseModel<>(context.bakeLayer(GOOSE_LAYER)), 0.6f); // 0.6f is the shadow radius.
    }

    @Override
    public ResourceLocation getTextureLocation(final T entity) {
        return ENTITY_TEXTURE;
    }
}
