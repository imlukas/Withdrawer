package me.imlukas.withdrawer;

import lombok.Getter;
import me.imlukas.withdrawer.commands.BankNoteWithdrawCommand;
import me.imlukas.withdrawer.commands.ExpBottleCommand;
import me.imlukas.withdrawer.listeners.InventoryClickListener;
import me.imlukas.withdrawer.listeners.ItemDropListener;
import me.imlukas.withdrawer.listeners.PlayerInteractListener;
import me.imlukas.withdrawer.managers.ExpBottleManager;
import me.imlukas.withdrawer.managers.NoteManager;
import me.imlukas.withdrawer.utils.EconomyUtil;
import me.imlukas.withdrawer.utils.ExpUtil;
import me.imlukas.withdrawer.utils.TextUtil;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

@Getter
public final class Withdrawer extends JavaPlugin {
    private Economy econ;
    private MessagesFile messages;
    private TextUtil textUtil;
    private EconomyUtil economyUtil;
    private ExpUtil expUtil;
    private NoteManager noteManager;
    private ExpBottleManager expBottleManager;
    private PlayerPointsAPI playerPointsAPI;

    private Logger log;

    @Override
    public void onEnable() {
        updateConfig();
        setupEconomies();
        expUtil = new ExpUtil();
        messages = new MessagesFile(this);
        textUtil = new TextUtil(this);
        economyUtil = new EconomyUtil(this);
        noteManager = new NoteManager(this);
        expBottleManager = new ExpBottleManager(this);

        System.out.println("[Withdrawer] Registered Classes!");
        registerCommands();
        System.out.println("[Withdrawer] Rsegistered Commands!");
        registerListeners();
        System.out.println("[Withdrawer] Registered Listeners!");


        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void updateConfig() {
        // TODO: make a proper config updater.
        saveDefaultConfig();
    }

    private void registerCommands() {
        getCommand("withdrawmoney").setExecutor(new BankNoteWithdrawCommand(this));
        getCommand("withdrawxp").setExecutor(new ExpBottleCommand(this));
    }

    private void registerListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ItemDropListener(this), this);
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
                return;
            }
        } else if (!setupEconomy() && economy.equalsIgnoreCase("vault")) {
            System.out.println("[Withdrawer] Vault dependency not found! DISABLING PLUGIN!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    // Vault Integration
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        if (getServer().getPluginManager().getPlugin("Essentials") == null) {
            System.out.println("[Withdrawer] Please install Essentials in order to use Vault!");
            return false;
        }
        System.out.println("[Withdrawer] Found Vault and Essentials!");
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
