/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.lib.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.json.simple.JSONObject;
import src.lib.modals.Field;
import src.lib.modals.Schema;
import src.lib.modals.Table;

/**
 *
 * @author ASUS
 */
public interface DBDetailsInter {
    /**
     * 
     * @param conn
     * @return
     * @throws SQLException 
     */
    public List<Schema> getSchemas(Connection conn) throws SQLException;
    
    public JSONObject getSchemaDetails(Connection conn) throws SQLException;
    
    public List<String> tableList(Connection conn, String schema) throws SQLException;
    
    public List<Table> tables(Connection conn, String schema) throws SQLException;
    
    public List<Field> fields(Connection conn, String schema, String table) throws SQLException;
    
    public boolean changeDatabase(Connection conn, String schema) throws SQLException;
    
    public Table getTable(Connection conn, String schema, String table) throws SQLException;
    
    public Field getField(Connection conn, String schema, String table, String field) throws SQLException;
} 
