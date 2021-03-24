/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.lib.restore.insert;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import src.lib.master.FileMaster;
import src.lib.master.FileMasterImpl;
import src.lib.modals.Field;
import src.lib.modals.Table;
import src.lib.service.DBDetails;

/**
 *
 * @author ASUS
 */
public class InsertTableData {
    private final String INSERT = "INSERT INTO ";
    private final String VALUES = " VALUES ";
    private final String START = " ( ";
    private final String END = " ) ";
    private final int BATCH_SIZE = 1000;
    private final String LOAD_QUERY_1 = "LOAD DATA LOCAL INFILE '";
    private final String LOAD_QUERY_2 = "' INTO TABLE ";
    
    public int loadTable(Connection conn, String schema, String table) throws SQLException{
        FileMaster fileMaster = new FileMasterImpl();
        String path = fileMaster.getTableDataPath(schema, table) + "\\" + table + ".dat";
        
        //LOAD DATA LOCAL INFILE 'C:/Users/ASUS/Documents/DBBackup/data/testdb/table2.dat' INTO TABLE table3;
        path = path.replace("\\", "/");
        String query = this.LOAD_QUERY_1 + path + this.LOAD_QUERY_2  + "`" + schema + "`." + table;
        PreparedStatement preparedStatement = null;
        int rows = -1;
        try{
        preparedStatement = conn.prepareStatement(query);
        rows  = preparedStatement.executeUpdate();
        } finally{
            if(preparedStatement != null){
                preparedStatement.close();
            }
        }
        return rows;
    }
    
    public int loadTable(Connection conn, String schema, String table, String schemaTo, String tableTo) throws SQLException{
        FileMaster fileMaster = new FileMasterImpl();
        String path = fileMaster.getTableDataPath(schema, table) + "\\" + table + ".dat";
        
        //LOAD DATA LOCAL INFILE 'C:/Users/ASUS/Documents/DBBackup/data/testdb/table2.dat' INTO TABLE table3;
        path = path.replace("\\", "/");
        String query = this.LOAD_QUERY_1 + path + this.LOAD_QUERY_2  + "`" + schemaTo + "`." + tableTo;
        System.out.println("query: "+ query);
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        int rows  = preparedStatement.executeUpdate();
        
        return rows;
    }
    
    public int insertData(Connection conn, Table table, JSONObject object) throws SQLException{
        String query = this.queryBuilder(table);
        PreparedStatement statement = conn.prepareStatement(query);
        JSONArray array = (JSONArray) object.get("data");
        JSONObject obj;
        int rows = -1;
        for(Object o : array){
            obj = (JSONObject) o;
            this.setData(statement, obj, table);
            rows = statement.executeUpdate();
        }
        return rows;
    }
    
    public int insertData(Connection conn, Table table, JSONObject object, String tableName) throws SQLException{
        String query = this.queryBuilder(table, tableName);
        PreparedStatement statement = conn.prepareStatement(query);
        JSONArray array = (JSONArray) object.get("data");
        
        JSONObject obj;
        int rows = -1;
        for(Object o : array){
            obj = (JSONObject) o;
            this.setData(statement, obj, table);
            rows = statement.executeUpdate();
        }
        return rows;
    }
    
    public int insertData(Connection conn, Table table, JSONObject object, String tableName, String schema) throws SQLException{
        String query = this.queryBuilder(table, tableName, schema);
        PreparedStatement statement = conn.prepareStatement(query);
        JSONArray array = (JSONArray) object.get("data");
        JSONObject obj;
        int rows = -1;
        for(Object o : array){
            obj = (JSONObject) o;
            this.setData(statement, obj, table);
            rows = statement.executeUpdate();
        }
        return rows;
    }
    
    public String queryBuilder(Table table){
        StringBuilder builder = new StringBuilder();
        builder.append(this.INSERT).append(" `").append(table.getSchemaName()).append("`.")
                .append(table.getName()).append(" ").append(this.START);
        
        table.getFields().forEach((f) -> {
            builder.append(f.getName()).append(", ");
        });
        builder.delete(builder.lastIndexOf(","), builder.length());
        builder.append(END).append(this.VALUES).append(this.START);
        table.getFields().forEach((f) -> {
            builder.append("?, ");
        });
        builder.delete(builder.lastIndexOf(","), builder.length());
        builder.append(END);
        
        return builder.toString();
    }
    
    public String queryBuilder(Table table, String tableName){
        StringBuilder builder = new StringBuilder();
        builder.append(this.INSERT).append(" `").append(table.getSchemaName()).append("`.")
                .append(tableName).append(" ").append(this.START);
        
        table.getFields().forEach((f) -> {
            builder.append(f.getName()).append(", ");
        });
        builder.delete(builder.lastIndexOf(","), builder.length());
        builder.append(END).append(this.VALUES).append(this.START);
        table.getFields().forEach((f) -> {
            builder.append("?, ");
        });
        builder.delete(builder.lastIndexOf(","), builder.length());
        builder.append(END);
        
        return builder.toString();
    }
    
    public String queryBuilder(Table table, String tableName, String schema){
        StringBuilder builder = new StringBuilder();
        builder.append(this.INSERT).append(" `").append(schema).append("`.")
                .append(tableName).append(" ").append(this.START);
        
        table.getFields().forEach((f) -> {
            builder.append(f.getName()).append(", ");
        });
        builder.delete(builder.lastIndexOf(","), builder.length());
        builder.append(END).append(this.VALUES).append(this.START);
        table.getFields().forEach((f) -> {
            builder.append("?, ");
        });
        builder.delete(builder.lastIndexOf(","), builder.length());
        builder.append(END);
        
        return builder.toString();
    }
    
    private boolean setData(PreparedStatement statement, JSONObject obj, Table table) throws SQLException {
        List<Field> f = table.getFields();
        for(int i = 0; i < f.size(); i++){
            this.parseForBulk(statement, (i + 1), f.get(i).getType(), obj.get(f.get(i).getName()));
            
        }
        return true;
    }

    private void parse(PreparedStatement ps, int index, String type, String value) throws SQLException {
        String tt = type.contains("(")?type.substring(0, type.indexOf("(")).toUpperCase():type.toUpperCase();
        
        switch(tt){
            case DBDetails.CT_VARCHAR:
                ps.setString(index, value);
                break;
            case DBDetails.CT_CHAR:
                ps.setString(index, value);
                break;
            case DBDetails.CT_TINYTEXT:
                ps.setString(index, value);
                break;
            case DBDetails.CT_TEXT:
                ps.setString(index, value);
                break;
            case DBDetails.CT_MEDIUMTEXT:
                ps.setString(index, value);
                break;
            case DBDetails.CT_LONGTEXT:
                ps.setString(index, value);
                break;
            case DBDetails.CT_TINYINT:
                ps.setInt(index, Integer.parseInt(value));
                break;
            case DBDetails.CT_SMALLINT:
                ps.setInt(index, Integer.parseInt(value));
                break;
            case DBDetails.CT_MEDIUMINT:
                ps.setInt(index, Integer.parseInt(value));
                break;
            case DBDetails.CT_INT:
                ps.setInt(index, Integer.parseInt(value));
                break;
            case DBDetails.CT_BIGINT:
                ps.setLong(index, Long.parseLong(value));
                break;
            case DBDetails.CT_FLOAT:
                ps.setFloat(index, Float.parseFloat(value));
                break;
            case DBDetails.CT_DOUBLE:
                ps.setDouble(index, Double.parseDouble(value));
                break;
            case DBDetails.CT_DECIMAL:
                ps.setDouble(index, Double.parseDouble(value));
                break;
            case DBDetails.CT_DATE:
                ps.setDate(index, ParseData.getDate(value));
                break;
            case DBDetails.CT_DATETIME:
                ps.setDate(index, ParseData.getDate(value));
                break;
            case DBDetails.CT_TIMESTAMP:
                ps.setTimestamp(index, ParseData.toTimestamp(value));
                break;
            case DBDetails.CT_TIME:
                ps.setTime(index, ParseData.getTime(value));
                break;
//            case DBDetails.CT_BLOB:
//                return this.blobToString(rs.getBlob(label));  
        }
        
    }
    public int bulkInsertData(Connection conn, Table table, JSONObject object, String tableName, String schema) throws SQLException{
        String query = this.queryBuilder(table, tableName, schema);
        PreparedStatement statement = conn.prepareStatement(query);
        JSONArray array = (JSONArray) object.get("data");
        JSONObject obj;
        int rows[] = null;
        int i = 0;
        for(Object o : array){
            obj = (JSONObject) o;
            this.setData(statement, obj, table);
            statement.addBatch();
            if(i % 1000 == 0 || i == array.size())
                rows = statement.executeBatch();
        }
        return rows.length;
    }
    
    private void parseForBulk(PreparedStatement ps, int index, String type, Object value) throws SQLException {
        String tt = type.contains("(")?type.substring(0, type.indexOf("(")).toUpperCase():type.toUpperCase();
        
        switch(tt){
            case DBDetails.CT_VARCHAR:
                ps.setString(index, (String) value);
                break;
            case DBDetails.CT_CHAR:
                ps.setString(index, ((char)value) + "");
                break;
            case DBDetails.CT_TINYTEXT:
                ps.setString(index, (String) value);
                break;
            case DBDetails.CT_TEXT:
                ps.setString(index, (String) value);
                break;
            case DBDetails.CT_MEDIUMTEXT:
                ps.setString(index, (String) value);
                break;
            case DBDetails.CT_LONGTEXT:
                ps.setString(index, (String) value);
                break;
            case DBDetails.CT_TINYINT:
                ps.setInt(index, (int) value);
                break;
            case DBDetails.CT_SMALLINT:
                ps.setInt(index, (int) value);
                break;
            case DBDetails.CT_MEDIUMINT:
                ps.setInt(index, (int) value);
                break;
            case DBDetails.CT_INT:
                ps.setInt(index, (int) value);
                break;
            case DBDetails.CT_BIGINT:
                ps.setLong(index, (long) value);
                break;
            case DBDetails.CT_FLOAT:
                ps.setFloat(index, (float) value);
                break;
            case DBDetails.CT_DOUBLE:
                ps.setDouble(index, (double) value);
                break;
            case DBDetails.CT_DECIMAL:
                ps.setDouble(index, (double) value);
                break;
            case DBDetails.CT_DATE:
                ps.setDate(index, (Date) value);
                break;
            case DBDetails.CT_DATETIME:
                ps.setDate(index, (Date) value);
                break;
            case DBDetails.CT_TIMESTAMP:
                ps.setTimestamp(index, (Timestamp) value);
                break;
            case DBDetails.CT_TIME:
                ps.setTime(index, (Time) value);
                break;
            case DBDetails.CT_BLOB:
                ps.setBlob(index, (Blob) value);
        }
        
    }

    public String bulkQueryBuilder(Table table, String tableName, String schema) {
        StringBuilder builder = new StringBuilder();
        builder.append(this.INSERT).append(" `").append(schema).append("`.")
                .append(tableName).append(" ").append(this.START);
        
        
        table.getFields().forEach((f) -> {
            builder.append("?, ");
        });
        builder.delete(builder.lastIndexOf(","), builder.length());
        builder.append(END);
        
        return builder.toString();
    }
}
