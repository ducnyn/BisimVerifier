package me.ducanh.thesis.util;

public class StringCheck {
public static boolean isInteger( String input ) {
    try {
        Integer.parseInt( input );
        return true;
    }
    catch( Exception e ) {
        return false;
    }
}
}
