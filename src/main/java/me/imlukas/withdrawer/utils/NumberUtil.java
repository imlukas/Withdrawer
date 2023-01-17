package me.imlukas.withdrawer.utils;

public class NumberUtil {

    public static int parse(String number) {
        int amountParsed;

        try {
            amountParsed = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return 0;
        }

        return amountParsed;

    }
}
