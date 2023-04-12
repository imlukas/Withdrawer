package me.imlukas.withdrawer.v2.utils.command.comparison;

import java.util.Comparator;

public class SmallestStringComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        return o1.length() - o2.length();
    }
}
