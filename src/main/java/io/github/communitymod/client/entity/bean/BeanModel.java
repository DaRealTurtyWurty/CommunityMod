package io.github.communitymod.client.entity.bean;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.communitymod.common.entities.BeanEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

public class BeanModel<T extends BeanEntity> extends EntityModel<T> {

    private final ModelPart main;

    public BeanModel(final ModelPart model) {
        this.main = model;
    }

    public static LayerDefinition createBodyMesh() {
        final var meshDefinition = new MeshDefinition();
        final var partDefinition = meshDefinition.getRoot();

        partDefinition.addOrReplaceChild("main",
                CubeListBuilder.create().texOffs(24, 19).addBox(-2.0f, -6.0f, 0.0f, 5.0f, 1.0f, 1.0f)
                        .texOffs(24, 17).addBox(-3.0f, -7.0f, 0.0f, 8.0f, 1.0f, 1.0f).texOffs(0, 14)
                        .addBox(-4.0f, -9.0f, 0.0f, 10.0f, 2.0f, 1.0f).texOffs(21, 13)
                        .addBox(-2.0f, -12.0f, 0.0f, 10.0f, 1.0f, 1.0f).texOffs(22, 2)
                        .addBox(-1.0f, -13.0f, 0.0f, 9.0f, 1.0f, 1.0f).texOffs(0, 0)
                        .addBox(-1.0f, -19.0f, 0.0f, 10.0f, 6.0f, 1.0f).texOffs(0, 21)
                        .addBox(-3.0f, -11.0f, 0.0f, 10.0f, 1.0f, 1.0f).texOffs(0, 9)
                        .addBox(-4.0f, -10.0f, 0.0f, 11.0f, 1.0f, 1.0f).texOffs(0, 19)
                        .addBox(-2.0f, -21.0f, 0.0f, 10.0f, 1.0f, 1.0f).texOffs(0, 17)
                        .addBox(-3.0f, -22.0f, 0.0f, 10.0f, 1.0f, 1.0f).texOffs(0, 7)
                        .addBox(-4.0f, -23.0f, 0.0f, 11.0f, 1.0f, 1.0f).texOffs(0, 11)
                        .addBox(-4.0f, -25.0f, 0.0f, 10.0f, 2.0f, 1.0f).texOffs(22, 15)
                        .addBox(-3.0f, -26.0f, 0.0f, 8.0f, 1.0f, 1.0f).texOffs(0, 23)
                        .addBox(-2.0f, -27.0f, 0.0f, 5.0f, 1.0f, 1.0f).texOffs(22, 0)
                        .addBox(-1.0f, -20.0f, 0.0f, 9.0f, 1.0f, 1.0f).texOffs(24, 4)
                        .addBox(-2.0f, -5.0f, 0.0f, 1.0f, 5.0f, 1.0f).texOffs(12, 23)
                        .addBox(2.0f, -5.0f, 0.0f, 1.0f, 5.0f, 1.0f).texOffs(0, 25)
                        .addBox(-2.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f).texOffs(24, 21)
                        .addBox(2.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f),
                PartPose.offsetAndRotation(0.0f, 24.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(final PoseStack poseStack, final VertexConsumer buffer, final int packedLight,
            final int packedOverlay, final float red, final float green, final float blue,
            final float alpha) {
        this.main.render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setupAnim(final T entity, final float limbSwing, final float limbSwingAmount,
            final float ageInTicks, final float netHeadYaw, final float headPitch) {
        // Use this method to setup the animation and rotation angles
    }
}
