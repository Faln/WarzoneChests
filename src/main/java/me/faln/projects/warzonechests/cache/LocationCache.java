package me.faln.projects.warzonechests.cache;

import lombok.Getter;
import me.faln.projects.warzonechests.WarzoneChests;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;

@Getter
public class LocationCache {

    private final List<Location> locations = new ArrayList<>();

    private final WarzoneChests plugin;
    private final List<String> whitelistedRegions;

    public LocationCache(WarzoneChests plugin) {
        this.plugin = plugin;
        this.whitelistedRegions = plugin.getFiles().getFile("config").list("whitelisted-regions");
        this.cache();
    }

    public void cache() {
        this.locations.clear();

        for (String location : plugin.getFiles().getFile("data").list("locations")) {
            String[] locSplit = location.split(":");
            if (locSplit.length != 4) continue;

            final World world = Bukkit.getWorld(locSplit[0]);
            if (world == null) continue;

            try {
                final int x = Integer.parseInt(locSplit[1]);
                final int y = Integer.parseInt(locSplit[2]);
                final int z = Integer.parseInt(locSplit[3]);

                this.locations.add(new Location(world, x, y, z));
            } catch (NumberFormatException e) {
                plugin.log("&cInvalid Location Format: " + location);
            }
        }
    }


}
