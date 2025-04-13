package sk.tuke.kpi.oop.game.openables;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.map.MapTile;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.items.Usable;

import java.util.Objects;

public class Door extends AbstractActor implements Openable, Usable<Actor> {
    private boolean opened;
    public static final Topic<Door> DOOR_OPENED = Topic.create("door opened", Door.class);
    public static final Topic<Door> DOOR_CLOSED = Topic.create("door closed", Door.class);
    public static final Topic<Door> DOOR_FINAL = Topic.create("door is final", Door.class);
    public enum  Orientation {VERTICAL, HORIZONTAL}
    private Animation opened_door_animation;
    private Animation closed_door_animation;
    private String animation = "sprites/vdoor.png";
    private boolean finalOpened;


    public Door() {
        closed_door_animation = new Animation(animation, 16, 32, 0.1f, Animation.PlayMode.ONCE);
        opened_door_animation = new Animation(animation, 16, 32, 0.1f, Animation.PlayMode.ONCE_REVERSED);
        setAnimation(new Animation(animation, 16, 32));
        getAnimation().stop();
        opened = false;
        finalOpened = false;
    }

    public Door (String name, Orientation orientation) {
        super(name);

        if (orientation == Orientation.VERTICAL) {
            animation = "sprites/vdoor.png";
            closed_door_animation = new Animation(animation, 16, 32, 0.1f, Animation.PlayMode.ONCE);
            opened_door_animation = new Animation(animation, 16, 32, 0.1f, Animation.PlayMode.ONCE_REVERSED);
            setAnimation(new Animation(animation, 16, 32));
        }
        if (orientation == Orientation.HORIZONTAL) {
            animation = "sprites/hdoor.png";
            closed_door_animation = new Animation(animation, 32, 16, 0.1f, Animation.PlayMode.ONCE);
            opened_door_animation = new Animation(animation, 32, 16, 0.1f, Animation.PlayMode.ONCE_REVERSED);
            setAnimation(new Animation(animation, 32, 16));
        }

        getAnimation().stop();
        opened = false;
        finalOpened = false;
    }

    @Override
    public void open() {
        if (finalOpened == true) {
            getScene().getMessageBus().publish(DOOR_FINAL, this);
            return;
        }
        opened = true;
        Objects.requireNonNull(getScene()).getMap().getTile(getPosX() / 16, getPosY() / 16).setType(MapTile.Type.CLEAR);
        getScene().getMap().getTile(getPosX() / 16, getPosY() / 16 + 1).setType(MapTile.Type.CLEAR);
        setAnimation(opened_door_animation);
        getAnimation().play();
        getAnimation().stop();
        getScene().getMessageBus().publish(DOOR_OPENED, this);

    }

    @Override
    public void close() {
        opened = false;
        Objects.requireNonNull(getScene()).getMap().getTile(getPosX() / 16, getPosY() / 16).setType(MapTile.Type.WALL);
        getScene().getMap().getTile(getPosX() / 16, getPosY() / 16 + 1).setType(MapTile.Type.WALL);
        setAnimation(closed_door_animation);
        getAnimation().play();
        getAnimation().stop();
        getScene().getMessageBus().publish(DOOR_CLOSED, this);
    }

    @Override
    public void useWith(Actor actor) {
        if (!isOpen()) open();
        else close();
    }

    public void turnFinalOpened() {
        finalOpened = !finalOpened;
    }

    @Override
    public boolean isOpen() {
        return opened;
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        Objects.requireNonNull(getScene()).getMap().getTile(this.getPosX() /16, this.getPosY() / 16).setType(MapTile.Type.WALL);
        getScene().getMap().getTile(this.getPosX() / 16, this.getPosY() / 16 + 1).setType(MapTile.Type.WALL);
    }

    @Override
    public Class<Actor> getUsingActorClass() {
        return Actor.class;
    }
}
