package me.imlukas.withdrawer.utils;

import org.bukkit.ChatColor;

public class TextUtil {

    public String getColor(String message) {
        // convert colors to minecraft color codes
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }
}
