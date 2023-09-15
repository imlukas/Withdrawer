package me.imlukas.withdrawer.utils.interactions.messages;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.utils.storage.YMLBase;
import me.imlukas.withdrawer.utils.text.Placeholder;
import me.imlukas.withdrawer.utils.text.TextUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang3.StringEscapeUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Collection;
import java.util.function.Function;

public class MessagesFile extends YMLBase {

    private final String prefix, arrow;
    protected boolean usePrefix, useActionBar, isLessIntrusive;
    private String msg;
    private final AutomatedMessages automatedMessages;

    public MessagesFile(Withdrawer plugin) {
        super(plugin, new File(plugin.getDataFolder(), "messages.yml"), true);
        prefix = getConfiguration().getString("messages.prefix");
        arrow = getConfiguration().getString("messages.arrow");
        usePrefix = getConfiguration().getBoolean("messages.use-prefix");
        useActionBar = getConfiguration().getBoolean("messages.use-actionbar");
        isLessIntrusive = getConfiguration().getBoolean("messages.less-intrusive");

        automatedMessages = new AutomatedMessages(this);
    }

    public AutomatedMessages getAutomatedMessages() {
        return automatedMessages;
    }

    private String setMessage(String name) {
        return setMessage(name, (s) -> s);
    }

    private String setMessage(String name, Function<String, String> action) {
        if (!getConfiguration().contains("messages." + name))
            return "";
        msg = getMessage(name);
        if (usePrefix && !isLessIntrusive) {
            msg = prefix + " " + arrow + " " + getMessage(name);
        } else {
            msg = msg.replace("%prefix%", prefix);
        }
        msg = action.apply(msg);
        return TextUtils.color(msg);
    }

    public void sendStringMessage(CommandSender player, String msg) {
        player.sendMessage(TextUtils.color(msg));
    }

    public void sendMessage(CommandSender sender, String name) {
        sendMessage(sender, name, (s) -> s);
    }


    @SafeVarargs
    public final <T extends CommandSender> void sendMessage(T sender, String name, Placeholder<T>... placeholders) {
        sendMessage(sender, name, (text) -> {
            for (Placeholder<T> placeholder : placeholders) {
                text = placeholder.replace(text, sender);
            }

            return text;
        });
    }

    public final <T extends CommandSender> void sendMessage(T sender, String name, Collection<Placeholder<T>> placeholders) {
        sendMessage(sender, name, (text) -> {
            for (Placeholder<T> placeholder : placeholders) {
                text = placeholder.replace(text, sender);
            }

            return text;
        });
    }


    public void sendMessage(CommandSender sender, String name, Function<String, String> action) {
        if (getConfiguration().isList("messages." + name)) {
            for (String str : getConfiguration().getStringList("messages." + name)) {
                msg = StringEscapeUtils.unescapeJava(str.replace("%prefix%", prefix));
                msg = action.apply(msg);
                sender.sendMessage(TextUtils.color(msg));
            }
            return;
        }

        msg = setMessage(name, action);
        if (useActionBar && sender instanceof Player player) {
            sendActionbarStringMessage(player, msg);
            return;
        }
        sender.sendMessage(msg);
    }

    public void sendActionbarStringMessage(Player player, String msg) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
    }

    public void sendActionBarMessage(Player player, String key) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(setMessage(key)));
    }

    public void sendActionBarMessage(Player player, String key, Function<String, String> action) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(setMessage(key, action)));
    }

    public String getMessage(String name) {
        return getConfiguration().getString("messages." + name);
    }


    public boolean togglePrefix() {
        return usePrefix = toggle("prefix", usePrefix);
    }

    public boolean toggleActionBar() {
        return useActionBar = toggle("actionbar", useActionBar);
    }

    public boolean toggleLessIntrusive() {
        return isLessIntrusive = toggle("less-intrusive", isLessIntrusive);
    }

    public boolean toggle(String type, boolean isEnabled) {
        if (isEnabled) {
            getConfiguration().set("messages.use-" + type, false);
        } else {
            getConfiguration().set("messages.use-" + type, true);
        }

        save();
        return !isEnabled;
    }
}

