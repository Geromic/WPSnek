package websnake.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import websnake.domain.*;
import websnake.model.DBManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class GameController extends HttpServlet {
    private Board gameSession;

    public GameController() {
        super();
        gameSession = new Board();
        int x= gameSession.getHeadX();
        int y= gameSession.getHeadY();
        /*DBManager dbManager = new DBManager();
        dbManager.addMove("START", x, y);*/
    }

    public void restartGame(){
        int x= gameSession.getHeadX();
        int y= gameSession.getHeadY();
        /*DBManager dbManager = new DBManager();
        dbManager.addMove("END", x, y);*/

        this.gameSession = new Board();

        int xNew= gameSession.getHeadX();
        int yNew= gameSession.getHeadY();
        /*DBManager dbManager = new DBManager();
        dbManager.addMove("START", xNew, yNew);*/
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user == null){
            PrintWriter out = new PrintWriter(response.getOutputStream());
            out.println("Log in before playing the game");
            out.flush();
            return;
        }


        String action = request.getParameter("action");
        if(action != null && action.equals("restart")){
            restartGame();
        }

        if(action != null && action.equals("score")){
            JSONObject jObj = new JSONObject();
            jObj.put("score", gameSession.getScore());

            PrintWriter out = new PrintWriter(response.getOutputStream());
            out.println(jObj.toJSONString());
            out.flush();
            return;
        }

        response.setContentType("application/json");
        ArrayList<Cell> snakeCells = gameSession.getSnakeCells();
        Cell foodCell = gameSession.getFoodCell();
        JSONArray jsonCells = new JSONArray();

        for (Cell snakeCell : snakeCells) {
            JSONObject jObj = new JSONObject();
            jObj.put("x", snakeCell.x);
            jObj.put("y", snakeCell.y);
            jObj.put("val", 3);
            jsonCells.add(jObj);
        }

        ArrayList<Cell> obstacleCells = gameSession.getObstacleCells();
        for (Cell obstacleCell : obstacleCells) {
            JSONObject jObj = new JSONObject();
            jObj.put("x", obstacleCell.x);
            jObj.put("y", obstacleCell.y);
            jObj.put("val", 1);
            jsonCells.add(jObj);
        }

        if(foodCell != null){
            JSONObject jObj = new JSONObject();
            jObj.put("x", foodCell.x);
            jObj.put("y", foodCell.y);
            jObj.put("val", 2);
            jsonCells.add(jObj);
        }

        PrintWriter out = new PrintWriter(response.getOutputStream());
        out.println(jsonCells.toJSONString());
        out.flush();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user == null){
            PrintWriter out = new PrintWriter(response.getOutputStream());
            out.println("Log in before making a move");
            out.flush();
            return;
        }

        if(gameSession.isOver()){
            JSONObject jObj = new JSONObject();
            jObj.put("mess", "Game over");
            PrintWriter out = new PrintWriter(response.getOutputStream());
            out.println(jObj.toJSONString());
            out.flush();
            return;
        }

        response.setContentType("application/json");
        String direction = request.getParameter("direction");
        Direction dir = Direction.valueOf(direction);
        gameSession.move(dir);
        int x= gameSession.getHeadX();
        int y= gameSession.getHeadY();
        /*DBManager dbManager = new DBManager();
        dbManager.addMove(dir, x, y);*/

        if(gameSession.isOver()){
            JSONObject jObj = new JSONObject();
            jObj.put("mess", "Game over");
            PrintWriter out = new PrintWriter(response.getOutputStream());
            out.println(jObj.toJSONString());
            out.flush();
        }
    }
}
