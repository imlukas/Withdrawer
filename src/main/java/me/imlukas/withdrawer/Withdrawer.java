package me.imlukas.withdrawer;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import me.imlukas.withdrawer.commands.WithdrawHealthCommand;
import me.imlukas.withdrawer.commands.WithdrawMoneyCommand;
import me.imlukas.withdrawer.commands.WithdrawXpCommand;
import me.imlukas.withdrawer.config.DefaultItemsHandler;
import me.imlukas.withdrawer.config.PluginSettings;
import me.imlukas.withdrawer.economy.EconomyManager;
import me.imlukas.withdrawer.hooks.PlaceholderHook;
import me.imlukas.withdrawer.item.WithdrawableItem;
import me.imlukas.withdrawer.item.impl.ExpItem;
import me.imlukas.withdrawer.item.impl.HealthItem;
import me.imlukas.withdrawer.item.impl.MoneyItem;
import me.imlukas.withdrawer.item.registry.WithdrawableItemInitializers;
import me.imlukas.withdrawer.item.registry.WithdrawableItemsStorage;
import me.imlukas.withdrawer.listener.*;
import me.imlukas.withdrawer.utils.command.SimpleCommand;
import me.imlukas.withdrawer.utils.command.impl.CommandManager;
import me.imlukas.withdrawer.utils.interactions.messages.MessagesFile;
import me.imlukas.withdrawer.utils.interactions.SoundManager;
import me.imlukas.withdrawer.utils.storage.YMLBase;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Function;

@Getter
public final class Withdrawer extends JavaPlugin {

    private MessagesFile messages;
    private SoundManager sounds;
    private PluginSettings pluginSettings;
    private CommandManager commandManager;
    private EconomyManager economyManager;
    private DefaultItemsHandler defaultItemsHandler;

    private WithdrawableItemsStorage withdrawableItemsStorage;
    private WithdrawableItemInitializers defaultWithdrawables;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        economyManager = new EconomyManager(this);
        commandManager = new CommandManager(this);

        messages = new MessagesFile(this);
        sounds = new SoundManager(this);
        pluginSettings = new PluginSettings(getConfig());
        withdrawableItemsStorage = new WithdrawableItemsStorage();
        defaultItemsHandler = new DefaultItemsHandler(this);

        defaultWithdrawables = new WithdrawableItemInitializers();

        registerDefaultWithdrawable("money", (item) -> new MoneyItem(this, item));
        registerDefaultWithdrawable("exp", (item) -> new ExpItem(this, item));
        registerDefaultWithdrawable("health", (item) -> new HealthItem(this, item));

        updateConfig(this, messages);
        updateConfig(this, defaultItemsHandler);
        System.out.println("[Withdrawer] Updated Config!");

        registerCommand(new WithdrawMoneyCommand(this));
        registerCommand(new WithdrawXpCommand(this));
        registerCommand(new WithdrawHealthCommand(this));
        System.out.println("[Withdrawer] Registered Commands!");

        registerListener(new HealthResetListener());
        registerListener(new ItemDropListener(this));
        registerListener(new RedeemListener(this));
        registerListener(new ConnectionListener(this));
        registerListener(new CraftingVillagerListener(this));
        System.out.println("[Withdrawer] Registered Listeners!");

        new PlaceholderHook(this).register();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        reloadConfig();
        messages = null;

    }

    public void registerDefaultWithdrawable(String name, Function<NBTItem, WithdrawableItem> function) {
        defaultWithdrawables.addDefault(name, function);
    }

    private void registerCommand(SimpleCommand command) {
        commandManager.register(command);
    }

    private void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    private void updateConfig(JavaPlugin plugin, YMLBase base) {
        File file = base.getFile();
        InputStream stream = plugin.getResource(file.getAbsolutePath().replace(plugin.getDataFolder().getAbsolutePath() + File.separator, ""));

        if (stream == null)
            return;

        FileConfiguration cfg = base.getConfiguration();
        FileConfiguration other = YamlConfiguration.loadConfiguration(new InputStreamReader(stream));

        for (String key : other.getKeys(true)) {
            if (!cfg.isSet(key)) {
                cfg.set(key, other.get(key));
            }
        }
        base.save();
    }
}
