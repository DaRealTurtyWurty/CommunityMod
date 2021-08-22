package io.github.communitymod.capabilities.entitylevel;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityMobLevel {

    @CapabilityInject(MobLevel.class)
    public static Capability<MobLevel> MOB_LEVEL_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(MobLevel.class);
    }
}
