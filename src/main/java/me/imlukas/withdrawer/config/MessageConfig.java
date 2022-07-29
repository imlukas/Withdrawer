package me.imlukas.withdrawer.config;

import me.imlukas.withdrawer.utils.illusion.storage.YMLBase;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageConfig extends YMLBase {
    public MessageConfig(JavaPlugin plugin) {
        super(plugin, "messages.yml");
    }
}
