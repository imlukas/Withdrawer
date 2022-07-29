package me.imlukas.withdrawer.manager;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.events.WithdrawEvent;
import me.imlukas.withdrawer.utils.ExpUtil;
import me.imlukas.withdrawer.utils.TextUtil;
import me.imlukas.withdrawer.utils.illusion.item.ItemBuilder;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ExpBottleManager {


    private final Withdrawer main;
    private final ExpUtil expUtil;
    private final TextUtil textUtil;
    private final MessagesFile messages;

    private ItemStack expItem;
    private ItemMeta meta;

    private WithdrawEvent withdrawEvent;

    public ExpBottleManager(Withdrawer main) {
        this.main = main;
        this.expUtil = main.getExpUtil();
        this.textUtil = main.getTextUtil();
        this.messages = main.getMessages();
    }

    public void give(Player player, int exp) {
        if (checkExp(player, exp)) {
            expUtil.changeExp(player, -exp);
            ItemStack expItem = setItemProperties(player, exp);
            player.getInventory().addItem(expItem);

            playWithdrawSound(player);
            sendMessages(player, exp, true);
            callEvent(player, exp);
            return;
        }
        sendMessages(player, exp, false);
    }


    public void give(Player player, int exp, int amount) {
        int total = exp * amount;
        if (checkExp(player, total)) {

            expUtil.changeExp(player, -total);
            ItemStack expItem = setItemProperties(player, exp);
            for (int i = 0; i < amount; i++) {
                player.getInventory().addItem(expItem);
            }
            playWithdrawSound(player);
            sendMessages(player, total, true);
            callEvent(player, exp, amount);
            return;
        }
        sendMessages(player, total, false);
    }

    private ItemStack setItemProperties(Player player, int exp) {
        expItem = new ItemBuilder(Material.EXPERIENCE_BOTTLE).name(textUtil.getColorConfig("expbottle.name")).glowing(true).build();
        // nbt setup
        NBTItem nbtItem = new NBTItem(expItem);
        nbtItem.setInteger("expbottle-value", exp);
        nbtItem.applyNBT(expItem);

        meta = expItem.getItemMeta();
        // item setup
        List<String> lore = new ArrayList<>();
        for (String str : main.getConfig().getStringList("expbottle.lore")) {
            String newText = str.replace("%exp%", "" + nbtItem.getDouble("expbottle-value"))
                    .replace("%owner%", player.getName());
            lore.add(textUtil.getColor(newText));
        }

        meta.setLore(lore);
        expItem.setItemMeta(meta);
        return expItem;
    }

    private void sendMessages(Player player, double exp, boolean sucess) {
        if (sucess) {
            messages.sendMessage(player, "expbottle-withdraw.success", (message) -> message
                    .replace("%exp%", String.valueOf(new DecimalFormat("#").format(exp)))
                    .replace("%current_exp%", String.valueOf(expUtil.getExp(player))));
            return;
        }
        messages.sendMessage(player, "expbottle-withdraw.error", (message) -> message
                .replace("%current_exp%", "" + expUtil.getExp(player)));
    }


    private boolean checkExp(Player player, int exp) {
        return (!(expUtil.getExp(player) < exp));
    }

    private void playWithdrawSound(Player player) {
        if (main.getConfig().getBoolean("expbottle.sounds.withdraw.enabled")) {
            player.playSound(player.getLocation(), Sound.valueOf(main.getConfig().getString("expbottle.sounds.withdraw.sound")), 0.8f, 1);
        }
    }

    private boolean callEvent(Player player, double money) {
        withdrawEvent = new WithdrawEvent(player, money, WithdrawEvent.WithdrawType.BANKNOTE);
        Bukkit.getServer().getPluginManager().callEvent(withdrawEvent);
        return withdrawEvent.isCancelled();
    }

    private boolean callEvent(Player player, double money, int amount) {
        withdrawEvent = new WithdrawEvent(player, money, amount, WithdrawEvent.WithdrawType.BANKNOTE);
        Bukkit.getServer().getPluginManager().callEvent(withdrawEvent);
        return withdrawEvent.isCancelled();
    }

}
