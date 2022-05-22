package com.example.lab11;


import java.sql.*;

public class Database {
    private static Database singleton = null;
    private Connection conn;

    private Database() {
        String url = "jdbc:mysql://127.0.0.1:3306/lab8-pa";
        conn = null;
        try {
            conn = DriverManager.getConnection(url, "mihai", "password");
            System.out.println("Database conn working");
        } catch (
                SQLException e) {
            System.out.println("Database connection not working: " + e);
        }
    }

    public static Database getInstance() {
        if(singleton == null) {
            singleton = new Database();
        }
        return singleton;
    }
    public ResultSet query(String sql) throws SQLException {
        try{
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("QUERY ERROR: " + e);
            return null;
        }
    }
    public boolean update(String sql) throws SQLException {
        try{
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            return true;
        } catch (SQLException e) {
            System.out.println("QUERY ERROR: " + e);
            return false;
        }
    }

}

