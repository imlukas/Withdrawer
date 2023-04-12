package me.imlukas.withdrawer;

import lombok.Getter;
import me.imlukas.withdrawer.v2.config.DefaultItemsHandler;
import me.imlukas.withdrawer.v2.economy.EconomyManager;
import me.imlukas.withdrawer.v2.economy.impl.PlayerPointsEconomy;
import me.imlukas.withdrawer.v2.economy.impl.Tokens;
import me.imlukas.withdrawer.v2.economy.impl.Vault;
import me.imlukas.withdrawer.v2.item.registry.WithdrawableItemsRegistry;
import me.imlukas.withdrawer.v2.utils.command.impl.CommandManager;
import me.imlukas.withdrawer.v2.utils.storage.MessagesFile;
import me.imlukas.withdrawer.v2.utils.storage.YMLBase;
import me.realized.tokenmanager.api.TokenManager;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Getter
public final class Withdrawer extends JavaPlugin {
    private Economy econ;
    private MessagesFile messages;
    private CommandManager commandManager;
    private EconomyManager economyManager;
    private DefaultItemsHandler defaultItemsHandler;
    private WithdrawableItemsRegistry withdrawableItemsRegistry;
    private TokenManager tokenManagerAPI;

    @Override
    public void onEnable() {
        setupEconomies();
        messages = new MessagesFile(this);
        commandManager = new CommandManager(this);
        economyManager = new EconomyManager();

        withdrawableItemsRegistry = new WithdrawableItemsRegistry();

        defaultItemsHandler = new DefaultItemsHandler(this);

        System.out.println("[Withdrawer] Registered Classes!");
        updateConfig(this, messages);
        updateConfig(this, defaultItemsHandler);

        System.out.println("[Withdrawer] Updated Config!");
        registerCommands();
        System.out.println("[Withdrawer] Registered Commands!");
        registerListeners();
        System.out.println("[Withdrawer] Registered Listeners!");

    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        reloadConfig();
        messages = null;

    }

    private void registerCommands() {
    }

    private void registerListeners() {
    }

    private void setupEconomies() {

        List<String> economies = getConfig().getStringList("economy-plugins");

        if (economies.isEmpty()) {
            System.err.println("[Withdrawer] No economy plugins found! DISABLING PLUGIN!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        for (String economy : economies) {
            if (economy.equalsIgnoreCase("vault") && !setupEconomy()) {
                System.err.println("[Withdrawer] Vault or plugin that handles vault economy not found! DISABLING PLUGIN!");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }

            if (economy.equalsIgnoreCase("playerpoints")) {
                if (Bukkit.getPluginManager().getPlugin("PlayerPoints") != null) {
                    economyManager.registerEconomy(new PlayerPointsEconomy(PlayerPoints.getInstance().getAPI()));
                    System.out.println("[Withdrawer] Found PlayerPoints!");
                    continue;
                }

                System.err.println("[Withdrawer] PlayerPoints not found! DISABLING PLUGIN!");
                Bukkit.getPluginManager().disablePlugin(this);
                continue;
            }

            if (economy.equalsIgnoreCase("tokenmanager")) {
                TokenManager tokenManager = (TokenManager) Bukkit.getServer().getPluginManager().getPlugin("TokenManager");
                if (tokenManager != null) {
                    tokenManagerAPI = tokenManager;
                    economyManager.registerEconomy(new Tokens(tokenManagerAPI));
                    System.out.println("[Withdrawer] Found TokenManager!");
                    continue;
                }
                System.err.println("[Withdrawer] TokenManager not found! DISABLING PLUGIN!");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }
    }

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
        economyManager.registerEconomy(new Vault(econ));
        return econ != null;
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
