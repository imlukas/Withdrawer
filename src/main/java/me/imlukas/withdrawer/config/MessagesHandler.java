package me.imlukas.withdrawer.config;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.utils.illusion.storage.YMLBase;
import org.bukkit.plugin.java.JavaPlugin;

public class MessagesHandler extends YMLBase {

    public MessagesHandler(Withdrawer main) {
        super(main, "messages.yml");
    }
}
