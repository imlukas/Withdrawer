package me.imlukas.withdrawer.v2.economy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EconomyManager {

    private final Map<String, IEconomy> registeredEconomies = new ConcurrentHashMap<>();

    public void registerEconomy(IEconomy economy) {
        registeredEconomies.put(economy.getIdentifier(), economy);
    }

    public void unregisterEconomy(String identifier) {
        registeredEconomies.remove(identifier);
    }

    public IEconomy getEconomy(String identifier) {
        return registeredEconomies.get(identifier);
    }

    public IEconomy getFirstEconomy() {
        return registeredEconomies.values().stream().findFirst().orElse(null);
    }

    public boolean isEconomyRegistered(String identifier) {
        return registeredEconomies.containsKey(identifier);
    }
}
