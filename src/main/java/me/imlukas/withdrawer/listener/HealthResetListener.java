package me.imlukas.withdrawer.listener;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class HealthResetListener implements Listener {


    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        double playerMaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

        if (playerMaxHealth > 20) {
            player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        }
    }
}
