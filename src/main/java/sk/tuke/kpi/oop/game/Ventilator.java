package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.messages.Topic;

import java.util.Objects;

public class Ventilator extends AbstractActor implements Repairable {
    private boolean used;

    public static final Topic<Ventilator> VENTILATOR_REPAIRED = Topic.create("ventilator repaired", Ventilator.class);

    public Ventilator() {
        Animation normalAnimation = new Animation("sprites/ventilator.png", 32, 32,  0.1f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(normalAnimation);

        useVentilator();
    }

    private void useVentilator() {
        used = true;
        getAnimation().stop();
    }

    @Override
    public boolean repair() {
        if (!used) return false;

        used = false;
        getAnimation().play();
        Objects.requireNonNull(getScene()).getMessageBus().publish(VENTILATOR_REPAIRED,this);

        return true;
    }
}
