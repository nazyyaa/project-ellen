package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.characters.Alive;
import sk.tuke.kpi.oop.game.items.*;

import java.util.Objects;

public class Helicopter extends AbstractActor implements Usable<Alive> {

    public static final Topic<Helicopter> HELICOPTER_FIND = Topic.create("helicopter intersect", Helicopter.class);

    public Helicopter() {
        Animation normalAnimation = new Animation("sprites/heli.png", 64, 64, 0.2f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(normalAnimation);
    }

    @Override
    public void useWith(Alive ripley) {
        if (ripley == null) return;
        Objects.requireNonNull(getScene()).getMessageBus().publish(HELICOPTER_FIND, this);
    }

    @Override
    public Class<Alive> getUsingActorClass() {
        return Alive.class;
    }
}