package me.imlukas.withdrawer.v2.config;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.constant.ItemType;
import me.imlukas.withdrawer.utils.TextUtil;
import me.imlukas.withdrawer.utils.item.ItemBuilder;
import me.imlukas.withdrawer.v2.item.ItemStackWrapper;
import me.imlukas.withdrawer.v2.utils.storage.YMLBase;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class DefaultItemsHandler extends YMLBase {

    private final List<Material> consumables = new ArrayList<>(Arrays.asList(Material.POTION, Material.SPLASH_POTION, Material.LINGERING_POTION));

    private final Map<ItemType, ItemStack> defaultItems = new HashMap<>();

    public DefaultItemsHandler(Withdrawer plugin) {
        super(plugin, "config.yml");
        load();
    }

    private void load() {
        defaultItems.put(ItemType.BANKNOTE, getBankNoteItem());
        defaultItems.put(ItemType.HEALTH, getHealthItem());
        defaultItems.put(ItemType.EXP, getExpItem());
    }

    public ItemStackWrapper getWrapper(ItemType itemType) {
        return new ItemStackWrapper(defaultItems.get(itemType).clone());
    }

    private ItemStack getBankNoteItem() {
        Material itemMaterial = getItemMaterial(ItemType.BANKNOTE);

        return new ItemBuilder(itemMaterial)
                .name(TextUtil.setColor(getConfiguration().getString(ItemType.BANKNOTE.getLowercase() + ".name")))
                .glowing(true)
                .build();
    }

    private ItemStack getHealthItem() {
        Material itemMaterial = getItemMaterial(ItemType.HEALTH);

        return new ItemBuilder(itemMaterial)
                .name(TextUtil.setColor(getConfiguration().getString(ItemType.HEALTH.getLowercase() + ".name")))
                .glowing(true)
                .build();
    }

    private ItemStack getExpItem() {
        Material itemMaterial = getItemMaterial(ItemType.EXP);

        return new ItemBuilder(itemMaterial)
                .name(TextUtil.setColor(getConfiguration().getString(ItemType.EXP.getLowercase() + ".name")))
                .glowing(true)
                .build();
    }


    private Material getItemMaterial(ItemType type) {
        Material itemMaterial = Material.getMaterial(getConfiguration().getString(type.getLowercase() + ".item").toUpperCase());

        if (itemMaterial == null || itemMaterial.isEdible() || consumables.contains(itemMaterial)) {
            itemMaterial = Material.BARRIER;
        }
        return itemMaterial;
    }
}
