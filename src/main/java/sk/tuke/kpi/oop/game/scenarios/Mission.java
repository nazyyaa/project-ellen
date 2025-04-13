package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.gamelib.graphics.Color;
import sk.tuke.kpi.oop.game.*;
import sk.tuke.kpi.oop.game.behaviours.FollowMoving;
import sk.tuke.kpi.oop.game.behaviours.RandomlyMoving;
import sk.tuke.kpi.oop.game.characters.*;
import sk.tuke.kpi.oop.game.controllers.*;
import sk.tuke.kpi.oop.game.items.*;
import sk.tuke.kpi.oop.game.openables.*;

import java.util.List;

public class Mission implements SceneListener {

    Disposable movableController;
    Disposable shooterController;
    Disposable keeperController;
    private int VentilatorRepaired;
    private Door doorBossRoom;
    private Door doorFinalReactor;
    private Door doorFinalBoss;

    public static class Factory implements ActorFactory {
        @Override
        public @Nullable Actor create(@Nullable String type, @Nullable String name) {
            assert name != null;
            assert type != null;

            switch (name) {
                case "Ripley":
                    return new Ripley();
                case "Boss":
                    return new Boss(1000, new FollowMoving());
                case "Alien":
                    return new Alien(100, new RandomlyMoving());
                case "MotherAlien":
                    return new MotherAlien(200, new RandomlyMoving());
                case "SpawnPoint":
                    return new SpawnPoint(10);
                case "Chest":
                    return new Chest();
                case "HealthBooster":
                    return new HealthBooster();
                case "NonStop":
                    return new NonStop();
                case "AmmoBooster":
                    return new AmmoBooster();
                case "Ammo":
                    return new Ammo();
                case "Energy":
                    return new Energy();
                case "AccessCard":
                    return new AccessCard();
                case "Hammer":
                    return new Hammer();
                case "vDoor Teleport 2":
                    return new LockedDoor("Door Teleport 2", Door.Orientation.VERTICAL);
                case "hDoor Storage":
                    return new LockedDoor("Door Storage", Door.Orientation.HORIZONTAL);
                case "hDoor Main":
                    return new LockedDoor("Door Main", Door.Orientation.HORIZONTAL);
                case "Helicopter":
                    return new Helicopter();
                case "Ventilator":
                    return new Ventilator();
                case "Reactor":
                    return new Reactor();
                case "RemoteControl":
                    return new RemoteControl();
                case "FireExtinguisher":
                    return new FireExtinguisher();
                case "Fan 1":
                    return new SmartCooler(null);
                case "Fan 2":
                    return new Cooler(null);
                default:
                    return null;
            }
        }
    }

    @Override
    public void sceneCreated(@NotNull Scene scene) {
        scene.setActorRenderOrder(List.of(
            SpawnPoint.class, Teleport.class, Reactor.class, Ventilator.class,
            Locker.class, PowerSwitch.class, SmartCooler.class, TimeBomb.class,
            ChainBomb.class, Light.class, DefectiveLight.class, RemoteControl.class,
            AccessCard.class, Energy.class, Hammer.class, HealthBooster.class,
            NonStop.class, AmmoBooster.class, FireExtinguisher.class, Ripley.class,
            Alien.class, MotherAlien.class, Helicopter.class, Boss.class
        ));
    }

    @Override
    public void sceneInitialized(@NotNull Scene scene) {
        Ripley ripley = scene.getFirstActorByType(Ripley.class);
        assert ripley != null;
        scene.follow(ripley);
        scene.getGame().pushActorContainer(ripley.getBackpack());

        setupTeleporters(scene);
        setupDoors(scene);
        setupControllers(scene, ripley);
        setupMessageBus(scene, ripley);

    }

    private void setupTeleporters(@NotNull Scene scene) {
        Teleport teleport1_1 = new Teleport(null);
        Teleport teleport1_2 = new Teleport(null);
        Teleport teleport2_1 = new Teleport(null);
        Teleport teleport2_2 = new Teleport(null);

        teleport1_1.setDestination(teleport1_2);
        teleport1_2.setDestination(teleport1_1);
        scene.addActor(teleport1_1, 720, 32);
        scene.addActor(teleport1_2, 688, 670);

        teleport2_1.setDestination(teleport2_2);
        teleport2_2.setDestination(teleport2_1);
        scene.addActor(teleport2_1, 338, 670);
        scene.addActor(teleport2_2, 720, 577);
    }

    private void setupDoors(@NotNull Scene scene) {
        doorFinalReactor = new Door("Door Final Reactor", Door.Orientation.VERTICAL);
        doorFinalReactor.turnFinalOpened();
        scene.addActor(doorFinalReactor, 450, 735);

        doorBossRoom = new Door("Door Boss Room", Door.Orientation.VERTICAL);
        doorBossRoom.turnFinalOpened();
        scene.addActor(doorBossRoom, 770, 260);

        doorFinalBoss = new Door("Door Final Boss", Door.Orientation.VERTICAL);
        doorFinalBoss.turnFinalOpened();
        scene.addActor(doorFinalBoss, 930, 160);
    }

    private void setupControllers(@NotNull Scene scene, Ripley ripley) {
        movableController = scene.getInput().registerListener(new MovableController(ripley));
        keeperController = scene.getInput().registerListener(new KeeperController(ripley));
        shooterController = scene.getInput().registerListener(new ShooterController(ripley));
    }

    private void setupMessageBus(@NotNull Scene scene, Ripley ripley) {
        int windowHeight = scene.getGame().getWindowSetup().getHeight();
        int windowWidth = scene.getGame().getWindowSetup().getWidth();

        scene.getMessageBus().subscribe(LockedDoor.DOOR_UNLOCK, (Ripley) -> {
            AccessCard accessCard = ripley.getBackpack().getContent().stream().filter(item -> item instanceof AccessCard).map(item -> (AccessCard) item).findFirst().orElse(null);if (accessCard != null) ripley.getBackpack().remove(accessCard);
        });

        scene.getMessageBus().subscribe(LockedDoor.DOOR_IS_LOCKED, (Ripley) -> {
            scene.getGame().getOverlay().drawRectangle((windowHeight - windowHeight / 2) - 10, (windowWidth - windowWidth / 2) - 10, 215, 30, Color.BLACK).showFor(2);
            scene.getGame().getOverlay().drawText("You need an access", windowHeight - windowHeight / 2, windowWidth - windowWidth / 2).showFor(2);
            scene.getGame().getOverlay().drawRectangle((windowHeight - windowHeight / 2) - 30, (windowWidth - windowWidth / 2) - 40, 250, 30, Color.BLACK).showFor(2);
            scene.getGame().getOverlay().drawText("card to open the door", (windowHeight - windowHeight / 2) - 15, (windowWidth - windowWidth / 2) - 30).showFor(2);
        });

        scene.getMessageBus().subscribe(LockedDoor.DOOR_FINAL, (Ripley) -> {
            scene.getGame().getOverlay().drawRectangle((windowHeight - windowHeight / 2) - 10, (windowWidth - windowWidth / 2) - 10, 225, 30, Color.BLACK).showFor(2);
            scene.getGame().getOverlay().drawText("This door will open", windowHeight - windowHeight / 2, windowWidth - windowWidth / 2).showFor(2);
            scene.getGame().getOverlay().drawRectangle((windowHeight - windowHeight / 2) - 30, (windowWidth - windowWidth / 2) - 40, 285, 30, Color.BLACK).showFor(2);
            scene.getGame().getOverlay().drawText("after completing the task", (windowHeight - windowHeight / 2) - 25, (windowWidth - windowWidth / 2) - 30).showFor(2);
        });

        scene.getMessageBus().subscribe(Reactor.REACTOR_ENABLED, (Ripley) -> {
            handleReactorEnabled(scene, windowHeight, windowWidth);
        });

        scene.getMessageBus().subscribe(Reactor.REACTOR_REPAIRED, (Ripley) -> {
            handleReactorRepaired(scene, ripley, windowHeight, windowWidth);
        });

        scene.getMessageBus().subscribe(Reactor.REACTOR_EXTINGUISHED, (Ripley) -> {
            handleReactorExtinguished(scene, ripley, windowHeight, windowWidth);
        });

        scene.getMessageBus().subscribe(Ventilator.VENTILATOR_REPAIRED, (Ripley) -> {
            handleVentilatorRepaired(scene, ripley, windowHeight, windowWidth);
        });

        scene.getMessageBus().subscribe(Boss.BOSS_DEFEAT, (Ripley) -> {
            handleBossDefeat(scene, windowHeight, windowWidth);
        });

        scene.getMessageBus().subscribe(Helicopter.HELICOPTER_FIND, (Ripley) -> {
            handleWinGame(scene, ripley);
        });

        scene.getMessageBus().subscribe(Ripley.RIPLEY_DIED, (Ripley) -> {
            handleRipleyDeath(scene, windowHeight, windowWidth);
        });
    }

    private void handleReactorEnabled(@NotNull Scene scene, int windowHeight, int windowWidth) {
        scene.getFirstActorByType(Door.class).turnFinalOpened();
        scene.getFirstActorByType(Cooler.class).turnOn();
        scene.getFirstActorByType(SmartCooler.class).turnOn();

        doorFinalReactor.turnFinalOpened();

        scene.getGame().getOverlay().drawRectangle((windowHeight - windowHeight / 2) - 30, (windowWidth - windowWidth / 2) - 40, 295, 30, Color.BLACK).showFor(2);
        scene.getGame().getOverlay().drawText("The escape door was open!", (windowHeight - windowHeight / 2) - 15, (windowWidth - windowWidth / 2) - 30).showFor(2);

        scene.getGame().getOverlay().drawRectangle((windowHeight - windowHeight / 2) - 50, (windowWidth - windowWidth / 2) - 10, 335, 30, Color.BLACK).showFor(2);
        scene.getGame().getOverlay().drawText("The reactor has been enabled", (windowHeight - windowHeight / 2) - 40, windowWidth - windowWidth / 2).showFor(2);
    }

    private void handleReactorRepaired(@NotNull Scene scene, Ripley ripley, int windowHeight, int windowWidth) {
        Hammer hammer = ripley.getBackpack().getContent().stream().filter(item -> item instanceof Hammer).map(item -> (Hammer) item).findFirst().orElse(null);
        if (hammer != null) ripley.getBackpack().remove(hammer);

        scene.getGame().getOverlay().drawRectangle((windowHeight - windowHeight / 2) - 50, (windowWidth - windowWidth / 2) - 10, 335, 30, Color.BLACK).showFor(2);
        scene.getGame().getOverlay().drawText("The reactor has been repaired", (windowHeight - windowHeight / 2) - 40, windowWidth - windowWidth / 2).showFor(2);
    }

    private void handleReactorExtinguished(@NotNull Scene scene, Ripley ripley, int windowHeight, int windowWidth) {
        FireExtinguisher fireExtinguisher = ripley.getBackpack().getContent().stream().filter(item -> item instanceof FireExtinguisher).map(item -> (FireExtinguisher) item).findFirst().orElse(null);
        if (fireExtinguisher != null) ripley.getBackpack().remove(fireExtinguisher);

        scene.getGame().getOverlay().drawRectangle((windowHeight - windowHeight / 2) - 70, (windowWidth - windowWidth / 2) - 10, 370, 30, Color.BLACK).showFor(2);
        scene.getGame().getOverlay().drawText("The reactor has been extinguished", (windowHeight - windowHeight / 2) - 60, windowWidth - windowWidth / 2).showFor(2);
    }

    private void handleVentilatorRepaired(@NotNull Scene scene, Ripley ripley, int windowHeight, int windowWidth) {
        Hammer hammer = ripley.getBackpack().getContent().stream().filter(item -> item instanceof Hammer).map(item -> (Hammer) item).findFirst().orElse(null);
        if (hammer != null) ripley.getBackpack().remove(hammer);

        VentilatorRepaired++;
        if (VentilatorRepaired > 1) {
            scene.getFirstActorByType(Door.class).turnFinalOpened();
            scene.getGame().getOverlay().drawRectangle((windowHeight - windowHeight / 2) - 25, (windowWidth - windowWidth / 2) - 40, 330, 30, Color.BLACK).showFor(2);
            scene.getGame().getOverlay().drawText("The door to the Boss is open!", (windowHeight - windowHeight / 2) - 15, (windowWidth - windowWidth / 2) - 30).showFor(2);

            doorBossRoom.turnFinalOpened();
        }
        scene.getGame().getOverlay().drawRectangle((windowHeight - windowHeight / 2) - 60, (windowWidth - windowWidth / 2) - 10, 375, 30, Color.BLACK).showFor(2);
        scene.getGame().getOverlay().drawText("The ventilator has been repaired", (windowHeight - windowHeight / 2) - 45, windowWidth - windowWidth / 2).showFor(2);
    }

    private void handleBossDefeat(@NotNull Scene scene, int windowHeight, int windowWidth) {

        doorFinalBoss.turnFinalOpened();

        scene.getGame().getOverlay().drawRectangle((windowHeight - windowHeight / 2) - 25, (windowWidth - windowWidth / 2) - 40, 330, 30, Color.BLACK).showFor(2);
        scene.getGame().getOverlay().drawText("The boss is defeated", (windowHeight - windowHeight / 2) - 15, (windowWidth - windowWidth / 2) - 30).showFor(2);

        scene.getGame().getOverlay().drawRectangle((windowHeight - windowHeight / 2) - 30, (windowWidth - windowWidth / 2) - 40, 295, 30, Color.BLACK).showFor(2);
        scene.getGame().getOverlay().drawText("The escape door was open!", (windowHeight - windowHeight / 2) - 15, (windowWidth - windowWidth / 2) - 30).showFor(2);
    }

    private void handleRipleyDeath(@NotNull Scene scene, int windowHeight, int windowWidth) {
        handleEndGame(scene);

        scene.getGame().getOverlay().drawRectangle((windowHeight - windowHeight / 2) - 15, (windowWidth - windowWidth / 2) - 10, 190, 30, Color.BLACK).showFor(1000);
        scene.getGame().getOverlay().drawText("You are dead...", windowHeight - windowHeight / 2, windowWidth - windowWidth / 2).showFor(1000);
        scene.getGame().getOverlay().drawRectangle((windowHeight - windowHeight / 2) - 75, (windowWidth - windowWidth / 2) - 40, 300, 30, Color.BLACK).showFor(1000);
        scene.getGame().getOverlay().drawText("Press ESC to exit to game", (windowHeight - windowHeight / 2) - 60, (windowWidth - windowWidth / 2) - 30).showFor(1000);
    }

    private void handleWinGame(@NotNull Scene scene, Ripley ripley) {
        handleEndGame(scene);
        ripley.setPosition(5000, 5000);
        int windowHeight = scene.getGame().getWindowSetup().getHeight();
        int windowWidth = scene.getGame().getWindowSetup().getWidth();

        scene.getGame().getOverlay().drawRectangle(0, 0, 10000, 10000, Color.BLACK).showFor(1000);
        scene.getGame().getOverlay().drawText("Congratulations!", windowHeight - windowHeight / 2, windowWidth - windowWidth / 2).showFor(1000);
        scene.getGame().getOverlay().drawText("Press ESC to exit to game", (windowHeight - windowHeight / 2) - 50, (windowWidth - windowWidth / 2) - 30).showFor(1000);
        scene.getGame().getOverlay().drawText("KPI FEI TUKE 2024. Nazar Andriichuk", (windowHeight - windowHeight / 2) + 100, (windowWidth - windowWidth / 2) - 350).showFor(1000);
    }

    private void handleEndGame(@NotNull Scene scene) {
        movableController.dispose();
        keeperController.dispose();
        shooterController.dispose();

        for (Actor actor : scene.getActors()) {
            scene.cancelActions(actor);
        }
    }

    @Override
    public void sceneUpdating(@NotNull Scene scene) {
        Ripley ripley = scene.getFirstActorByType(Ripley.class);
        assert ripley != null;
        ripley.showRipleyState();
        if (ripley.getPosX() > 940) handleWinGame(scene, ripley);
    }
}
