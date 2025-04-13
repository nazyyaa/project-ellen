package sk.tuke.kpi.oop.game.openables;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.messages.Topic;

public class LockedDoor extends Door {
    private boolean locked;
    public static final Topic<LockedDoor> DOOR_UNLOCK = Topic.create("door unlock", LockedDoor.class);
    public static final Topic<LockedDoor> DOOR_IS_LOCKED = Topic.create("door is locked", LockedDoor.class);

    private boolean isLocked() {
        return locked;
    }

    public LockedDoor() {
        super();
        locked = true;
    }

    public LockedDoor(String name, Orientation orientation) {
        super(name, orientation);
        locked = true;
    }

    public void lock() {
        if (locked) return;
        locked = true;
        this.close();
    }
    public void unlock() {
        if (!locked) return;
        locked = false;
        this.open();
        getScene().getMessageBus().publish(DOOR_UNLOCK, this);
    }

    @Override
    public void useWith(Actor actor) {
        if (isLocked()) {
            getScene().getMessageBus().publish(DOOR_IS_LOCKED, this);
            return;
        }
        super.useWith(actor);
    }
}
