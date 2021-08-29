package io.github.communitymod.client;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.client.blockentity.renderer.MeatballChaliceRenderer;
import io.github.communitymod.client.entity.bean.BeanModel;
import io.github.communitymod.client.entity.bean.BeanRenderer;
import io.github.communitymod.client.entity.extremetnt.ExtremeTntRenderer;
import io.github.communitymod.client.entity.goose.GooseModel;
import io.github.communitymod.client.entity.goose.GooseRenderer;
import io.github.communitymod.client.entity.meatball.MeatballRenderer;
import io.github.communitymod.client.entity.stick.ThrownStickEntityRenderer;
import io.github.communitymod.client.entity.wolf.WolfStickRenderLayer;
import io.github.communitymod.core.init.BlockEntityInit;
import io.github.communitymod.core.init.BlockInit;
import io.github.communitymod.core.init.EntityInit;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Wolf;
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

            BlockEntityRenderers.register(BlockEntityInit.MEATBALL_CHALICE_BE.get(), MeatballChaliceRenderer::new);
            ItemBlockRenderTypes.setRenderLayer(BlockInit.GLASS_TABLE.get(), RenderType.translucent());
        }

        @SubscribeEvent
        public static void addCustomLayers(final EntityRenderersEvent.AddLayers event) {
            LivingEntityRenderer<Wolf, ? extends EntityModel<Wolf>> renderer = event.getRenderer(EntityType.WOLF);
            if (renderer instanceof WolfRenderer wolfRenderer) {
                wolfRenderer.addLayer(new WolfStickRenderLayer(wolfRenderer));
            }
        }

        @SubscribeEvent
        public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(BeanRenderer.BEAN_LAYER, BeanModel::createBodyMesh);
            event.registerLayerDefinition(GooseRenderer.GOOSE_LAYER, GooseModel::createBodyLayer);
            event.registerLayerDefinition(MeatballRenderer.MEATBALL_LAYER, MeatballRenderer::createBodyLayer);

        }

        @SubscribeEvent
        public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(EntityInit.BEAN_ENTITY.get(), BeanRenderer::new);
            event.registerEntityRenderer(EntityInit.GOOSE_ENTITY.get(), GooseRenderer::new);
            event.registerEntityRenderer(EntityInit.THROWN_STICK.get(), ThrownStickEntityRenderer::new);
            event.registerEntityRenderer(EntityInit.EXTREME_TNT_ENTITY.get(), ExtremeTntRenderer::new);
            event.registerEntityRenderer(EntityInit.MEATBALL.get(), MeatballRenderer::new);
        }
    }
}
