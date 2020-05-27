package websnake.domain;

import java.util.Objects;

public class Coordonate {
    public int x;
    public int y;
    public int value;
    public Direction dir;

    public Coordonate(int x, int y, Direction dir){
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.value = 0;
    }

    public String toString(){
        return Integer.toString(value);
    }

    public boolean equals(Integer val) {
        return value == val;
    }
}
