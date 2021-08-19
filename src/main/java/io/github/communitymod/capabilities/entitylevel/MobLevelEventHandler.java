package io.github.communitymod.capabilities.entitylevel;

import io.github.communitymod.CommunityMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

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
                if (mobLevel.getMobLevel() < 1) {
                    mobLevel.setMobLevel(randomizeMobLevel(event.getWorld().random));
                }
            });
        }
    }

    private static int randomizeMobLevel(Random random) {
        long rnd = random.nextLong();
        while (rnd < 0 | rnd > Math.pow(2, 50)) {
            rnd = random.nextLong();
        }

        if (rnd <= Math.pow(2, 1)) {
            return 100;
        } else if (rnd <= Math.pow(2, 1.5)) {
            return 99;
        } else if (rnd <= Math.pow(2, 2)) {
            return 98;
        } else if (rnd <= Math.pow(2, 2.5)) {
            return 97;
        } else if (rnd <= Math.pow(2, 3)) {
            return 96;
        } else if (rnd <= Math.pow(2, 3.5)) {
            return 95;
        } else if (rnd <= Math.pow(2, 4)) {
            return 94;
        } else if (rnd <= Math.pow(2, 4.5)) {
            return 93;
        } else if (rnd <= Math.pow(2, 5)) {
            return 92;
        } else if (rnd <= Math.pow(2, 5.5)) {
            return 91;
        } else if (rnd <= Math.pow(2, 6)) {
            return 90;
        } else if (rnd <= Math.pow(2, 6.5)) {
            return 89;
        } else if (rnd <= Math.pow(2, 7)) {
            return 88;
        } else if (rnd <= Math.pow(2, 7.5)) {
            return 87;
        } else if (rnd <= Math.pow(2, 8)) {
            return 86;
        } else if (rnd <= Math.pow(2, 8.5)) {
            return 85;
        } else if (rnd <= Math.pow(2, 9)) {
            return 84;
        } else if (rnd <= Math.pow(2, 9.5)) {
            return 83;
        } else if (rnd <= Math.pow(2, 10)) {
            return 82;
        } else if (rnd <= Math.pow(2, 10.5)) {
            return 81;
        } else if (rnd <= Math.pow(2, 11)) {
            return 80;
        } else if (rnd <= Math.pow(2, 11.5)) {
            return 79;
        } else if (rnd <= Math.pow(2, 12)) {
            return 78;
        } else if (rnd <= Math.pow(2, 12.5)) {
            return 77;
        } else if (rnd <= Math.pow(2, 13)) {
            return 76;
        } else if (rnd <= Math.pow(2, 13.5)) {
            return 75;
        } else if (rnd <= Math.pow(2, 14)) {
            return 74;
        } else if (rnd <= Math.pow(2, 14.5)) {
            return 73;
        } else if (rnd <= Math.pow(2, 15)) {
            return 72;
        } else if (rnd <= Math.pow(2, 15.5)) {
            return 71;
        } else if (rnd <= Math.pow(2, 16)) {
            return 70;
        } else if (rnd <= Math.pow(2, 16.5)) {
            return 69;
        } else if (rnd <= Math.pow(2, 17)) {
            return 68;
        } else if (rnd <= Math.pow(2, 17.5)) {
            return 67;
        } else if (rnd <= Math.pow(2, 18)) {
            return 66;
        } else if (rnd <= Math.pow(2, 18.5)) {
            return 65;
        } else if (rnd <= Math.pow(2, 19)) {
            return 64;
        } else if (rnd <= Math.pow(2, 19.5)) {
            return 63;
        } else if (rnd <= Math.pow(2, 20)) {
            return 62;
        } else if (rnd <= Math.pow(2, 20.5)) {
            return 61;
        } else if (rnd <= Math.pow(2, 21)) {
            return 60;
        } else if (rnd <= Math.pow(2, 21.5)) {
            return 59;
        } else if (rnd <= Math.pow(2, 22)) {
            return 58;
        } else if (rnd <= Math.pow(2, 22.5)) {
            return 57;
        } else if (rnd <= Math.pow(2, 23)) {
            return 56;
        } else if (rnd <= Math.pow(2, 23.5)) {
            return 55;
        } else if (rnd <= Math.pow(2, 24)) {
            return 54;
        } else if (rnd <= Math.pow(2, 24.5)) {
            return 53;
        } else if (rnd <= Math.pow(2, 25)) {
            return 52;
        } else if (rnd <= Math.pow(2, 25.5)) {
            return 51;
        } else if (rnd <= Math.pow(2, 26)) {
            return 50;
        } else if (rnd <= Math.pow(2, 26.5)) {
            return 49;
        } else if (rnd <= Math.pow(2, 27)) {
            return 48;
        } else if (rnd <= Math.pow(2, 27.5)) {
            return 47;
        } else if (rnd <= Math.pow(2, 28)) {
            return 46;
        } else if (rnd <= Math.pow(2, 28.5)) {
            return 45;
        } else if (rnd <= Math.pow(2, 29)) {
            return 44;
        } else if (rnd <= Math.pow(2, 29.5)) {
            return 43;
        } else if (rnd <= Math.pow(2, 30)) {
            return 42;
        } else if (rnd <= Math.pow(2, 30.5)) {
            return 41;
        } else if (rnd <= Math.pow(2, 31)) {
            return 40;
        } else if (rnd <= Math.pow(2, 31.5)) {
            return 39;
        } else if (rnd <= Math.pow(2, 32)) {
            return 38;
        } else if (rnd <= Math.pow(2, 32.5)) {
            return 37;
        } else if (rnd <= Math.pow(2, 33)) {
            return 36;
        } else if (rnd <= Math.pow(2, 33.5)) {
            return 35;
        } else if (rnd <= Math.pow(2, 34)) {
            return 34;
        } else if (rnd <= Math.pow(2, 34.5)) {
            return 33;
        } else if (rnd <= Math.pow(2, 35)) {
            return 32;
        } else if (rnd <= Math.pow(2, 35.5)) {
            return 31;
        } else if (rnd <= Math.pow(2, 36)) {
            return 30;
        } else if (rnd <= Math.pow(2, 36.5)) {
            return 29;
        } else if (rnd <= Math.pow(2, 37)) {
            return 28;
        } else if (rnd <= Math.pow(2, 37.5)) {
            return 27;
        } else if (rnd <= Math.pow(2, 38)) {
            return 26;
        } else if (rnd <= Math.pow(2, 38.5)) {
            return 25;
        } else if (rnd <= Math.pow(2, 39)) {
            return 24;
        } else if (rnd <= Math.pow(2, 39.5)) {
            return 23;
        } else if (rnd <= Math.pow(2, 40)) {
            return 22;
        } else if (rnd <= Math.pow(2, 40.5)) {
            return 21;
        } else if (rnd <= Math.pow(2, 41)) {
            return 20;
        } else if (rnd <= Math.pow(2, 41.5)) {
            return 19;
        } else if (rnd <= Math.pow(2, 42)) {
            return 18;
        } else if (rnd <= Math.pow(2, 42.5)) {
            return 17;
        } else if (rnd <= Math.pow(2, 43)) {
            return 16;
        } else if (rnd <= Math.pow(2, 43.5)) {
            return 15;
        } else if (rnd <= Math.pow(2, 44)) {
            return 14;
        } else if (rnd <= Math.pow(2, 44.5)) {
            return 13;
        } else if (rnd <= Math.pow(2, 45)) {
            return 12;
        } else if (rnd <= Math.pow(2, 45.5)) {
            return 11;
        } else if (rnd <= Math.pow(2, 46)) {
            return 10;
        } else if (rnd <= Math.pow(2, 46.5)) {
            return 9;
        } else if (rnd <= Math.pow(2, 47)) {
            return 8;
        } else if (rnd <= Math.pow(2, 47.5)) {
            return 7;
        } else if (rnd <= Math.pow(2, 48)) {
            return 6;
        } else if (rnd <= Math.pow(2, 48.5)) {
            return 5;
        } else if (rnd <= Math.pow(2, 49)) {
            return 4;
        } else if (rnd <= Math.pow(2, 49.5)) {
            return 3;
        } else if (rnd <= Math.pow(2, 50)) {
            return 2;
        } else if (rnd <= Math.pow(2, 50.5)) {
            return 1;
        } else {
            return 1;
        }
    }
}
