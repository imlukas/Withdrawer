package me.imlukas.withdrawer;

import lombok.Getter;
import me.imlukas.withdrawer.commands.ExpBottleCommand;
import me.imlukas.withdrawer.commands.WithdrawCommand;
import me.imlukas.withdrawer.config.MessagesHandler;
import me.imlukas.withdrawer.listeners.PlayerInteractListener;
import me.imlukas.withdrawer.manager.NoteManager;
import me.imlukas.withdrawer.utils.TextUtil;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;
@Getter
public final class Withdrawer extends JavaPlugin {
    private Economy econ;
    private MessagesFile messages;
    private MessagesHandler messagesHandler;
    private TextUtil textUtil;
    private NoteManager noteManager;

    private Logger log;
    @Override
    public void onEnable() {
        messages = new MessagesFile(this);
        messagesHandler = new MessagesHandler(this);
        noteManager = new NoteManager(this);
        textUtil = new TextUtil(this);

        System.out.println("Registered Classes");
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        registerCommands();
        registerListeners();



        saveDefaultConfig();

        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    private void registerCommands(){
        getCommand("withdrawmoney").setExecutor(new WithdrawCommand(this));
        getCommand("withdrawxp").setExecutor(new ExpBottleCommand(this));
    }

    private void registerListeners(){
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
    }
    // Vault Integration
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
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
