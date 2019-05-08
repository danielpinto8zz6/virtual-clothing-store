package com.sample.utils;

public class ParseUtils {
    public static boolean isDoubleParsable(String input){
        try{
            Double.valueOf(input);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
}
