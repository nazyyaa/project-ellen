package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.oop.game.characters.Ripley;

import java.util.Objects;

public class Teleport extends AbstractActor {
    private Teleport destination;
    private boolean IsSuccess;

    public Teleport(Teleport destination) {
        this.destination = destination;
        Animation normalAnimation = new Animation("sprites/lift.png");
        setAnimation(normalAnimation);
    }

    public void setDestination(Teleport destination) {
        if (this == destination)
            return;

        this.destination = destination;
    }

    public Teleport getDestination() {
        return destination;
    }

    public void processTeleport() {
        Ripley Player = Objects.requireNonNull(getScene()).getLastActorByType(Ripley.class);

        int PosX = Player.getPosX() + Player.getWidth() / 2;
        int PosY = Player.getPosY() + Player.getHeight() / 2;

        if (IsSuccess && !checkIncorrectPosition(Player) && this.destination != null
            && PosX >= this.getPosX() && PosY <= this.getPosY() + this.getWidth()
            && PosY >= this.getPosY() && PosX <= this.getPosX() + this.getWidth())
                destination.teleportPlayer(Player);

    }

    public void teleportPlayer(Ripley Player) {
        if (Player == null)
            return;

        Player.setPosition(this.getPosX() + (this.getWidth() / 2) - (Player.getWidth() / 2), this.getPosY() + (this.getHeight() / 2) - Player.getHeight() / 2);
        IsSuccess = false;

    }

    public void checkStatus() {
        Ripley player = Objects.requireNonNull(getScene()).getLastActorByType(Ripley.class);
        if (!IsSuccess  && checkIncorrectPosition(player))
            IsSuccess = true;
    }

    private boolean checkIncorrectPosition(Ripley player) {
        if (player.getPosY() + player.getHeight() < this.getPosY() || player.getPosX() + player.getWidth() < this.getPosX() || player.getPosX() > this.getPosX() + this.getWidth() || player.getPosY() > this.getPosY() + this.getHeight())
            return true;

        return false;
    }

    @Override
    public void addedToScene(@NotNull Scene scene) {
        super.addedToScene(scene);
        Ripley player = Objects.requireNonNull(getScene()).getLastActorByType(Ripley.class);
        new Loop<>(new Invoke<>(this::processTeleport)).scheduleFor(player);
        new Loop<>(new Invoke<>(this::checkStatus)).scheduleFor(player);
    }
}
