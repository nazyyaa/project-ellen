package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Armed;

import java.util.Objects;

public class AmmoBooster extends AbstractActor implements Usable<Armed> {

    public AmmoBooster() {
        Animation Animation = new Animation("sprites/ammobooster.png");
        setAnimation(Animation);
    }

    @Override
    public void useWith(Armed ripley) {
        if (ripley == null) return;

        int ammo = ripley.getFirearm().getMaxAmmo();
        ripley.getFirearm().setMaxAmmo(ammo + 30);
        Objects.requireNonNull(getScene()).removeActor(this);

    }

    @Override
    public Class<Armed> getUsingActorClass() {
        return Armed.class;
    }
}
