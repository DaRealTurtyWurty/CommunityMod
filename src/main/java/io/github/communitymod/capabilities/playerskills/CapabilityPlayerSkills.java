package io.github.communitymod.capabilities.playerskills;


import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityPlayerSkills {

    @CapabilityInject(PlayerSkills.class)
    public static Capability<PlayerSkills> PLAYER_STATS_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(PlayerSkills.class);
    }
}
