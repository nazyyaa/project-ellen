package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;

import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;

import sk.tuke.kpi.gamelib.graphics.Animation;

import sk.tuke.kpi.oop.game.behaviours.FollowMoving;
import sk.tuke.kpi.oop.game.characters.Alien;
import sk.tuke.kpi.oop.game.characters.Ripley;

import java.util.Objects;

public class SpawnPoint extends AbstractActor {
    private int remainingEnemies;
    private boolean isActivate;
    private Disposable disposable;

    public SpawnPoint(int spawnAliens) {
        remainingEnemies = spawnAliens;
        isActivate = true;

        Animation normalAnimation = new Animation("sprites/spawn.png", 32, 32, 0.1f);
        setAnimation(normalAnimation);
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        startSpawning();
    }

    private void startSpawning() {
        disposable = new Loop<>(new Invoke<>(this::spawnsEnemies)).scheduleFor(this);
    }

    private void spawnsEnemies() {
        if (!isActivate || remainingEnemies <= 0) {
            if (disposable != null) {
                disposable.dispose();
                disposable = null;
            }
            return;
        }

        Ripley ripley = Objects.requireNonNull(getScene()).getFirstActorByType(Ripley.class);
        if (ripley != null && isWithinDistance(ripley)) {
            spawnAlien();
            isActivate = false;
        }
    }

    private boolean isWithinDistance(Actor actor) {
        int dx = actor.getPosX() - this.getPosX();
        int dy = actor.getPosY() - this.getPosY();
        return Math.sqrt(dx * dx + dy * dy) <= 50;
    }

    private void spawnAlien() {
        Alien alien = new Alien(50, new FollowMoving());
        Objects.requireNonNull(getScene()).addActor(alien, getPosX(), getPosY());
        remainingEnemies--;

        if (remainingEnemies > 0) new ActionSequence<>(new Wait<>(3), new Invoke<>(this::spawnAlien)).scheduleFor(this);
    }
}