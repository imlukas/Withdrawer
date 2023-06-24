package me.imlukas.withdrawer.item.registry;

import me.imlukas.withdrawer.item.WithdrawableItem;
import me.imlukas.withdrawer.utils.pdc.PDCWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class WithdrawableItemInitializers {

    private final Map<String, Function<PDCWrapper, WithdrawableItem>> initializers = new HashMap<>();

    public void addDefault(String identifier, Function<PDCWrapper, WithdrawableItem> supplier) {
        initializers.put(identifier, supplier);
    }

    public void removeDefault(String identifier) {
        initializers.remove(identifier);
    }

    public WithdrawableItem getNewItemInstance(PDCWrapper pdcWrapper) {
        String identifier = pdcWrapper.getString("withdrawer-type");
        return initializers.get(identifier).apply(pdcWrapper);
    }

    public WithdrawableItem getNewItemInstance(String identifier, PDCWrapper pdcWrapper) {
        return initializers.get(identifier).apply(pdcWrapper);
    }
}
