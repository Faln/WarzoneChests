package me.faln.projects.warzonechests.objects;

import lombok.Getter;
import me.faln.projects.warzonechests.utils.Item;
import me.faln.projects.warzonechests.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

@Getter
public class Reward {

    private final String type;

    private Item item;
    private String command;
    private double chance;

    public Reward(final String type, final String command) {
        this.type = type;
        this.command = command;
    }

    public Reward(final String type, final ConfigurationSection section) {
        this.type = type;
        this.item = new Item(section);
        this.chance = section.getDouble("chance", 0.0);
    }

    public Reward setChance(final double chance) {
        this.chance = chance;
        return this;
    }

    public void process(final Player player) {
        switch (type) {
            case "item":
                Utils.giveItem(player, item.build());
                break;
            case "command":
                Bukkit.dispatchCommand(player, command.replace("%player%", player.getName()));
                break;
        }
    }
}
