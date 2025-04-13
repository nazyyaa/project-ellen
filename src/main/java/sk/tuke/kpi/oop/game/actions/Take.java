package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.gamelib.graphics.Color;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.items.Collectible;
import java.util.List;
import java.util.Objects;

public class Take<A extends Keeper> extends AbstractAction<A> {

    public Take() {}

    @Override
    public void execute(float deltaTime) {
        if (getActor() == null || getActor().getScene() == null || isDone()) {
            setDone(true);
            return;
        }

        Scene scene = getActor().getScene();
        List<Actor> actor_list = Objects.requireNonNull(getActor().getScene()).getActors();

        for (Actor actors : actor_list) {
            if (actors instanceof Collectible && actors.intersects(getActor())) {
                try {
                    getActor().getBackpack().add(((Collectible) actors));
                    assert scene != null;
                    scene.removeActor(actors);
                    break;

                } catch (IllegalStateException exception) {
                    int windowHeight = scene.getGame().getWindowSetup().getHeight();
                    int windowWidth = scene.getGame().getWindowSetup().getWidth();
                    scene.getGame().getOverlay().drawRectangle((windowHeight - windowHeight / 2) - 8, (windowWidth - windowWidth / 2) - 10, 200, 30, Color.BLACK).showFor(3);
                    scene.getGame().getOverlay().drawText("Backpack is full!", windowHeight - windowHeight / 2, windowWidth - windowWidth / 2).showFor(3);
                }
            }
        }
        setDone(true);
    }
}


