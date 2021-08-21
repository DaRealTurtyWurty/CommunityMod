package io.github.communitymod.capabilities.entitylevel;

import io.github.communitymod.CommunityMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CommunityMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MobLevelEventHandler {
    @SubscribeEvent
    public static void onAttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Mob) {
            MobLevelProvider providerMobLevel = new MobLevelProvider();
            event.addCapability(new ResourceLocation(CommunityMod.MODID, "moblevel"), providerMobLevel);
            event.addListener(providerMobLevel::invalidate);
        }
    }

    @SubscribeEvent
    public static void OnSpawn(final EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof Mob target) {
            target.getCapability(CapabilityMobLevel.MOB_LEVEL_CAPABILITY).ifPresent(mobLevel -> {
                DefaultMobLevel actualMobLevel = (DefaultMobLevel) mobLevel;
                if (actualMobLevel.mobLevel < 1) {
                    actualMobLevel.mobLevel = (randomizeMobLevel());
                }
            });
        }
    }

    /*
     * Randomization code by MetalTurtle#9999, shout out to him!
     */
    private static int randomizeMobLevel() {
        double lambda = 0.056; // Tweak this to desired distribution (I found this to be a good number)
        double randomNumber = Math.random();
        long calculated = Math.round(Math.log(1 - randomNumber) / -lambda); // Should be [0, +∞)
        int result = calculated > 100L ? 1 : (int) calculated;
        return result == 0 ? 1 : result;
    }
}
