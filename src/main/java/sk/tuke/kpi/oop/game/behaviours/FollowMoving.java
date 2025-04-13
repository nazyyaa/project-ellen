package sk.tuke.kpi.oop.game.behaviours;

import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.actions.Move;
import sk.tuke.kpi.oop.game.characters.Ripley;

import java.util.Objects;

public class FollowMoving implements Behaviour<Movable> {

    public void followMove(Movable actor) {
        Ripley ripley = getRipley(actor);
        if (ripley == null) return;

        double distance = calculateDistance(actor, ripley);
        if (distance > 300 || distance < 20) return;

        Direction direction = calculateDirection(actor, ripley);
        actor.getAnimation().setRotation(direction.getAngle());
        new Move<>(direction, 0.1f).scheduleFor(actor);
    }

    private Ripley getRipley(Movable actor) {
        return Objects.requireNonNull(actor.getScene()).getFirstActorByType(Ripley.class);
    }

    private double calculateDistance(Movable actor, Ripley ripley) {
        int deltaX = ripley.getPosX() - actor.getPosX();
        int deltaY = ripley.getPosY() - actor.getPosY();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    private Direction calculateDirection(Movable actor, Ripley ripley) {
        int deltaX = ripley.getPosX() - actor.getPosX();
        int deltaY = ripley.getPosY() - actor.getPosY();

        int moveX = getMoveDirection(deltaX);
        int moveY = getMoveDirection(deltaY);

        for (Direction direction : Direction.values()) {
            if (direction.getDx() == moveX && direction.getDy() == moveY) {
                return direction;
            }
        }
        return Direction.NONE;
    }

    private int getMoveDirection(int delta) {
        if (Math.abs(delta) > 15) {
            return (delta > 0) ? 1 : -1;
        }
        return 0;
    }

    @Override
    public void setUp(Movable movable) {
        if (movable == null) return;

        new Loop<>(new ActionSequence<>(new Invoke<>(this::followMove), new Wait<>(0.2f))).scheduleFor(movable);
    }
}
