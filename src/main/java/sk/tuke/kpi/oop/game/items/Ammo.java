package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Armed;

import java.util.Objects;

public class Ammo extends AbstractActor implements Usable<Armed> {

    public Ammo() {
        Animation Animation = new Animation("sprites/ammo.png", 16, 16);
        setAnimation(Animation);
    }

    @Override
    public void useWith(Armed ripley) {
        if (ripley == null) return;

        if (ripley.getFirearm().getAmmo() < ripley.getFirearm().getMaxAmmo()){
            ripley.getFirearm().reload(50);
            Objects.requireNonNull(getScene()).removeActor(this);
        }
    }

    @Override
    public Class<Armed> getUsingActorClass() {
        return Armed.class;
    }
}