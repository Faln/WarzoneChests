package me.faln.projects.warzonechests;

import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import me.faln.projects.warzonechests.cache.ChestCache;
import me.faln.projects.warzonechests.cache.ClickCache;
import me.faln.projects.warzonechests.cache.ConfigCache;
import me.faln.projects.warzonechests.cache.LocationCache;
import me.faln.projects.warzonechests.commands.WChestCmd;
import me.faln.projects.warzonechests.listeners.ChestOpenListener;
import me.faln.projects.warzonechests.listeners.ChestPlaceListener;
import me.faln.projects.warzonechests.managers.FilesManager;
import me.faln.projects.warzonechests.tasks.SpawningTask;
import me.faln.projects.warzonechests.utils.ActionBar;
import me.faln.projects.warzonechests.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class WarzoneChests extends JavaPlugin {

    private FilesManager files;
    private ConfigCache configCache;
    private LocationCache locationCache;
    private ChestCache chestCache;
    private ClickCache clickCache;
    private SpawningTask spawningTask;
    private PaperCommandManager paperCommandManager;

    private final ActionBar actionBar = new ActionBar();

    @Override
    public void onEnable() {
        long time = System.currentTimeMillis();

        this.reload();

        this.log("Loading time " + (System.currentTimeMillis() - time) + "ms.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reload() {

        this.paperCommandManager = new PaperCommandManager(this);
        this.paperCommandManager.registerCommand(new WChestCmd(this));
        this.paperCommandManager.getCommandCompletions().registerAsyncCompletion("chests", context -> this.chestCache.getChests().keySet());

        this.files = new FilesManager(this);

        this.configCache = new ConfigCache(this);
        this.chestCache = new ChestCache(this);
        this.locationCache = new LocationCache(this);
        this.clickCache = new ClickCache();

        new ChestOpenListener(this);
        new ChestPlaceListener(this);

        Bukkit.getScheduler().runTaskLater(this, () -> {
            this.spawningTask.stopTask(0);
            this.spawningTask = new SpawningTask(this);
        }, 20);
    }


    public FilesManager getFiles() {
        return this.files;
    }

    public void log(String message) {
        this.getLogger().info(Utils.colorize(message));
    }

    public void sendActionBar(Player player, String message) {
        this.actionBar.getMethod().send(player, Utils.colorize(message));
    }


}
