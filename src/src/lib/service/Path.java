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
public class Path {
    private static String inputPath;
    private static String outputPath;

    static {
        inputPath = "";
        outputPath = "";
    }
    public static void setInputPath(String inputPath){
        Path.inputPath = inputPath;
    }
    
    public static String getInputPath(){
        return Path.inputPath;
    }
    
    public static void setOutputPath(String outputPath){
        Path.outputPath = outputPath + "\\";
    }
    
    public static String getOutputPath(){
        return Path.outputPath;
    }
    
    public static String getDefaultPath(){
        return System.getProperty("user.home") + "\\Documents\\";
    }
    
    public String changeSeperator(String path){
        return path.replace("\\", "/");
    }
}
