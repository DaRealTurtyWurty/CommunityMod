package io.github.communitymod.client.entity.stick;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.communitymod.common.entities.ThrownStickEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import javax.annotation.ParametersAreNonnullByDefault;

public class ThrownStickEntityRenderer extends EntityRenderer<ThrownStickEntity> {

    private static final float DEGREE = (float) (Math.PI / 180);

    public ThrownStickEntityRenderer(EntityRendererProvider.Context p_174414_) {
        super(p_174414_);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void render(ThrownStickEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        pMatrixStack.scale(2, 2, 2);
        float yRotation = pEntity.getFiredRotation() - 90;
        pMatrixStack.mulPose(Vector3f.YN.rotationDegrees(yRotation));

        if (!pEntity.isOnGround()) {

            int animationRotation = Math.toIntExact(System.currentTimeMillis() % 360);
            pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(animationRotation));
        } else {
            pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(90));
            pMatrixStack.mulPose(Vector3f.ZN.rotationDegrees(45));
        }

        Minecraft.getInstance().getItemRenderer().renderStatic(
                Items.STICK.getDefaultInstance(),
                ItemTransforms.TransformType.GROUND,
                pPackedLight,
                OverlayTexture.NO_OVERLAY,
                pMatrixStack,
                pBuffer,
                pEntity.getId());
        pMatrixStack.popPose();
    }

    @ParametersAreNonnullByDefault
    @MethodsReturnNonnullByDefault
    @Override
    public ResourceLocation getTextureLocation(ThrownStickEntity pEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

}
