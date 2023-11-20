package me.imlukas.withdrawer.listener;

import me.imlukas.withdrawer.WithdrawerPlugin;
import me.imlukas.withdrawer.item.registry.WithdrawableItemsStorage;
import me.imlukas.withdrawer.utils.interactions.Messages;
import me.imlukas.withdrawer.utils.pdc.PDCWrapper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class StackingListener implements Listener {

    private final WithdrawerPlugin plugin;
    private final Messages messages;
    private final WithdrawableItemsStorage withdrawableItemsStorage;

    public StackingListener(WithdrawerPlugin plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMessages();
        this.withdrawableItemsStorage = plugin.getWithdrawableItemsStorage();
    }

    @EventHandler
    public void onStack(InventoryClickEvent event) {
        ItemStack current = event.getCurrentItem();
        ItemStack cursor = event.getCursor();

        if (event.isRightClick()) {
            return;
        }

        if (current == null || cursor == null || current.getType().isAir() || cursor.getType().isAir()) {
            return;
        }

        int currentAmount = current.getAmount();
        int cursorAmount = cursor.getAmount();

        if (currentAmount == 64 || cursorAmount == 64) {
            messages.sendMessage(event.getWhoClicked(), "stacking.max-stack");
            return;
        }

        PDCWrapper pdcWrapper = new PDCWrapper(plugin, current);
        UUID currentItemId = pdcWrapper.getUUID("withdrawer-uuid");

        pdcWrapper = new PDCWrapper(plugin, cursor);
        UUID cursorItemId = pdcWrapper.getUUID("withdrawer-uuid");

        if (currentItemId == null || cursorItemId == null) {
            return;
        }

        WithdrawableItem currentItem = withdrawableItemsStorage.getItem(currentItemId);
        WithdrawableItem cursorItem = withdrawableItemsStorage.getItem(cursorItemId);

        if (currentItem == null || cursorItem == null) {
            return;
        }

        if (currentItem.getValue() != cursorItem.getValue()) {
            return;
        }

        if (currentItem.getAmount() == 64 || cursorItem.getAmount() == 64) {
            messages.sendMessage(event.getWhoClicked(), "stacking.max-stack");
            return;
        }

        int amountToAdd = 1;

        if (event.isLeftClick()) {
            amountToAdd = cursorAmount; // 32, item that is being dragged to item in inventory
        }

        int totalAmount = currentAmount + amountToAdd;
        cursorItem.setAmount(cursorItem.getAmount() - amountToAdd);
        cursor.setAmount(cursorAmount - amountToAdd);

        if (totalAmount > 64) {
            int difference = totalAmount - 64;
            totalAmount = totalAmount - difference;
            cursor.setAmount(difference);
        }

        if (cursorItem.getAmount() <= 0) {
            withdrawableItemsStorage.removeItem(currentItemId);
        }

        currentItem.setAmount(totalAmount);
        current.setAmount(totalAmount);
    }
}
