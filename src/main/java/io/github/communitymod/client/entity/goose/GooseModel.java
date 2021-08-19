package io.github.communitymod.client.entity.goose;

// Forge model conversion from 1.16 to 1.17 by Steven (Steaf23), program outline loosely based on https://github.com/Globox1997/ModelConverter
// Generate all required imports yourself
// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.communitymod.common.entities.GooseEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class GooseModel<T extends GooseEntity> extends EntityModel<T>
{
	private final ModelPart leg1;
	private final ModelPart body;
	private final ModelPart cube_r1;
	private final ModelPart cube_r2;
	private final ModelPart neck;
	private final ModelPart head;
	private final ModelPart leg2;

	public GooseModel(ModelPart model) {
		this.leg1 = model;
		this.body = model;
		this.cube_r1 = model.getChild("cube_r1");
		this.cube_r2 = model.getChild("cube_r2");
		this.neck = model.getChild("neck");
		this.head = this.neck.getChild("head");
		this.leg2 = model;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshDefinition = new MeshDefinition();
		PartDefinition partDefinition = meshDefinition.getRoot();

		partDefinition.addOrReplaceChild("leg1", CubeListBuilder.create()
						.texOffs(0, 6).addBox(-1.5f, 1.0f, 0.0f, 2.0f, 2.0f, 2.0f)
						.texOffs(2, 20).addBox(-1.0f, 3.0f, 1.0f, 1.0f, 6.0f, 0.0f)
						.texOffs(24, 5).addBox(-3.0f, 9.0f, -4.0f, 5.0f, 0.0f, 5.0f),
				PartPose.offsetAndRotation(-3.0f, 15.0f, 0.0f, 0.0f, 0.0f, 0.0f));

		partDefinition.addOrReplaceChild("body", CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0f, 25.0f, 0.0f, 0.0f, 0.0f, 0.0f));

		partDefinition.addOrReplaceChild("cube_r1", CubeListBuilder.create()
						.texOffs(0, 20).addBox(-3.0f, -2.0f, -1.5f, 6.0f, 4.0f, 6.0f),
				PartPose.offsetAndRotation(0.0f, 13.0f, 7.5f, -0.48f, 0.0f, 0.0f));

		partDefinition.addOrReplaceChild("cube_r2", CubeListBuilder.create()
						.texOffs(0, 0).addBox(-4.0f, -3.5f, -6.5f, 8.0f, 7.0f, 13.0f),
				PartPose.offsetAndRotation(0.0f, 12.5f, 0.5f, -0.1309f, 0.0f, 0.0f));

		PartDefinition partDefinition3 = partDefinition.addOrReplaceChild("neck", CubeListBuilder.create()
						.texOffs(0, 30).addBox(-2.0f, -7.0f, -0.5f, 4.0f, 8.0f, 3.0f),
				PartPose.offsetAndRotation(0.0f, 8.0f, -6.5f, -0.1309f, 0.0f, 0.0f));

		partDefinition3.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(18, 24).addBox(-2.0f, -1.5f, -4.0f, 4.0f, 3.0f, 6.0f, new CubeDeformation(0.10f))
						.texOffs(32, 20).addBox(-1.0f, -0.5f, -7.0f, 2.0f, 2.0f, 3.0f)
						.texOffs(18, 20).addBox(-1.0f, 0.5f, -10.0f, 2.0f, 1.0f, 3.0f),
				PartPose.offsetAndRotation(0.0f, -7.5f, 0.5f, 0.1745f, 0.0f, 0.0f));

		partDefinition.addOrReplaceChild("leg2", CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.5f, 1.0f, 0.0f, 2.0f, 2.0f, 2.0f)
						.texOffs(0, 20).addBox(-1.0f, 3.0f, 1.0f, 1.0f, 6.0f, 0.0f)
						.texOffs(24, 0).addBox(-3.0f, 9.0f, -4.0f, 5.0f, 0.0f, 5.0f),
				PartPose.offsetAndRotation(4.0f, 15.0f, 0.0f, 0.0f, 0.0f, 0.0f));

		return LayerDefinition.create(meshDefinition, 64, 64);
	}

	@Override
	public void setupAnim(GooseEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// Use this method to setup the animation and rotation angles
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		if (young)
		{
			poseStack.translate(0, 1.0, 0);
			poseStack.scale(0.3F, 0.5F, 0.3F);
		}
		leg1.render(poseStack, buffer, packedLight, packedOverlay);
		body.render(poseStack, buffer, packedLight, packedOverlay);
		leg2.render(poseStack, buffer, packedLight, packedOverlay);
	}
}
