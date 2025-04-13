package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;

public class TimeBomb extends AbstractActor {
    private Animation explodedAnimation;
    private Animation activatedAnimation;

    private float time;
    private boolean IsActivated;
    private boolean IsExploeded;

    public TimeBomb(float time) {
        this.time = time;
        IsActivated = false;
        IsExploeded = false;
        Animation normalAnimation = new Animation("sprites/bomb.png");
        explodedAnimation = new Animation("sprites/small_explosion.png", 64, 32, 0.1f, Animation.PlayMode.ONCE);
        activatedAnimation = new Animation("sprites/bomb_activated.png", 16, 16, 0.2f);
        setAnimation(normalAnimation);
    }

    public float getTime() {
        return time;
    }

    public boolean isActivated() {
        return IsActivated;
    }

    public boolean isExploded() {
        return IsExploeded;
    }

    public void activate() {
        IsActivated = true;
        setAnimation(activatedAnimation);

        new ActionSequence<>(new Wait<>(this.time), new Invoke<>(this::explode), new Invoke<>(this::remove)).scheduleFor(this);
    }

    public void explode() {
        IsExploeded = true;
        setAnimation(explodedAnimation);
    }

    private void remove() {
        getScene().removeActor(this);
    }
}
