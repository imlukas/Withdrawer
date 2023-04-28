package me.imlukas.withdrawer.item.registry;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.item.WithdrawableItem;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class WithdrawableItemInitializers {
    
    private final Map<String, Function<NBTItem, WithdrawableItem>> initializers = new HashMap<>();
    
    public void addDefault(String identifier, Function<NBTItem, WithdrawableItem> supplier) {
        initializers.put(identifier, supplier);
    }

    public void removeDefault(String identifier) {
        initializers.remove(identifier);
    }

    public WithdrawableItem getNewItemInstance(NBTItem nbtItem) {
        String identifier = nbtItem.getString("withdrawer-type");
        return initializers.get(identifier).apply(nbtItem);
    }

    public WithdrawableItem getNewItemInstance(String identifier, NBTItem nbtItem) {
        return initializers.get(identifier).apply(nbtItem);
    }
}
