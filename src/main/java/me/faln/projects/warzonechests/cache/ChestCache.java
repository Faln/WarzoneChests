package me.faln.projects.warzonechests.cache;

import lombok.Getter;
import me.faln.projects.warzonechests.WarzoneChests;
import me.faln.projects.warzonechests.objects.ChestObject;
import me.faln.projects.warzonechests.objects.RandomCollection;
import me.faln.projects.warzonechests.objects.Reward;
import me.faln.projects.warzonechests.utils.Item;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

@Getter
public class ChestCache {

    private final Map<String, ChestObject> chests = new HashMap<>();
    private final RandomCollection<String> chestPool = new RandomCollection<>();

    private final WarzoneChests plugin;

    public ChestCache(WarzoneChests plugin) {
        this.plugin = plugin;
        this.cache();
    }

    public String getRandomChest() {
        return chestPool.get();
    }

    public void cache() {

        this.chests.clear();
        this.chestPool.clear();

        ConfigurationSection section = plugin.getFiles().getFile("chests").section("");
        if (section == null) {
            return;
        }

        for (String chest : section.getKeys(false)) {

            ConfigurationSection chestSection = section.getConfigurationSection(chest);
            if (chestSection == null) {
                return;
            }
            ConfigurationSection rewardsSection = section.getConfigurationSection(chest + ".rewards");
            if (rewardsSection == null) {
                return;
            }

            List<Reward> rewards = new ArrayList<>();

            for (String rewardID : rewardsSection.getKeys(false)) {

                final String rewardType = rewardsSection.getString(rewardID + ".type", null);
                if (rewardType == null || rewardType.equalsIgnoreCase("")) {
                    continue;
                }

                if (rewardType.equalsIgnoreCase("item")) {
                    for (String item : rewardsSection.getConfigurationSection(rewardID + ".items").getKeys(false)) {
                        rewards.add(new Reward(rewardType, rewardsSection.getConfigurationSection(rewardID + ".items." + item))
                                .setChance(rewardsSection.getDouble(rewardID + ".chance")));
                    }
                } else if (rewardType.equalsIgnoreCase("command")) {
                    rewards.add(new Reward(rewardType, rewardsSection.getString(rewardID + ".command"))
                            .setChance(rewardsSection.getDouble(rewardID + ".chance")));
                }
            }

            this.chests.put(chest, new ChestObject(new Item(chestSection).setPData("warzone-chest", PersistentDataType.STRING, chest))
                    .setSpawnChance(section.getDouble(chest + ".chance", 0.0D))
                    .setMaxRewards(section.getInt(chest + ".max-rewards-amount"))
                    .setMinRewards(section.getInt(chest + ".min-rewards-amount"))
                    .setMaxPercentage(section.getDouble(chest + ".max-percentage"))
                    .setMinPercentage(section.getDouble(chest + ".min-percentage"))
                    .setDisplayName(section.getString(chest + ".chest-display"))
                    .setRewards(rewards)
            );

            this.chestPool.add(section.getDouble(chest + ".chance", 0.0D), chest);
        }
    }

    public ChestObject getChest(final String chest) {
        return this.chests.get(chest);
    }

}
