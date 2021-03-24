/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.lib.master;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import src.lib.modals.Table;

/**
 *
 * @author ASUS
 */
public interface FileMaster {
    public boolean createOutputFolder() throws IOException, FileNotFoundException;
    
    /**
     * Creates a folder named DBBackup on the given location
     * In DBBackup folder, two other folders named DBDetails and data are also
     * created.
     * DBDetails folder will contain two types of files:
     * 1) TABLENAME.table 
     *    where TABLENAME is the name of the table.
     *  The table description object of table TABLENAME is stored in this file.
     *  Every table in schema have the file for getting the full description of 
     *  table.
     * 2) schemas.json
     *  This file contains the schema names of DB and some information like numbers
     *  of tables in that schema etc.
     * data folder contains table data in json file having file name same as the
     * table name containing the data.
     * 
     * @param path
     * @return 
     * @throws java.io.IOException 
     * @throws java.io.FileNotFoundException 
     */
    public boolean createOutputFolder(String path) throws IOException, FileNotFoundException;
    
    public boolean insertTableDetails(Table table) throws IOException, FileNotFoundException, ClassNotFoundException;
    
    public boolean insertTableData(JSONObject tableData) throws IOException, FileNotFoundException;
    
    public boolean insertTableBin(JSONObject tableBin) throws IOException, FileNotFoundException;
    
    public boolean insertSchemaDetails(String schema, boolean isData) throws IOException, FileNotFoundException, ParseException;
    
    public boolean deleteTableDetails(Table table) throws IOException, FileNotFoundException, ClassNotFoundException;
    
    public boolean deleteTableDetails(String schema, String table) throws IOException, FileNotFoundException, ClassNotFoundException;
     
    public boolean deleteTableData(JSONObject tableData) throws IOException, FileNotFoundException;
    
    public boolean deleteTableData(String schema, String table) throws IOException, FileNotFoundException, ClassNotFoundException;
    
    public boolean deleteSchemaDetails() throws IOException, FileNotFoundException;
    
    public boolean updateTableDetails(Table table) throws IOException, FileNotFoundException, ClassNotFoundException;
    
    public boolean updateTableData(JSONObject tableData) throws IOException, FileNotFoundException;
    
    public boolean updateSchemaDetails(String schema) throws IOException, FileNotFoundException, ParseException;
    
    public boolean deleteOutputFolder() throws FileNotFoundException;
    
    public boolean searchTable(String schema, Table table) throws FileNotFoundException;
    
    public boolean searchTable(String schema, String table);
    
    public boolean searchDataTable(String schema, String table) throws FileNotFoundException;
    
    public String getTableDetailPath(String schema, String table);
    
    public String getTableDataPath(String schema, String table);
    
    public Table readTableDetails(String schema, String table) throws FileNotFoundException, IOException, ClassNotFoundException;
    
    public JSONObject readTableData(String schema, String table) throws FileNotFoundException, ParseException;
    
    public JSONObject readTableDataBin(String schema, String table) throws FileNotFoundException, ParseException, IOException, ClassNotFoundException;
    
    public boolean insertTableFields(JSONObject object, boolean isData) throws FileNotFoundException, ParseException, IOException;
    
    public File createFieldsFile(String schema, boolean isData) throws IOException;
    
    public void insertDataToDat(JSONObject object) throws FileNotFoundException, ParseException, IOException;
    
    public File createSchemaFile(boolean isData, boolean replace) throws IOException;
    
    public List<String> getSchemas(boolean isData) throws FileNotFoundException, ParseException;
    
    public List<String> getTables(String schema, boolean isData) throws FileNotFoundException, ParseException;
}
