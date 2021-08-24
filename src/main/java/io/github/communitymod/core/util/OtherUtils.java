package io.github.communitymod.core.util;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class OtherUtils {
    public static void sendChat(Player player, String msg) {
        player.displayClientMessage(Component.nullToEmpty(msg), false);
    }

    public static void sendActionBar(Player player, String msg) {
        player.displayClientMessage(Component.nullToEmpty(msg), true);
    }

    public static void broadcastServerWide(MinecraftServer server, String msg, UUID sender, ChatType type) {
        server.getPlayerList().broadcastMessage(Component.nullToEmpty(msg), type, sender);
    }

    public static double distanceOf(Entity a, Entity b) {
        double x1 = a.position().x;
        double y1 = a.position().y;
        double z1 = a.position().z;
        double x2 = b.position().x;
        double y2 = b.position().y;
        double z2 = b.position().z;
        return distanceOf(x1, y1, z1, x2, y2, z2);
    }

    public static double distanceOf(Entity a, double x2, double y2, double z2) {
        double x1 = a.position().x;
        double y1 = a.position().y;
        double z1 = a.position().z;
        return distanceOf(x1, y1, z1, x2, y2, z2);
    }

    public static double distanceOf(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Math.sqrt(Math.pow((x1 - x2), 2d) + Math.pow((y1 - y2), 2d) + Math.pow((z1 - z2), 2d));
    }
}
