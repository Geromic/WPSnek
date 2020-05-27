package websnake.model;

import websnake.domain.*;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by forest.
 */
public class DBManager {
    private Statement stmt;

    public DBManager() {
        connect();
    }

    public void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://rpigeromic.go.ro:16000/wp-snake", "issuser", "kmsdatabase");
            stmt = con.createStatement();
        } catch(Exception ex) {
            System.out.println("eroare la connect:"+ex.getMessage());
            ex.printStackTrace();
        }
    }

    public User authenticate(String username, String password) {
        ResultSet rs;
        User u = null;
        System.out.println(username+" "+password);
        try {
            rs = stmt.executeQuery("select * from users where username='"+username+"' and pass='"+password+"'");
            if (rs.next()) {
                u = new User(rs.getInt("uid"), rs.getString("username"), rs.getString("pass"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }

    public void addMove(Direction dir, int x, int y){
        try{
            stmt.executeUpdate("insert into moves(dir, x, y) values('" + dir + "', " + x + ", " + y + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}