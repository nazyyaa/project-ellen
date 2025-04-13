package sk.tuke.kpi.oop.game.scenarios;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.SceneListener;
import sk.tuke.kpi.oop.game.characters.Alien;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.controllers.KeeperController;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.items.Energy;

public class FirstSteps implements SceneListener {

    @Override
    public void sceneInitialized(@NotNull Scene scene) {
        Ripley ripley = new Ripley();
        scene.addActor(ripley, 0, 0);

        MovableController movableController = new MovableController(ripley);
        scene.getInput().registerListener(movableController);

        Energy energy = new Energy();
        scene.addActor(energy, -100, 50);

        KeeperController keeperController = new KeeperController(ripley);
        scene.getInput().registerListener(keeperController);

        Alien alien = new Alien();
        scene.addActor(alien, 0, 0);
    }

    @Override
    public void sceneUpdating(@NotNull Scene scene) {
        Ripley ripley = scene.getFirstActorByType(Ripley.class);
        if (ripley == null) return;
        ripley.showRipleyState();
    }

}
