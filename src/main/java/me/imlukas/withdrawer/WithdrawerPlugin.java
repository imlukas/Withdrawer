package me.imlukas.withdrawer;

import lombok.Getter;
import me.imlukas.withdrawer.api.WithdrawerAPI;
import me.imlukas.withdrawer.commands.HelpCommand;
import me.imlukas.withdrawer.commands.ReloadCommand;
import me.imlukas.withdrawer.commands.ToggleCommand;
import me.imlukas.withdrawer.commands.gift.GiftCommand;
import me.imlukas.withdrawer.commands.gift.GiftMoneyCommand;
import me.imlukas.withdrawer.commands.give.GiveCommand;
import me.imlukas.withdrawer.commands.give.GiveMoneyCommand;
import me.imlukas.withdrawer.commands.withdraw.WithdrawCommand;
import me.imlukas.withdrawer.commands.withdraw.WithdrawMoneyCommand;
import me.imlukas.withdrawer.config.ItemHandler;
import me.imlukas.withdrawer.config.PluginSettings;
import me.imlukas.withdrawer.economy.EconomyManager;
import me.imlukas.withdrawer.item.impl.ExpItem;
import me.imlukas.withdrawer.item.impl.HealthItem;
import me.imlukas.withdrawer.item.impl.MoneyItem;
import me.imlukas.withdrawer.item.registry.WithdrawableItemInitializers;
import me.imlukas.withdrawer.item.registry.WithdrawableItemsStorage;
import me.imlukas.withdrawer.listener.*;
import me.imlukas.withdrawer.utils.command.SimpleCommand;
import me.imlukas.withdrawer.utils.command.impl.CommandManager;
import me.imlukas.withdrawer.utils.interactions.Sounds;
import me.imlukas.withdrawer.utils.interactions.Messages;
import me.imlukas.withdrawer.utils.pdc.PDCWrapper;
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
import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
public final class WithdrawerPlugin extends JavaPlugin {

    private static WithdrawerAPI API;
    private Messages messages;
    private Sounds sounds;
    private PluginSettings pluginSettings;
    private CommandManager commandManager;
    private EconomyManager economyManager;
    private ItemHandler itemHandler;


    @Override
    public void onEnable() {
        API = new WithdrawerAPI(this);
        saveDefaultConfig();
        economyManager = new EconomyManager(this);
        commandManager = new CommandManager(this);

        messages = new Messages(this);
        sounds = new Sounds(this);
        pluginSettings = new PluginSettings(getConfig());


        registerDefaultWithdrawable("money", (item) -> new MoneyItem(this, item));
        registerDefaultWithdrawable("exp", (item) -> new ExpItem(this, item));
        registerDefaultWithdrawable("hp", (item) -> new HealthItem(this, item));

        itemHandler = new ItemHandler(this);

        updateConfig(this, messages, itemHandler);
        System.out.println("[Withdrawer] Updated Config!");

        BiFunction<Integer, Integer, WithdrawableItem> expFunction = (value, amount) -> new ExpItem(this, value, amount);
        BiFunction<Integer, Integer, WithdrawableItem> hpFunction = (value, amount) -> new HealthItem(this, value, amount);

        registerCommand(new WithdrawCommand(this, "xp", expFunction));
        registerCommand(new WithdrawCommand(this, "hp", hpFunction));
        registerCommand(new WithdrawMoneyCommand(this));

        registerCommand(new GiftCommand(this, "xp", expFunction));
        registerCommand(new GiftCommand(this, "hp", hpFunction));
        registerCommand(new GiftMoneyCommand(this));

        registerCommand(new GiveCommand(this, "xp", expFunction));
        registerCommand(new GiveCommand(this, "hp", hpFunction));
        registerCommand(new GiveMoneyCommand(this));

        registerCommand(new HelpCommand(this));
        registerCommand(new ReloadCommand(this));
        registerCommand(new ToggleCommand(this));

        registerListener(new HealthResetListener());
        registerListener(new ItemPickupAndDropListener(this));
        registerListener(new RedeemListener(this));
        registerListener(new ConnectionListener(this));
        registerListener(new CraftingVillagerListener(this));
        // registerListener(new StackingListener(this));
        registerListener(new ItemRegistryListener(this));
    }

    @Override
    public void onDisable() {
        commandManager = null;
        economyManager = null;

        messages = null;
        sounds = null;
        pluginSettings = null;
        itemHandler = null;

        reloadConfig();
        HandlerList.unregisterAll(this);

    }

    public static WithdrawerAPI getWithdrawerAPI() {
        return API;
    }


    public void registerCommand(SimpleCommand command) {
        commandManager.register(command);
    }

    private void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    private void updateConfig(JavaPlugin plugin, YMLBase... base) {
        for (YMLBase ymlBase : base) {
            File file = ymlBase.getFile();
            InputStream stream = plugin.getResource(file.getAbsolutePath().replace(plugin.getDataFolder().getAbsolutePath() + File.separator, ""));

            if (stream == null)
                return;

            FileConfiguration cfg = ymlBase.getConfiguration();
            FileConfiguration other = YamlConfiguration.loadConfiguration(new InputStreamReader(stream));

            for (String key : other.getKeys(true)) {
                if (!cfg.isSet(key)) {
                    cfg.set(key, other.get(key));
                }
            }
            ymlBase.save();
        }
    }
}
