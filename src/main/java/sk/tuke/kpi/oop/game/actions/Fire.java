package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.characters.Armed;
import sk.tuke.kpi.oop.game.weapons.Fireable;

import java.util.Objects;

public class Fire <A extends Armed> extends AbstractAction<A> {

    @Override
    public void execute(float deltaTime) {
        if (getActor() == null || isDone()) {
            setDone(true);
            return;
        }

        Fireable fireable = getActor().getFirearm().fire();
        if (fireable == null) return;

        int position_x = Direction.fromAngle(getActor().getAnimation().getRotation()).getDx();
        int position_y = Direction.fromAngle(getActor().getAnimation().getRotation()).getDy();

        int actor_position_x = getActor().getPosX() + 8 + (position_x * 24);
        int actor_position_y = getActor().getPosY() + 8 + (position_y * 24);

        Objects.requireNonNull(getActor().getScene()).addActor(fireable, actor_position_x, actor_position_y);

        fireable.startedMoving(Direction.fromAngle(getActor().getAnimation().getRotation()));
        new Move<Fireable>(Direction.fromAngle(getActor().getAnimation().getRotation()),Float.MAX_VALUE).scheduleFor(fireable);
        setDone(true);
    }
}
