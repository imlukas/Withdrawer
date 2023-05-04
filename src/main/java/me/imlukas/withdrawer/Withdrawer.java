package me.imlukas.withdrawer;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import me.imlukas.withdrawer.api.WithdrawerAPI;
import me.imlukas.withdrawer.commands.*;
import me.imlukas.withdrawer.commands.gift.GiftCommand;
import me.imlukas.withdrawer.commands.gift.GiftMoneyCommand;
import me.imlukas.withdrawer.commands.withdraw.WithdrawCommand;
import me.imlukas.withdrawer.commands.withdraw.WithdrawMoneyCommand;
import me.imlukas.withdrawer.config.ItemHandler;
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
import java.util.UUID;
import java.util.function.Function;

@Getter
public final class Withdrawer extends JavaPlugin {

    private static WithdrawerAPI API;
    private static Withdrawer instance;
    private MessagesFile messages;
    private SoundManager sounds;
    private PluginSettings pluginSettings;
    private CommandManager commandManager;
    private EconomyManager economyManager;
    private ItemHandler itemHandler;

    private WithdrawableItemsStorage withdrawableItemsStorage;
    private WithdrawableItemInitializers itemInitializers;

    @Override
    public void onEnable() {
        instance = this;
        API = new WithdrawerAPI(this);
        saveDefaultConfig();
        economyManager = new EconomyManager(this);
        commandManager = new CommandManager(this);

        messages = new MessagesFile(this);
        sounds = new SoundManager(this);
        pluginSettings = new PluginSettings(getConfig());

        itemInitializers = new WithdrawableItemInitializers();

        registerDefaultWithdrawable("money", (item) -> new MoneyItem(this, item));
        registerDefaultWithdrawable("exp", (item) -> new ExpItem(this, item));
        registerDefaultWithdrawable("health", (item) -> new HealthItem(this, item));

        withdrawableItemsStorage = new WithdrawableItemsStorage(this).load();
        itemHandler = new ItemHandler(this);

        updateConfig(this, messages);
        updateConfig(this, itemHandler);
        System.out.println("[Withdrawer] Updated Config!");

        registerCommand(new WithdrawCommand(this, "xp", (value, amount) -> new ExpItem(this, UUID.randomUUID(), value, amount)));
        registerCommand(new WithdrawCommand(this, "hp", (value, amount) -> new HealthItem(this, UUID.randomUUID(), value, amount)));
        registerCommand(new WithdrawMoneyCommand(this));

        registerCommand(new GiftCommand(this, "xp", (value, amount) -> new ExpItem(this, UUID.randomUUID(), value, amount)));
        registerCommand(new GiftCommand(this, "hp", (value, amount) -> new HealthItem(this, UUID.randomUUID(), value, amount)));
        registerCommand(new GiftMoneyCommand(this));

        registerCommand(new HelpCommand(this));
        registerCommand(new ReloadCommand(this));
        registerCommand(new ToggleCommand(this));

        registerListener(new HealthResetListener());
        registerListener(new ItemDropListener(this));
        registerListener(new RedeemListener(this));
        registerListener(new ConnectionListener(this));
        registerListener(new CraftingVillagerListener(this));

        new PlaceholderHook(this).register();
    }

    @Override
    public void onDisable() {
        commandManager = null;
        economyManager = null;

        messages = null;
        sounds = null;
        pluginSettings = null;
        itemHandler = null;
        itemInitializers = null;

        reloadConfig();
        HandlerList.unregisterAll(this);

    }

    public static Withdrawer getInstance() {
        return instance;
    }

    public static WithdrawerAPI getWithdrawerAPI() {
        return API;
    }

    private void registerDefaultWithdrawable(String name, Function<NBTItem, WithdrawableItem> function) {
        itemInitializers.addDefault(name, function);
    }

    public void registerCommand(SimpleCommand command) {
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
