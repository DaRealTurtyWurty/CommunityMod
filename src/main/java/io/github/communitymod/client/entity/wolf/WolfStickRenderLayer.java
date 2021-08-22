package io.github.communitymod.client.entity.wolf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

public class WolfStickRenderLayer extends RenderLayer<Wolf, WolfModel<Wolf>> {

    public WolfStickRenderLayer(RenderLayerParent<Wolf, WolfModel<Wolf>> parent) {
        super(parent);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, Wolf pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        pMatrixStack.pushPose();

        pMatrixStack.translate(this.getParentModel().head.x / 16.0f,
                this.getParentModel().head.y / 16.0f,
                this.getParentModel().head.z / 16.0f);
        float headRoll = pLivingEntity.getHeadRollAngle(pPartialTicks);
        pMatrixStack.mulPose(Vector3f.ZP.rotation(headRoll));
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(pNetHeadYaw));
        pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(pHeadPitch));
        pMatrixStack.translate(-0.03f, 0.1f, -0.3f);

        pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0f));
        pMatrixStack.mulPose(Vector3f.ZN.rotationDegrees(45f));

        ItemStack itemstack = pLivingEntity.getItemBySlot(EquipmentSlot.MAINHAND);
        Minecraft.getInstance().getItemInHandRenderer().renderItem(pLivingEntity, itemstack, ItemTransforms.TransformType.GROUND, false, pMatrixStack, pBuffer, pPackedLight);
        pMatrixStack.popPose();
    }

}
