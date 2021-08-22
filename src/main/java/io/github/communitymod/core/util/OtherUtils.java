package io.github.communitymod.core.util;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
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
}
