package me.imlukas.withdrawer.utils.illusion.storage;

import lombok.Getter;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.function.Function;

public class MessagesFile extends YMLBase {

    @Getter
    private final String prefix, arrow;
    private Boolean usePrefix;
    private String msg;

    public MessagesFile(JavaPlugin plugin) {
        super(plugin, new File(plugin.getDataFolder(), "messages.yml"), true);

        prefix = StringEscapeUtils.unescapeJava(getConfiguration().getString("messages.prefix"));
        arrow = StringEscapeUtils.unescapeJava(getConfiguration().getString("messages.arrow"));

    }

    public void sendStringMessage(CommandSender player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void sendMessage(CommandSender player, String name) {
        sendMessage(player, name, (s) -> s);
    }

    public void sendMessage(CommandSender player, String name, Function<String, String> action) {
        usePrefix = getConfiguration().getBoolean("messages.use-prefix");
        if (!getConfiguration().contains("messages." + name))
            return;
        if (getConfiguration().isList("messages." + name)) {
            for (String str : getConfiguration().getStringList("messages." + name)) {
                msg = StringEscapeUtils.unescapeJava(str.replace("%prefix%", prefix));
                msg = action.apply(msg);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }
            return;
        }
        if (usePrefix){
            msg = prefix + " " + arrow + " " + getMessage(name);
        }
        else if (getMessage(name).contains("%prefix%")) {
            msg = getMessage(name).replace("%prefix%", prefix + " " + arrow + getMessage(name));
        }
        else{
            msg = getMessage(name);
        }
        msg = action.apply(msg);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public String getMessage(String name) {
        return getConfiguration().getString("messages." + name);
    }
    public void setPrefixEnabled(Boolean enable){
        getConfiguration().set("messages.use-prefix", enable);
        save();
    }
}

