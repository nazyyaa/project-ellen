package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.openables.LockedDoor;


public class AccessCard extends AbstractActor implements Collectible, Usable<LockedDoor> {

    public AccessCard() {
        Animation normalAnimation = new Animation("sprites/key.png", 16, 16);
        setAnimation(normalAnimation);
    }

    @Override
    public void useWith(LockedDoor door) {
        door.unlock();
    }

    @Override
    public Class<LockedDoor> getUsingActorClass() {
        return LockedDoor.class;
    }
}
