package com.hotelsystem.management.util;

public class Utils {

    public static boolean isNumericAndPositive(String number) {
        if (number == null) return false;
        int no;
        try {
            no = Integer.parseInt(number);
        } catch (NumberFormatException exception) {
            return false;
        }
        return no > 0;
    }
}
