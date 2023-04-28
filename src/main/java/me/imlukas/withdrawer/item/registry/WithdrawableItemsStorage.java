package me.imlukas.withdrawer.item.registry;

import me.imlukas.withdrawer.item.Withdrawable;
import me.imlukas.withdrawer.item.WithdrawableItem;

import java.util.*;

public class WithdrawableItemsStorage {

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
