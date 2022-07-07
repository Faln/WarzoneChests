package me.faln.projects.warzonechests.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class ActionBar implements Reflection {

    private final ActionBar.GetActionBar actionBar = ServerVersion.isOver_V1_12() ? this.newActionBar() : this.oldActionBar();

    public ActionBar.GetActionBar getMethod() {
        return this.actionBar;
    }

    private ActionBar.GetActionBar oldActionBar() {
        return (player, message) -> {
            try {
                Class<?> chat = this.getNMSClass("IChatBaseComponent");
                Constructor<?> constructor = this.getNMSClass("PacketPlayOutChat").getConstructor(chat, Byte.TYPE);
                message = "{\"text\":\"" + message + "\"}";
                Object icbc = chat.getDeclaredClasses()[0].getMethod("a", String.class).invoke((Object)null, message);
                Object packet = constructor.newInstance(icbc, 2);
                this.sendPacket(player, packet);
            } catch (Exception var7) {
                var7.printStackTrace();
            }

        };
    }

    private ActionBar.GetActionBar newActionBar() {
        return (player, message) -> {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
        };
    }

    public interface GetActionBar {
        void send(Player var1, String var2);
    }
}
