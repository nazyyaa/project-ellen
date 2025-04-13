package sk.tuke.kpi.oop.game.characters;

import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.GameApplication;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.graphics.Overlay;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.items.Backpack;
import sk.tuke.kpi.oop.game.weapons.Firearm;
import sk.tuke.kpi.oop.game.weapons.Gun;

import java.util.Objects;

public class Ripley extends AbstractActor implements Movable, Keeper, Alive, Armed{

    private Health health;
    private Backpack backpack;
    private Firearm firearm;
    private Animation normalAnimation;

    private int speed;
    private int ammo;

    private Disposable disposable;


    public static final Topic<Ripley> RIPLEY_DIED = Topic.create("DIED...", Ripley.class);

    public Ripley() {
        super("Ellen");
        health = new Health(50, 100);
        backpack = new Backpack("Backpack", 3);
        firearm = new Gun (30, 150);
        normalAnimation = new Animation("sprites/player.png", 32, 32, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(normalAnimation);
        speed = 1;
        ammo = 30;

        disposable = null;

        health.onFatigued(this::handleDeath);
    }

    private void handleDeath() {
        this.setAnimation(new Animation("sprites/player_die.png",32,32,0.1f, Animation.PlayMode.ONCE));
        Objects.requireNonNull(getScene()).getMessageBus().publish(RIPLEY_DIED,this);
    }

    @Override
    public void startedMoving(Direction direction) {
        normalAnimation.setRotation(direction.getAngle());
        normalAnimation.play();
    }

    @Override
    public void stoppedMoving() {
        normalAnimation.stop();
    }

    @Override
    public Firearm getFirearm() {
        return firearm;
    }

    @Override
    public void setFirearm(Firearm firearm) {
        this.firearm = firearm;
    }

    public int getAmmo() { return ammo; }

    public void setAmmo(int ammo) { this.ammo = ammo; }

    @Override
    public Health getHealth() {
        return health;
    }

    public void setHealth(Health health) {
        this.health = health;
    }

    @Override
    public int getSpeed() {
        return this.speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public Backpack getBackpack() {
        return backpack;
    }

    public Disposable stopDecreasingEnergy() {
        return disposable;
    }

    public void decreaseEnergy() {
        if (this.health.getValue() == 0) {
            handleDeath();
            return;
        }

        disposable = new Loop<>(new ActionSequence<>(
            new Invoke<>(() -> {
                if (this.health.getValue() == 0) {
                    handleDeath();
                    return;
                }
                this.getHealth().drain(5);
            }), new Wait<>(1) )).scheduleFor(this);
    }

    public void showRipleyState() {
        Overlay overlay = getScene().getGame().getOverlay();
        overlay.drawText("  | HP: " + getHealth().getValue() + "/" + getHealth().getMaxValue() + " | Ammo: " + getFirearm().getAmmo() + "/" + getFirearm().getMaxAmmo(), 90, getScene().getGame().getWindowSetup().getHeight() - GameApplication.STATUS_LINE_OFFSET);
    }
}
