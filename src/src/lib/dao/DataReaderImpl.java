/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.lib.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import src.lib.modals.Field;
import src.lib.modals.Table;
import src.lib.modals.Type;
import src.lib.service.DBDetails;

/**
 *
 * @author ASUS
 */
public class DataReaderImpl implements DataReaderInter {

    @Override
    public JSONObject readTable(Connection conn, String schema, Table table) throws SQLException {
        this.changeDatabase(conn, schema);
        String query = "select * from " + table.getName();
//        Map<String, String> map = new LinkedHashMap<>();
        Iterator<Map.Entry<String, String>> itr;
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject obj;
        Map.Entry<String, String> entry = null;

        while (rs.next()) {
            itr = this.parseData(table, rs).entrySet().iterator();
            obj = new JSONObject();
            while (itr.hasNext()) {
                entry = itr.next();
                obj.put(entry.getKey(), entry.getValue());
            }
            array.add(obj);
        }

        object.put("desc", table.toJSON());
        object.put("data", array);
        return object;
    }

    public JSONObject readTableDat(Connection conn, String schema, Table table) throws SQLException {
        this.changeDatabase(conn, schema);
        String query = "select * from " + table.getName();
//        Map<String, String> map = new LinkedHashMap<>();
        Iterator<Map.Entry<String, String>> itr;
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject obj;
        Map.Entry<String, String> entry = null;

        while (rs.next()) {
            itr = this.parseData(table, rs).entrySet().iterator();
            obj = new JSONObject();
            while (itr.hasNext()) {
                entry = itr.next();
                obj.put(entry.getKey(), entry.getValue());
            }
            array.add(obj);
        }

        object.put("desc", table.toJSON());
        object.put("data", array);
        return object;
    }
    
    @Override
    public JSONObject readByQuery(Connection conn, String schema, String query) throws SQLException, IOException {
        this.changeDatabase(conn, schema);

        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        ResultSetMetaData metaData = rs.getMetaData();
        int columns = metaData.getColumnCount();
        String label, type;
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();

        JSONObject obj = null;
        while (rs.next()) {
            obj = new JSONObject();
            for (int i = 1; i <= columns; i++) {
                label = metaData.getColumnLabel(i);
                type = metaData.getColumnTypeName(i);
                obj.put(label,
                        this.getData(rs, label, type));
            }
            array.add(obj);
        }
        object.put("query", query);
        object.put("data", array);
        return object;
    }

    @Override
    public Map<String, String> parseData(Table table, ResultSet rs) throws SQLException {
        Map<String, String> map = new LinkedHashMap<>();
        List<Field> fields = table.getFields();
        int size = fields.size();
        for (Field f : fields) {
            Type type = new Type(f.getType());
            switch (type.getType()) {
                case DBDetails.CT_VARCHAR:
                    map.put(f.getName(), rs.getString(f.getName()));
                    break;
                case DBDetails.CT_CHAR:
                    map.put(f.getName(), rs.getString(f.getName()));
                    break;
                case DBDetails.CT_TINYTEXT:
                    map.put(f.getName(), rs.getString(f.getName()));
                case DBDetails.CT_TEXT:
                    map.put(f.getName(), rs.getString(f.getName()));
                    break;
                case DBDetails.CT_MEDIUMTEXT:
                    map.put(f.getName(), rs.getString(f.getName()));
                    break;
                case DBDetails.CT_LONGTEXT:
                    map.put(f.getName(), rs.getString(f.getName()));
                    break;
                case DBDetails.CT_TINYINT:
                    map.put(f.getName(), rs.getInt(f.getName()) + "");
                    break;
                case DBDetails.CT_SMALLINT:
                    map.put(f.getName(), rs.getInt(f.getName()) + "");
                    break;
                case DBDetails.CT_MEDIUMINT:
                    map.put(f.getName(), rs.getInt(f.getName()) + "");
                    break;
                case DBDetails.CT_INT:
                    map.put(f.getName(), rs.getInt(f.getName()) + "");
                    break;
                case DBDetails.CT_BIGINT:
                    map.put(f.getName(), rs.getInt(f.getName()) + "");
                    break;
                case DBDetails.CT_FLOAT:
                    map.put(f.getName(), rs.getFloat(f.getName()) + "");
                    break;
                case DBDetails.CT_DOUBLE:
                    map.put(f.getName(), rs.getDouble(f.getName()) + "");
                    break;
                case DBDetails.CT_DECIMAL:
                    map.put(f.getName(), rs.getDouble(f.getName()) + "");
                    break;
                case DBDetails.CT_DATE:
                    map.put(f.getName(), rs.getDate(f.getName()).toString());
                    break;
                case DBDetails.CT_DATETIME:
                    map.put(f.getName(), rs.getDate(f.getName()).toString());
                    break;
                case DBDetails.CT_TIMESTAMP:
                    map.put(f.getName(), rs.getTimestamp(f.getName()).toString());
                    break;
                case DBDetails.CT_TIME:
                    map.put(f.getName(), rs.getTime(f.getName()).toString());
                    break;
                case DBDetails.CT_ENUM:
                    map.put(f.getName(), rs.getArray(f.getName()).toString());
                    break;
                default:
                    map.put(f.getName(), "");
            }
        }
        return map;
    }
    
    public String getData(ResultSet rs, String label, String type) throws SQLException, NullPointerException, IOException {
        if (type.contains(" ")) {
            type = type.substring(0, type.indexOf(" "));
        }
        switch (type) {
            case DBDetails.CT_VARCHAR:
                return rs.getString(label);
            case DBDetails.CT_CHAR:
                return rs.getString(label);
            case DBDetails.CT_TINYTEXT:
                return rs.getString(label);
            case DBDetails.CT_TEXT:
                return rs.getString(label);
            case DBDetails.CT_MEDIUMTEXT:
                return rs.getString(label);
            case DBDetails.CT_LONGTEXT:
                return rs.getString(label);
            case DBDetails.CT_TINYINT:
                return rs.getInt(label) + "";
            case DBDetails.CT_SMALLINT:
                return rs.getInt(label) + "";
            case DBDetails.CT_MEDIUMINT:
                return rs.getInt(label) + "";
            case DBDetails.CT_INT:
                return rs.getInt(label) + "";
            case DBDetails.CT_BIGINT:
                return rs.getInt(label) + "";
            case DBDetails.CT_FLOAT:
                return rs.getFloat(label) + "";
            case DBDetails.CT_DOUBLE:
                return rs.getDouble(label) + "";
            case DBDetails.CT_DECIMAL:
                return rs.getDouble(label) + "";
            case DBDetails.CT_DATE:
                return rs.getDate(label).toString();
            case DBDetails.CT_DATETIME:
                return rs.getDate(label).toString();
            case DBDetails.CT_TIMESTAMP:
                return rs.getTimestamp(label).toString();
            case DBDetails.CT_TIME:
                return rs.getTime(label).toString();
            case DBDetails.CT_ENUM:
                return rs.getArray(label).toString();
            case DBDetails.CT_BLOB:
                return this.blobToString(rs.getBlob(label));
            default:
                return "";
        }
    }
    
    public Object getBin(ResultSet rs, String label, String type) throws SQLException, NullPointerException, IOException {
        if (type.contains(" ")) {
            type = type.substring(0, type.indexOf(" "));
        }
        switch (type) {
            case DBDetails.CT_VARCHAR:
                return rs.getString(label);
            case DBDetails.CT_CHAR:
                return rs.getString(label).charAt(0);
            case DBDetails.CT_TINYTEXT:
                return rs.getString(label);
            case DBDetails.CT_TEXT:
                return rs.getString(label);
            case DBDetails.CT_MEDIUMTEXT:
                return rs.getString(label);
            case DBDetails.CT_LONGTEXT:
                return rs.getString(label);
            case DBDetails.CT_TINYINT:
                return rs.getInt(label);
            case DBDetails.CT_SMALLINT:
                return rs.getInt(label);
            case DBDetails.CT_MEDIUMINT:
                return rs.getInt(label);
            case DBDetails.CT_INT:
                return rs.getInt(label);
            case DBDetails.CT_BIGINT:
                return rs.getInt(label);
            case DBDetails.CT_FLOAT:
                return rs.getFloat(label);
            case DBDetails.CT_DOUBLE:
                return rs.getDouble(label);
            case DBDetails.CT_DECIMAL:
                return rs.getDouble(label);
            case DBDetails.CT_DATE:
                return rs.getDate(label);
            case DBDetails.CT_DATETIME:
                return rs.getDate(label);
            case DBDetails.CT_TIMESTAMP:
                return rs.getTimestamp(label);
            case DBDetails.CT_TIME:
                return rs.getTime(label);
            case DBDetails.CT_ENUM:
                return rs.getArray(label);
            case DBDetails.CT_BLOB:
                return rs.getBlob(label);
            default:
                return "";
        }
    }

    public JSONObject extractData(Connection conn, String schema, Table table) throws SQLException, IOException {
        this.changeDatabase(conn, schema);
        String query = "select * from " + table.getName();

        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        ResultSetMetaData metaData = rs.getMetaData();
        int columns = metaData.getColumnCount();
        String label, type;
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();

        String data = null;
        JSONObject obj = null;
        while (rs.next()) {
            obj = new JSONObject();
            for (int i = 1; i <= columns; i++) {
                label = metaData.getColumnLabel(i);
                type = metaData.getColumnTypeName(i);
                try {
                    data = this.getData(rs, label, type);
                } catch (NullPointerException ex) {
                    data = "$$$";
                }
                obj.put(label, data);
            }
            array.add(obj);
        }
        object.put("desc", table.toJSON());
        object.put("data", array);
        return object;
    }
    
    @Override
    public JSONObject extractBin(Connection conn, String schema, Table table) throws SQLException, IOException {
        this.changeDatabase(conn, schema);
        String query = "select * from " + table.getName();

        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        ResultSetMetaData metaData = rs.getMetaData();
        int columns = metaData.getColumnCount();
        String label, type;
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();

        Object bin = null;
        JSONObject obj = null;
        while (rs.next()) {
            obj = new JSONObject();
            for (int i = 1; i <= columns; i++) {
                label = metaData.getColumnLabel(i);
                type = metaData.getColumnTypeName(i);
                try {
                    bin = this.getBin(rs, label, type);
                } catch (NullPointerException ex) {
                    bin = "\\N";
                }
                obj.put(label, bin);
            }
            array.add(obj);
        }
        object.put("desc", table);
        object.put("data", array);
        return object;
    }

    public boolean changeDatabase(Connection conn, String schema) throws SQLException {
        String query = "use " + schema;

        PreparedStatement stmt = conn.prepareStatement(query);
        return stmt.execute();
    }

    private String blobToString(Blob b) throws IOException, SQLException {
        InputStream is = b.getBinaryStream();
        List<Integer> list = new LinkedList<>();
        int i;
        while (is.available() > 0) {
            i = is.read();
            list.add(i);
        }
        return list.toString();
    }
    
}
