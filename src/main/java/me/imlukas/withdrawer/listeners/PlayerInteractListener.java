package me.imlukas.withdrawer.listeners;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.constant.ItemType;
import me.imlukas.withdrawer.events.RedeemEvent;
import me.imlukas.withdrawer.utils.EconomyUtil;
import me.imlukas.withdrawer.utils.ExpUtil;
import me.imlukas.withdrawer.utils.HealthUtil;
import me.imlukas.withdrawer.utils.storage.MessagesFile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

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
        if (event.getItem() == null || event.getItem().getType().equals(Material.AIR)) {
            return;
        }

        NBTItem nbtItem = new NBTItem(event.getItem());

        if (nbtItem.hasKey("banknote-value")) {
            event.setCancelled(true);
            setRedeemProperties(player, nbtItem, ItemType.BANKNOTE);
        } else if (nbtItem.hasKey("expbottle-value")) {
            event.setCancelled(true);
            setRedeemProperties(player, nbtItem, ItemType.EXPBOTTLE);
        } else if (nbtItem.hasKey("health-value")) {
            event.setCancelled(true);
            setRedeemProperties(player, nbtItem, ItemType.HEALTH);
        }
    }

    private void setRedeemProperties(Player player, NBTItem nbtItem, ItemType type) {
        if (!(player.hasPermission("withdrawer.redeem." + type.toString().toLowerCase()))) {
            messages.sendMessage(player, "global.no-permission");
            return;
        }

        double value = nbtItem.getDouble(type.lowercase + "-value");

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
        redeemItem(player, value, itemAmount, type);

    }

    private void redeemItem(Player player, double value, int itemAmount, ItemType type) {
        if (player.isSneaking() && itemAmount > 1 && type != ItemType.HEALTH) {
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            playSounds(player, type);
            value = value * itemAmount;
        } else {
            player.getInventory().getItemInMainHand().setAmount(itemAmount - 1);
            playSounds(player, type);
        }

        if (type == ItemType.EXPBOTTLE) {
            expUtil.changeExp(player, (int) value);
        } else if (type == ItemType.BANKNOTE) {
            economyUtil.giveMoney(player, value);
        } else {
            healthUtil.addHealth(player, (int) value);
        }
        sendMessages(player, value, type);
    }

    private void sendMessages(Player player, double value, ItemType type) {
        String currencySign = economyUtil.getCurrencySign();
        String balance = String.valueOf(economyUtil.getMoney(player));
        String currentExp = String.valueOf(expUtil.getExp(player));
        String currentHealth = String.valueOf(healthUtil.getHealth(player) / 2);
        if (messages.isLessIntrusive()) {
            if (type == ItemType.EXPBOTTLE) {
                messages.sendStringMessage(player, "&a+" + value + "EXP");
            } else if (type == ItemType.BANKNOTE) {
                messages.sendStringMessage(player, "&a+" + value + currencySign);
            } else {
                messages.sendStringMessage(player, "&a+" + value + "HP");
            }
            return;
        }
        messages.sendMessage(player, type.lowercase + "-redeem.message", (message) -> message
                .replace("%value%", String.valueOf(value))
                .replace("%hp%", String.valueOf(value))
                .replace("%balance%", balance)
                .replace("%current_exp%", currentExp)
                .replace("%currency_sign%", currencySign)
                .replace("%current_hp%", currentHealth));

    }


    private void playSounds(Player player, ItemType type) {
        if (main.getConfig().getBoolean(type.lowercase + ".sounds.redeem.enabled")) {
            String sound = main.getConfig().getString(type.lowercase + ".sounds.redeem.sound");
            player.playSound(player.getLocation(), Sound.valueOf(sound), 0.8f, 1);
        }
    }
}