package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;

import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import org.jetbrains.annotations.NotNull;

public class DefectiveLight extends Light implements Repairable {
    private Disposable disposable;
    private boolean repaired;

    public DefectiveLight() {
        super();
        repaired = false;
    }

    public void changeLight() {
        repaired = false;
        int i = (int)(Math.random() * 20);
        if (i == 1)
            super.toggle();
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        disposable = new Loop<>(new Invoke<Actor>(this::changeLight)).scheduleFor(this);
    }


    public void breakLight() {
        disposable = new Loop<>(new Invoke<>(this::changeLight)).scheduleFor(this);
    }

    @Override
    public boolean repair() {
        if (disposable == null || repaired)
            return false;

        repaired = true;
        disposable.dispose();

        disposable = new ActionSequence<>(new Wait<>(10),new Loop<>(new Invoke<>(this::toggle))).scheduleFor(this);
        return true;

    }
}
