/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.lib.service;

/**
 *
 * @author ASUS
 */
public class ConnDetail {
    private static String forName;
    private static String url;
    private static String user;
    private static String pass;
    private static String schema;

    public static String getForName() {
        return forName;
    }

    public static void setForName(String forName) {
        ConnDetail.forName = forName;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        ConnDetail.url = url;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        ConnDetail.user = user;
    }

    public static String getPass() {
        return pass;
    }

    public static void setPass(String pass) {
        ConnDetail.pass = pass;
    }

    public static String getSchema() {
        return schema;
    }

    public static void setSchema(String schema) {
        ConnDetail.schema = schema;
    }
    
    
    
}
