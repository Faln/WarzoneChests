package me.faln.projects.warzonechests.listeners;

import me.faln.projects.warzonechests.WarzoneChests;
import me.faln.projects.warzonechests.tasks.OpeningTask;
import me.faln.projects.warzonechests.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public class ChestOpenListener implements Listener {

    private final WarzoneChests plugin;

    public ChestOpenListener(WarzoneChests plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onRightClick(final PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK
                || !event.hasBlock()
                || event.getClickedBlock() == null
                || event.getClickedBlock().getType() == Material.AIR
                || !event.getClickedBlock().getType().toString().contains("CHEST")
                || event.getHand() != EquipmentSlot.HAND
                || !plugin.getLocationCache().getLocations().contains(event.getClickedBlock().getLocation())
        ) return;

        final Block block = event.getClickedBlock();

        if (!Utils.hasStringData(block)) {
            return;
        }

        event.setCancelled(true);

        final Player player = event.getPlayer();
        final UUID id = player.getUniqueId();
        final ConcurrentMap<UUID, OpeningTask> clicksMap = plugin.getClickCache().getClicks().asMap();
        final String chestType = Utils.getStringData(block);

        if (!clicksMap.containsKey(id)) {
            clicksMap.put(id, new OpeningTask(plugin,
                    player,
                    event.getClickedBlock().getLocation(),
                    chestType)
                    .setLastClicked(System.currentTimeMillis()));
            return;
        }

        if (System.currentTimeMillis() - clicksMap.get(id).getLastClicked() >= 400) {
            clicksMap.remove(id);
            return;
        }

        clicksMap.get(id).setLastClicked(System.currentTimeMillis());
    }
}
