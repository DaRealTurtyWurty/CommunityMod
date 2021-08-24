package io.github.communitymod.client.entity.meatball;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.common.entities.Meatball;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class MeatballRenderer extends EntityRenderer<Meatball> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(CommunityMod.MODID,
			"textures/entities/meatball.png");


	public static final ModelLayerLocation MEATBALL_LAYER = new ModelLayerLocation(
			new ResourceLocation(CommunityMod.MODID, "meatball"), "main");

	private final ModelPart meatball;

	public MeatballRenderer(Context context) {
		super(context);
		this.meatball = context.bakeLayer(MEATBALL_LAYER);
	}

	public static LayerDefinition createBodyLayer() {

		MeshDefinition meshDefinition = new MeshDefinition();
		PartDefinition partDefinition = meshDefinition.getRoot();

		partDefinition.addOrReplaceChild("meatball",
				CubeListBuilder.create().texOffs(0, 0).addBox(-3.0f, -3.0f, -3.0f, 6.0f, 6.0f, 6.0f), PartPose.ZERO);

		return LayerDefinition.create(meshDefinition, 32, 32);
	}

	@Override
	public void render(Meatball pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
			MultiBufferSource pBuffer, int pPackedLight) {

		VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));

		this.meatball.xRot+= pEntity.getDeltaMovement().x;
		this.meatball.yRot+= pEntity.getDeltaMovement().y;
		this.meatball.zRot+= pEntity.getDeltaMovement().z;

		this.meatball.render(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY);

		

		super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(Meatball pEntity) {
		return TEXTURE;
	}

}
