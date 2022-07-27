package me.imlukas.withdrawer.manager;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.utils.EconomyUtil;
import me.imlukas.withdrawer.utils.TextUtil;
import me.imlukas.withdrawer.utils.illusion.item.ItemBuilder;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.Sound;
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
    private final TextUtil textUtil;

    private ItemStack note;
    private ItemMeta meta;

    public NoteManager(Withdrawer main) {
        this.main = main;
        this.messages = main.getMessages();
        this.economyUtil = new EconomyUtil(main);
        this.econ = main.getEconomy();
        this.textUtil = main.getTextUtil();
    }


    public void give(Player player, double money) {
        if (checkBalance(player, money)) {
            economyUtil.removeMoney(player, money);
            ItemStack noteItem = setItemProperties(player, money);
            player.getInventory().addItem(noteItem);

            playWithdrawSound(player);
            sendMessages(player, money, true);
            return;
        }
        sendMessages(player, money, false);
        
    }

    public void give(Player player, double money, double amount) {
        double total = money * amount;
        if (checkBalance(player, money * amount)) {
            economyUtil.removeMoney(player, total);
            ItemStack noteItem = setItemProperties(player, money);
            for (int i = 0; i < amount; i++) {
                player.getInventory().addItem(noteItem);
            }
            playWithdrawSound(player);
            sendMessages(player, total, true);
            return;
        }
        sendMessages(player, total, false);


    }
    private void playWithdrawSound(Player player) {
        if (main.getConfig().getBoolean("banknote.sounds.withdraw.enabled")) {
            player.playSound(player.getLocation(), Sound.valueOf(main.getConfig().getString("banknote.sounds.withdraw.sound").toUpperCase()), 0.8f, 1);
        }
    }
    private boolean checkBalance(Player player, double money) {
        return economyUtil.hasMoney(player, money);
    }

    private ItemStack setItemProperties(Player player, double money) {
        note = new ItemBuilder(Material.PAPER).name(textUtil.getColorConfig("banknote.name")).glowing(true).build();
        // nbt setup
        NBTItem nbtItem = new NBTItem(note);
        nbtItem.setDouble("money-value", money);
        nbtItem.applyNBT(note);

        meta = note.getItemMeta();
        // item setup
        List<String> lore = new ArrayList<>();
        for (String str : main.getConfig().getStringList("banknote.lore")){
            String newText = str.replace("%value%", "" + nbtItem.getDouble("money-value"))
                    .replace("%owner%", player.getName());
            lore.add(textUtil.getColor(newText));
        }
        meta.setLore(lore);
        note.setItemMeta(meta);
        return note;
    }

    private void sendMessages(Player player, double money, boolean sucess) {
        if (sucess) {
            messages.sendMessage(player, "banknote-withdraw.success", (message) -> message
                    .replace("%money%", String.valueOf(new DecimalFormat("#").format(money)))
                    .replace("%balance%", String.valueOf(economyUtil.getMoney(player))));
            return;
        }
        messages.sendMessage(player, "banknote-withdraw.error", (message) -> message
                .replace("%balance%", "" + economyUtil.getMoney(player)));
    }
}
