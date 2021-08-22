package io.github.communitymod.core.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraftforge.fmllegacy.network.NetworkEvent.Context;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

import java.util.function.Supplier;

public class ExplosionMessage {
    public BlockInteraction blockInteraction;

    public float explosionRadius;

    public double x, y, z;

    public ExplosionMessage(final double x, final double y, final double z, final float explosionRadius,
            final BlockInteraction blockInteraction) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.explosionRadius = explosionRadius;
        this.blockInteraction = blockInteraction;
    }

    public static void encode(final ExplosionMessage message, final FriendlyByteBuf buf) {
        buf.writeDouble(message.x);
        buf.writeDouble(message.y);
        buf.writeDouble(message.z);
        buf.writeFloat(message.explosionRadius);
        buf.writeEnum(message.blockInteraction);
    }

    public static ExplosionMessage from(final FriendlyByteBuf buf) {
        return new ExplosionMessage(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readFloat(),
                buf.readEnum(BlockInteraction.class));
    }

    public static void handle(final ExplosionMessage msg, final Supplier<Context> ctx) {
        ctx.get().enqueueWork(() -> ServerLifecycleHooks.getCurrentServer().overworld().explode(null, msg.x,
                msg.y, msg.z, msg.explosionRadius, msg.blockInteraction));
        ctx.get().setPacketHandled(true);
    }
}
