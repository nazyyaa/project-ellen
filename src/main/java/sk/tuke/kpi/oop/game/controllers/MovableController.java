package sk.tuke.kpi.oop.game.controllers;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Input;
import sk.tuke.kpi.gamelib.KeyboardListener;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.actions.Move;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MovableController implements KeyboardListener {

    private final Movable movable;
    private Move<Movable> move = null;
    private final Set<Input.Key> keys;
    private Input.Key key_1 = null;
    private Input.Key key_2 = null;
    private Disposable disposable;
    private final Map<Input.Key, Direction> keyDirectionMap = Map.ofEntries(Map.entry(Input.Key.UP, Direction.NORTH), Map.entry(Input.Key.RIGHT, Direction.EAST), Map.entry(Input.Key.DOWN, Direction.SOUTH), Map.entry(Input.Key.LEFT, Direction.WEST));

    public MovableController(Movable movable) {
        this.movable = movable;
        keys = new HashSet<>();
    }

    private void updateMove() {
        Direction redirect = null;
        int i = 0;
        for (Input.Key k:keys) {
            if (i == 0) redirect = keyDirectionMap.get(k);
            if (i == 1) redirect = redirect.combine(keyDirectionMap.get(k));
            i++;
        }

        stopMoving();

        if (redirect == null) return;

        move = new Move<>(redirect, Float.MAX_VALUE);
        disposable = move.scheduleFor(movable);

    }

    private void stopMoving() {
        if (move == null) return;

        move.stop();
        disposable.dispose();
        move = null;
    }

    @Override
    public void keyPressed(@NotNull Input.Key key) {
        if (!keyDirectionMap.containsKey(key)) return;

        keys.add(key);
        if(key_1 == null) key_1 = key;
        else if(key_2 == null) key_2 = key;
        updateMove();

    }

    @Override
    public void keyReleased(@NotNull Input.Key key) {
        if (!keyDirectionMap.containsKey(key)) return;
        keys.remove(key);

        if (key == key_1) key_1 = null;
        if (key == key_2) key_1 = null;

        updateMove();
    }
}