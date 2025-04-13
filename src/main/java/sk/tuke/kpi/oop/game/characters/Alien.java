package sk.tuke.kpi.oop.game.characters;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.behaviours.Behaviour;

import java.util.List;
import java.util.Objects;

public class Alien extends AbstractActor implements Movable, Alive, Enemy {
    private final Health health;
    private Behaviour<? super Alien> use;
    private Disposable attack = null;

    public Alien() {
        Animation normalAnimation = new Animation("sprites/alien.png", 32, 32, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(normalAnimation);

        health = new Health(100, 100);
        health.onFatigued(()->Objects.requireNonNull(getScene()).removeActor(this));
    }

    public Alien(int healthValue, Behaviour<? super Alien> behaviour) {
        Animation normalAnimation = new Animation("sprites/alien.png", 32, 32, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(normalAnimation);

        use = behaviour;

        health = new Health(healthValue, 100);
        health.onFatigued(() -> Objects.requireNonNull(getScene()).removeActor(this));
    }

    @Override
    public int getSpeed() {
        return 2;
    }

    @Override
    public Health getHealth() {
        return health;
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        if (use != null) use.setUp(this);

        attack = new Loop<>(new ActionSequence<>(new Invoke<>(this::removeAlive), new Wait<>(0.3f))).scheduleFor(this);
    }

    public void setAttack() {
        if (attack == null) return;

        attack = null;
    }

    public void removeAlive() {
        if (getScene() == null) return;

        List<Actor> alive_list;
        alive_list = getScene().getActors();

        for (Actor alive : alive_list) {
            if (alive instanceof Alive && !(alive instanceof Enemy) && this.intersects(alive)) {
                ((Alive) alive).getHealth().drain(3);
                new ActionSequence<>(new Invoke<>(this::setAttack), new Wait<>(1), new Invoke<>(this::removeAlive)).scheduleFor(this);
            }
        }
    }
}
