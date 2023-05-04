package me.imlukas.withdrawer.item.registry;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.item.Withdrawable;
import me.imlukas.withdrawer.item.WithdrawableItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WithdrawableItemsStorage {
    private final WithdrawableItemInitializers defaultWithdrawables;

    public WithdrawableItemsStorage(Withdrawer plugin) {
        this.defaultWithdrawables = plugin.getItemInitializers();
    }

    public WithdrawableItemsStorage load() {
        long ms = System.nanoTime() / 1000000;
        for (Player player : Bukkit.getOnlinePlayers()) {
            Inventory inventory = player.getInventory();

            for (ItemStack itemStack : inventory.getContents()) {
                if (itemStack == null) {
                    continue;
                }

                NBTItem nbtItem = new NBTItem(itemStack);
                UUID itemUUID = nbtItem.getUUID("withdrawer-uuid");

                if (itemUUID == null) {
                    continue;
                }

                WithdrawableItem item = defaultWithdrawables.getNewItemInstance(nbtItem);
                addItem(item);
            }
        }

        return this;
    }

    private final Map<UUID, WithdrawableItem> withdrawableItems = new HashMap<>();

    public void addItem(WithdrawableItem item) {
        withdrawableItems.put(item.getUuid(), item);
    }

    public void removeItem(UUID uuid) {
        withdrawableItems.remove(uuid);
    }

    public void removeItem(Withdrawable item) {
        withdrawableItems.remove(item.getUuid());
    }

    public WithdrawableItem getItem(UUID uuid) {
        return withdrawableItems.get(uuid);
    }


    public Map<UUID, WithdrawableItem> getItems() {
        return withdrawableItems;
    }
}
