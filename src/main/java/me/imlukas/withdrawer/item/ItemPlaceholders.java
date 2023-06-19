package me.imlukas.withdrawer.item;

import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemPlaceholders {

    private final Map<String, String> placeholders = new HashMap<>();

    public ItemPlaceholders(Map<String, String> defaults) {
        placeholders.putAll(defaults);
    }

    public void addPlaceholder(String placeholder, String value) {
        if (placeholder.startsWith("%") && placeholder.endsWith("%")) {
            placeholder = placeholder.substring(1, placeholder.length() - 1);
        }

        placeholders.put(placeholder, value);
    }

    public void removePlaceholder(String placeholder) {
        placeholders.remove(placeholder);
    }

    public void clearPlaceholders() {
        placeholders.clear();
    }

    public void replace(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>(meta.getLore());

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String toReplace = entry.getKey();
            String replacement = entry.getValue();

            lore.replaceAll(line -> line.replace("%" + toReplace + "%", replacement));
        }

        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }
}
