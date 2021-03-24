/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.lib.modals;

import java.io.Serializable;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author ASUS
 */
public class Table implements Serializable{
    private String name;
    private String schemaName;
    private List<Field> fields;

    public Table() {
        this.name = null;
        this.fields = null;
        this.schemaName = null;
    }

    public Table(String name, String schemaName, List<Field> fields) {
        this.name = name;
        this.schemaName = schemaName;
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "Table{" + "name=" + name + ", schemaName=" + schemaName + ", fields=" + fields + '}';
    }
    
    public String toJSONString() {
        return this.toJSON().toJSONString();
    }
    
    public JSONObject toJSON() {
        JSONObject object = new JSONObject();
        object.put("TableName", this.name);
        object.put("SchemaName", this.schemaName);
        JSONArray array = new JSONArray();
        for(Field field : fields){
            array.add(field.toJSON());
        }
        object.put("Fields", array);
        return object;
    }
}
