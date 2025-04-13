package sk.tuke.kpi.oop.game;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.actions.PerpetualReactorHeating;

import java.util.HashSet;
import java.util.Set;

public class Reactor extends AbstractActor implements Switchable, Repairable {
    private int temperature;
    private int damage;

    private Animation normalAnimation;
    private Animation hotAnimation;
    private Animation brokenAnimation;
    private Animation offAnimation;
    private Animation reactorExtinguishAnimation;

    private boolean isOn;
    private Set<EnergyConsumer> devices;
    public static final Topic<Reactor> REACTOR_REPAIRED = Topic.create("reactor repaired", Reactor.class);
    public static final Topic<Reactor> REACTOR_EXTINGUISHED = Topic.create("reactor extinguished", Reactor.class);
    public static final Topic<Reactor> REACTOR_ENABLED = Topic.create("reactor enabled", Reactor.class);

    public Reactor() {
        temperature = 6000;
        damage = 100;
        isOn = false;

        devices = new HashSet<>();

        normalAnimation = new Animation("sprites/reactor_on.png", 80, 80, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        hotAnimation = new Animation("sprites/reactor_hot.png", 80, 80, 0.05f, Animation.PlayMode.LOOP_PINGPONG);
        brokenAnimation = new Animation("sprites/reactor_broken.png", 80, 80, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        offAnimation = new Animation("sprites/reactor.png");
        reactorExtinguishAnimation = new Animation("sprites/reactor_extinguished.png",80,80);
        setAnimation(brokenAnimation);

    }

    public int getTemperature() {
        return temperature;
    }

    public int getDamage() {
        return damage;
    }

    public void increaseTemperature(int increment) {
//        if (isOn && increment >= 0) {
//            if (damage < 33)
//                temperature += increment;
//            else if (damage <= 66)
//                temperature = Math.round((1.5f * increment) + temperature);
//            else
//                temperature += increment * 2;
//
//            if (temperature > 2000)
//                this.damage = Math.round((100f * (temperature - 2000)) / 4000);
//
//            if (damage >= 100 || temperature >= 6000) {
//                damage = 100;
//                isOn = false;
//            }
//            updateAnimation();
//        }
    }

    public void decreaseTemperature(int decrement) {
        if (isOn && decrement >= 0) {
            if (damage >= 50 && damage < 100) {
                temperature = (int)(temperature - (decrement * 0.5));
            } else {
                temperature -= decrement;
            }

            if (temperature < 0)
                temperature = 0;

            updateAnimation();
        }
    }

    private void updateAnimation() {
        if (isOn) {
            if (temperature < 0)
                return;
            else if (temperature <= 4000)
                setAnimation(normalAnimation);
            else if (temperature <= 6000)
                setAnimation(hotAnimation);
            else
                setAnimation(brokenAnimation);
        } else {
            if (damage == 100)
                setAnimation(brokenAnimation);
            else
                setAnimation(offAnimation);
        }

        updateAllDevices();
    }

    public boolean repair() {
        if (damage <= 0 || damage >= 100)
            return false;

        temperature = ((damage - 50) * 40) + 2000;

        if (damage > 50)
            damage = damage - 50;
        else
            damage = 0;

        getScene().getMessageBus().publish(REACTOR_REPAIRED, this);
        updateAnimation();
        return true;
    }

    public void turnOn() {
        if (getDamage() < 100) {
            isOn = true;
            getScene().getMessageBus().publish(REACTOR_ENABLED, this);
        } else {
            isOn = false;
        }
        updateAnimation();
    }


    public void turnOff() {
        isOn = false;
        updateAnimation();
    }

    public boolean isOn() {
        return isOn;
    }

    public boolean extinguish() {
        if (damage < 50)
            return false;
        this.temperature -= 4000;
        this.damage -= 10;

        getScene().getMessageBus().publish(REACTOR_EXTINGUISHED, this);
        setAnimation(reactorExtinguishAnimation);
        return true;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    private boolean isElectricity() {
        return isOn() && damage != 100;
    }

    public void addDevice(EnergyConsumer device) {
        devices.add(device);
        if (damage == 0 && isOn())
            device.setPowered(true);

        device.setPowered(isOn());

    }

    public void removeDevice(EnergyConsumer device) {
        device.setPowered(false);
        this.devices.remove(device);
    }

    private void updateStateOfDevice(EnergyConsumer device){
        device.setPowered(isElectricity());
    }

    private void updateAllDevices(){
        this.devices.forEach(this::updateStateOfDevice);
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        scene.scheduleAction(new PerpetualReactorHeating(1), this);
    }
}
