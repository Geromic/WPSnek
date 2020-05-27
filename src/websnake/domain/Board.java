package websnake.domain;

import websnake.model.DBManager;

import java.util.ArrayList;
import java.util.Random;

public class Board {
    private Coordonate[][] table;
    private int headX;
    private int headY;
    private int tailX;
    private int tailY;
    private int foodX;
    private int foodY;
    private int removeCounter = 0;
    private int score;
    private boolean gameOver = false;

    public Board(){
        table = new Coordonate[10][20];
        for(int i=0; i<10; i++){
            for(int j=0; j<20; j++){
                table[i][j] = new Coordonate(i,j,Direction.NONE);
            }
        }

        score = 0;
        this.addObstacles();
        this.addHead();
        this.addFood();
    }

    private void addHead() {
        Random r = new Random();
        int x = r.nextInt(10);
        int y = r.nextInt(20);
        if(table[x][y].equals(0)){
            // ------------
            headX = tailX = x;
            headY = tailY = y;
            //dbManager.addFoodCell(x, y);
            // ------------
            table[x][y].value = 3;
            int dir = r.nextInt(4);
            switch(dir){
                case 0: table[headX][headY].dir = Direction.UP;
                case 1: table[headX][headY].dir = Direction.DOWN;
                case 2: table[headX][headY].dir = Direction.LEFT;
                case 3: table[headX][headY].dir = Direction.RIGHT;
            }
            return;
        }
        addHead();
    }

    public ArrayList<Cell> getSnakeCells(){
        ArrayList<Cell> cells = new ArrayList<>();
        for(int i=0; i<10; i++){
            for(int j=0; j<20; j++){
                if(table[i][j].value == 3){
                    cells.add(new Cell(i, j));
                }
            }
        }
        return cells;
    }

    public ArrayList<Cell> getObstacleCells(){
        ArrayList<Cell> cells = new ArrayList<>();
        cells.add(new Cell(1,1));
        cells.add(new Cell(1,2));
        cells.add(new Cell(1,3));
        cells.add(new Cell(3,4));
        cells.add(new Cell(7,10));
        cells.add(new Cell(8,10));
        cells.add(new Cell(9,10));
        return cells;
    }

    public Cell getFoodCell(){
        return new Cell(foodX, foodY);
    }

    private void addObstacles(){
        table[1][1].value = 1;
        table[1][2].value = 1;
        table[1][3].value = 1;
        table[3][4].value = 1;
        table[7][10].value = 1;
        table[8][10].value = 1;
        table[9][10].value = 1;
    }

    private void addFood(){
        Random r = new Random();
        int x = r.nextInt(10);
        int y = r.nextInt(20);
        if(table[x][y].equals(0)){
            foodX = x;
            foodY = y;
            table[x][y].value = 2;
            return;
        }
        addFood();
    }

    private void moveTail(){
        if(removeCounter == 0){
            this.table[tailX][tailY].value = 0;
            // ------------
            //dbManager.deleteSnakeCell(tailX, tailY);
            // ------------
            Coordonate coord = getNextLocation(this.table[tailX][tailY]);
            this.table[tailX][tailY].dir = Direction.NONE;
            tailX = coord.x;
            tailY = coord.y;
            return;
        }
        removeCounter--;
    }

    public void move(Direction dir){
        boolean ok = true;
        if(gameOver)
            return;

        if(dir == Direction.opposite(table[headX][headY].dir) && score > 0) {
            gameOver = true;
            return;
        }
        if(dir == Direction.NONE){
            dir = table[headX][headY].dir;
        }
        else{
            table[headX][headY].dir = dir;
        }
        Coordonate coord = getNextLocation(table[headX][headY]);
        if(score != 0){
            this.moveTail();
            ok=false;
        }
        if(table[coord.x][coord.y].value == 1 || table[coord.x][coord.y].value == 3){
            gameOver = true;
        }
        else{
            table[coord.x][coord.y].dir = table[headX][headY].dir;
            this.headX = coord.x;
            this.headY = coord.y;
            int oldScore = score;
            if(table[coord.x][coord.y].value == 2){
                this.score++;
                this.removeCounter++;
                addFood();
            }
            table[headX][headY].value = 3;
            if(oldScore == 0){
                if(ok){
                    this.moveTail();
                }
            }
        }
    }

    private Coordonate getNextLocation(Coordonate coord) {
        switch (coord.dir){
            case UP: return new Coordonate(coord.x-1 == -1 ? 9 : coord.x-1, coord.y, coord.dir);
            case DOWN: return new Coordonate(coord.x+1 == 10 ? 0 : coord.x+1, coord.y, coord.dir);
            case LEFT: return new Coordonate(coord.x, coord.y-1 == -1 ? 19 : coord.y-1, coord.dir);
            case RIGHT: return new Coordonate(coord.x, coord.y+1 == 20 ? 0 : coord.y+1, coord.dir);
        }
        return new Coordonate(-1, -1, Direction.NONE);
    }

    public boolean isOver() {
        return gameOver;
    }

    public int getScore() {
        return this.score;
    }

    public int getHeadX() {
        return headX;
    }

    public int getHeadY() {
        return headY;
    }
}
