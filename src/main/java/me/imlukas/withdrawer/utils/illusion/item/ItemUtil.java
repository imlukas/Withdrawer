package me.imlukas.withdrawer.utils.illusion.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Map;

public final class ItemUtil {

    private ItemUtil() {

    }

    public static void give(Player player, ItemStack item) {
        PlayerInventory inv = player.getInventory();

        for (Map.Entry<Integer, ItemStack> entry : inv.addItem(item).entrySet()) {
            ItemStack copy = entry.getValue().clone();
            item.setAmount(entry.getKey());
            player.getWorld().dropItemNaturally(player.getLocation(), copy);
        }
    }
}
