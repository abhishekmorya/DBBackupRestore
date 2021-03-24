/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.lib.master;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import src.lib.modals.Table;
import src.lib.service.Path;

/**
 *
 * @author ASUS
 */
public class FileMasterImpl implements FileMaster, Serializable {

    private final String DB_DETAILS = "DBDetails";
    private final String DATA = "data";
    private final String DB_BACKUP = "DBBackup";
    private final String TABLE = ".table";
    private final String JSON = ".JSON";
    private final String BSON = ".BJSON";
    private final String TABLE_FIELD = "TableField.JSON";
    private final String SCHEMA_FILE = "Schema.JSON";
    private final String DAT = ".dat";
    

    @Override
    public boolean createOutputFolder() throws IOException, FileNotFoundException {
        return this.createOutputFolder(Path.getOutputPath());
    }

    @Override
    public boolean createOutputFolder(String path) throws IOException, FileNotFoundException {
        File file = new File(path + this.DB_BACKUP);
        if (!file.exists()) {
            if (!file.mkdir()) {
                return false;
            }
        }
        file = new File(path + "\\" + this.DB_BACKUP + "\\" + this.DB_DETAILS);
        if (!file.exists()) {
            if (!file.mkdir()) {
                return false;
            }
        }

        file = new File(path + "\\" + this.DB_BACKUP + "\\" + this.DATA);
        if (!file.exists()) {
            if (!file.mkdir()) {
                return false;
            }
        }
        Path.setOutputPath(path);
        System.out.println("Parent: " + Path.getOutputPath());
        return true;
    }

    public File createFile(String path, String fileName) throws IOException{
        File file = new File(path);
        if(!file.exists()){
            file.mkdir();
        }
        file = new File(path + fileName);
        if(!file.exists()){
            file.createNewFile();
        }
        return file;
    }
    
    public File createTableFieldFile(String schema) throws IOException {
        String path = Path.getOutputPath() + "\\" + this.DB_BACKUP + "\\" + this.DATA + "\\" + schema 
                + "\\" + this.TABLE_FIELD;
        File file = new File(path);
        if(file.exists())
            return file;
        JSONObject obj = new JSONObject();
        JSONObject object = new JSONObject();
        obj.put("data", object);
        FileWriter writer = new FileWriter(file);
        writer.write(obj.toJSONString());
        writer.flush();
        writer.close();
        return file;
    }
    
    @Override
    public boolean insertTableDetails(Table table) throws IOException, FileNotFoundException, ClassNotFoundException {
        if (Path.getOutputPath() == null) {
            return false;
        }
        String fileName = table.getName() + this.TABLE;
        String path = Path.getOutputPath() + "\\" + this.DB_BACKUP + "\\" + this.DB_DETAILS + "\\" + table.getSchemaName() + "\\";
        this.createFile(path, fileName);
        FileOutputStream stream = new FileOutputStream(path + fileName);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(stream);
        objectOutputStream.writeObject(table);
        stream.close();
        objectOutputStream.close();
        return true;
    }

    @Override
    public boolean insertTableData(JSONObject tableData) throws IOException, FileNotFoundException {
        JSONObject desc = (JSONObject) tableData.get("desc");
        String filename = (String) desc.get("TableName") + this.JSON;
        String schemaName = (String) desc.get("SchemaName");
        String path = Path.getOutputPath() + "\\" + this.DB_BACKUP + "\\" + this.DATA + "\\" + schemaName + "\\";
        File file = this.createFile(path, filename);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(tableData.toJSONString());
            writer.flush();
            writer.close();
        }
        return true;
    }
    
    /**
     * To insert the fields in Table_fields file
     * object parameter is JSON Object for that table which also contain in key 'data' and description of table in 'desc'
     * key.
     * 
     * 
     * @param object
     * @param isData
     * @return
     * @throws FileNotFoundException
     * @throws ParseException 
     */
    @Override
    public boolean insertTableFields(JSONObject object, boolean isData) throws FileNotFoundException, ParseException, IOException{
        JSONObject desc = (JSONObject) object.get("desc");
        JSONArray array = (JSONArray) desc.get("Fields");
        String path;
        if(isData)
            path = Path.getOutputPath() + this.DB_BACKUP + "\\" + this.DATA + "\\" + desc.get("SchemaName") + "\\"
                + this.TABLE_FIELD;
        else
            path = Path.getOutputPath() + this.DB_BACKUP + "\\" + this.DB_DETAILS + "\\" + desc.get("SchemaName") + "\\"
                + this.TABLE_FIELD;
        File file = new File(path);
        JSONObject tableField = this.getJSONObject(path);
        JSONObject fields = (JSONObject) tableField.get("data");
        JSONObject field = new JSONObject();
        JSONObject f = null;
        int i = 1;
        for(Object o : array){
            f = (JSONObject) o;
            field.put(i, f.get("Name"));
            i++;
        }
        fields.put(desc.get("TableName"), field);
        tableField.put("data", fields);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(tableField.toJSONString());
            writer.flush();
        }
        return true;
    }    
    
    public JSONObject getJSONObject(String path) throws FileNotFoundException, ParseException{
        JSONObject object = null;
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        JSONParser parser = new JSONParser();
        object = (JSONObject) parser.parse(scanner.nextLine());
        return object;
    }
    
    public File createDatFile(String schema, String table) throws IOException {
        String path = this.getDataPath(schema, table) + "\\" + table + this.DAT;
        File file = new File(path);
        
        if(file.exists()){
            System.out.println("File already exists");
            file.delete();
        }
        file.createNewFile();
        if(!file.exists()){
            System.out.println("File not created at specified location");
            return null;
        }
        return file;
    }
    
    @Override
    public File createFieldsFile(String schema, boolean isData) throws IOException{
        String path;
        if(isData)
            path = Path.getOutputPath() + this.DB_BACKUP + "\\" + this.DATA + "\\"
                + schema;
        else
            path = Path.getOutputPath() + this.DB_BACKUP + "\\" + this.DB_DETAILS + "\\"
                + schema;
        JSONObject object = new JSONObject();
        File file = new File(path);
        if(!file.exists())
            file.mkdirs();
        file = new File(path + "\\" + this.TABLE_FIELD);
        if(!file.exists())
            file.createNewFile();
        
        try (FileWriter writer = new FileWriter(file)) {
            JSONObject obj = new JSONObject();
            object.put("data", obj);
            writer.write(object.toJSONString());
            if(!file.exists()){
                System.out.println("File not created at specified location");
                return null;
            }
            writer.flush();
        }
        return file;
    }
    
    /**
     * Meaning of parameters used.
     * If isData is true then file is being created in /DATA folder otherwise in /DBDETAILS folder.
     * If replace is true then it will delete the existing file and will create a new one. Otherwise
     * it will create a new one only if it not exists. It won't create the file if it already exists when replace
     * is false.
     * 
     * @param isData
     * @param replace
     * @return
     * @throws IOException 
     */
    
    @Override
    public File createSchemaFile(boolean isData, boolean replace) throws IOException{
        String path;
        JSONObject object = new JSONObject();
        JSONObject schemas = new JSONObject();
        if(isData)
            path = Path.getOutputPath() + this.DB_BACKUP + "\\" + this.DATA + "\\";
        else
            path = Path.getOutputPath() + this.DB_BACKUP + "\\" + this.DB_DETAILS + "\\";
        
        File file = new File(path);        
        if(!file.exists())
            file.mkdirs();
        file = new File(path + "\\" + this.SCHEMA_FILE);
        if(file.exists() && replace){
            file.delete();
            file.createNewFile();
            object.put("data", schemas);
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(object.toJSONString());
                writer.flush();
            }
        } else if(!file.exists()){
            file.createNewFile();
            object.put("data", schemas);
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(object.toJSONString());
                writer.flush();
            }
        } else{
            return file;
        }
        
        return file;
    }
    
    @Override
    public void insertDataToDat(JSONObject object) throws FileNotFoundException, ParseException, IOException{
        JSONArray array = (JSONArray) object.get("data");
        JSONObject desc = (JSONObject) object.get("desc");
        
        String schema = (String) desc.get("SchemaName");
        String table = (String) desc.get("TableName");
        
        File file = this.createDatFile(schema, table);
        FileWriter writer = new FileWriter(file);
        JSONObject obj = null;
        StringBuilder line = new StringBuilder();
        int i = 0, fieldIndex = 1, size = array.size();
        
        JSONObject tables = (JSONObject) this.getJSONObject(this.getDataPath(schema, table) + "\\" + this.TABLE_FIELD)
                .get("data");
        JSONObject fields = (JSONObject) tables.get(table.toLowerCase());
        int fieldCount = fields.size();
        for(Object o : array){
            obj = (JSONObject) o;
            
            while(fieldIndex <= fieldCount){
                line.append(obj.get(fields.get(fieldIndex+""))).append("\t");
                fieldIndex++;
            }
            line.append("\n");
            writer.write(line.toString());
            line = new StringBuilder();
            i++;
            fieldIndex = 1;
            writer.flush();
        }
        System.out.println("rows in file: " + i);
        writer.close();
    }
    
    @Override
    public boolean insertTableBin(JSONObject tableBin) throws IOException, FileNotFoundException {
//        JSONObject desc = (JSONObject) tableBin.get("desc");
        Table table = (Table) tableBin.get("desc");
        String filename = table.getName() + this.BSON;
        String schemaName = table.getSchemaName();
        String path = Path.getOutputPath() + "\\" + this.DB_BACKUP + "\\" + this.DATA + "\\" + schemaName + "\\";
        File file = this.createFile(path, filename);
        ObjectOutputStream objectOutputStream = null;
        try (FileOutputStream stream = new FileOutputStream(path + filename)) {
            objectOutputStream = new ObjectOutputStream(stream);
            objectOutputStream.writeObject(tableBin);
        } finally{
            if(objectOutputStream != null)
                objectOutputStream.close();
        }
        return true;
    }

    @Override
    public boolean insertSchemaDetails(String schema, boolean isData) throws IOException, FileNotFoundException, 
            ParseException {
        String path;
        if(isData)
            path = Path.getOutputPath() + this.DB_BACKUP + "\\" + this.DATA + "\\" + this.SCHEMA_FILE;
        else
            path = Path.getOutputPath() + this.DB_BACKUP + "\\" + this.DB_DETAILS + "\\" + this.SCHEMA_FILE;
        File file = new File(path);
        
        JSONObject object = this.getJSONObject(path);
        JSONObject schemas = (JSONObject) object.get("data");
        if(schemas.containsValue(schema)){
            return false;
        }
        schemas.put(schemas.size() + 1, schema);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(object.toJSONString());
            writer.flush();
            writer.close();
        }
        return true;
    }
    
    @Override
    public List<String> getSchemas(boolean isData) throws FileNotFoundException, ParseException{
        List<String> list = new ArrayList<>();
        String path;
        if(isData)
            path = Path.getOutputPath() + this.DB_BACKUP + "\\" + this.DATA + "\\";
        else
            path = Path.getOutputPath() + this.DB_BACKUP + "\\" + this.DB_DETAILS + "\\";
        
        File file = new File(path);
        
        System.out.println("path: "+path);
        File[] files = file.listFiles();
        for(File f : files){
            if(f.isDirectory())
                list.add(f.getName());
        }
        System.out.println("list: "+list);
        if(validate(list, path))
            return list;
        else return null;
    }

    public boolean validate(List<String> list, String path) throws FileNotFoundException, ParseException{
        JSONObject object = (JSONObject) this.getJSONObject(path + this.SCHEMA_FILE).get("data");
        
        if(list.size() == object.size()){
            
            if (!list.stream().noneMatch((str) -> (!object.containsValue(str.toLowerCase())))) {
                System.out.println("list mismatch");
                return false;
            }
        } else {
            System.out.println("size mismatch");
            System.out.println("list: " + list.size());
            System.out.println("object: " + object.size());
            return false;
        }
        return true;
    }
    
    @Override
    public List<String> getTables(String schema, boolean isData) throws FileNotFoundException, ParseException{
        List<String> list = new ArrayList<>();
        String path;
        if(isData)
            path = Path.getOutputPath() + this.DB_BACKUP + "\\" + this.DATA + "\\" + schema + "\\";
        else
            path = Path.getOutputPath() + this.DB_BACKUP + "\\" + this.DB_DETAILS + "\\" + schema + "\\";
        
        File file = new File(path);
        System.out.println("path: " + path);
        File[] files = file.listFiles();
        System.out.println("list of files: " + files.length);
        
        for(File f : files){
            if(f.isFile() && !f.getName().equalsIgnoreCase(this.TABLE_FIELD))
                list.add(f.getName().substring(0, f.getName().indexOf('.')));
        }
        if(validate(list, path, schema))
            return list;
        else return null;
    }
    
    public boolean validate(List<String> list, String path, String schema) throws FileNotFoundException, ParseException{
        JSONObject object = (JSONObject) this.getJSONObject(path + this.TABLE_FIELD).get("data");
        
        if(list.size() == object.size()){
            
            if (!list.stream().noneMatch((str) -> (!object.containsKey(str.toLowerCase())))) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
    
    @Override
    public boolean deleteTableDetails(Table table) throws IOException, FileNotFoundException, ClassNotFoundException {
        return this.deleteTableDetails(table.getName());
    }

    public boolean deleteTableDetails(String tableName) throws IOException, FileNotFoundException, ClassNotFoundException {
        return this.deleteFile(tableName + ".table", Path.getOutputPath() + "\\" + this.DB_DETAILS);
    }

    @Override
    public boolean deleteTableData(JSONObject tableData) throws IOException, FileNotFoundException {
        JSONObject obj = (JSONObject) tableData.get("desc");
        return this.deleteTableData(obj.get("TableName").toString());
    }

    public boolean deleteTableData(String tableName) throws IOException, FileNotFoundException {
        return this.deleteFile(tableName + ".JSON", Path.getOutputPath() + "\\" + this.DATA);
    }

    @Override
    public boolean deleteSchemaDetails() throws IOException, FileNotFoundException {
        return this.deleteFile("Schemas.JSON", Path.getOutputPath() + "\\" + this.DB_DETAILS);
    }

    @Override
    public boolean updateTableDetails(Table table) throws IOException, FileNotFoundException, ClassNotFoundException {
        return this.deleteTableDetails(table) && this.insertTableDetails(table);
    }

    @Override
    public boolean updateTableData(JSONObject tableData) throws IOException, FileNotFoundException {
        return this.deleteTableData(tableData) && this.insertTableData(tableData);
    }

    @Override
    public boolean updateSchemaDetails(String schema) throws IOException, FileNotFoundException, ParseException {
        return this.insertSchemaDetails(schema, false);
    }

    @Override
    public boolean deleteOutputFolder() throws FileNotFoundException {
        File file = new File(Path.getOutputPath());
        return file.delete();
    }

    private boolean deleteFile(String filename, String filePath) {
        File file = new File(filePath + "\\" + filename);
        return file.delete();
    }

    @Override
    public boolean searchTable(String schema, Table table) {
        String path = Path.getOutputPath() + "\\" + this.DB_BACKUP + "\\" + this.DB_DETAILS + "\\"
                + schema + "\\";
        String fileName = table.getName() + this.TABLE;
        
        File file = new File(path + fileName);
        return file.exists();
    }
    
    @Override
    public boolean searchTable(String schema, String table) {
        String path = Path.getOutputPath() + "\\" + this.DB_BACKUP + "\\" + this.DB_DETAILS + "\\"
                + schema + "\\";
        String fileName = table + this.TABLE;
        
        File file = new File(path + fileName);
        return file.exists();
    }
    
    @Override
    public boolean searchDataTable(String schema, String table){
        String path = Path.getOutputPath() + "\\" + this.DB_BACKUP + "\\" +this.DATA + "\\"
                + schema + "\\";
        String fileName = table + this.DAT;
        File file = new File(path + fileName);
        return file.exists();
    }

    @Override
    public boolean deleteTableDetails(String schema, String table) throws IOException, FileNotFoundException, ClassNotFoundException {
        return this.deleteFile(table + this.TABLE, 
                Path.getOutputPath() + "\\" + this.DB_BACKUP + "\\" + this.DB_DETAILS + "\\" + schema + "\\");
    }

    @Override
    public boolean deleteTableData(String schema, String table) throws IOException, FileNotFoundException, ClassNotFoundException {
        return this.deleteFile(table + this.JSON, 
                Path.getOutputPath() + "\\" + this.DB_BACKUP + "\\" + this.DATA + "\\" + schema + "\\");
    }
    
    @Override
    public String getTableDetailPath(String schema, String table) {
        if(this.searchTable(schema, table)){
            return Path.getOutputPath() + "\\" + this.DB_BACKUP + "\\" + this.DB_DETAILS + "\\"
                + schema;
        } else{
            return null;
        }
    }
    
    @Override
    public String getTableDataPath(String schema, String table){
        if(this.searchDataTable(schema, table)){
            return Path.getOutputPath() + "\\" + this.DB_BACKUP + "\\" +this.DATA + "\\"
                + schema;
        } else {
            return null;
        }
    }
    
    public String getDataPath(String schema, String table){
        String path = Path.getOutputPath() + "\\" + this.DB_BACKUP + "\\" +this.DATA + "\\"
                + schema;
        File file = new File(path);
        file.mkdirs();
        if(file.exists()){
            return path;
        } else {
            return null;
        }
    }
    
    @Override
    public Table readTableDetails(String schema, String table) throws FileNotFoundException, IOException, ClassNotFoundException{
        String path = this.getTableDetailPath(schema, table) + "\\" + table + this.TABLE;
        
        ObjectInputStream objectInputStream;
        Table object;
        try (FileInputStream stream = new FileInputStream(path)) {
            objectInputStream = new ObjectInputStream(stream);
            object = (Table) objectInputStream.readObject();
        }
        objectInputStream.close();
        
        return object;
    }
    
    @Override
    public JSONObject readTableData(String schema, String table) throws FileNotFoundException, ParseException{
        String path = this.getTableDataPath(schema, table) + "\\" + table + this.JSON;
        System.out.println("path: " + path);
        File file = new File(path);
        JSONObject object;
        JSONParser parser = new JSONParser();
        Scanner scanner = new Scanner(file);
        StringBuilder data = new StringBuilder();
        while(scanner.hasNext()){
            data.append(scanner.nextLine());
        }
        object = (JSONObject) parser.parse(data.toString());
        
        return object;
    }
    
    @Override
    public JSONObject readTableDataBin(String schema, String table) throws FileNotFoundException, ParseException, IOException, ClassNotFoundException{
        String path = this.getTableDataPath(schema, table) + "\\" + table + this.BSON;
        System.out.println("path is:"+path);
        ObjectInputStream objectInputStream;
        JSONObject object;
        try (FileInputStream stream = new FileInputStream(path)) {
            objectInputStream = new ObjectInputStream(stream);
            object = (JSONObject) objectInputStream.readObject();
        }
        objectInputStream.close();
        
        return object;
    }
    
}
