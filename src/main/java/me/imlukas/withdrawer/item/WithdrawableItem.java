package me.imlukas.withdrawer.item;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.item.interactions.Interactions;
import me.imlukas.withdrawer.item.item.ItemPlaceholders;
import me.imlukas.withdrawer.item.preparator.WithdrawablePreparator;
import me.imlukas.withdrawer.item.item.ItemWrapper;
import me.imlukas.withdrawer.utils.interactions.messages.MessagesFile;
import me.imlukas.withdrawer.utils.pdc.PDCWrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public abstract class WithdrawableItem implements Withdrawable {

    protected final Withdrawer plugin;
    protected final MessagesFile messages;
    protected final Interactions interactions;
    protected final ItemWrapper itemWrapper;
    protected final WithdrawablePreparator preparator;

    private final UUID uuid;
    private final int value;

    private ItemPlaceholders itemPlaceholders;

    protected int amount;
    private boolean isGifted = false;

    protected WithdrawableItem(Withdrawer withdrawer, PDCWrapper pdcWrapper) {
        this(withdrawer, pdcWrapper.getUUID("withdrawer-uuid"), pdcWrapper.getInteger("withdrawer-value"),
                pdcWrapper.getInteger("withdrawer-amount"));
    }

    protected WithdrawableItem(Withdrawer withdrawer, int value, int amount) {
        this(withdrawer, UUID.randomUUID(), value, amount);
    }

    protected WithdrawableItem(Withdrawer withdrawer, UUID uuid, int value, int amount) {
        this.plugin = withdrawer;
        this.messages = withdrawer.getMessages();
        this.interactions = new Interactions(plugin, getConfigName());
        this.itemWrapper = new ItemWrapper(plugin, withdrawer.getItemHandler().get(getConfigName()));
        this.preparator = new WithdrawablePreparator(plugin, this);

        this.uuid = uuid;
        this.value = value;
        this.amount = amount;

        this.itemWrapper.setupPDC(uuid, value, amount);
        createItemPlaceholders();
    }

    public void createItemPlaceholders() {
        this.itemPlaceholders = new ItemPlaceholders(Map.of(
                "value", String.valueOf(value),
                "amount", String.valueOf(amount)));
    }

    public Interactions getInteractions() {
        return interactions;
    }

    public PDCWrapper getPDCWrapper() {
        return new PDCWrapper(plugin, getDisplayItem(false));
    }

    public ItemPlaceholders getItemPlaceholders() {
        return itemPlaceholders;
    }

    @Override
    public WithdrawablePreparator getPreparator() {
        return preparator;
    }

    @Override
    public ItemWrapper getItemWrapper() {
        return itemWrapper;
    }

    @Override
    public ItemStack getDisplayItem(boolean clone) {
        if (clone) {
            return getItemWrapper().getItem().clone();
        } else {
            return getItemWrapper().getItem();
        }
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public boolean isGifted() {
        return isGifted;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public void setAsGifted(boolean isGifted) {
        this.isGifted = isGifted;
    }

    public void addItem(Player player) {
        itemPlaceholders.replace(getDisplayItem(false));
        plugin.getWithdrawableItemsStorage().addItem(this);
        player.getInventory().addItem(getDisplayItem(true));
    }

    public void removeItem(Player player) {
        plugin.getWithdrawableItemsStorage().removeItem(this);
        player.getInventory().removeItem(player.getInventory().getItemInMainHand());
    }

}
