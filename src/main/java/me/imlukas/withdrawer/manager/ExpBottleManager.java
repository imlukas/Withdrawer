package me.imlukas.withdrawer.manager;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.utils.ExpUtil;
import me.imlukas.withdrawer.utils.TextUtil;
import me.imlukas.withdrawer.utils.illusion.item.ItemBuilder;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import org.bukkit.Material;
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

    public ExpBottleManager(Withdrawer main) {
        this.main = main;
        this.expUtil = main.getExpUtil();
        this.textUtil = main.getTextUtil();
        this.messages = main.getMessages();
    }

    public void give(Player player, int exp){
        if (checkExp(player, exp)){
            expUtil.removeExp(player, exp);
            ItemStack expItem = setItemProperties(player, exp);
            player.getInventory().addItem(expItem);

            playWithdrawSound(player);
            sendMessages(player, exp, true);
            return;
        }
        sendMessages(player, exp, false);
    }


    public void give(Player player, int exp, int amount){
        double total = exp * amount;
        if (checkExp(player, exp * amount)) {
            expUtil.removeExp(player, total);
            ItemStack noteItem = setItemProperties(player, exp);
            for (int i = 0; i < amount; i++) {
                player.getInventory().addItem(noteItem);
            }
            playWithdrawSound(player);
            sendMessages(player, total, true);
            return;
        }
        sendMessages(player, total, false);
    }

    private ItemStack setItemProperties(Player player, double money) {
        expItem = new ItemBuilder(Material.PAPER).name(textUtil.getColorConfig("expbottle.name")).glowing(true).build();
        // nbt setup
        NBTItem nbtItem = new NBTItem(expItem);
        nbtItem.setDouble("exp-value", money);
        nbtItem.applyNBT(expItem);

        meta = expItem.getItemMeta();
        // item setup
        List<String> lore = new ArrayList<>();
        for (String str : main.getConfig().getStringList("expbottle.lore")){
            String newText = str.replace("%value%", "" + nbtItem.getDouble("exp-value"))
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
                    .replace("%current_exp%", String.valueOf(expUtil.getTotalExperience(player))));
            return;
        }
        messages.sendMessage(player, "expbottle-withdraw.error", (message) -> message
                .replace("%current_exp%", "" + expUtil.getTotalExperience(player)));
    }


    private boolean checkExp(Player player, int exp){
        return (!(expUtil.getTotalExperience(player) < exp));
    }

    private void playWithdrawSound(Player player){
        if (main.getConfig().getBoolean("expbottle.sounds.withdraw.enabled")) {
            player.playSound(player.getLocation(), main.getConfig().getString("expbottle.sounds.withdraw.sound"), 1, 1);
        }
    }

}
