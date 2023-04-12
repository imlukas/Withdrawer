package me.imlukas.withdrawer.v2.item.registry;

import me.imlukas.withdrawer.v2.item.WithdrawableItem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WithdrawableItemsRegistry {

    private final Map<UUID, WithdrawableItem> withdrawableItems = new HashMap<>();

    public void addItem(WithdrawableItem item) {
        withdrawableItems.put(item.getUuid(), item);
    }

    public void removeItem(WithdrawableItem item) {
        withdrawableItems.remove(item.getUuid());
    }

    public WithdrawableItem getItem(UUID uuid) {
        return withdrawableItems.get(uuid);
    }

    public Map<UUID, WithdrawableItem> getItems() {
        return withdrawableItems;
    }
}
