package me.faln.projects.warzonechests.tasks;

import lombok.Getter;
import me.faln.projects.warzonechests.WarzoneChests;
import me.faln.projects.warzonechests.objects.ChestObject;
import me.faln.projects.warzonechests.utils.Lang;
import me.faln.projects.warzonechests.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class OpeningTask {

    private final WarzoneChests plugin;
    private final Player player;
    private double currentPercentage = 0.0;
    @Getter private long lastClicked;
    private final Location location;
    private final ChestObject chest;
    private final DecimalFormat formatter;

    public OpeningTask(WarzoneChests plugin, final Player player, final Location location, final String chest) {
        this.plugin = plugin;
        this.player = player;
        this.location = location;
        this.chest = plugin.getChestCache().getChest(chest);
        this.formatter = new DecimalFormat("#.##");
        this.run();
    }

    public void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!plugin.getClickCache().getClicks().asMap().containsKey(player.getUniqueId())) {
                    this.cancel();
                    return;
                }

                double increaseAmount = ThreadLocalRandom.current().nextDouble(chest.getMinPercentage(), chest.getMaxPercentage() + 1);

                currentPercentage = (currentPercentage + increaseAmount) <= 100 ? currentPercentage + increaseAmount : 100.0;

                final String progressBar = Utils.getProgressBar(currentPercentage, 100.0);

                plugin.sendActionBar(player, Lang.ACTION_BAR.getMessage()
                        .replace("%amount-gained%", formatter.format(increaseAmount))
                        .replace("%percentage%", formatter.format(currentPercentage))
                        .replace("%progress-bar%", progressBar));

                if (currentPercentage >= 100.0) {
                    plugin.getSpawningTask().getLocations().remove(location);
                    location.getBlock().setType(Material.AIR);
                    Utils.giveItem(player, chest.getItem().build());
                    player.sendMessage(Lang.CHEST_RECEIVED.getMessage()
                            .replace("%type%", chest.getDisplayName())
                            .replace("%amount%", "1"));
                    this.cancel();
                }

            }
        }.runTaskTimer(plugin, 10, 20);
    }

    public OpeningTask setLastClicked(long time) {
        this.lastClicked = time;
        return this;
    }

}
