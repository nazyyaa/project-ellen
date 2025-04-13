package sk.tuke.kpi.oop.game.characters;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.behaviours.Behaviour;
import sk.tuke.kpi.oop.game.behaviours.FollowMoving;

import java.util.Objects;

public class Boss extends Alien {

    private Animation invisibleAnimation;
    private Animation normalAnimation;

    public static final Topic<Boss> BOSS_DEFEAT = Topic.create("boss defeat", Boss.class);


    public Boss(int healthValue, Behaviour<? super Alien> behaviour) {
        super(healthValue, behaviour);

        normalAnimation = new Animation("sprites/boss.png", 32, 32, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        invisibleAnimation = new Animation("sprites/invisible.png");
        setAnimation(normalAnimation);
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);

        getHealth().onFatigued(() -> {
            Objects.requireNonNull(getScene()).getMessageBus().publish(BOSS_DEFEAT, this);
        });
        new Loop<>(new ActionSequence<>(
            new Invoke<>(this::spawnAlien),
            new Wait<>(5),
            new Invoke<>(this::enableInvisible),
            new Wait<>(2),
            new Invoke<>(this::disableInvisible)
        )).scheduleFor(this);
    }

    private void spawnAlien() {
        if (getScene() == null) return;

        Alien newAlien = new Alien(30, new FollowMoving());
        Objects.requireNonNull(getScene()).addActor(newAlien, getPosX() + 50, getPosY() + 50);
    }

    private void enableInvisible() {
        if (getScene() == null) return;

        setAnimation(invisibleAnimation);
    }

    private void disableInvisible() {
        if (getScene() == null) return;
        setAnimation(normalAnimation);
    }
}
