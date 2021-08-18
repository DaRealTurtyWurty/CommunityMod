package io.github.communitymod.client.entity.stick;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.communitymod.common.entities.ThrownStickEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import javax.annotation.ParametersAreNonnullByDefault;

public class ThrownStickEntityRenderer extends EntityRenderer<ThrownStickEntity> {

    public ThrownStickEntityRenderer(EntityRendererProvider.Context p_174414_) {
        super(p_174414_);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void render(ThrownStickEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        Minecraft.getInstance().getItemRenderer().renderStatic(
                Items.STICK.getDefaultInstance(),
                ItemTransforms.TransformType.GROUND,
                pPackedLight,
                OverlayTexture.NO_OVERLAY,
                pMatrixStack,
                pBuffer,
                pEntity.getId());
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownStickEntity pEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

}
