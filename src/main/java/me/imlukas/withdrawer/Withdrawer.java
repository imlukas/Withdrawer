package me.imlukas.withdrawer;

import lombok.Getter;
import me.imlukas.withdrawer.commands.BankNoteWithdrawCommand;
import me.imlukas.withdrawer.commands.ExpBottleCommand;
import me.imlukas.withdrawer.commands.HealthWithdrawCommand;
import me.imlukas.withdrawer.commands.WithdrawerCommand;
import me.imlukas.withdrawer.config.ConfigHandler;
import me.imlukas.withdrawer.listeners.InventoryClickListener;
import me.imlukas.withdrawer.listeners.ItemDropListener;
import me.imlukas.withdrawer.listeners.PlayerInteractListener;
import me.imlukas.withdrawer.listeners.PlayerJoinListener;
import me.imlukas.withdrawer.managers.ExpBottle;
import me.imlukas.withdrawer.managers.HealthItem;
import me.imlukas.withdrawer.managers.Note;
import me.imlukas.withdrawer.utils.EconomyUtil;
import me.imlukas.withdrawer.utils.ExpUtil;
import me.imlukas.withdrawer.utils.HealthUtil;
import me.imlukas.withdrawer.utils.TextUtil;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import me.imlukas.withdrawer.utils.illusion.storage.YMLBase;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

@Getter
public final class Withdrawer extends JavaPlugin {
    private Economy econ;
    private MessagesFile messages;
    private ConfigHandler configHandler;
    private TextUtil textUtil;
    private EconomyUtil economyUtil;
    private ExpUtil expUtil;
    private HealthUtil healthUtil;
    private Note noteManager;
    private ExpBottle expBottleManager;
    private HealthItem healthItemManager;
    private PlayerPointsAPI playerPointsAPI;

    @Override
    public void onEnable() {
        setupEconomies();
        expUtil = new ExpUtil();
        messages = new MessagesFile(this);
        configHandler = new ConfigHandler(this);
        textUtil = new TextUtil(this);
        economyUtil = new EconomyUtil(this);
        healthUtil = new HealthUtil();
        noteManager = new Note(this);
        expBottleManager = new ExpBottle(this);
        healthItemManager = new HealthItem(this);
        System.out.println("[Withdrawer] Registered Classes!");
        updateConfig(this, messages);
        updateConfig(this, configHandler);

        System.out.println("[Withdrawer] Updated Config!");
        registerCommands();
        System.out.println("[Withdrawer] Registered Commands!");
        registerListeners();
        System.out.println("[Withdrawer] Registered Listeners!");


        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        reloadConfig();
        messages = null;
        configHandler = null;
        noteManager = null;
        expBottleManager = null;
        healthItemManager = null;

    }

    private void updateConfig(JavaPlugin plugin, YMLBase base) {
        File file = base.getFile();
        InputStream stream = plugin.getResource(file.getAbsolutePath().replace(plugin.getDataFolder().getAbsolutePath() + File.separator, ""));

        if(stream ==null)
            return;

        FileConfiguration cfg = base.getConfiguration();
        FileConfiguration other = YamlConfiguration.loadConfiguration(new InputStreamReader(stream));

        for(String key : other.getKeys(true)) {
            if (!cfg.isSet(key)){
                cfg.set(key, other.get(key));
            }
        }
        base.save();
    }

    private void registerCommands() {
        getCommand("withdrawmoney").setExecutor(new BankNoteWithdrawCommand(this));
        getCommand("withdrawxp").setExecutor(new ExpBottleCommand(this));
        getCommand("withdrawhp").setExecutor(new HealthWithdrawCommand(this));
        getCommand("withdrawer").setExecutor(new WithdrawerCommand(this));
    }

    private void registerListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ItemDropListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    private void setupEconomies() {

        String economy = this.getConfig().getString("economy-plugin");
        if (economy.equalsIgnoreCase("playerpoints")) {
            if (Bukkit.getPluginManager().getPlugin("PlayerPoints") != null) {
                playerPointsAPI = PlayerPoints.getInstance().getAPI();
                System.out.println("[Withdrawer] Found PlayerPoints!");
            } else {
                System.out.println("[Withdrawer] PlayerPoints not found! DISABLING PLUGIN!");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        } else if (!setupEconomy() && economy.equalsIgnoreCase("vault")) {
            System.out.println("[Withdrawer] Vault or plugin that handles vault economy not found! DISABLING PLUGIN!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    public void addDefaults(JavaPlugin plugin, YMLBase base) {

    }

    // Vault Integration
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        System.out.println("[Withdrawer] Found Vault and EconomyHandler!");
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEconomy() {
        return econ;
    }
}
