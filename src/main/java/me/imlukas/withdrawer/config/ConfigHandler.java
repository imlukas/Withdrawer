package me.imlukas.withdrawer.config;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.constant.ItemType;
import me.imlukas.withdrawer.utils.TextUtil;
import me.imlukas.withdrawer.utils.item.ItemBuilder;
import me.imlukas.withdrawer.utils.storage.YMLBase;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigHandler extends YMLBase {

    private final List<Material> consumables = new ArrayList<>(Arrays.asList(Material.POTION, Material.SPLASH_POTION, Material.LINGERING_POTION));

    public ConfigHandler(Withdrawer plugin) {
        super(plugin, "config.yml");
    }


    public ItemStack getBankNoteItem() {
        Material itemMaterial = getItemMaterial(ItemType.BANKNOTE);

        return new ItemBuilder(itemMaterial)
                .name(TextUtil.setColor(getConfiguration().getString(ItemType.BANKNOTE.lowercase + ".name")))
                .glowing(true)
                .build();
    }

    public ItemStack getHealthItem() {
        Material itemMaterial = getItemMaterial(ItemType.HEALTH);

        return new ItemBuilder(itemMaterial)
                .name(TextUtil.setColor(getConfiguration().getString(ItemType.HEALTH.lowercase + ".name")))
                .glowing(true)
                .build();
    }

    public ItemStack getExpItem() {
        Material itemMaterial = getItemMaterial(ItemType.EXPBOTTLE);

        return new ItemBuilder(itemMaterial)
                .name(TextUtil.setColor(getConfiguration().getString(ItemType.EXPBOTTLE.lowercase + ".name")))
                .glowing(true)
                .build();
    }


    private Material getItemMaterial(ItemType type) {
        Material itemMaterial = Material.getMaterial(getConfiguration().getString(type.lowercase + ".item").toUpperCase());

        if (itemMaterial == null || itemMaterial.isEdible() || consumables.contains(itemMaterial)) {
            itemMaterial = Material.BARRIER;
        }
        return itemMaterial;
    }
}
