package me.faln.projects.warzonechests.utils;

import me.faln.projects.warzonechests.WarzoneChests;

import java.util.List;

public enum Lang {

    USAGE("usage"),
    CHEST_RECEIVED("reward-chest-received"),
    CANNOT_OPEN("cannot-open-chest"),
    NEXT_SPAWN("next-spawn"),
    RELOAD_SUCCESS("reload-success"),
    ADDED_LOCATION("added-location"),
    ACTION_BAR("action-bar"),
    CHEST_SPAWNING("chest-spawning");

    private final WarzoneChests plugin = WarzoneChests.getPlugin(WarzoneChests.class);
    private final String path;

    Lang(final String path) {
        this.path = path;
    }

    public List<String> getList() {
        return Utils.colorize(plugin.getFiles().getFile("lang").list(path));
    }

    public String getMessage() {
        return Utils.colorize(plugin.getFiles().getFile("lang").string(path, null));
    }

}
