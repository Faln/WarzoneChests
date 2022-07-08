package me.faln.projects.warzonechests.utils;

import com.google.common.base.Strings;
import me.faln.projects.warzonechests.WarzoneChests;
import me.faln.projects.warzonechests.cache.ConfigCache;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    private static final WarzoneChests plugin = WarzoneChests.getPlugin(WarzoneChests.class);
    private static final NamespacedKey key = new NamespacedKey(plugin, "warzone-chest");

    public static String colorize(final String message) {
        return message == null ? null : ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> colorize(final List<String> list) {
        return list.stream().map(Utils::colorize).collect(Collectors.toList());
    }

    public static void send(final Player player, final List<String> messageList) {
        messageList.forEach(s -> player.sendMessage(Utils.colorize(s)));
    }

    public static boolean addStringData(final Block block, final String value) {
        if (!(block.getState() instanceof TileState)) {
            return false;
        }
        TileState state = (TileState) block.getState();
        state.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
        state.update();
        return true;
    }

    public static String getStringData(final Block block) {
        if (!(block.getState() instanceof TileState)) {
            return null;
        }

        TileState state = (TileState) block.getState();

        if (!state.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            return null;
        }

        return state.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    public static String getStringData(final ItemStack item) {
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta() || item.getItemMeta() == null) {
            return null;
        }

        return item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    public static boolean hasStringData(final Block block) {
        if (!(block.getState() instanceof TileState)) {
            return false;
        }

        TileState state = (TileState) block.getState();

        return state.getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }

    public static boolean hasStringData(final ItemStack item) {
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta() || item.getItemMeta() == null) {
            return false;
        }

        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }

    public static String getProgressBar(final double current, final double max) {

        ConfigCache configCache = plugin.getConfigCache();

        if (current >= max) {
            return Strings.repeat(Utils.colorize(configCache.getCompletedColor() + configCache.getSymbol()), configCache.getTotalBars());
        }

        double percent = current / max;
        int progressBars = (int) (configCache.getTotalBars() * percent);
        return Strings.repeat(Utils.colorize(configCache.getCompletedColor() + configCache.getSymbol()), progressBars)
                + Strings.repeat(Utils.colorize(configCache.getNotCompletedColor() + configCache.getSymbol()), configCache.getTotalBars() - progressBars);
    }

    public static void giveItem(final Player player, final ItemStack item) {
        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), item);
            return;
        }
        player.getInventory().addItem(item);
    }

}
