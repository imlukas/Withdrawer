package me.imlukas.withdrawer.item;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.item.wrapper.ItemStackWrapper;
import me.imlukas.withdrawer.utils.interactions.messages.MessagesFile;
import me.imlukas.withdrawer.utils.interactions.SoundManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.function.Predicate;

public abstract class WithdrawableItem implements Withdrawable {

    protected final Withdrawer plugin;
    protected final MessagesFile messages;
    protected final SoundManager sounds;

    private final ItemStackWrapper itemStackWrapper;
    private final UUID uuid;
    private final int value;

    protected Predicate<Player> withdrawPredicate;
    protected int amount;
    private boolean isGifted = false;

    protected WithdrawableItem(Withdrawer withdrawer, NBTItem nbtItem) {
        this.plugin = withdrawer;
        this.uuid = nbtItem.getUUID("withdrawer-uuid");
        this.value = nbtItem.getInteger("withdrawer-value");
        this.amount = nbtItem.getItem().getAmount();

        this.itemStackWrapper = new ItemStackWrapper(nbtItem);

        this.messages = withdrawer.getMessages();
        this.sounds = withdrawer.getSounds();
    }

    protected WithdrawableItem(Withdrawer withdrawer, UUID uuid, int value, int amount) {
        this.plugin = withdrawer;
        this.uuid = uuid;
        this.value = value;
        this.amount = amount;

        this.itemStackWrapper = plugin.getDefaultItemsHandler().createWrapper(getConfigName(), value, amount, uuid);
        this.messages = withdrawer.getMessages();
        this.sounds = withdrawer.getSounds();
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public ItemStackWrapper getWrappedItem() {
        return itemStackWrapper;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStackWrapper.getItemStack();
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public boolean isGifted() {
        return isGifted;
    }

    public void setAsGifted(boolean isGifted) {
        this.isGifted = isGifted;
        getWrappedItem().setBoolean("withdrawer-gifted", isGifted);
    }

    public void setWithdrawPredicate(Predicate<Player> withdrawPredicate) {
        this.withdrawPredicate = withdrawPredicate;
    }

    /*
        These methods handle item setup for the three possible actions:
        - Redeem
        - Gift
        - Withdraw
        Basically, just value checking and other stuff that doesn't need to be made over and over again.
        Prevents code repetition on item implementations.
     */
    public int setupRedeem(Player player, boolean isShift) {
        int totalValue = value;

        if (isShift || amount == 1) {
            totalValue *= amount;
            removeItem(player);
        }

        if (!isShift && amount > 1 ) {
            amount--;
            player.getInventory().getItemInMainHand().setAmount(amount);
        }

        return totalValue;
    }

    public boolean setupGift(Player gifter, Player target) {
        setAsGifted(true);
        if (!withdrawPredicate.test(gifter)) {
            messages.sendMessage(gifter, getConfigName() + ".no-money");
            return false;
        }

        addItem(target);
        return true;
    }

    public boolean setupWithdraw(Player player) {
        if (!withdrawPredicate.test(player)) {
            messages.sendMessage(player, getConfigName() + ".no-money");
            return false;
        }

        addItem(player);
        return true;
    }

    private void addItem(Player player) {
        plugin.getWithdrawableItemsStorage().addItem(this);
        player.getInventory().addItem(getItemStack());
    }

    private void removeItem(Player player) {
        plugin.getWithdrawableItemsStorage().removeItem(this);
        player.getInventory().removeItem(getItemStack());
    }


    // Interactions (Messages and Sounds)
    public void sendRedeemInteractions(Player player, int totalAmount) {
        sendRedeemInteractions(player, totalAmount, "");
    }

    public void sendRedeemInteractions(Player player, int totalAmount, String currencySign) {
        messages.getAutomatedMessages().sendRedeemMessage(player, getConfigName(), totalAmount, currencySign);
        sounds.playSound(player, getConfigName() + ".redeem");
    }

    public void sendWithdrawInteractions(Player player, int totalAmount) {
        sendWithdrawInteractions(player, totalAmount, "");
    }

    public void sendWithdrawInteractions(Player player, int totalAmount, String currencySign) {
        messages.getAutomatedMessages().sendWithdrawMessage(player, getConfigName(), totalAmount, currencySign);
        sounds.playSound(player, getConfigName() + ".withdraw");
    }

    public void sendGiftedInteractions(Player player, Player gifter, int totalAmount) {
        sendGiftedInteractions(player, gifter, totalAmount, "");
    }

    public void sendGiftedInteractions(Player player, Player gifter, int totalAmount, String currencySign) {
        messages.getAutomatedMessages().sendGiftedMessage(player, gifter, getConfigName(), totalAmount, currencySign);
        sounds.playSound(player, getConfigName() + ".gifted");
    }

}
