/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.lib.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import src.lib.modals.Field;
import src.lib.modals.Schema;
import src.lib.modals.Table;

/**
 *
 * @author ASUS
 */
public class DBDetailsImpl implements DBDetailsInter {

    @Override
    public List<Schema> getSchemas(Connection conn) throws SQLException {
        List<Schema> schemas = new ArrayList<>();
        String query = "show databases";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            schemas.add(new Schema(rs.getString(1)));
        }
        return schemas;
    }
    
    @Override
    public List<String> tableList(Connection conn, String schema) throws SQLException {
        List<String> list = new ArrayList<>();
        this.changeDatabase(conn, schema);
        String query = "show tables";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            list.add(rs.getString(1));
        }
        return list;
    }
    
    public List<Table> tablesNew(Connection conn, String schema) throws SQLException {
        List<String> list = new ArrayList<>();
        this.changeDatabase(conn, schema);
        String query = "show tables";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            list.add(rs.getString(1));
        }
//        return list.stream().map(this.getTable(conn, schema, String::toString)).collect(Collectors.toList());
        return null;
    }
    
    @Override
    public List<Table> tables(Connection conn, String schema) throws SQLException {
        Iterator itr;
        itr = this.tableList(conn, schema).iterator();
        List<Table> list = new ArrayList<>();
        while (itr.hasNext()) {
            list.add(this.getTable(conn, schema, itr.next().toString()));
        }
        return list;
    }

    @Override
    public List<Field> fields(Connection conn, String schema, String table) throws SQLException {
        List<Field> list = new LinkedList<>();

        String query = "desc " + table;
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        Field f = null;
        while (rs.next()) {
            f = new Field();
            f.setName(rs.getString("Field"));
            f.setType(rs.getString("Type"));
            if (rs.getString("Null").equals("NO")) {
                f.setIsNull(false);
            } else {
                f.setIsNull(true);
            }
            f.setKey(rs.getString("Key"));
            if (f.getKey().equals("PRI")) {
                f.setPrimary(true);
            }
            f.setDefaultValue(rs.getString("Default"));
            f.setExtra(rs.getString("Extra"));
            list.add(f);
        }
        return list;
    }

    @Override
    public boolean changeDatabase(Connection conn, String schema) throws SQLException {
        String query = "use " + schema;

        PreparedStatement stmt = conn.prepareStatement(query);
        return stmt.execute();
    }

    @Override
    public Table getTable(Connection conn, String schema, String table) throws SQLException {
        this.changeDatabase(conn, schema);
        Table obj = new Table();
        obj.setName(table);
        obj.setSchemaName(schema);
        List<Field> list = this.fields(conn, schema, table);
        obj.setFields(list);

        return obj;
    }

    @Override
    public Field getField(Connection conn, String schema, String table, String field) throws SQLException {
        List<Field> list = this.fields(conn, schema, table);
        Field obj = null;
        for (Field f : list) {
            if (f.getName().equalsIgnoreCase(field)) {
                obj = f;
            }
        }
        return obj;
    }

    @Override
    public JSONObject getSchemaDetails(Connection conn) throws SQLException {
        List<Schema> schemas = this.getSchemas(conn);
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject obj = null;
        object.put("Size", schemas.size() + "");
        for(Schema s : schemas){
            obj = new JSONObject();
            obj.put("Name", s.getName());
            obj.put("NumberOfTables", this.tableList(conn, s.getName()).size() + "");
            obj.put("TableNames", this.tableList(conn, s.getName()).toString());
            array.add(obj);
        }
        object.put("Details", array);
        return object;
    }

}
