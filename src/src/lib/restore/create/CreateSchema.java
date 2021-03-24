/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.lib.restore.create;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author ASUS
 */
public class CreateSchema {
    public boolean createSchema(Connection conn, String schema) throws SQLException{
        String query = "CREATE DATABASE " + schema;
        if(this.isExist(conn, schema))
            return true;
        PreparedStatement ps = conn.prepareStatement(query);
        ps.execute();
        return this.isExist(conn, schema);
    }
    
    public boolean isExist(Connection conn, String schema) throws SQLException{
        String query = "SHOW SCHEMAS";
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            if(schema.equalsIgnoreCase(resultSet.getString(1)))
                return true;
        }
        return false;
    }
}
