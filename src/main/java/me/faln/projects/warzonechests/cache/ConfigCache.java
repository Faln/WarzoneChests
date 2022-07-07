package me.faln.projects.warzonechests.cache;

import lombok.Getter;
import me.faln.projects.warzonechests.WarzoneChests;
import me.faln.projects.warzonechests.objects.YMLConfig;

@Getter
public class ConfigCache {

    private final WarzoneChests plugin;

    private int minDelay;
    private int maxDelay;
    private int idleTime;
    private int amountToSpawn;

    private int totalBars;
    private char symbol;
    private String completedColor;
    private String notCompletedColor;

    public ConfigCache(WarzoneChests plugin) {
        this.plugin = plugin;
        this.cache();
    }

    public void cache() {
        YMLConfig config = plugin.getFiles().getFile("config");
        this.minDelay = config.parseInt("spawn-timer.min-delay", 60);
        this.maxDelay = config.parseInt("spawn-timer.max-delay", 300);
        this.idleTime = config.parseInt("chest-expire-in");
        this.amountToSpawn = config.parseInt("amount-to-spawn");
        this.totalBars = config.parseInt("progress-bar.amount-of-bars", 40);
        this.symbol = config.parseChar("progress-bar.symbol");
        this.completedColor = config.string("progress-bar.primary-color", "&a");
        this.notCompletedColor = config.string("progress-bar.secondary-color", "&c");
    }

}
