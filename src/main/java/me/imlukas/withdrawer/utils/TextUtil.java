package me.imlukas.withdrawer.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class TextUtil {

    private final FileConfiguration config;

    public TextUtil(JavaPlugin main) {
        this.config = main.getConfig();

    }

    public String getColor(String message) {
        // convert colors to minecraft color codes
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

    public String getColorConfig(String key) {
        return ChatColor.translateAlternateColorCodes('&', config.getString(key));
    }
}
