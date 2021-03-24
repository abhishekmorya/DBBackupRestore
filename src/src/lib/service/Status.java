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
public class Status {
    private static int value;
    private static boolean status;
    private static String message;

    public static int getValue() {
        return value;
    }

    public static void setValue(int value) {
        Status.value = value;
    }

    public static boolean isStatus() {
        return status;
    }

    public static void setStatus(boolean status) {
        Status.status = status;
    }

    public static String getMessage() {
        return message;
    }

    public static void setMessage(String message) {
        Status.message = message;
    }

    
    
    
}
