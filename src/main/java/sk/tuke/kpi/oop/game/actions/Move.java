package sk.tuke.kpi.oop.game.actions;

import org.jetbrains.annotations.Nullable;
import sk.tuke.kpi.gamelib.actions.Action;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;


public class Move<A extends Movable> implements Action<A> {

    private A actor;
    private Direction direction;
    private boolean isDone;
    private float duration;
    private int firstTime;

    public Move(Direction direction, float duration) {
        this.direction = direction;
        this.duration = duration;
        isDone = false;
        firstTime = 0;
    }

    private Move(Direction direction) {
        this.direction = direction;
        isDone = false;
        firstTime = 0;
    }

    @Override
    public void execute(float deltaTime) {
        if (getActor() == null) return;

        duration -= deltaTime;

        if (isDone()) return;

        if (firstTime == 0 ) {
            actor.startedMoving(direction);
            firstTime = 1;
        }

        if (duration > 0) {
            int plus_position_x = actor.getPosX() + direction.getDx() * actor.getSpeed();
            int plus_position_y = actor.getPosY() + direction.getDy() * actor.getSpeed();
            actor.setPosition(plus_position_x, plus_position_y);
            if ((getActor().getScene()).getMap().intersectsWithWall(actor)) {
                int minus_position_x = actor.getPosX() - direction.getDx() * actor.getSpeed();
                int minus_position_y = actor.getPosY() - direction.getDy() * actor.getSpeed();
                actor.setPosition(minus_position_x, minus_position_y);
                actor.collidedWithWall();
            }
        } else {
            stop();
        }
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    public void stop() {
        if (actor == null) return;

        isDone = true;
        actor.stoppedMoving();
    }

    @Override
    public void reset() {
        actor.stoppedMoving();
        isDone = false;
        firstTime = 0;
        duration = 0;
    }

    @Nullable @Override
    public A getActor() {
        return actor;
    }

    @Override
    public void setActor(@Nullable A movable) {
        this.actor = movable;
    }
}