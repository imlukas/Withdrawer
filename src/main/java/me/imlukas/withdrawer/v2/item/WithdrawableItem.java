package me.imlukas.withdrawer.v2.item;

import me.imlukas.withdrawer.constant.ItemType;
import org.bukkit.inventory.ItemStack;

public interface WithdrawableItem {

    ItemStack getItem();
    int getValue();
    ItemType getType();


    public void withdraw();

    public void redeem();
}
