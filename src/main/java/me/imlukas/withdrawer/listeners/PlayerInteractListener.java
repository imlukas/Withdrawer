package me.imlukas.withdrawer.listeners;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.utils.EconomyUtil;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private final Withdrawer main;
    private final EconomyUtil economyUtil;
    private final MessagesFile messages;

    public PlayerInteractListener(Withdrawer main) {
        this.main = main;
        this.economyUtil = new EconomyUtil(main);
        this.messages = main.getMessages();

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
        NBTItem nbtitem = new NBTItem(event.getItem());

        if (itemMaterial.equals(Material.getMaterial(main.getConfig().getString("banknote.item").toUpperCase())) && nbtitem.hasKey("money-value")) {
            int money = nbtitem.getInteger("money-value");
            noteRedeem(player, money);
        }
        else if (itemMaterial.equals(Material.getMaterial(main.getConfig().getString("expbottle.item").toUpperCase())) && nbtitem.hasKey("exp-value")) {
            int exp = nbtitem.getInteger("exp-value");
            //todo
        }

        event.setCancelled(true);
        if (event.getItem().getAmount() > 1 ){
            event.getItem().setAmount(event.getItem().getAmount() - 1);
            return;
        }
        player.getInventory().setItemInMainHand(null);
    }

    private void noteRedeem(Player player, int money) {
        economyUtil.giveMoney(player, money);
        if (main.getConfig().getBoolean("banknote.sounds.redeem.enabled")) {
            String sound =  main.getConfig().getString("banknote.sounds.redeem.sound");
            player.playSound(player.getLocation(), Sound.valueOf(sound), 0.8f, 1);
        }
    }
}