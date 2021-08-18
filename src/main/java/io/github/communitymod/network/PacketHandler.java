package io.github.communitymod.network;

import java.util.function.Supplier;

import io.github.communitymod.CommunityMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CommunityMod.MODID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);
    private static int id = 0;

    public static void init() {
        INSTANCE.registerMessage(id++, ExplosionMessage.class, ExplosionMessage::encode, ExplosionMessage::from,
                PacketHandler::handle);

        MinecraftForge.EVENT_BUS.register(PacketHandler.INSTANCE);
    }

    public static void handle(ExplosionMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerLifecycleHooks.getCurrentServer().overworld().explode(null, msg.x, msg.y, msg.z, msg.explosionRadius,
                    msg.blockInteraction);
        });
        ctx.get().setPacketHandled(true);
    }
}
