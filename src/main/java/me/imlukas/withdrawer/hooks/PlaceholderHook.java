package me.imlukas.withdrawer.hooks;

import de.tr7zw.nbtapi.NBTItem;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.item.WithdrawableItem;
import me.imlukas.withdrawer.item.registry.WithdrawableItemsStorage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlaceholderHook extends PlaceholderExpansion {

    private final WithdrawableItemsStorage withdrawableItemsStorage;

    public PlaceholderHook(Withdrawer plugin) {
        this.withdrawableItemsStorage = plugin.getWithdrawableItemsStorage();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "withdrawer";
    }

    @Override
    public @NotNull String getAuthor() {
        return "imlukas";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (itemStack.getType().isAir()) {
            return "0";
        }

        NBTItem nbtItem = new NBTItem(itemStack);

        UUID withdrawableUUID = nbtItem.getUUID("withdrawer-uuid");

        if (withdrawableUUID == null) {
            return "0";
        }

        WithdrawableItem withdrawableItem = withdrawableItemsStorage.getItem(withdrawableUUID);

        if (params.equals("value")) {
            return String.valueOf(withdrawableItem.getValue());
        }

        if (params.equals("total-value")) {
            return String.valueOf(withdrawableItem.getValue() * withdrawableItem.getAmount());
        }

        if (params.equals("owner")) {
            return player.getDisplayName();
        }

        return "0";
    }
}
