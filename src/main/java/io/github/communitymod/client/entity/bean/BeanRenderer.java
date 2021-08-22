package io.github.communitymod.client.entity.bean;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.common.entities.BeanEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BeanRenderer<T extends BeanEntity> extends MobRenderer<T, BeanModel<T>> {

    public static final ModelLayerLocation BEAN_LAYER = new ModelLayerLocation(
            new ResourceLocation(CommunityMod.MODID, "bean"), "main");

    private static final ResourceLocation ENTITY_TEXTURE = new ResourceLocation(CommunityMod.MODID,
            "textures/entities/bean.png");

    public BeanRenderer(final Context context) {
        super(context, new BeanModel<>(context.bakeLayer(BEAN_LAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(final T entity) {
        return ENTITY_TEXTURE;
    }
}
