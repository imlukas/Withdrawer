package me.imlukas.withdrawer.item;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.utils.interactions.SoundManager;
import me.imlukas.withdrawer.utils.interactions.messages.MessagesFile;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class WithdrawableItem implements Withdrawable {

    protected final Withdrawer plugin;
    protected final MessagesFile messages;
    protected final SoundManager sounds;

    private final UUID uuid;
    private final int value;

    private final ItemPlaceholders itemPlaceholders;
    private final ItemStack displayItem;
    private final NBTItem nbtItem;

    protected Predicate<Player> withdrawPredicate;
    protected int amount;
    private boolean isGifted = false;

    protected WithdrawableItem(Withdrawer withdrawer, NBTItem nbtItem) {
        this.plugin = withdrawer;
        this.uuid = nbtItem.getUUID("withdrawer-uuid");
        this.value = nbtItem.getInteger("withdrawer-value");
        this.amount = nbtItem.getItem().getAmount();

        this.messages = withdrawer.getMessages();
        this.sounds = withdrawer.getSounds();

        this.itemPlaceholders = new ItemPlaceholders(Map.of(
                "value", String.valueOf(value),
                "amount", String.valueOf(amount)));

        this.nbtItem = nbtItem;
        this.displayItem = nbtItem.getItem();
    }

    protected WithdrawableItem(Withdrawer withdrawer, UUID uuid, int value, int amount) {
        this.plugin = withdrawer;
        this.uuid = uuid;
        this.value = value;
        this.amount = amount;

        this.messages = withdrawer.getMessages();
        this.sounds = withdrawer.getSounds();
        this.itemPlaceholders = new ItemPlaceholders(Map.of(
                "value", String.valueOf(value),
                "amount", String.valueOf(amount)));

        this.displayItem = withdrawer.getItemHandler().get(getConfigName());
        displayItem.setAmount(amount);
        this.nbtItem = createNBTItem(displayItem);
        applyNBT();
    }

    public NBTItem createNBTItem(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setUUID("withdrawer-uuid", uuid);
        nbtItem.setInteger("withdrawer-value", value);
        nbtItem.setString("withdrawer-type", getConfigName());
        return nbtItem;
    }

    public void applyNBT() {
        nbtItem.applyNBT(displayItem);
    }

    public int getAmount() {
        return amount;
    }

    public ItemPlaceholders getItemPlaceholders() {
        return itemPlaceholders;
    }

    @Override
    public ItemStack getDisplayItem() {
        return displayItem;
    }

    public NBTItem getNBTItem() {
        return nbtItem;
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
        if (!player.hasPermission("withdrawer.redeem." + getConfigName())) {
            messages.sendMessage(player, "global.no-permission");
            return 0;
        }
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
        if (!withdrawPredicate.test(gifter)) {
            messages.sendMessage(gifter, getConfigName() + ".no-money");
            return false;
        }

        if (!gifter.hasPermission("withdrawer.gift." + getConfigName())) {
            messages.sendMessage(gifter, "global.no-permission");
            return false;
        }

        setAsGifted(true);
        addItem(target);
        return true;
    }

    public boolean setupWithdraw(Player player) {
        if (!withdrawPredicate.test(player)) {
            messages.sendMessage(player, getConfigName() + ".no-money");
            return false;
        }

        if (!player.hasPermission("withdrawer.withdraw." + getConfigName())) {
            messages.sendMessage(player, "global.no-permission");
            return false;
        }

        addItem(player);
        return true;
    }

    private void addItem(Player player) {
        itemPlaceholders.replace(displayItem);
        plugin.getWithdrawableItemsStorage().addItem(this);
        player.getInventory().addItem(displayItem);
    }

    private void removeItem(Player player) {
        plugin.getWithdrawableItemsStorage().removeItem(this);
        player.getInventory().removeItem(player.getInventory().getItemInMainHand());
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
