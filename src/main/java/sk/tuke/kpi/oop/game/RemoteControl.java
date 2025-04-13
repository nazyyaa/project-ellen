package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Alive;
import sk.tuke.kpi.oop.game.items.*;

    import java.util.Objects;

public class RemoteControl extends AbstractActor implements Usable<Alive> {

    public RemoteControl() {
        Animation normalAnimation = new Animation("sprites/remotecontrol.png", 80, 48, 0.2f,Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(normalAnimation);
    }

    @Override
    public void useWith(Alive ripley) {
        if (ripley == null) return;

        Reactor reactor = Objects.requireNonNull(getScene()).getLastActorByType(Reactor.class);
        reactor.turnOn();
    }

    @Override
    public Class<Alive> getUsingActorClass() {
        return Alive.class;
    }
}