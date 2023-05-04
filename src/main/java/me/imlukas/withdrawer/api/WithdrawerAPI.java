package me.imlukas.withdrawer.api;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.commands.withdraw.WithdrawCommand;
import me.imlukas.withdrawer.item.WithdrawableItem;
import me.imlukas.withdrawer.item.registry.WithdrawableItemsStorage;

import java.util.function.BiFunction;
import java.util.function.Function;

public class WithdrawerAPI {

    private final Withdrawer plugin;

    public WithdrawerAPI(Withdrawer plugin) {
        this.plugin = plugin;
    }

    public void registerDefaultCommand(String identifier, BiFunction<Integer, Integer, WithdrawableItem> itemFunction) {
        plugin.registerCommand(new WithdrawCommand(plugin, identifier, itemFunction));
    }

    public void registerWithdrawableItem(String identifier, Function<NBTItem, WithdrawableItem> itemFunction) {
        plugin.getItemInitializers().addDefault(identifier, itemFunction);
    }

    public WithdrawableItemsStorage getItemStorage() {
        return plugin.getWithdrawableItemsStorage();
    }
}
