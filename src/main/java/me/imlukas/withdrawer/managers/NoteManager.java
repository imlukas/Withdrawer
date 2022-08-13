package me.imlukas.withdrawer.managers;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.events.WithdrawEvent;
import me.imlukas.withdrawer.utils.EconomyUtil;
import me.imlukas.withdrawer.utils.TextUtil;
import me.imlukas.withdrawer.utils.illusion.item.ItemBuilder;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class NoteManager extends Managers {

    private final Withdrawer main;
    private final EconomyUtil economyUtil;

    public NoteManager(Withdrawer main) {
        super(main, "banknote");
        this.main = main;
        this.economyUtil = new EconomyUtil(main);
    }

    public void give(Player player, double money, int amount) {
        double total = money * amount;
        if (callEvent(player, total, amount, WithdrawEvent.WithdrawType.BANKNOTE)) {
            return;
        }
        if (economyUtil.hasMoney(player, money * amount)) {
            economyUtil.removeMoney(player, total);
            ItemStack noteItem = setItemProperties(player, (int) money);
            if (amount > 1) {
                for (int i = 0; i < amount; i++) {
                    player.getInventory().addItem(noteItem);
                }
            } else {
                player.getInventory().addItem(noteItem);
            }
            playWithdrawSound(player);
            sendMessages(player, total, true);
            return;
        }
        sendMessages(player, total, false);
    }
}
