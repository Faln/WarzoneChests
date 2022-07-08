package me.faln.projects.warzonechests.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.faln.projects.warzonechests.WarzoneChests;
import me.faln.projects.warzonechests.objects.Reward;
import me.faln.projects.warzonechests.utils.Lang;
import me.faln.projects.warzonechests.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ChestPlaceListener implements Listener {

    private final WarzoneChests plugin;

    public ChestPlaceListener(WarzoneChests plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChestPlace(final BlockPlaceEvent event) {
        if (event.isCancelled() || event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        final Block block = event.getBlockPlaced();
        final Player player = event.getPlayer();
        final ItemStack item = event.getItemInHand();

        if (block.getType() == Material.AIR || !Utils.hasStringData(item)) {
            return;
        }

        event.setCancelled(true);

        if (plugin.getServer().getPluginManager().isPluginEnabled("WorldGuard") && !isWhitelistedRegion(player)) {
            player.sendMessage(Lang.CANNOT_OPEN.getMessage());
            return;
        }

        for (Reward reward : plugin.getChestCache().getChest(Utils.getStringData(item)).getRandomRewards()) {
            reward.process(player);
        }

        if (item.getAmount() <= 1) {
            player.getInventory().remove(item);
            return;
        }

        item.setAmount(item.getAmount() - 1);
        player.getInventory().setItemInMainHand(item);

    }

    private boolean isWhitelistedRegion(final Player player) {

        final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        final RegionManager manager = container.get(BukkitAdapter.adapt(player.getWorld()));
        final ApplicableRegionSet set = container.createQuery().getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));

        if (manager == null) {
            return false;
        }

        for (String whitelistedRegions : plugin.getLocationCache().getWhitelistedRegions()) {
            ProtectedRegion region = manager.getRegion(whitelistedRegions);
            if (region != null && set.getRegions().contains(region)) {
                return true;
            }
        }

        return false;
    }

}
