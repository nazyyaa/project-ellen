package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Alive;
import sk.tuke.kpi.oop.game.items.*;

import java.util.Objects;

public class Chest extends AbstractActor implements Usable<Alive> {

    public Chest() {
        Animation normalAnimation = new Animation("sprites/chest.png", 16, 16);
        setAnimation(normalAnimation);
    }

    @Override
    public void useWith(Alive ripley) {
        if (ripley == null) return;

        int positionX = getPosX();
        int positionY = getPosY();

        getScene().addActor(new Hammer(), positionX + 16, positionY);
        getScene().addActor(new HealthBooster(), positionX, positionY + 16);
        getScene().addActor(new AmmoBooster(), positionX - 16, positionY);
        getScene().addActor(new Ammo(), positionX, positionY - 16);

        Objects.requireNonNull(getScene()).removeActor(this);
    }

    @Override
    public Class<Alive> getUsingActorClass() {
        return Alive.class;
    }

}
