package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Scene;
import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.actions.Loop;

public class SmartCooler extends Cooler {

    public SmartCooler(Reactor reactor) {
        super(reactor);
    }

    private void smartCooler() {
        Reactor reactor = getReactor();
        if (reactor == null)
            return;

        if (getReactor().getTemperature() > 2500 )
            turnOn();
        else if (getReactor().getTemperature() < 1500)
            turnOff();

    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        new Loop<>(new Invoke<>(this::smartCooler)).scheduleFor(this);
    }
}
