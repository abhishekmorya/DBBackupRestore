/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.lib.master;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import src.lib.dao.DBDetailsImpl;
import src.lib.dao.DBDetailsInter;
import src.lib.dao.DataReaderImpl;
import src.lib.dao.DataReaderInter;
import src.lib.modals.Table;
import src.lib.service.ConnDetail;
import src.lib.service.DBConnection;
import src.lib.service.Path;
import src.lib.service.Status;

/**
 *
 * @author ASUS
 */
public class BackupMaster {

    /**
     * Backup the current schema to selected folder
     */
    public void backupSchema() {
        JSONObject object = new JSONObject();
        Connection conn = null;
        DBDetailsInter inter = new DBDetailsImpl();
        DBConnection dbc = new DBConnection();
        FileMaster fileMaster = new FileMasterImpl();
        try {
            conn = dbc.getConnection(ConnDetail.getForName(),
                    ConnDetail.getUrl(),
                    ConnDetail.getUser(),
                    ConnDetail.getPass());
            object = inter.getSchemaDetails(conn);
            String path = Path.getOutputPath().isEmpty() ? Path.getDefaultPath() : Path.getOutputPath();
            fileMaster.createOutputFolder(path);
//            fileMaster.insertSchemaDetails();
            Status.setStatus(true);
            Status.setMessage("Schema details are backed up");
        } catch (SQLException ex) {
            Logger.getLogger(BackupMaster.class.getName()).log(Level.SEVERE, null, ex);
            Status.setStatus(false);
            Status.setMessage("Database Exception: " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(BackupMaster.class.getName()).log(Level.SEVERE, null, ex);
            Status.setStatus(false);
            Status.setMessage("IOException: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BackupMaster.class.getName()).log(Level.SEVERE, null, ex);
            Status.setStatus(false);
            Status.setMessage("Class Not Found: " + ex.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BackupMaster.class.getName()).log(Level.SEVERE, null, ex);
                    Status.setStatus(false);
                    Status.setMessage("SQLException: " + ex.getMessage());
                }
            }
        }
    }

    /**
     * Backup the table details to the .table file to the defined path. The
     * tables will be taken from the list of tables provided and if that is
     * available in the schema. If the any of the table is not available in the
     * schema then none of the table will be backed up.
     *
     * @param schema
     * @param table
     */
    public void backupTableDetails(List<Table> table) {
        Connection conn = null;
        DBDetailsInter inter = new DBDetailsImpl();
        DBConnection dbc = new DBConnection();
        FileMaster fileMaster = new FileMasterImpl();
        DataReaderInter dataReaderInter = new DataReaderImpl();
        JSONObject object = null;
        try {
            conn = dbc.getConnection(ConnDetail.getForName(),
                    ConnDetail.getUrl(),
                    ConnDetail.getUser(),
                    ConnDetail.getPass());
            if (table.size() == 0) {
                Status.setStatus(false);
                Status.setMessage("No table present for backup");
            }
            String path = null;
            if (Path.getOutputPath().isEmpty()) {
                path = Path.getDefaultPath();
            } else {
                path = Path.getOutputPath();
            }
            fileMaster.createOutputFolder();
            
            fileMaster.createSchemaFile(false, false);
            fileMaster.createFieldsFile(table.get(0).getSchemaName().trim(), false);
            for (Table t : table) {
                object = (JSONObject)dataReaderInter.extractData(conn, t.getSchemaName().trim(), t);
                fileMaster.insertTableDetails(t);
                
                fileMaster.insertTableFields(object, false);
                fileMaster.insertSchemaDetails(t.getSchemaName().trim(), false);
            }
            Status.setStatus(true);
            Status.setMessage("Table details are backed up");
        } catch (SQLException ex) {
            Logger.getLogger(BackupMaster.class.getName()).log(Level.SEVERE, null, ex);
            Status.setStatus(false);
            Status.setMessage("Database Exception: " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(BackupMaster.class.getName()).log(Level.SEVERE, null, ex);
            Status.setStatus(false);
            Status.setMessage("IOException: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BackupMaster.class.getName()).log(Level.SEVERE, null, ex);
            Status.setStatus(false);
            Status.setMessage("Class Not Found: " + ex.getMessage());
        } catch (ParseException ex) {
            Logger.getLogger(BackupMaster.class.getName()).log(Level.SEVERE, null, ex);
            Status.setStatus(false);
            Status.setMessage("File foramat mismatch: " + ex.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BackupMaster.class.getName()).log(Level.SEVERE, null, ex);
                    Status.setStatus(false);
                    Status.setMessage("SQLException: " + ex.getMessage());
                }
            }
        }
    }

    /**
     * The data contained by the tables will be backed up on the selected path.
     * If any of the tables is not present then none of it is considered i.e.
     * data of none of the table is backed up. Data is backed up as .JSON file
     * and for each table there is a separate file. File name will be
     * table_name.JSON if table_name is the name of the table.
     *
     * @param schema
     * @param table
     */
    public void backupTableData(String schema, List<Table> table) {
        Connection conn = null;
        DataReaderInter inter = new DataReaderImpl();
        DBConnection dbc = new DBConnection();
        FileMaster fileMaster = new FileMasterImpl();
        try {
            conn = dbc.getConnection(ConnDetail.getForName(),
                    ConnDetail.getUrl(),
                    ConnDetail.getUser(),
                    ConnDetail.getPass());
            if (table.isEmpty()) {
                Status.setStatus(false);
                Status.setMessage("No table present for backup");
            }
            String path = Path.getOutputPath().isEmpty() ? Path.getDefaultPath() : Path.getOutputPath();
            fileMaster.createOutputFolder(path);
            // Searching for table details in Table details folder.
            for (Table t : table) {
                if (!fileMaster.searchTable(schema, t)) {
                    Status.setStatus(false);
                    Status.setMessage("Table Details are not available");
                    return;
                }
            }
            for (Table t : table) {
                fileMaster.insertTableData(inter.extractData(conn, schema, t));
            }
            Status.setStatus(true);
            Status.setMessage("Table data is backed up");
        } catch (SQLException ex) {
            Logger.getLogger(BackupMaster.class.getName()).log(Level.SEVERE, null, ex);
            Status.setStatus(false);
            Status.setMessage("Database Exception: " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(BackupMaster.class.getName()).log(Level.SEVERE, null, ex);
            Status.setStatus(false);
            Status.setMessage("IOException: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BackupMaster.class.getName()).log(Level.SEVERE, null, ex);
            Status.setStatus(false);
            Status.setMessage("Class Not Found: " + ex.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BackupMaster.class.getName()).log(Level.SEVERE, null, ex);
                    Status.setStatus(false);
                    Status.setMessage("SQLException: " + ex.getMessage());
                }
            }
        }
    }

    /**
     *
     * @param schema
     * @param table
     */
    public void backupTableDataBin(String schema, List<Table> table) {
        Connection conn = null;
        DataReaderInter inter = new DataReaderImpl();
        DBConnection dbc = new DBConnection();
        FileMaster fileMaster = new FileMasterImpl();
        try {
            conn = dbc.getConnection(ConnDetail.getForName(),
                    ConnDetail.getUrl(),
                    ConnDetail.getUser(),
                    ConnDetail.getPass());
            if (table.isEmpty()) {
                Status.setStatus(false);
                Status.setMessage("No table present for backup");
            }
            String path = Path.getOutputPath().isEmpty() ? Path.getDefaultPath() : Path.getOutputPath();
            fileMaster.createOutputFolder(path);
            // Searching for table details in Table details folder.
            for (Table t : table) {
                if (!fileMaster.searchTable(schema, t)) {
                    Status.setStatus(false);
                    Status.setMessage("Table Details are not available");
                    return;
                }
            }
            for (Table t : table) {
                fileMaster.insertTableBin(inter.extractBin(conn, schema, t));
            }
            Status.setStatus(true);
            Status.setMessage("Table data is backed up");
        } catch (SQLException ex) {
            Logger.getLogger(BackupMaster.class.getName()).log(Level.SEVERE, null, ex);
            Status.setStatus(false);
            Status.setMessage("Database Exception: " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(BackupMaster.class.getName()).log(Level.SEVERE, null, ex);
            Status.setStatus(false);
            Status.setMessage("IOException: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BackupMaster.class.getName()).log(Level.SEVERE, null, ex);
            Status.setStatus(false);
            Status.setMessage("Class Not Found: " + ex.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BackupMaster.class.getName()).log(Level.SEVERE, null, ex);
                    Status.setStatus(false);
                    Status.setMessage("SQLException: " + ex.getMessage());
                }
            }
        }
    }

    public void backupTableDataDat(String schema, List<Table> table) {
        Connection conn = null;
        DataReaderInter inter = new DataReaderImpl();
        DBConnection dbc = new DBConnection();
        JSONObject object = null;
        FileMaster fileMaster = new FileMasterImpl();
        try {
            conn = dbc.getConnection(ConnDetail.getForName(),
                    ConnDetail.getUrl(),
                    ConnDetail.getUser(),
                    ConnDetail.getPass());
            if (table.isEmpty()) {
                Status.setStatus(false);
                Status.setMessage("No table present for backup");
            }
            fileMaster.createOutputFolder();
            fileMaster.createFieldsFile(schema, true);
            fileMaster.createSchemaFile(true, false);
            fileMaster.insertSchemaDetails(schema, true);
            // Searching for table details in Table details folder.
            for (Table t : table) {
                if (!fileMaster.searchTable(schema, t)) {
                    Status.setStatus(false);
                    Status.setMessage("Table Details are not available");
                    return;
                }
            }
            for (Table t : table) {
                object = inter.extractData(conn, schema, t);
                fileMaster.insertTableFields(object, true);
                fileMaster.insertDataToDat(object);
            }
            Status.setStatus(true);
            Status.setMessage("Table data is backed up");
        } catch (SQLException ex) {
            Logger.getLogger(BackupMaster.class.getName()).log(Level.SEVERE, null, ex);
            Status.setStatus(false);
            Status.setMessage("Database Exception: " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(BackupMaster.class.getName()).log(Level.SEVERE, null, ex);
            Status.setStatus(false);
            Status.setMessage("IOException: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BackupMaster.class.getName()).log(Level.SEVERE, null, ex);
            Status.setStatus(false);
            Status.setMessage("Class Not Found: " + ex.getMessage());
        } catch (ParseException ex) {
            Logger.getLogger(BackupMaster.class.getName()).log(Level.SEVERE, null, ex);
            Status.setStatus(false);
            Status.setMessage("JSON Parse Exception: " + ex.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BackupMaster.class.getName()).log(Level.SEVERE, null, ex);
                    Status.setStatus(false);
                    Status.setMessage("SQLException: " + ex.getMessage());
                }
            }
        }
    }

    public void backupAllTables(String schema) {
        BackupMasterUtil masterUtil = new BackupMasterUtil();
        this.backupTableDetails(masterUtil.tables(schema));
    }

    public void backupAllTableData(String schema) {
        BackupMasterUtil masterUtil = new BackupMasterUtil();
        this.backupTableData(schema, masterUtil.tables(schema));
    }

    public void backup(String schema) {
        this.backupAllTables(schema);
        this.backupAllTableData(schema);
    }

    public void clearBackup(String schema, List<String> tableList, boolean dataFlag) {
        FileMaster fileMaster = new FileMasterImpl();
        try {
            for (String table : tableList) {
                fileMaster.deleteTableDetails(schema, table);
                Status.setMessage("Table " + table + " details deleted.");
                if (dataFlag) {
                    fileMaster.deleteTableData(schema, table);
                    Status.setMessage("Table " + table + " data deleted.");
                }
            }
            Status.setStatus(true);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(BackupMaster.class.getName()).log(Level.SEVERE, null, ex);
            Status.setMessage("Error: " + ex.getMessage());
            Status.setStatus(false);
        }
    }

}
