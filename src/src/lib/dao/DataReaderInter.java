/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.lib.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.json.simple.JSONObject;
import src.lib.modals.Table;

/**
 *
 * @author ASUS
 */
public interface DataReaderInter {
    /**
     * 
     * @param conn
     * @param schema
     * @param table
     * @return
     * @throws SQLException 
     */
    public JSONObject readTable(Connection conn, String schema, Table table) throws SQLException;
    
    /**
     * 
     * @param conn
     * @param schema
     * @param query
     * @return
     * @throws SQLException 
     */
    public JSONObject readByQuery(Connection conn, String schema, String query) throws SQLException, IOException;
    
    /**
     * 
     * @param table
     * @param rs
     * @return
     * @throws SQLException 
     */
    public Map<String, String> parseData(Table table, ResultSet rs)throws SQLException;
    
    /**
     * 
     * @param conn
     * @param schema
     * @param table
     * @return
     * @throws SQLException 
     * @throws java.io.IOException 
     */
    public JSONObject extractData(Connection conn, String schema, Table table) throws SQLException, IOException;
    
    public JSONObject extractBin(Connection conn, String schema, Table table) throws SQLException, IOException;
}
