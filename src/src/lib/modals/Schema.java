/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.lib.modals;

/**
 *
 * @author ASUS
 */
public class Schema {
    private String name;
    public Schema() {
        this.name = null;
    }

    public Schema(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
