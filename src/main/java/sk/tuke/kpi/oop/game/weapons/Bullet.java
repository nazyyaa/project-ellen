package sk.tuke.kpi.oop.game.weapons;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.characters.Alive;

import java.util.Objects;


public class Bullet extends AbstractActor implements Fireable {
    private int damage;
    private int speed;

    public Bullet() {
        damage = 30;
        speed = 5;
        Animation normalAnimation = new Animation("sprites/bullet.png", 16, 16);
        setAnimation(normalAnimation);
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    private void shot() {
        for (Actor actor : Objects.requireNonNull(getScene()).getActors()) {
            if (this.intersects(actor) && (actor instanceof Alive)) {
                ((Alive) actor).getHealth().drain(damage);
                collidedWithWall();
            }
        }
    }

    @Override
    public void collidedWithWall() {
        Objects.requireNonNull(getScene()).removeActor(this);
    }

    @Override
    public void startedMoving(Direction direction) {
        if (direction == null || direction == Direction.NONE) return;

        this.getAnimation().setRotation(direction.getAngle());

    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        new Loop<>(new Invoke<>(this::shot)).scheduleFor(this);
    }
}