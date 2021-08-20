package io.github.communitymod.core.network;

import io.github.communitymod.CommunityMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CommunityMod.MODID, "main"), () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void init() {
        var id = 0;
        INSTANCE.registerMessage(id++, ExplosionMessage.class, ExplosionMessage::encode,
                ExplosionMessage::from, ExplosionMessage::handle);

        MinecraftForge.EVENT_BUS.register(PacketHandler.INSTANCE);
    }
}
