package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Alive;

import java.util.Objects;

public class HealthBooster extends AbstractActor implements Usable<Alive> {

    public HealthBooster() {
        Animation normalAnimation = new Animation("sprites/healthbooster.png");
        setAnimation(normalAnimation);
    }

    @Override
    public void useWith(Alive ripley) {
        if (ripley == null) return;

        int health = ripley.getHealth().getMaxValue();
        ripley.getHealth().setMaxHealth(health + 30);
        Objects.requireNonNull(getScene()).removeActor(this);
    }

    @Override
    public Class<Alive> getUsingActorClass() {
        return Alive.class;
    }
}
