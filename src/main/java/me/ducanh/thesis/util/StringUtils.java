package me.ducanh.thesis.util;

public class StringUtils {
public static boolean notInteger(String input) {
    try {
        Integer.parseInt(input);
        return false;
    } catch (Exception e) {
        return true;
    }



}
}
