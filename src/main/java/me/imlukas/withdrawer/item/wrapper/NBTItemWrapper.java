package me.imlukas.withdrawer.item.wrapper;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Getter
public class NBTItemWrapper {

    private final ItemStack itemStack;
    private final NBTItem nbtItem;

    public NBTItemWrapper(ItemStack item, String itemType, int value, int amount, UUID uuid) {
        this.itemStack = item;
        this.nbtItem = new NBTItem(item);
        setValue(value);
        setUUID(uuid);
        setAmount(amount);
        setString("withdrawer-type", itemType);
        nbtItem.applyNBT(itemStack);
    }

    public NBTItemWrapper(NBTItem item) {
        this.itemStack = item.getItem();
        this.nbtItem = item;
        nbtItem.applyNBT(itemStack);
    }


    public void setValue(int value) {
        nbtItem.setInteger("withdrawer-value", value);
        nbtItem.applyNBT(itemStack);
    }

    public void setUUID(UUID uuid) {
        nbtItem.setUUID("withdrawer-uuid", uuid);
        nbtItem.applyNBT(itemStack);
    }

    public void setString(String identifier, String value) {
        nbtItem.setString(identifier, value);
        nbtItem.applyNBT(itemStack);
    }

    public void setBoolean(String identifier, boolean value) {
        nbtItem.setBoolean(identifier, value);
        nbtItem.applyNBT(itemStack);
    }

    public void setAmount(int amount) {
        itemStack.setAmount(amount);
    }

    public int getValue() {
        return nbtItem.getInteger("withdrawer-value");
    }

    public UUID getUUID() {
        return nbtItem.getUUID("withdrawer-uuid");
    }

    public int getAmount() {
        return itemStack.getAmount();
    }
}
