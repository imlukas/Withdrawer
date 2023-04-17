package me.imlukas.withdrawer.v2.item;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Getter
public class ItemStackWrapper {

    private final ItemStack itemStack;
    private final NBTItem nbtItem;

    public ItemStackWrapper(ItemStack item) {
        this.itemStack = item;
        this.nbtItem = new NBTItem(item);
    }

    public void setValue(int value) {
        nbtItem.setInteger("withdrawer-value", value);
        nbtItem.applyNBT(itemStack);
    }

    public void setUUID(UUID uuid) {
        nbtItem.setUUID("withdrawer-uuid", uuid);
        nbtItem.applyNBT(itemStack);
    }
}
