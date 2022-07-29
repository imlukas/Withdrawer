package me.imlukas.withdrawer.listeners;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.events.RedeemEvent;
import me.imlukas.withdrawer.utils.EconomyUtil;
import me.imlukas.withdrawer.utils.ExpUtil;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class PlayerInteractListener implements Listener {

    private final Withdrawer main;
    private final EconomyUtil economyUtil;
    private final ExpUtil expUtil;
    private final MessagesFile messages;
    private RedeemEvent reedemEvent;

    public PlayerInteractListener(Withdrawer main) {
        this.main = main;
        this.economyUtil = main.getEconomyUtil();
        this.messages = main.getMessages();
        this.expUtil = main.getExpUtil();

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (event.getItem() == null) {
            return;
        }

        Material itemMaterial = event.getItem().getType();
        NBTItem nbtItem = new NBTItem(event.getItem());

        Material bankNoteMaterial = Material.getMaterial(main.getConfig().getString("banknote.item").toUpperCase());
        Material expBottleMaterial = Material.getMaterial(main.getConfig().getString("expbottle.item").toUpperCase());

        if (itemMaterial.equals(bankNoteMaterial) && nbtItem.hasKey("banknote-value")) {
            redeemItem(player, nbtItem, RedeemEvent.ReedemType.BANKNOTE);
        } else if (itemMaterial.equals(expBottleMaterial) && nbtItem.hasKey("expbottle-value")) {
            event.setCancelled(true);
            redeemItem(player, nbtItem, RedeemEvent.ReedemType.EXPBOTTLE);

        }


    }

    private void redeemItem(Player player, NBTItem nbtItem, RedeemEvent.ReedemType type) {
        String itemType = type.toString().toLowerCase();

        int value = nbtItem.getInteger(itemType + "-value");

        reedemEvent = new RedeemEvent(player, value, type);
        Bukkit.getServer().getPluginManager().callEvent(reedemEvent);
        if (reedemEvent.isCancelled()) {
            return;
        }
        if (type == RedeemEvent.ReedemType.BANKNOTE) {
            noteRedeem(player, value);
        } else if (type == RedeemEvent.ReedemType.EXPBOTTLE) {
            expBottleRedeem(player, value);
        }
        sendMessages(player, value, itemType);
    }

    private void expBottleRedeem(Player player, int exp) {
        int itemAmount = player.getInventory().getItemInMainHand().getAmount();

        if (player.isSneaking() && itemAmount > 1) {
            expUtil.changeExp(player, exp * itemAmount);
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        } else {
            expUtil.changeExp(player, exp);
            player.getInventory().getItemInMainHand().setAmount(itemAmount - 1);
        }
        if (main.getConfig().getBoolean("expbottle.sounds.redeem.enabled")) {
            String sound = main.getConfig().getString("expbottle.sounds.redeem.sound");
            player.playSound(player.getLocation(), Sound.valueOf(sound), 0.8f, 1);
        }
    }

    private void noteRedeem(Player player, int money) {
        int itemAmount = player.getInventory().getItemInMainHand().getAmount();

        if (player.isSneaking() && itemAmount > 1) {
            economyUtil.giveMoney(player, money * itemAmount);
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));

        } else {
            economyUtil.giveMoney(player, money);
            player.getInventory().getItemInMainHand().setAmount(itemAmount - 1);
        }

        if (main.getConfig().getBoolean("banknote.sounds.redeem.enabled")) {
            String sound = main.getConfig().getString("banknote.sounds.redeem.sound");
            player.playSound(player.getLocation(), Sound.valueOf(sound), 0.8f, 1);
        }
    }
    private void sendMessages(Player player, double value, String type) {
        if (main.getConfig().getBoolean("messages.less-intrusive")){
            if (type.equalsIgnoreCase("banknote")){
                messages.sendStringMessage(player,"&a+" + value + "$");
            }
            messages.sendStringMessage(player,"&a+" + value + "EXP");
            return;
        }
        messages.sendMessage(player, type + "-redeem", (message) -> message
                .replace("%money%", String.valueOf(new DecimalFormat("#").format(value)))
                .replace("%balance%", String.valueOf(economyUtil.getMoney(player)))
                .replace("%exp%", String.valueOf(new DecimalFormat("#").format(value)) )
                .replace("%current_exp%", String.valueOf(expUtil.getExp(player))));
    }
}