package me.imlukas.withdrawer.managers;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.events.WithdrawEvent;
import me.imlukas.withdrawer.utils.ExpUtil;
import me.imlukas.withdrawer.utils.TextUtil;
import me.imlukas.withdrawer.utils.illusion.item.ItemBuilder;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExpBottleManager extends Managers {


    private final Withdrawer main;
    private final ExpUtil expUtil;

    public ExpBottleManager(Withdrawer main) {
        super(main, "expbottle");
        this.main = main;
        this.expUtil = main.getExpUtil();
    }

    public void give(Player player, int exp, int amount){
        int total = exp * amount;
        if (callEvent(player, total, amount, WithdrawEvent.WithdrawType.EXPBOTTLE)) {
            return;
        }
        if (!(expUtil.getExp(player) < total)) {
            expUtil.changeExp(player, -total);
            ItemStack expItem = setItemProperties(player, exp);
            if (amount > 1) {
                for (int i = 0; i < amount; i++) {
                    player.getInventory().addItem(expItem);
                }
            } else {
                player.getInventory().addItem(expItem);
            }
            playWithdrawSound(player);
            sendMessages(player, total, true);
            callEvent(player, total, 1, WithdrawEvent.WithdrawType.EXPBOTTLE);
            return;
        }
        sendMessages(player, total, false);
    }

}
