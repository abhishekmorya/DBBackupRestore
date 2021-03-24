/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.lib.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ASUS
 */
public class DBConnection {
    
    public synchronized Connection getConnection(String forName, String url, String user, String pass) throws ClassNotFoundException, SQLException{
        Class.forName(forName);
        Connection conn = DriverManager.getConnection(url, user, pass);
        return conn;
    }
}
