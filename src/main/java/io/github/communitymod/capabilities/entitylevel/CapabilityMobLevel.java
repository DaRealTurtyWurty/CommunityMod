package io.github.communitymod.capabilities.entitylevel;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityMobLevel {

    @CapabilityInject(IMobLevel.class)
    public static Capability<IMobLevel> MOB_LEVEL_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IMobLevel.class);
    }
}
