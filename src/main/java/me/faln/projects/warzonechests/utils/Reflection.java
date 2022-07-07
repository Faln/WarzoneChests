package me.faln.projects.warzonechests.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public interface Reflection {
    default void sendPacket(Player player, Object packet) {
        try {
            Object handler = player.getClass().getMethod("getHandle").invoke(player);
            Object connect = handler.getClass().getField("playerConnection").get(handler);
            connect.getClass().getMethod("sendPacket", this.getNMSClass("Packet")).invoke(connect, packet);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    default Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException var4) {
            var4.printStackTrace();
            return null;
        }
    }
}
