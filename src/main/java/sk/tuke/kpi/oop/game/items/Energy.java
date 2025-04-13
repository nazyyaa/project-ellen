package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Alive;

import java.util.Objects;

public class Energy extends AbstractActor implements Usable<Alive> {

    public Energy() {
        Animation normalAnimation = new Animation("sprites/energy.png");
        setAnimation(normalAnimation);
    }

    @Override
    public void useWith(Alive ripley) {
        if (ripley == null || ripley.getHealth().getValue() == ripley.getHealth().getMaxValue()) return;

        ripley.getHealth().restore();
        Objects.requireNonNull(getScene()).removeActor(this);
    }

    @Override
    public Class<Alive> getUsingActorClass() {
        return Alive.class;
    }
}
