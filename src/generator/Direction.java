package generator;

import javafx.util.Pair;


public enum Direction {
    NORTH(0, -1), WEST(-1, 0), EAST(1, 0), SOUTH(0, 1);

    private final int x;
    private final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pair<Integer, Integer> getCoords() {
        return new Pair<>(x, y);
    }
}