package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.items.Collectible;


public class Drop <A extends Keeper> extends AbstractAction<A> {

    @Override
    public void execute(float deltaTime) {
        if (isDone() || getActor() == null || getActor().getBackpack().peek()== null || getActor().getScene() == null) {
            setDone(true);
            return;
        }

        Collectible collectible = getActor().getBackpack().peek();
        assert collectible != null;
        int collectible_pos_x = getActor().getPosX() + (getActor().getWidth() - collectible.getWidth() / 2);
        int collectible_pos_y = getActor().getPosY() + (getActor().getHeight() - collectible.getHeight() / 2);
        getActor().getScene().addActor(collectible, collectible_pos_x, collectible_pos_y);
        getActor().getBackpack().remove(collectible);
        setDone(true);
    }
}

