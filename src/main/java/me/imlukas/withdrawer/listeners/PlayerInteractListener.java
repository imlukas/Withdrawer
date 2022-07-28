package me.imlukas.withdrawer.listeners;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.events.ItemRedeemEvent;
import me.imlukas.withdrawer.utils.EconomyUtil;
import me.imlukas.withdrawer.utils.ExpUtil;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
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
    private final MessagesFile messages;
    private ItemRedeemEvent reedemEvent;

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
            redeemItem(player, nbtItem, ItemRedeemEvent.ReedemType.BANKNOTE);
        } else if (itemMaterial.equals(expBottleMaterial) && nbtItem.hasKey("expbottle-value")) {
            event.setCancelled(true);
            redeemItem(player, nbtItem, ItemRedeemEvent.ReedemType.EXPBOTTLE);

        }

        int itemAmount = event.getItem().getAmount();

        if (itemAmount > 1) {
            event.getItem().setAmount(itemAmount - 1);
            return;
        }
        player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
    }

    private void redeemItem(Player player, NBTItem nbtItem, ItemRedeemEvent.ReedemType type) {

        String itemType = type.toString().toLowerCase();

        int value = nbtItem.getInteger(itemType + "-value");

        reedemEvent = new ItemRedeemEvent(player, value, type);
        Bukkit.getServer().getPluginManager().callEvent(reedemEvent);
        if (reedemEvent.isCancelled()) {
            return;
        }
        if (type == ItemRedeemEvent.ReedemType.BANKNOTE) {
            noteRedeem(player, value);
        } else if (type == ItemRedeemEvent.ReedemType.EXPBOTTLE) {
            expBottleRedeem(player, value);
        }
    }
    private void expBottleRedeem(Player player, int exp) {
        expUtil.changeExp(player, exp);
        if (main.getConfig().getBoolean("expbottle.sounds.redeem.enabled")) {
            String sound = main.getConfig().getString("expbottle.sounds.redeem.sound");
            player.playSound(player.getLocation(), Sound.valueOf(sound), 0.8f, 1);
        }
    }

    private void noteRedeem(Player player, int money) {

        economyUtil.giveMoney(player, money);
        if (main.getConfig().getBoolean("banknote.sounds.redeem.enabled")) {
            String sound = main.getConfig().getString("banknote.sounds.redeem.sound");
            player.playSound(player.getLocation(), Sound.valueOf(sound), 0.8f, 1);
        }
    }
}