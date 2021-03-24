/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.lib.modals;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ASUS
 */
public class Type {

    private String input;
    private int size;
    private String type;
    private boolean unsigned;

    public Type(String input) {
        this.input = input;
    }

    public String getType() {
        Pattern p = Pattern.compile("\\(*\\)");
        Matcher m = p.matcher(this.input);
        if (m.find()) {
            return this.input.substring(0, this.input.indexOf("(")).toUpperCase();
        }
        return this.input.toUpperCase();
    }

    public int getSize() {
        Pattern p = Pattern.compile("\\(*\\)");
        Matcher m = p.matcher(this.input);
        if (m.find()) {
            return Integer.parseInt(this.input.substring(this.input.indexOf("(") + 1, this.input.indexOf(")")));
        }
        return -1;
    }

    public boolean isUnsigned() {
        return this.input.contains("unsigned");
    }

    @Override
    public String toString() {
        return "Type{Type: " + this.getType() + ", Size: " + this.getSize() + ", Unsigned: " + this.isUnsigned() + '}';
    }

}
