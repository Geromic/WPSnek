package websnake.domain;

public class Snake {
    private Direction currentDir;
    private int length;

    public Snake(int length){
        this.length = length;
        this.currentDir = Direction.NONE;
    }

    public Snake(){
        this.length = 1;
        this.currentDir = Direction.NONE;
    }

    public int getLength(){
        return this.length;
    }

    public int getScore(){
        return this.length-1;
    }

    public Direction getDir(){
        return this.currentDir;
    }

    public void addScore(){
        this.length++;
    }
}
