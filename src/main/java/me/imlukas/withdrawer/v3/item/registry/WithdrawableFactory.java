package me.imlukas.withdrawer.v3.item.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class WithdrawableFactory {

    private final Map<String, Supplier<Withdrawable>> itemSuppliers = new HashMap<>();

    public void register(String identifier, Supplier<Withdrawable> supplier) {
        itemSuppliers.put(identifier, supplier);
    }

    public <T extends Withdrawable> T create(String identifier) {
        return (T) itemSuppliers.get(identifier).get();
    }


}
