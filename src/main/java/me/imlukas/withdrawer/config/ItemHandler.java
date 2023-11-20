package me.imlukas.withdrawer.config;

import me.imlukas.withdrawer.WithdrawerPlugin;
import me.imlukas.withdrawer.utils.item.ItemBuilder;
import me.imlukas.withdrawer.utils.storage.YMLBase;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ItemHandler extends YMLBase {

    private final List<Material> consumables = new ArrayList<>(Arrays.asList(Material.POTION, Material.SPLASH_POTION, Material.LINGERING_POTION));
    private final Map<String, ItemStack> defaultItems = new HashMap<>();

    public ItemHandler(WithdrawerPlugin plugin) {
        super(plugin, "items.yml");
        load();
    }

    private void load() {
        for (String identifier : getConfiguration().getKeys(false)) {
            defaultItems.put(identifier, getItem(identifier));
        }
    }

    public ItemStack get(String identifier) {
        return defaultItems.get(identifier).clone();
    }

    private ItemStack getItem(String identifier) {
        ItemStack item = ItemBuilder.fromSection(getConfiguration().getConfigurationSection(identifier));

        if (!checkMaterial(item.getType())) {
            item.setType(Material.BARRIER);
        }

        return item;
    }

    private boolean checkMaterial(Material itemMaterial) {
        return itemMaterial != null && !itemMaterial.isEdible() && !consumables.contains(itemMaterial);
    }

    public int getMinValue(String identifier) {
        return getConfiguration().getInt(identifier + ".min");
    }

    public int getMaxValue(String identifier) {
        return getConfiguration().getInt(identifier + ".max");
    }
}
