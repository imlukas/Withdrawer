package me.imlukas.withdrawer.economy;

import me.imlukas.withdrawer.WithdrawerPlugin;
import me.imlukas.withdrawer.economy.impl.PlayerPointsAdapter;
import me.imlukas.withdrawer.economy.impl.TokensAdapter;
import me.imlukas.withdrawer.economy.impl.VaultAdapter;
import me.realized.tokenmanager.api.TokenManager;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EconomyManager {

    private final WithdrawerPlugin plugin;

    public EconomyManager(WithdrawerPlugin plugin) {
        this.plugin = plugin;
        setupEconomies();
    }

    private final Map<String, Economy> registeredEconomies = new LinkedHashMap<>();

    public void registerEconomy(Economy economy) {
        registeredEconomies.put(economy.getIdentifier(), economy);
    }

    public void unregisterEconomy(String identifier) {
        registeredEconomies.remove(identifier);
    }

    public Economy getEconomy(String identifier) {
        return registeredEconomies.get(identifier);
    }

    public List<String> getEconomyIdentifiers() {
        return registeredEconomies.keySet().stream().toList();
    }

    public Economy getFirstEconomy() {
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

            switch (economy) {
                case "vault" -> setupVault();
                case "tokenmanager" -> setupTokenManager();
                case "playerpoints" -> setupPlayerPoints();
                default -> System.out.println("[Withdrawer] Unknown economy plugin: " + economy);
            }
        }
    }

    private void setupPlayerPoints() {
        if (Bukkit.getPluginManager().getPlugin("PlayerPoints") != null) {
            registerEconomy(new PlayerPointsAdapter(PlayerPoints.getInstance().getAPI()));
            System.out.println("[Withdrawer] Found PlayerPoints!");
            return;
        }

        System.err.println("[Withdrawer] PlayerPoints not found! DISABLING PLUGIN!");
        Bukkit.getPluginManager().disablePlugin(plugin);
    }


    private boolean setupTokenManager() {
        TokenManager tokenManager = (TokenManager) Bukkit.getServer().getPluginManager().getPlugin("TokenManager");
        if (tokenManager != null) {
            registerEconomy(new TokensAdapter(tokenManager));
            System.out.println("[Withdrawer] Found TokenManager!");
            return true;
        }
        System.err.println("[Withdrawer] TokenManager not found! DISABLING PLUGIN!");
        Bukkit.getPluginManager().disablePlugin(plugin);
        return false;
    }

    private boolean setupVault() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            System.err.println("[Withdrawer] Vault or plugin that handles vault economy not found! DISABLING PLUGIN!");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return false;
        }
        System.out.println("[Withdrawer] Found Vault and EconomyHandler!");
        RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> rsp = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (rsp == null) {
            System.err.println("[Withdrawer] Vault or plugin that handles vault economy not found! DISABLING PLUGIN!");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return false;
        }

        net.milkbowl.vault.economy.Economy econ = rsp.getProvider();
        registerEconomy(new VaultAdapter(econ));
        return true;
    }

}
