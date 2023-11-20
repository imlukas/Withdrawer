package me.imlukas.withdrawer.v3.item;

import lombok.Getter;
import me.imlukas.withdrawer.WithdrawerPlugin;
import me.imlukas.withdrawer.utils.interactions.Sounds;
import me.imlukas.withdrawer.utils.interactions.Messages;
import me.imlukas.withdrawer.utils.pdc.PDCWrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public abstract class BaseWithdrawableItem implements GiftableWithdrawable {

    protected final WithdrawerPlugin plugin;
    protected final Messages messages;
    protected final Sounds sounds;
    private final ItemStack associatedItem;
    private final PDCWrapper itemPDC;
    private final int baseValue;

    private boolean isGifted = false;

    protected BaseWithdrawableItem(WithdrawerPlugin plugin, ItemStack itemStack) {
        this.plugin = plugin;
        this.messages = plugin.getMessages();
        this.sounds = plugin.getSounds();
        this.associatedItem = itemStack;
        this.itemPDC = new PDCWrapper(plugin, itemStack);

        this.baseValue = itemPDC.getInteger("withdrawer-value");

        if (baseValue == 0) {
            throw new IllegalArgumentException("Item " + itemStack.getType() + " has no value");
        }
    }

    @Override
    public ItemStack getAssociatedItem() {
        return associatedItem;
    }

    public int getItemAmount() {
        return associatedItem.getAmount();
    }

    public int getTotalValue() {
        return baseValue * getItemAmount();
    }

    public void setGifted(boolean isGifted) {
        this.isGifted = isGifted;
    }

    public boolean isGifted() {
        return isGifted;
    }

    @Override
    public void give(Player target) {
        if (!checkPermission(target, "give")) {
            return;
        }

        target.getInventory().addItem(getAssociatedItem());
    }


    public boolean checkPermission(Player player, String type) {
        if (!player.hasPermission("withdrawer." + type + ".*") && (!player.hasPermission("withdrawer." + type + "." + getConfigName()))) {
            messages.sendMessage(player, "global.no-permission");
            return false;
        }

        return true;
    }

    /**
     * Makes generic checks for redeeming an item and handles value calculation.
     * @param player The player redeeming the item
     * @return The total value of the item
     */
    public int setupRedeem(Player player, boolean isShift) {
        int value = getBaseValue();
        int amount = isShift ? getItemAmount() : 1;

        if (!checkPermission(player, "redeem")) {
            return 0;
        }

        int totalValue = value * amount;
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (isShift) {
            player.getInventory().setItemInMainHand(null);
        } else {
            itemInHand.setAmount(itemInHand.getAmount() - amount);
        }

        return totalValue;
    }
}
