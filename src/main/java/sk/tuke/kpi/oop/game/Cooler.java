package sk.tuke.kpi.oop.game;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Cooler extends AbstractActor implements Switchable {
    private Reactor reactor;
    private boolean isOn;

    public Cooler(Reactor reactor) {
        isOn = false;
        this.reactor = reactor;
        Animation NormalAnimation = new Animation("sprites/fan.png", 32, 32, 0.2f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(NormalAnimation);
        getAnimation().stop();
    }

    public Reactor getReactor() {
        return reactor;
    }

    @Override
    public void turnOn() {
        isOn = true;
        getAnimation().play();
    }

    @Override
    public void turnOff() {
        isOn = false;
        getAnimation().stop();
    }

    @Override
    public boolean isOn() {
        return isOn;
    }

    private void coolReactor() {
        if (reactor == null)
            return;

        if (isOn)
            reactor.decreaseTemperature(1);
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        new Loop<>(new Invoke<>(this::coolReactor)).scheduleFor(this);
    }
}
