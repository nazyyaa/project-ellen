package sk.tuke.kpi.oop.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum Direction {
    NORTH (0, 1),
    EAST (1, 0),
    SOUTH (0, -1),
    WEST (-1, 0),
    NORTHEAST(1, 1),
    SOUTHEAST(1, -1),
    NORTHWEST(-1, 1),
    SOUTHWEST(-1, -1),
    NONE(0, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    private static class RedirectionHelper {
        public static Map<Direction, Float> redirectionAngle = new HashMap<>(Map.ofEntries(
            Map.entry(NORTH, 0.f),
            Map.entry(EAST, 270.f),
            Map.entry(SOUTH, 180.f),
            Map.entry(WEST, 90.f),
            Map.entry(NORTHEAST, 315.f),
            Map.entry(SOUTHEAST, 225.f),
            Map.entry(NORTHWEST, 45.f),
            Map.entry(SOUTHWEST, 135.f)));
    }
    public float getAngle() {
        return RedirectionHelper.redirectionAngle.getOrDefault(this, 0.f);
    }

    public Direction combine(Direction other) {
        for (Direction position : Direction.values()) {
            int new_dx = getDx() + other.getDx();
            int new_dy = getDy() + other.getDy();

            new_dx = Math.max(-1, Math.min(1, new_dx));
            new_dy = Math.max(-1, Math.min(1, new_dy));

            if (position.dx == new_dx && position.dy == new_dy) return position;
        }

        return NONE;
    }

    public static Direction fromAngle(float angle) {
        for (var directions : RedirectionHelper.redirectionAngle.entrySet()) {
            if (Objects.equals(angle, directions.getValue())) return directions.getKey();
        }

        return NONE;
    }
}