package io.github.communitymod.client;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.client.entity.bean.BeanModel;
import io.github.communitymod.client.entity.bean.BeanRenderer;
import io.github.communitymod.client.entity.goose.GooseModel;
import io.github.communitymod.client.entity.goose.GooseRenderer;
import io.github.communitymod.core.init.EntityInit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public final class ClientEvents {

	@EventBusSubscriber(modid = CommunityMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
	public static final class ForgeEvents {

	}

	@EventBusSubscriber(modid = CommunityMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
	public static final class ModEvents {
		@SubscribeEvent
		public static void clientSetup(final FMLClientSetupEvent event) {

		}

		@SubscribeEvent
		public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions event) {
			event.registerLayerDefinition(BeanRenderer.BEAN_LAYER, BeanModel::createBodyMesh);
			event.registerLayerDefinition(GooseRenderer.GOOSE_LAYER, GooseModel::createBodyLayer);
		}

		@SubscribeEvent
		public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
			event.registerEntityRenderer(EntityInit.BEAN_ENTITY.get(), BeanRenderer::new);
			event.registerEntityRenderer(EntityInit.GOOSE_ENTITY.get(), GooseRenderer::new);
		}
	}
}
