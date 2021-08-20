package io.github.communitymod.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraftforge.fmllegacy.network.NetworkEvent.Context;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

public class ExplosionMessage {
	public BlockInteraction blockInteraction;

	public float explosionRadius;

	public double x, y, z;

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
	}

	public static ExplosionMessage from(FriendlyByteBuf buf) {
		return new ExplosionMessage(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readFloat(),
				buf.readEnum(BlockInteraction.class));
	}

	public static void handle(ExplosionMessage msg, Supplier<Context> ctx) {
		ctx.get().enqueueWork(() -> ServerLifecycleHooks.getCurrentServer().overworld().explode(null, msg.x, msg.y,
				msg.z, msg.explosionRadius, msg.blockInteraction));
		ctx.get().setPacketHandled(true);
	}
}
