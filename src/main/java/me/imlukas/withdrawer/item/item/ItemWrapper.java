package me.imlukas.withdrawer.item.item;

import lombok.Data;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.utils.pdc.PDCWrapper;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Data
public class ItemWrapper {

    private final Withdrawer plugin;
    private final ItemStack item;

    public ItemWrapper(Withdrawer plugin, ItemStack item) {
        this.plugin = plugin;
        this.item = item;
    }

    public void setupPDC(UUID uuid, int value, int amount) {
        PDCWrapper.modifyItem(plugin, item, pdcWrapper -> {
            pdcWrapper.setUUID("withdrawer-uuid", uuid);
            pdcWrapper.setInteger("withdrawer-value", value);
            pdcWrapper.setInteger("withdrawer-amount", amount);
        });

        setAmount(amount);
    }

    public void setAmount(int amount) {
        item.setAmount(amount);
    }
}
