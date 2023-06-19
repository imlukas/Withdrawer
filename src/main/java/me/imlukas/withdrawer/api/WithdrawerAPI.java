package me.imlukas.withdrawer.api;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.commands.withdraw.WithdrawCommand;
import me.imlukas.withdrawer.config.ItemHandler;
import me.imlukas.withdrawer.economy.EconomyManager;
import me.imlukas.withdrawer.economy.IEconomy;
import me.imlukas.withdrawer.item.WithdrawableItem;
import me.imlukas.withdrawer.item.registry.WithdrawableItemInitializers;
import me.imlukas.withdrawer.item.registry.WithdrawableItemsStorage;
import me.imlukas.withdrawer.utils.command.SimpleCommand;
import org.bukkit.command.CommandExecutor;

import java.util.function.BiFunction;
import java.util.function.Function;

public class WithdrawerAPI {

    private final Withdrawer plugin;

    public WithdrawerAPI(Withdrawer plugin) {
        this.plugin = plugin;
    }

    /**
     * Registers an economy, so it can be used to withdraw money
     * @param economy The economy to register
     */
    public void registerEconomy(IEconomy economy) {
        plugin.getEconomyManager().registerEconomy(economy);
    }

    /**
     * Registers a withdraw command with the given identifier and item function<br>
     * The identifier is used for tab complete and to get config values.<br>
     * <b>This is a general implementation for a withdraw command, you can always create your own.</b>
     * @param identifier The identifier of the command
     * @param itemFunction The function to create the item, takes a value and an amount, returns a new WithdrawableItem.
     */
    public void registerWithdrawCommand(String identifier, BiFunction<Integer, Integer, WithdrawableItem> itemFunction) {
        plugin.registerCommand(new WithdrawCommand(plugin, identifier, itemFunction));
    }

    /**
     * Registers a command using withdrawer's command system
     * @param command The command to register
     */
    public void registerCommand(SimpleCommand command) {
        plugin.registerCommand(command);
    }

    /**
     * Registers a withdrawable item, so it can be retrieved when the player joins the server.<br>
     * <b>This is obligatory, make sure to register your item!</b>
     * @param identifier The identifier of the item
     * @param itemFunction The function to create the item, takes a NBTItem, returns a new WithdrawableItem. Most have a constructor that takes a NBTItem.
     */
    public void registerWithdrawableItem(String identifier, Function<NBTItem, WithdrawableItem> itemFunction) {
        plugin.getItemInitializers().addDefault(identifier, itemFunction);
    }

    /**
     * Gets the withdrawer item storage, where all WithdrawableItems are stored
     * @return The withdrawer item storage
     */
    public WithdrawableItemsStorage getItemStorage() {
        return plugin.getWithdrawableItemsStorage();
    }

    public WithdrawableItemInitializers getItemInitializers() {
        return plugin.getItemInitializers();
    }

    /**
     * Gets the item handler, where all display items are stored <br>
     * Use this to retrieve display items for your withdrawables
     * @return The item handler
     */
    public ItemHandler getItemHandler() {
        return plugin.getItemHandler();
    }

}
