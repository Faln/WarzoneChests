package me.faln.projects.warzonechests.tasks;

import lombok.Getter;
import lombok.Setter;
import me.faln.projects.warzonechests.WarzoneChests;
import me.faln.projects.warzonechests.cache.ConfigCache;
import me.faln.projects.warzonechests.utils.Lang;
import me.faln.projects.warzonechests.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Getter @Setter
public class SpawningTask {

    private final WarzoneChests plugin;

    private int nextSpawn = 0;
    private boolean inAction = false;
    private boolean forced = false;
    private Set<Location> locations;
    private final ConfigCache cache;

    public SpawningTask(WarzoneChests plugin) {
        this.plugin = plugin;
        this.cache = plugin.getConfigCache();
        this.run();
    }

    private void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!inAction) {

                    nextSpawn = forced ? 1 : ThreadLocalRandom.current().nextInt(cache.getMinDelay(), cache.getMaxDelay());
                    inAction = true;
                    forced = false;

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            int amountToSpawn = Math.min(cache.getAmountToSpawn(), plugin.getLocationCache().getLocations().size());

                            locations = plugin.getLocationCache().getLocations().stream().limit(amountToSpawn).collect(Collectors.toSet());

                            for (Location location : locations) {
                                final Block block = location.getBlock();
                                block.setType(Material.CHEST);
                                Utils.addStringData(block, plugin.getChestCache().getRandomChest());
                            }

                            Bukkit.getOnlinePlayers().forEach(player -> Utils.send(player, Lang.CHEST_SPAWNING.getList()
                                    .stream()
                                    .map(s -> s.replace("%amount%", String.valueOf(locations.size())))
                                    .collect(Collectors.toList())));
                        }
                    }.runTaskLater(plugin, nextSpawn * 20L);

                    stopTask(nextSpawn + cache.getIdleTime());
                }
            }
        }.runTaskTimer(plugin, 20, 20);
    }

    public void stopTask(int delay) {
        new BukkitRunnable() {
            @Override
            public void run() {
                inAction = false;
                for (Location location : locations) {
                    location.getBlock().setType(Material.AIR);
                }
            }
        }.runTaskLater(plugin, (delay + nextSpawn) * 20L);
    }

}
