package me.faln.projects.warzonechests.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.faln.projects.warzonechests.WarzoneChests;
import me.faln.projects.warzonechests.objects.ChestObject;
import me.faln.projects.warzonechests.utils.Lang;
import me.faln.projects.warzonechests.utils.Utils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CommandAlias("chest|treasurechest|tc|chests")
@CommandPermission("warzonechests.use")
public class WChestCmd extends BaseCommand {

    private final WarzoneChests plugin;

    public WChestCmd(WarzoneChests plugin) {
        this.plugin = plugin;
    }

    @Default
    @HelpCommand
    public void defaultCommand(CommandSender sender) {
        Lang.USAGE.getList().forEach(sender::sendMessage);
    }

    @Subcommand("give")
    @CommandCompletion("@chests 1|2|3|10 @players")
    @Syntax("<chest-type> <amount> <player>")
    public void giveCommand(String chestType, Player targetPlayer, int amount) {
        final ChestObject chest = plugin.getChestCache().getChest(chestType);
        final ItemStack item = chest.getItem().setAmount(amount).build();
        Utils.giveItem(targetPlayer, item);
        targetPlayer.sendMessage(Lang.CHEST_RECEIVED.getMessage()
                .replace("%type%", chest.getDisplayName())
                .replace("%amount%", String.valueOf(amount)));
    }

    @Subcommand("reload")
    public void reloadCommand(CommandSender sender) {
        plugin.reload();
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            player.sendMessage(Lang.RELOAD_SUCCESS.getMessage());
        }
    }

    @Subcommand("status|next")
    public void statusCommand(CommandSender sender) {
        sender.sendMessage(Lang.NEXT_SPAWN.getMessage()
                .replace("%time%", new SimpleDateFormat("HH/mm/ss").format(new Date(plugin.getSpawningTask().getNextSpawn() * 1000L))));
    }

    @Subcommand("addlocation")
    public void addLocation(CommandSender sender) {
        if (!(sender instanceof Player))
            return;

        final Player player = (Player) sender;
        final Location location = player.getLocation();
        final List<Location> locationList = plugin.getLocationCache().getLocations();

        if (locationList.contains(location))
            return;

        locationList.add(location);

        player.sendMessage(Lang.ADDED_LOCATION.getMessage());

        player.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            List<String> list = new ArrayList<>();
            for (Location locInList : locationList) {
                if (location.getWorld() == null)
                    continue;

                final String loc = locInList.getWorld().getName() + ":" +
                        locInList.getBlockX() + ":" +
                        locInList.getBlockY() + ":" +
                        locInList.getBlockZ();

                list.add(loc);
            }
            plugin.getFiles().getFile("data").getConfig().set("locations", list);
        });

    }






}
