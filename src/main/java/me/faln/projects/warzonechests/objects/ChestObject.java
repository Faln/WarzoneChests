package me.faln.projects.warzonechests.objects;

import lombok.Getter;
import me.faln.projects.warzonechests.utils.Item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class ChestObject {

    private final RandomCollection<Reward> rewardPool;

    private final Item item;
    private String displayName;
    private int minRewards = 1;
    private int maxRewards = 100;
    private double spawnChance = 0.0;
    private double minPercentage = 0.0;
    private double maxPercentage = 100.0;
    private List<Reward> rewards = new ArrayList<>();

    public ChestObject(final Item item) {
        this.item = item;
        this.rewardPool = new RandomCollection<>();
    }

    public ChestObject setMinRewards(int amount) {
        if (amount > this.minRewards) this.minRewards = amount;
        return this;
    }

    public ChestObject setMaxRewards(int amount) {
        if (amount < this.maxRewards) this.maxRewards = amount;
        return this;
    }

    public ChestObject setSpawnChance(double chance) {
        this.spawnChance = chance;
        return this;
    }

    public ChestObject setRewards(List<Reward> rewards) {
        this.rewards = rewards;
        this.rewardPool.clear();

        for (Reward reward : this.rewards) {
            this.rewardPool.add(reward.getChance(), reward);
        }

        return this;
    }

    public ChestObject setMinPercentage(double percentage) {
        this.minPercentage = percentage;
        return this;
    }

    public ChestObject setMaxPercentage(double percentage) {
        this.maxPercentage = percentage;
        return this;
    }


    public ChestObject setDisplayName(String name) {
        this.displayName = name;
        return this;
    }

    public Set<Reward> getRandomRewards() {
        Set<Reward> rewardSet = new HashSet<>();
        final int amount = ThreadLocalRandom.current().nextInt(minRewards, maxRewards + 1);
        for (int c = 0; c <= amount; c++) {
            rewardSet.add(rewardPool.get());
        }
        return rewardSet;
    }

}
