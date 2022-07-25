package me.ducanh.thesis.ui.util;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Iterator;

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
