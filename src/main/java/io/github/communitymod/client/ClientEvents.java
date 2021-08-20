package io.github.communitymod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import io.github.communitymod.CommunityMod;
import io.github.communitymod.client.entity.bean.BeanModel;
import io.github.communitymod.client.entity.bean.BeanRenderer;
import io.github.communitymod.client.entity.stick.ThrownStickEntityRenderer;
import io.github.communitymod.core.init.EntityInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public final class ClientEvents {

	@EventBusSubscriber(modid = CommunityMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
	public static final class ForgeEvents {

		@SubscribeEvent
		public static void renderStickOnWolf(final RenderLivingEvent.Post<Wolf, WolfModel<Wolf>> event) {
			LivingEntity entity = event.getEntity();
			PoseStack matrixStack = event.getMatrixStack();
			MultiBufferSource buffers = event.getBuffers();
			int light = event.getLight();
			float partialTicks = event.getPartialRenderTick();

			if (entity instanceof Wolf) {
				matrixStack.pushPose();

				double xTrans = 0.7f * Math.cos(Math.toRadians(entity.yBodyRot + 90));
				double zTrans = 0.7f * Math.sin(Math.toRadians(entity.yBodyRot + 90));

				matrixStack.translate(xTrans, 0.6f, zTrans);

				matrixStack.mulPose(Vector3f.XP.rotationDegrees(90));
				matrixStack.mulPose(Vector3f.ZP.rotationDegrees(90));


				ItemStack mainStack = entity.getMainHandItem();

				Minecraft.getInstance().getItemRenderer()
						.renderStatic(mainStack, ItemTransforms.TransformType.GROUND,
								light, OverlayTexture.NO_OVERLAY, matrixStack, buffers, 0);

				matrixStack.popPose();
			}
		}

	}

	@EventBusSubscriber(modid = CommunityMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
	public static final class ModEvents {
		@SubscribeEvent
		public static void clientSetup(final FMLClientSetupEvent event) {

		}

		@SubscribeEvent
		public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions event) {
			event.registerLayerDefinition(BeanRenderer.BEAN_LAYER, BeanModel::createBodyMesh);
		}

		@SubscribeEvent
		public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
			event.registerEntityRenderer(EntityInit.BEAN_ENTITY.get(), BeanRenderer::new);
			event.registerEntityRenderer(EntityInit.THROWN_STICK.get(), ThrownStickEntityRenderer::new);
		}
	}
}
