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

        NBTItem nbtItem = new NBTItem(event.getItem());


        if (nbtItem.hasKey("banknote-value")) {
            event.setCancelled(true);
            setRedeemProperties(player, nbtItem, RedeemEvent.ReedemType.BANKNOTE);
        } else if (nbtItem.hasKey("expbottle-value")) {
            event.setCancelled(true);
            setRedeemProperties(player, nbtItem, RedeemEvent.ReedemType.EXPBOTTLE);

        }
    }
    private void setRedeemProperties(Player player, NBTItem nbtItem, RedeemEvent.ReedemType type) {
        if (!(player.hasPermission("withdrawer.reedem." + type.toString().toLowerCase()))) {
            messages.sendMessage(player, "global.no-permission");
            return;
        }
        String itemType = type.toString().toLowerCase();

        int value = nbtItem.getInteger(itemType + "-value");

        int itemAmount = player.getInventory().getItemInMainHand().getAmount();

        if (player.isSneaking() && itemAmount > 1){
            if (!(player.hasPermission("withdrawer.reedem." + type + ".bulk"))) {
                messages.sendStringMessage(player, "&c[ERROR] &7ou don't have permission to bulk open this item.");
                return;
            }
            reedemEvent = new RedeemEvent(player, value * itemAmount, type, itemAmount);
        } else{
            reedemEvent = new RedeemEvent(player, value, type);
        }
        Bukkit.getServer().getPluginManager().callEvent(reedemEvent);
        if (reedemEvent.isCancelled()) {
            return;
        }
        if (type == RedeemEvent.ReedemType.BANKNOTE) {
            redeemItem(player, value, itemAmount, itemType);
        } else if (type == RedeemEvent.ReedemType.EXPBOTTLE) {
            redeemItem(player, value, itemAmount, itemType);
        }

    }

    private void redeemItem(Player player, int value, int itemAmount, String type) {
        if (player.isSneaking() && itemAmount > 1) {
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            playSounds(player, type);
            sendMessages(player, value * itemAmount, type);
            if (type.equalsIgnoreCase("expbottle")) {
                expUtil.changeExp(player, value * itemAmount);
                return;
            }
            economyUtil.giveMoney(player, value * itemAmount);
            return;
        }
        player.getInventory().getItemInMainHand().setAmount(itemAmount - 1);
        playSounds(player, type);
        sendMessages(player, value, type);
        if (type.equalsIgnoreCase("expbottle")) {
            expUtil.changeExp(player, value);
            return;
        }
        economyUtil.giveMoney(player, value);
    }

    private void sendMessages(Player player, double value, String type) {
        if (messages.getConfiguration().getBoolean("messages.less-intrusive")) {
            System.out.println("test");
            if (type.equalsIgnoreCase("banknote")) {
                messages.sendStringMessage(player, "&a+" + value + "$");
                return;
            }
            messages.sendStringMessage(player, "&a+" + value + "EXP");
            return;
        }
        messages.sendMessage(player, type + "-redeem", (message) -> message
                .replace("%money%", String.valueOf(new DecimalFormat("#").format(value)))
                .replace("%balance%", String.valueOf(economyUtil.getMoney(player)))
                .replace("%exp%", String.valueOf(new DecimalFormat("#").format(value))));
    }

    private void playSounds(Player player, String type) {
        if (main.getConfig().getBoolean(type + ".sounds.redeem.enabled")) {
            String sound = main.getConfig().getString(type + ".sounds.redeem.sound");
            player.playSound(player.getLocation(), Sound.valueOf(sound), 0.8f, 1);
        }
    }
}