package sk.tuke.kpi.oop.game.behaviours;

import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.actions.Move;

public class RandomlyMoving implements Behaviour<Movable> {

    public void randomMove(Movable actor) {
        int position_x = (int)(Math.random() * (3)) - 1;
        int position_y = (int)(Math.random() * (3)) - 1;

        Direction direction = null;

        for (Direction positions : Direction.values()) {
            if (position_x == positions.getDx() && position_y == positions.getDy()) direction = positions;
        }

        assert direction != null;
        actor.getAnimation().setRotation(direction.getAngle());
        new Move<>(direction, 2).scheduleFor(actor);
    }

    @Override
    public void setUp(Movable movable) {
        if (movable == null) return;

        new Loop<>(new ActionSequence<>(new Invoke<>(this::randomMove), new Wait<>(2))).scheduleFor(movable);
    }
}