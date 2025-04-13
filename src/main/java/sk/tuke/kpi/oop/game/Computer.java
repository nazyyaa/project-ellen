package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Computer extends AbstractActor implements EnergyConsumer {
    private boolean powered;

    public Computer() {
        Animation normalAnimation = new Animation("sprites/computer.png", 80, 48, 0.2f,Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(normalAnimation);
    }

    private void updateAnimation() {
        if (powered)
            this.getAnimation().play();
        else
            this.getAnimation().pause();
    }

    public int add(int x, int y) {
        if (!powered)
            return 0;
        return x + y;
    }

    public int sub(int x, int y) {
        if (!powered)
            return 0;
        return x - y;
    }

    public float add(float x, float y) {
        if (!powered)
            return 0;
        return x + y;
    }

    public float sub(float x, float y) {
        if (!powered)
            return 0;
        return x - y;
    }

    @Override
    public void setPowered(boolean powered) {
        this.powered = powered;
        updateAnimation();
    }
}

