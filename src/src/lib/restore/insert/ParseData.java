/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.lib.restore.insert;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 *
 * @author ASUS
 */
public class ParseData {
    public Timestamp toTime(String timeString){
//        -- "last_update": "2006-02-15 03:57:16.0"
        Timestamp t;
        
        String time, date;
        if(timeString.contains(" ")){
            time = timeString.substring(timeString.indexOf(" ")).trim();
            date = timeString.substring(0, timeString.indexOf(" ")).trim();
        } else {
            date = timeString;
            time = "00:00:00.0";
        }
        
        int year = Integer.parseInt(date.substring(0, date.indexOf("-"))) - 1900;
        int month = Integer.parseInt(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));
        int day = Integer.parseInt(date.substring(date.lastIndexOf("-") + 1));
        
        int hour = Integer.parseInt(time.substring(0, time.indexOf(":")));
        int min = Integer.parseInt(time.substring(time.indexOf(":") + 1, time.lastIndexOf(":")));
        int sec = Integer.parseInt(time.substring(time.lastIndexOf(":") + 1, time.indexOf(".")));
        int nano = Integer.parseInt(time.substring(time.indexOf(".") + 1));
        t = new Timestamp(year, month, day, hour, min, sec, nano);
        return t;
    }
    
    public Date toDate(String timeString) {
        Timestamp timestamp = this.toTime(timeString);
        Date date = new Date(timestamp.getTime());
        return date;
    }
    
    public static Date getDate(String timeString){
        ParseData data = new ParseData();
        return data.toDate(timeString);
    }
    
    public static Time getTime(String timeString){
        ParseData data = new ParseData();
        return new Time(data.toTime(timeString).getTime());
    }
    
    public static Timestamp toTimestamp(String timestamp){
        ParseData data = new ParseData();
        return data.toTime(timestamp);
    }
    
}
