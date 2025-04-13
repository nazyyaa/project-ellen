package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Ripley;

import java.util.Objects;

public class NonStop extends AbstractActor implements Usable<Ripley> {
    public NonStop() {
        setAnimation(new Animation("sprites/nonstop.png"));
    }

    @Override
    public void useWith(Ripley ripley) {
        if (ripley == null) return;

        ripley.setSpeed(ripley.getSpeed() * 2);
        Objects.requireNonNull(getScene()).removeActor(this);


        new ActionSequence<>(new Wait<>(3), new Invoke<>(() -> ripley.setSpeed(1))).scheduleFor(ripley);
    }

    @Override
    public Class<Ripley> getUsingActorClass() {
        return Ripley.class;
    }
}
