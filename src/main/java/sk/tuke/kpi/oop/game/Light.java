package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Light extends AbstractActor implements Switchable, EnergyConsumer {
    private Animation OnAnimation;
    private Animation OffAnimation;

    private boolean isOn;
    private boolean isPowered;

    public Light() {
        isOn = false;
        isPowered = false;
        OnAnimation = new Animation("sprites/light_on.png");
        OffAnimation = new Animation("sprites/light_off.png");
        setAnimation(OffAnimation);
    }

    public void toggle() {
        isOn = !isOn;
        updateAnimation();
    }

    public void turnOn() {
        isOn = true;
        updateAnimation();
    }

    public void turnOff() {
        isOn = false;
        updateAnimation();
    }

    public boolean isOn() {
        return isOn;
    }

    private void updateAnimation() {
        if (isOn && isPowered)
            setAnimation(OnAnimation);
        else
            setAnimation(OffAnimation);

    }

    public void setPowered(boolean powered) {
        this.isPowered = powered;
        updateAnimation();
    }
}
