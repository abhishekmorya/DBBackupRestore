/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.lib.master;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import src.lib.dao.DBDetailsImpl;
import src.lib.dao.DBDetailsInter;
import src.lib.modals.Field;
import src.lib.modals.Schema;
import src.lib.modals.Table;
import src.lib.service.ConnDetail;
import src.lib.service.DBConnection;
import java.util.stream.Collectors;
import src.lib.service.Path;
import src.lib.service.Status;

/**
 *
 * @author ASUS
 */
public class BackupMasterUtil {
    private final String DB_DETAILS = "DBDetails";
    private final String DATA = "data";
    private final String DB_BACKUP = "DBBackup";
    private final String TABLE = "table";
    public Table table(String schema, String tableName){
        Connection conn = null;
        DBDetailsInter detailsInter = new DBDetailsImpl();
        DBConnection dbc = new DBConnection();
        Table table = null;
        try {
            conn = dbc.getConnection(ConnDetail.getForName(),
                    ConnDetail.getUrl(),
                    ConnDetail.getUser(),
                    ConnDetail.getPass());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BackupMasterUtil.class.getName()).log(Level.SEVERE, null, ex);
            Status.setMessage("Class not found.\nPlease check For Name.");
            Status.setStatus(false);
        } catch (SQLException ex) {
            Logger.getLogger(BackupMasterUtil.class.getName()).log(Level.SEVERE, null, ex);
            Status.setMessage("Database connection not established");
            Status.setStatus(false);
        }

        if (conn == null) {
            return null;
        }

        try {
            table = detailsInter.getTable(conn, schema, tableName);
            Status.setMessage("Table details fatched");
            Status.setStatus(true);
        } catch (SQLException ex) {
            Logger.getLogger(BackupMasterUtil.class.getName()).log(Level.SEVERE, null, ex);
            Status.setMessage("Error in fatching details from database: " + ex);
            Status.setStatus(false);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(BackupMasterUtil.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
        return table;
    }
    
    
    
    public List<Table> tables(String schema) {
        Connection conn = null;
        DBDetailsInter detailsInter = new DBDetailsImpl();
        DBConnection dbc = new DBConnection();
        List<Table> list = null;

        try {
            conn = dbc.getConnection(ConnDetail.getForName(),
                    ConnDetail.getUrl(),
                    ConnDetail.getUser(),
                    ConnDetail.getPass());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BackupMasterUtil.class.getName()).log(Level.SEVERE, null, ex);
            Status.setMessage("Class not found.\nPlease check For Name.");
            Status.setStatus(false);
        } catch (SQLException ex) {
            Logger.getLogger(BackupMasterUtil.class.getName()).log(Level.SEVERE, null, ex);
            Status.setMessage("Database connection not established");
            Status.setStatus(false);
        }

        if (conn == null) {
            return null;
        }

        try {
            list = detailsInter.tables(conn, schema);
            Status.setMessage("Table details fatched");
            Status.setStatus(true);
        } catch (SQLException ex) {
            Logger.getLogger(BackupMasterUtil.class.getName()).log(Level.SEVERE, null, ex);
            Status.setMessage("Error in fatching details from database: " + ex);
            Status.setStatus(false);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(BackupMasterUtil.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
        return list;
    }

    public List<String> tableList(String schema) {
        List<Table> list = this.tables(schema);
        if(list == null)
            return null;
        return list.stream().map(Table::getName).collect(Collectors.toList());
    }

    public List<Field> fields(String schema, String table) {
        Connection conn = null;
        DBDetailsInter detailsInter = new DBDetailsImpl();
        DBConnection dbc = new DBConnection();
        List<Field> list = null;

        try {
            conn = dbc.getConnection(ConnDetail.getForName(),
                    ConnDetail.getUrl(),
                    ConnDetail.getUser(),
                    ConnDetail.getPass());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BackupMasterUtil.class.getName()).log(Level.SEVERE, null, ex);
            Status.setMessage("Class not found.\nPlease check For Name.");
            Status.setStatus(false);
        } catch (SQLException ex) {
            Logger.getLogger(BackupMasterUtil.class.getName()).log(Level.SEVERE, null, ex);
            Status.setMessage("Database connection not established");
            Status.setStatus(false);
        }

        if (conn == null) {
            return null;
        }

        try {
            list = detailsInter.fields(conn, schema, table);
            Status.setMessage("Field details fatched");
            Status.setStatus(true);
        } catch (SQLException ex) {
            Logger.getLogger(BackupMasterUtil.class.getName()).log(Level.SEVERE, null, ex);
            Status.setMessage("Error in fatching details from database: " + ex);
            Status.setStatus(false);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(BackupMasterUtil.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
        return list;
    }

    public List<String> fieldList(String schema, String table) {
        List<Field> list = this.fields(schema, table);
        if(list == null)
            return null;
        return list.stream().map(Field::getName).collect(Collectors.toList());
    }

    public List<Schema> schemas() {
        Connection conn = null;
        DBDetailsInter detailsInter = new DBDetailsImpl();
        DBConnection dbc = new DBConnection();
        List<Schema> list = null;

        try {
            conn = dbc.getConnection(ConnDetail.getForName(),
                    ConnDetail.getUrl(),
                    ConnDetail.getUser(),
                    ConnDetail.getPass());
        } catch (SQLException ex) {
            Logger.getLogger(BackupMasterUtil.class.getName()).log(Level.SEVERE, null, ex);
            Status.setMessage("Database connection not established");
            Status.setStatus(false);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BackupMasterUtil.class.getName()).log(Level.SEVERE, null, ex);
            Status.setMessage("Class not found.\nPlease check For Name.");
            Status.setStatus(false);
        }
        if (conn == null) {
            return null;
        }
        try {
            list = detailsInter.getSchemas(conn);
            Status.setMessage("Schema List Fatched");
            Status.setStatus(true);
        } catch (SQLException ex) {
            Logger.getLogger(BackupMasterUtil.class.getName()).log(Level.SEVERE, null, ex);
            Status.setMessage("Error in fatching details from database: " + ex);
            Status.setStatus(false);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(BackupMasterUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return list;
    }

    public List<String> schemaList() {
        List<Schema> list = this.schemas();
        if(list == null)
            return null;
        return list.stream().map(Schema::getName).collect(Collectors.toList());
    }

    public boolean deleteTableBackup(String schema, String table) {
        String path = Path.getOutputPath() + this.DB_BACKUP + "\\";
        String tableDetail = path + this.DB_DETAILS + "\\" + schema + "\\" + table + "\\";
        String tableData = path + this.DATA + "\\" + schema + "\\" + table + "\\";
        String msg = "Table details and data not deleted";
        boolean status = false;
        File tableDetailFile = new File(tableDetail);
        File tableDataFile = new File(tableData);
        if(!tableDetailFile.exists()) {
            Status.setMessage("Selected schema or table detail file does not exist");
            Status.setStatus(false);
        }
        else if(!tableDataFile.exists()){
            Status.setMessage("Selected schema or table data file does not exist");
            Status.setStatus(false);
        } else {
            if(!tableDataFile.delete()) {
                msg = "Table data is not deleted";
                status  = false;
            } else if(!tableDetailFile.delete()){
                msg = "Table data is deleted but details are not deleted.";
                status = false;
            } else {
                msg = "Table data and details are deleted.";
                status = true;
            }
            Status.setMessage(msg);
            Status.setStatus(status);
        }
        return status;
    }
    
}
