package io.github.communitymod.core;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.common.entities.BeanEntity;
import io.github.communitymod.core.init.EntityInit;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public final class CommonEvents {

	@EventBusSubscriber(modid = CommunityMod.MODID, bus = Bus.FORGE)
	public static final class ForgeEvents {

	}

	@EventBusSubscriber(modid = CommunityMod.MODID, bus = Bus.MOD)
	public static final class ModEvents {

		@SubscribeEvent
		public static void entityAttributes(final EntityAttributeCreationEvent event) {
			event.put(EntityInit.BEAN_ENTITY.get(), BeanEntity.createAttributes().build());
		}
	}
}
