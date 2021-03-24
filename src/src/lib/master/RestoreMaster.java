/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.lib.master;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;
import src.lib.dao.DBDetailsImpl;
import src.lib.dao.DBDetailsInter;
import src.lib.modals.Table;
import src.lib.restore.create.CreateSchema;
import src.lib.restore.create.CreateTable;
import src.lib.restore.insert.InsertTableData;
import src.lib.service.ConnDetail;
import src.lib.service.DBConnection;
import src.lib.service.Path;
import src.lib.service.Status;


/**
 *
 * @author ASUS
 */
public class RestoreMaster {
    public void restoreDetails(String schema, List<String> tables){
        Connection conn = null;
        DBDetailsInter inter = new DBDetailsImpl();
        DBConnection dbc = new DBConnection();
        FileMaster fileMaster = new FileMasterImpl();
        CreateTable createTable = new CreateTable();
        CreateSchema createSchema = new CreateSchema();
        Table table = null;
        try {
            conn = dbc.getConnection(ConnDetail.getForName(),
                    ConnDetail.getUrl(),
                    ConnDetail.getUser(),
                    ConnDetail.getPass());
            if(tables.isEmpty()){
                Status.setStatus(false);
                Status.setMessage("None of the table is selected.");
                return;
            }
            
            for(String tab : tables){
                if(!fileMaster.searchTable(schema, tab)){
                    Status.setStatus(false);
                    Status.setMessage("Table " + tab + " detail is not present on selected input path\n"
                            + "Can not proceed with backup");
                    return;
                }
            }
            int i = 0;
            
            if(!createSchema.createSchema(conn, schema)){
                Status.setStatus(false);
                Status.setMessage(schema + " has not been created.");
                return;
            }
            
            for(String tab : tables){
                table = fileMaster.readTableDetails(schema, tab);
                createTable.createTable(conn, table, schema, tab);
                i++;
            }
            
            Status.setStatus(true);
            Status.setMessage(i + "of " + tables.size() + " tables are created in schema " + schema + " successfully");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RestoreMaster.class.getName()).log(Level.SEVERE, null, ex);
            Status.setStatus(false);
            Status.setMessage("Class Not Found Exception: " + ex.getMessage());
        } catch (SQLException ex) {
            Logger.getLogger(RestoreMaster.class.getName()).log(Level.SEVERE, null, ex);
            Status.setStatus(false);
            Status.setMessage("Database Exception: " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(RestoreMaster.class.getName()).log(Level.SEVERE, null, ex);
            Status.setStatus(false);
            Status.setMessage("File Exception: " + ex.getMessage());
        } finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RestoreMaster.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void restoreData(String schema, List<String> tables){
        Connection conn = null;
        InsertTableData data = new InsertTableData();
        DBConnection dbc = new DBConnection();
        FileMaster fileMaster = new FileMasterImpl();
        try {
            conn = dbc.getConnection(ConnDetail.getForName(),
                    ConnDetail.getUrl(),
                    ConnDetail.getUser(),
                    ConnDetail.getPass());
            if(tables.isEmpty()){
                Status.setMessage("None of tables is selected.");
                Status.setStatus(false);
                return;
            }
            
            for(String tab : tables){
                if(!fileMaster.searchDataTable(schema, tab)){
                    Status.setStatus(false);
                    Status.setMessage("Table " + tab + " data file is not present on selected input path\n"
                            + "Can not proceed with backup");
                    return;
                }
            }
            int i = 0;
            for(String tab : tables){
                data.loadTable(conn, schema, tab);
                i++;
            }
            Status.setMessage("Data for " + i + "of " + tables.size() + " tables has been inserted in schema " 
                    + schema + " successfully");
            Status.setStatus(true);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RestoreMaster.class.getName()).log(Level.SEVERE, null, ex);
            Status.setStatus(false);
            Status.setMessage("Class not found: " + ex.getMessage());
        } catch (SQLException ex) {
            Logger.getLogger(RestoreMaster.class.getName()).log(Level.SEVERE, null, ex);
            Status.setMessage("Database Exception: " + ex.getMessage());
            Status.setStatus(false);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RestoreMaster.class.getName()).log(Level.SEVERE, null, ex);
            Status.setMessage("File not found: " + ex.getMessage());
            Status.setStatus(false);
        }
        
    }
    
    public List<String> tableFiles(String schema, boolean isData){
        List<String> list = new ArrayList<>();
        String path;
        if(isData)
            path = Path.getOutputPath() + "DB_Backup" + "\\" + "data\\" + schema + "\\";
        else
            path = Path.getOutputPath() + "DB_Backup" + "\\" + "DBDetails\\" + schema + "\\";
        File file = new File(path);
        File[] files = file.listFiles();
        for(File f : files){
            if(f.isFile())
                list.add(f.getName());
        }
        return list;
    }
    
    public List<String> schemaDirs(boolean isData){
        List<String> list = new ArrayList<>();
        String path;
        if(isData)
            path = Path.getOutputPath() + "DB_Backup" + "\\" + "data\\";
        else
            path = Path.getOutputPath() + "DB_Backup" + "\\" + "DBDetails\\";
        File file = new File(path);
        String[] files = file.list();
        for(String f : files){
            list.add(f);
        }
        return list;
    }
    
    public List<String> schemaList(boolean isData){
        List<String> list = new ArrayList<>();
        FileMaster fileMaster = new FileMasterImpl();
        try {
            list = fileMaster.getSchemas(isData);
            Status.setMessage("Schema list fetched.");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RestoreMasterUtil.class.getName()).log(Level.SEVERE, null, ex);
            Status.setMessage("File not found: " + ex.getMessage());
        } catch (ParseException ex) {
            Logger.getLogger(RestoreMasterUtil.class.getName()).log(Level.SEVERE, null, ex);
            Status.setMessage("JSON File format: " + ex.getMessage());
        }
        return list;
    }
    
    public List<String> tableList(String schema, boolean isData){
        List<String> list = new ArrayList<>();
        FileMaster fileMaster = new FileMasterImpl();
        try {
            list = fileMaster.getTables(schema, isData);
            Status.setMessage("Schema list fetched.");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RestoreMasterUtil.class.getName()).log(Level.SEVERE, null, ex);
            Status.setMessage("File not found: " + ex.getMessage());
        } catch (ParseException ex) {
            Logger.getLogger(RestoreMasterUtil.class.getName()).log(Level.SEVERE, null, ex);
            Status.setMessage("JSON File format: " + ex.getMessage());
        }
        return list;
    }
}
