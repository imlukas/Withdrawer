package me.imlukas.withdrawer.utils.interactions;

import me.imlukas.withdrawer.WithdrawerPlugin;
import me.imlukas.withdrawer.utils.storage.YMLBase;
import me.imlukas.withdrawer.utils.text.Placeholder;
import me.imlukas.withdrawer.utils.text.TextUtils;
import me.imlukas.withdrawer.v3.item.interaction.InteractionType;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang3.StringEscapeUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Messages extends YMLBase {

    private final String prefix;
    private boolean usePrefix, useActionBar, isLessIntrusive;
    private String msg;

    public Messages(WithdrawerPlugin plugin) {
        super(plugin, new File(plugin.getDataFolder(), "messages.yml"), true);
        prefix = getConfiguration().getString("messages.prefix");
        usePrefix = getConfiguration().getBoolean("messages.use-prefix");
        useActionBar = getConfiguration().getBoolean("messages.use-actionbar");
        isLessIntrusive = getConfiguration().getBoolean("messages.less-intrusive");
    }

    public boolean isLessIntrusive() {
        return isLessIntrusive;
    }

    private String setMessage(String name, UnaryOperator<String> action) {
        if (!getConfiguration().contains("messages." + name)) {
            return "";
        }

        msg = getMessage(name).replace("%prefix%", prefix);

        if (usePrefix) {
            msg = prefix + " " + getMessage(name);
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
        sendMessage(sender, name, List.of(placeholders));
    }

    public final <T extends CommandSender> void sendMessage(T sender, String name, Collection<Placeholder<T>> placeholders) {
        sendMessage(sender, name, text -> {
            for (Placeholder<T> placeholder : placeholders) {
                text = placeholder.replace(text, sender);
            }

            return text;
        });
    }


    public void sendMessage(CommandSender sender, String name, UnaryOperator<String> action) {
        if (getConfiguration().isList("messages." + name)) {
            for (String str : getConfiguration().getStringList("messages." + name)) {
                msg = action.apply(str.replace("%prefix%", prefix));
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

    public void sendInteractionMessage(Player player, InteractionType interactionType, String configName, Map<String, String> placeholders) {
        List<Placeholder<Player>> placeholderList = new ArrayList<>();

        for (Map.Entry<String, String> stringStringEntry : placeholders.entrySet()) {
            String key = stringStringEntry.getKey();
            String value = stringStringEntry.getValue();

            placeholderList.add(new Placeholder<>(key, value));
        }

        if (isLessIntrusive) {
            sendMessage(player, configName + "." + interactionType.getConfigName() + "-less-intrusive", placeholderList);
            return;
        }

        sendMessage(player, configName + "." + interactionType.getConfigName(), placeholderList);

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

