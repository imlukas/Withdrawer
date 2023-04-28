package me.imlukas.withdrawer.config;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.item.wrapper.ItemStackWrapper;
import me.imlukas.withdrawer.utils.item.ItemBuilder;
import me.imlukas.withdrawer.utils.storage.YMLBase;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class DefaultItemsHandler extends YMLBase {

    private final List<Material> consumables = new ArrayList<>(Arrays.asList(Material.POTION, Material.SPLASH_POTION, Material.LINGERING_POTION));

    private final Map<String, ItemStack> defaultItems = new HashMap<>();

    public DefaultItemsHandler(Withdrawer plugin) {
        super(plugin, "items.yml");
        load();
    }

    private void load() {
        for (String identifier : getConfiguration().getKeys(false)) {
            defaultItems.put(identifier, getItem(identifier));
        }
    }

    public ItemStackWrapper createWrapper(String identifier, int value, int amount, UUID uuid) {
        return new ItemStackWrapper(defaultItems.get(identifier).clone(), value, amount, uuid);
    }

    private ItemStack getItem(String identifier) {
        ItemStack item = ItemBuilder.fromSection(getConfiguration().getConfigurationSection(identifier));

        if (!checkMaterial(item.getType())) {
            item.setType(Material.BARRIER);
        }

        return item;
    }

    private boolean checkMaterial(Material itemMaterial) {
        if (itemMaterial == null || itemMaterial.isEdible() || consumables.contains(itemMaterial)) {
            return false;
        }
        return true;
    }
}
