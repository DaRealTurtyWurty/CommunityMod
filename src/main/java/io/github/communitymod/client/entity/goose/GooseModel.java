package io.github.communitymod.client.entity.goose;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.communitymod.common.entities.GooseEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

public class GooseModel<T extends GooseEntity> extends EntityModel<T> {
    private final ModelPart leg1;

    private final ModelPart body;
    private final ModelPart cube_r1;
    private final ModelPart cube_r2;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart leg2;

    public GooseModel(final ModelPart model) {
        this.leg1 = model;
        this.body = model;
        this.cube_r1 = model.getChild("cube_r1");
        this.cube_r2 = model.getChild("cube_r2");
        this.neck = model.getChild("neck");
        this.head = this.neck.getChild("head");
        this.leg2 = model;
    }

    public static LayerDefinition createBodyLayer() {
        final var meshDefinition = new MeshDefinition();
        final var partDefinition = meshDefinition.getRoot();

        partDefinition.addOrReplaceChild("leg1",
                CubeListBuilder.create().texOffs(0, 6).addBox(-1.5f, 1.0f, 0.0f, 2.0f, 2.0f, 2.0f)
                        .texOffs(2, 20).addBox(-1.0f, 3.0f, 1.0f, 1.0f, 6.0f, 0.0f).texOffs(24, 5)
                        .addBox(-3.0f, 9.0f, -4.0f, 5.0f, 0.0f, 5.0f),
                PartPose.offsetAndRotation(-3.0f, 15.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        partDefinition.addOrReplaceChild("body", CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0f, 25.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        partDefinition.addOrReplaceChild("cube_r1",
                CubeListBuilder.create().texOffs(0, 20).addBox(-3.0f, -2.0f, -1.5f, 6.0f, 4.0f, 6.0f),
                PartPose.offsetAndRotation(0.0f, 13.0f, 7.5f, -0.48f, 0.0f, 0.0f));

        partDefinition.addOrReplaceChild("cube_r2",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -3.5f, -6.5f, 8.0f, 7.0f, 13.0f),
                PartPose.offsetAndRotation(0.0f, 12.5f, 0.5f, -0.1309f, 0.0f, 0.0f));

        final var partDefinition3 = partDefinition.addOrReplaceChild("neck",
                CubeListBuilder.create().texOffs(0, 30).addBox(-2.0f, -7.0f, -0.5f, 4.0f, 8.0f, 3.0f),
                PartPose.offsetAndRotation(0.0f, 8.0f, -6.5f, -0.1309f, 0.0f, 0.0f));

        partDefinition3.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(18, 24)
                        .addBox(-2.0f, -1.5f, -4.0f, 4.0f, 3.0f, 6.0f, new CubeDeformation(0.10f))
                        .texOffs(32, 20).addBox(-1.0f, -0.5f, -7.0f, 2.0f, 2.0f, 3.0f).texOffs(18, 20)
                        .addBox(-1.0f, 0.5f, -10.0f, 2.0f, 1.0f, 3.0f),
                PartPose.offsetAndRotation(0.0f, -7.5f, 0.5f, 0.1745f, 0.0f, 0.0f));

        partDefinition.addOrReplaceChild("leg2",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5f, 1.0f, 0.0f, 2.0f, 2.0f, 2.0f)
                        .texOffs(0, 20).addBox(-1.0f, 3.0f, 1.0f, 1.0f, 6.0f, 0.0f).texOffs(24, 0)
                        .addBox(-3.0f, 9.0f, -4.0f, 5.0f, 0.0f, 5.0f),
                PartPose.offsetAndRotation(4.0f, 15.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(final PoseStack poseStack, final VertexConsumer buffer, final int packedLight,
            final int packedOverlay, final float red, final float green, final float blue,
            final float alpha) {
        if (this.young) {
            poseStack.translate(0, 1.0, 0);
            poseStack.scale(0.3F, 0.5F, 0.3F);
        }
        this.leg1.render(poseStack, buffer, packedLight, packedOverlay);
        this.body.render(poseStack, buffer, packedLight, packedOverlay);
        this.leg2.render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setupAnim(final GooseEntity entity, final float limbSwing, final float limbSwingAmount,
            final float ageInTicks, final float netHeadYaw, final float headPitch) {
        // Use this method to setup the animation and rotation angles
    }
}
