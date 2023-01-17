package me.imlukas.withdrawer.config;

import me.imlukas.withdrawer.utils.storage.YMLBase;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageConfig extends YMLBase {
    public MessageConfig(JavaPlugin main) {
        super(main, "messages.yml");
    }
}
