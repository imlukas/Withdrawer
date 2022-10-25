package me.imlukas.withdrawer.config;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.utils.illusion.storage.YMLBase;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigHandler extends YMLBase {
    public ConfigHandler(JavaPlugin main) {
        super(main, "config.yml");
    }
}
