package me.imlukas.withdrawer.config;

import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;

@Data
public class PluginSettings {

    private final boolean dropable, craftable, villagerTrades, xpDropable;

    public PluginSettings(boolean dropable, boolean craftable, boolean villagerTrades, boolean xpDropable) {
        this.dropable = dropable;
        this.craftable = craftable;
        this.villagerTrades = villagerTrades;
        this.xpDropable = xpDropable;
    }

    public PluginSettings(FileConfiguration configuration) {
        this.dropable = configuration.getBoolean("drop");
        this.craftable = configuration.getBoolean("crafting");
        this.villagerTrades = configuration.getBoolean("villager-trades");
        this.xpDropable = configuration.getBoolean("xp-drop");
    }
}
