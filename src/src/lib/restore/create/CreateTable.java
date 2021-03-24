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
import src.lib.modals.Field;
import src.lib.modals.Table;

/**
 *
 * @author ASUS
 */
public class CreateTable {
    private final String CREATE = "CREATE TABLE ";
    private final String START = " ( ";
    private final String END = ")";
    
    public boolean createTable(Connection conn, Table table) throws SQLException{
        String query = this.createQuery(table);
        PreparedStatement statement = conn.prepareStatement(query);
        return statement.execute();
    }
    
    public boolean createTable(Connection conn, Table table, String schema) throws SQLException{
        String query = this.createQuery(table, schema);
        PreparedStatement statement = conn.prepareStatement(query);
        return statement.execute();
    }
    
    public boolean createTable(Connection conn, Table table, String schema, String tableName) throws SQLException{
        String query = this.createQuery(table, schema, tableName);
        if(this.isTableExists(conn, tableName, schema))
            this.dropTable(conn, tableName, schema);
        PreparedStatement statement = conn.prepareStatement(query);
        return statement.execute();
    }
    
    public boolean dropTable(Connection conn, String table, String schema) throws SQLException{
        String query = "drop table " + table;
        PreparedStatement statement = conn.prepareStatement("use " + schema);
        statement.execute();
        statement = conn.prepareStatement(query);
        statement.execute();
        return this.isTableExists(conn, table, schema);
    }
    
    public boolean isTableExists(Connection conn, String table, String schema) throws SQLException{
        String query = "show tables";
        PreparedStatement statement = conn.prepareStatement("use " + schema);
        statement.execute();
        statement = conn.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            if(table.equalsIgnoreCase(resultSet.getString(1)))
                return true;
        }
        return false;
    }
    
    public String createQuery(Table table){
        
        String name = table.getName();
        String schema = table.getSchemaName();
        StringBuilder query  = new StringBuilder();
        query.append(this.CREATE).append("`").append(schema)
                .append("`.")
                .append(name)
                .append(this.START);
        for(Field f : table.getFields()){
            query.append(this.getLine(f)).append(", ");
        }
        query.delete(query.lastIndexOf(","), query.length());
        query.append(END);
        
        return query.toString();
    }
    
    public String createQuery(Table table, String schema){
        
        String name = table.getName();
        StringBuilder query  = new StringBuilder();
        query.append(this.CREATE).append("`").append(schema)
                .append("`.")
                .append(name)
                .append(this.START);
        for(Field f : table.getFields()){
            query.append(this.getLine(f)).append(", ");
        }
        query.delete(query.lastIndexOf(","), query.length());
        query.append(END);
        
        return query.toString();
    }
    
    public String createQuery(Table table, String schema, String tableName){
        
//        String name = table.getName();
        StringBuilder query  = new StringBuilder();
        query.append(this.CREATE).append("`").append(schema)
                .append("`.")
                .append(tableName)
                .append(this.START);
        for(Field f : table.getFields()){
            query.append(this.getLine(f)).append(", ");
        }
        query.delete(query.lastIndexOf(","), query.length());
        query.append(END);
        
        return query.toString();
    }
    
    private String getLine(Field f){
        String name = f.getName() + " ";
        String type = f.getType() + " ";
        String Null = f.isIsNull()?"":" NOT NULL ";
        String unique = f.isUnique()?" UNIQUE ":"";
        String primary = f.isPrimary()?" PRIMARY KEY ":"";
        String Default;
        if(type.contains("enum") && type.contains("'")){
            Default = f.getDefaultValue()!=null? "DEFAULT '" + f.getDefaultValue() + "' ":"";
        }
        else
            Default = f.getDefaultValue()!=null? "DEFAULT " + f.getDefaultValue() + " ":"";
        String extra = f.getExtra(); 
        return name + type + Null + primary + Default + extra;
    }
}