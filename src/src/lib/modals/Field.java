/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.lib.modals;

import java.io.Serializable;
import org.json.simple.JSONObject;

/**
 *
 * @author ASUS
 */
public class Field implements Serializable{
    private String name;
    private String type;
    private String key;
    private boolean primary;
    private boolean unique;
    private boolean index;
    private boolean isNull;
    private String defaultValue;
    private String extra;

    public Field() {
        this.name = null;
        this.type = null;
        this.key = null;
        this.primary = false;
        this.unique = false;
        this.index = false;
        this.isNull = false;
        this.defaultValue = null;
        this.extra = null;
    }

    public Field(String name, String type, String key, boolean primary, boolean unique, boolean index, boolean isNull, String defaultValue, String extra) {
        this.name = name;
        this.type = type;
        this.key = key;
        this.primary = primary;
        this.unique = unique;
        this.index = index;
        this.isNull = isNull;
        this.defaultValue = defaultValue;
        this.extra = extra;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey(){
        return this.key;
    }
    
    public void setKey(String key){
        this.key = key;
    }
    
    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isIndex() {
        return index;
    }

    public void setIndex(boolean index) {
        this.index = index;
    }

    public boolean isIsNull() {
        return isNull;
    }

    public void setIsNull(boolean isNull) {
        this.isNull = isNull;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "Field{" + "name=" + name + ", type=" + type + ", key=" + key + ", primary=" + primary + ", unique=" + unique + ", index=" + index + ", isNull=" + isNull + ", defaultValue=" + defaultValue + ", extra=" + extra + '}';
    }
    
    public JSONObject toJSON() {
        JSONObject object = new JSONObject();
        object.put("Name", this.name);
        object.put("Type", this.type);
        object.put("Key", this.key);
        object.put("Primary", this.primary);
        object.put("Unique", this.unique);
        object.put("Index", this.index);
        object.put("IsNull", this.isNull);
        object.put("DefaultValue", this.defaultValue);
        object.put("Extra", this.extra);
        return object;
    }
    
    public String toJSONString(){
        return this.toJSON().toJSONString();
    }
}
