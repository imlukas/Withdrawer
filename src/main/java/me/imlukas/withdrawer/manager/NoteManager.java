package me.imlukas.withdrawer.manager;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.utils.EconomyUtil;
import me.imlukas.withdrawer.utils.illusion.item.ItemBuilder;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class NoteManager {

    private final Withdrawer main;
    private final MessagesFile messages;
    private final EconomyUtil economyUtil;
    private final Economy econ;

    private ItemStack note;
    private ItemMeta meta;

    public NoteManager(Withdrawer main) {
        this.main = main;
        this.messages = main.getMessages();
        this.economyUtil = new EconomyUtil(main);
        this.econ = main.getEconomy();
    }


    public void give(Player player, int money) {
        if (checkBalance(player, money)) {
            economyUtil.removeMoney(player, money);
            ItemStack noteItem = setItem(player, money);
            player.getInventory().addItem(noteItem);
        }
        
    }

    public void give(Player player, int money, double amount) {
        double total = money * amount;
        if (checkBalance(player, money * amount)) {
            economyUtil.removeMoney(player, total);
            ItemStack noteItem = setItem(player, money);
            for (int i = 0; i < amount; i++) {
                player.getInventory().addItem(noteItem);
            }
        }
    }

    private boolean checkBalance(Player player, double money) {
        if (!(economyUtil.hasMoney(player, money))) {
            sendMessages(player, money, false);
            return false;
        }
        return true;
    }

    private ItemStack setItem(Player player, double money) {
        note = new ItemBuilder(Material.PAPER).name("&aBank Note").glowing(true).build();
        // nbt setup
        NBTItem nbtItem = new NBTItem(note);
        nbtItem.setDouble("money-value", money);
        nbtItem.applyNBT(note);


        meta = note.getItemMeta();
        // item setup
        List<String> lore = new ArrayList<>();
        lore.add(main.getTextUtil().getColor("&d&lValue: &7" + nbtItem.getDouble("money-value")));
        lore.add(main.getTextUtil().getColor("&d&lOriginal Owner: &7" + player.getName()) );


        meta.setLore(lore);
        note.setItemMeta(meta);


        return note;
    }

    private void sendMessages(Player player, double money, boolean sucess) {
        if (sucess) {
            messages.sendMessage(player, "withdraw.success", (message) -> message
                    .replace("%money%", String.valueOf(new DecimalFormat("#").format(money)))
                    .replace("%balance%", String.valueOf(economyUtil.getMoney(player))));
            return;
        }
        messages.sendMessage(player, "withdraw.error", (message) -> message
                .replace("%balance%", "" + economyUtil.getMoney(player)));
    }
}
