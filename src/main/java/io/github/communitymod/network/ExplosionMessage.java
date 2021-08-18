package io.github.communitymod.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Explosion.BlockInteraction;

public class ExplosionMessage {
    public double x;
    public double y;
    public double z;
    public float explosionRadius;
    public BlockInteraction blockInteraction;

    public ExplosionMessage(double x, double y, double z, float explosionRadius, BlockInteraction blockInteraction) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.explosionRadius = explosionRadius;
        this.blockInteraction = blockInteraction;
    }

    public static void encode(ExplosionMessage message, FriendlyByteBuf buf) {
        buf.writeDouble(message.x);
        buf.writeDouble(message.y);
        buf.writeDouble(message.z);
        buf.writeFloat(message.explosionRadius);
        buf.writeEnum(message.blockInteraction);
    };

    public static ExplosionMessage from(FriendlyByteBuf buf) {
        return new ExplosionMessage(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readFloat(),
                buf.readEnum(BlockInteraction.class));
    }
}
