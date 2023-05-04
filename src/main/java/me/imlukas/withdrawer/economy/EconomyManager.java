package me.imlukas.withdrawer.economy;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.economy.impl.PlayerPoints;
import me.imlukas.withdrawer.economy.impl.Tokens;
import me.imlukas.withdrawer.economy.impl.Vault;
import me.realized.tokenmanager.api.TokenManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EconomyManager {

    private final Withdrawer plugin;

    public EconomyManager(Withdrawer plugin) {
        this.plugin = plugin;
        setupEconomies();
    }

    private final Map<String, IEconomy> registeredEconomies = new LinkedHashMap<>();

    public void registerEconomy(IEconomy economy) {
        registeredEconomies.put(economy.getIdentifier(), economy);
    }

    public void unregisterEconomy(String identifier) {
        registeredEconomies.remove(identifier);
    }

    public IEconomy getEconomy(String identifier) {
        return registeredEconomies.get(identifier);
    }

    public List<String> getEconomyIdentifiers() {
        return registeredEconomies.keySet().stream().toList();
    }

    public IEconomy getFirstEconomy() {
        return registeredEconomies.values().iterator().next();
    }

    public boolean isEconomyRegistered(String identifier) {
        return registeredEconomies.containsKey(identifier);
    }

    public void setupEconomies() {
        List<String> economies = plugin.getConfig().getStringList("economy-plugins");

        if (economies.isEmpty()) {
            System.err.println("[Withdrawer] No economy plugins found! DISABLING PLUGIN!");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }

        for (String economy : economies) {
            if (economy.equalsIgnoreCase("vault") && !setupVault()) {
                System.err.println("[Withdrawer] Vault or plugin that handles vault economy not found! DISABLING PLUGIN!");
                Bukkit.getPluginManager().disablePlugin(plugin);
                return;
            }

            if (economy.equalsIgnoreCase("playerpoints")) {
                if (Bukkit.getPluginManager().getPlugin("PlayerPoints") != null) {
                    registerEconomy(new PlayerPoints(org.black_ixx.playerpoints.PlayerPoints.getInstance().getAPI()));
                    System.out.println("[Withdrawer] Found PlayerPoints!");
                    continue;
                }

                System.err.println("[Withdrawer] PlayerPoints not found! DISABLING PLUGIN!");
                Bukkit.getPluginManager().disablePlugin(plugin);
                continue;
            }

            if (economy.equalsIgnoreCase("tokenmanager")) {
                TokenManager tokenManager = (TokenManager) Bukkit.getServer().getPluginManager().getPlugin("TokenManager");
                if (tokenManager != null) {
                    registerEconomy(new Tokens(tokenManager));
                    System.out.println("[Withdrawer] Found TokenManager!");
                    continue;
                }
                System.err.println("[Withdrawer] TokenManager not found! DISABLING PLUGIN!");
                Bukkit.getPluginManager().disablePlugin(plugin);
            }
        }
    }


    private boolean setupVault() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        System.out.println("[Withdrawer] Found Vault and EconomyHandler!");
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        Economy econ = rsp.getProvider();
        registerEconomy(new Vault(econ));
        return true;
    }

}
