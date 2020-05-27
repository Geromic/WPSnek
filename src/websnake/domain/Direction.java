package websnake.domain;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    NONE;

    static Direction opposite(Direction dir){
        switch (dir){
            case UP: return DOWN;
            case DOWN: return UP;
            case LEFT: return RIGHT;
            case RIGHT: return LEFT;
            default: return NONE;
        }
    }
}
