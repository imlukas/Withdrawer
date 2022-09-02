package me.imlukas.withdrawer.listeners;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.events.RedeemEvent;
import me.imlukas.withdrawer.utils.EconomyUtil;
import me.imlukas.withdrawer.utils.ExpUtil;
import me.imlukas.withdrawer.utils.HealthUtil;
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
    private final HealthUtil healthUtil;
    private final MessagesFile messages;

    public PlayerInteractListener(Withdrawer main) {
        this.main = main;
        this.economyUtil = main.getEconomyUtil();
        this.messages = main.getMessages();
        this.expUtil = main.getExpUtil();
        this.healthUtil = main.getHealthUtil();

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
            setRedeemProperties(player, nbtItem, RedeemEvent.redeemType.BANKNOTE);
        } else if (nbtItem.hasKey("expbottle-value")) {
            event.setCancelled(true);
            setRedeemProperties(player, nbtItem, RedeemEvent.redeemType.EXPBOTTLE);
        } else if (nbtItem.hasKey("health-value")) {
            event.setCancelled(true);
            setRedeemProperties(player, nbtItem, RedeemEvent.redeemType.HEALTH);
        }
    }

    private void setRedeemProperties(Player player, NBTItem nbtItem, RedeemEvent.redeemType type) {
        if (!(player.hasPermission("withdrawer.redeem." + type.toString().toLowerCase()))) {
            System.out.println(type);
            messages.sendMessage(player, "global.no-permission");
            return;
        }
        String itemType = type.toString().toLowerCase();

        double value = nbtItem.getDouble(itemType + "-value");

        int itemAmount = player.getInventory().getItemInMainHand().getAmount();

        RedeemEvent redeemEvent;
        if (player.isSneaking() && itemAmount > 1) {
            if (!(player.hasPermission("withdrawer.redeem." + type + ".bulk"))) {
                messages.sendStringMessage(player, "&c[ERROR] &7ou don't have permission to bulk open this item.");
                return;
            }
            redeemEvent = new RedeemEvent(player, value * itemAmount, type, itemAmount);
        } else {
            redeemEvent = new RedeemEvent(player, value, type);
        }
        Bukkit.getServer().getPluginManager().callEvent(redeemEvent);
        if (redeemEvent.isCancelled()) {
            return;
        }
        if (type == RedeemEvent.redeemType.BANKNOTE) {
            redeemItem(player, value, itemAmount, itemType);
        } else if (type == RedeemEvent.redeemType.EXPBOTTLE) {
            redeemItem(player, value, itemAmount, itemType);
        } else if (type == RedeemEvent.redeemType.HEALTH) {
            redeemItem(player, value, itemAmount, itemType);
        }

    }

    private void redeemItem(Player player, double value, int itemAmount, String type) {
        if (player.isSneaking() && itemAmount > 1 && !(type.equalsIgnoreCase("health"))) {
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            playSounds(player, type);
            if (type.equalsIgnoreCase("expbottle")) {
                expUtil.changeExp(player, (int) value * itemAmount);
            } else if (type.equalsIgnoreCase("banknote")) {
                economyUtil.giveMoney(player, value * itemAmount);
            }
            sendMessages(player, value * itemAmount, type);
            return;
        }
        player.getInventory().getItemInMainHand().setAmount(itemAmount - 1);
        playSounds(player, type);
        if (type.equalsIgnoreCase("expbottle")) {
            expUtil.changeExp(player, (int) value);
        } else if (type.equalsIgnoreCase("banknote")) {
            economyUtil.giveMoney(player, value);
        } else {
            healthUtil.addHealth(player, (int) value);
        }

        sendMessages(player, value, type);
    }

    private void sendMessages(Player player, double value, String type) {
        String currencySign = economyUtil.getCurrencySign();
        String balance = String.valueOf(economyUtil.getMoney(player));
        String currentExp = String.valueOf(expUtil.getExp(player));
        String currentHealth = String.valueOf(healthUtil.getHealth(player) / 2);
        if (messages.getConfiguration().getBoolean("messages.less-intrusive")) {
            if (type.equalsIgnoreCase("expbottle")) {
                messages.sendStringMessage(player, "&a+" + value + "EXP");
            } else if (type.equalsIgnoreCase("banknote")){
                messages.sendStringMessage(player, "&a+" + value + currencySign);
            } else {
                messages.sendStringMessage(player, "&a+" + value + "HP");
            }
            return;
        }
        messages.sendMessage(player, type + "-redeem", (message) -> message
                .replace("%value%", String.valueOf(value))
                .replace("%hp%", String.valueOf(value))
                .replace("%balance%", balance)
                .replace("%current_exp%", currentExp)
                .replace("%currency_sign%", currencySign)
                .replace("%current_hp%", currentHealth));

    }

    private void playSounds(Player player, String type) {
        if (main.getConfig().getBoolean(type + ".sounds.redeem.enabled")) {
            String sound = main.getConfig().getString(type + ".sounds.redeem.sound");
            player.playSound(player.getLocation(), Sound.valueOf(sound), 0.8f, 1);
        }
    }
}