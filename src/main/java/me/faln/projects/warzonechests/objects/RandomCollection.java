package me.faln.projects.warzonechests.objects;

import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

public class RandomCollection<E> {

    private final NavigableMap<Double, E> map = new TreeMap<>();
    private double total = 0;

    public RandomCollection<E> add(double weight, E result) {
        if (weight <= 0 || map.containsValue(result))
            return this;
        total += weight;
        map.put(total, result);
        return this;
    }

    public E get() {
        return map.ceilingEntry(ThreadLocalRandom.current().nextDouble() * total).getValue();
    }

    public void clear() {
        this.map.clear();
    }

}
