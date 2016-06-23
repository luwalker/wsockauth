/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.wsockauth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alex
 */
public class DbConnection {
    
    private static final String connectionURL = "jdbc:derby://localhost:1527/wsockauth";
    private static final String dbUserLogin = "wauserbot";
    private static final String dbUserPassword = "us80Twa!";

    private Connection conn = null;
    private Statement statement = null;

    public DbConnection() {
        try {
            conn = DriverManager.getConnection(connectionURL, dbUserLogin, dbUserPassword);
            statement = conn.createStatement();
            System.out.println("DB connect successfully ! ");
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Statement getStatement() {
        return statement;
    }

    public ResultSet executeQuery(String sql) {
        try {
            return statement.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public int executeUpdate(String sql) {
        try {
            return statement.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
}
