package sk.tuke.kpi.oop.game.items;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.ActorContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Backpack implements ActorContainer<Collectible> {
    private final String name;
    private final int capacity;
    private final List<Collectible> array_list = new ArrayList<>();

    public Backpack(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int getSize() {
        return array_list.size();
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public void add(@NotNull Collectible actor) {
        if (array_list.size() < getCapacity()) array_list.add(actor);
        else throw new IllegalStateException(getName() + " is full");
    }

    @Override
    public void remove(@NotNull Collectible actor) {
        array_list.remove(actor);
    }

    @Override
    public void shift() {
        Collections.rotate(array_list, 1);
    }

    @Nullable @Override
    public Collectible peek() {
        if (getSize() > 0) return array_list.get(getSize() - 1);
        return null;
    }

    @Override
    public @NotNull List<Collectible> getContent() {
        return List.copyOf(array_list);
    }

    @NotNull @Override
    public Iterator<Collectible> iterator() {
        return array_list.iterator();
    }
}