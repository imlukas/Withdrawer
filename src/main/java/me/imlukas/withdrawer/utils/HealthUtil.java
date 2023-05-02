package me.imlukas.withdrawer.utils;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class HealthUtil {

    public static void addHealth(Player player, int health) {
        double oldHealth = getHealth(player);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(oldHealth + health);
    }

    public static void removeHealth(Player player, int health) {
        double oldHealth = getHealth(player);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(oldHealth - health);
    }

    public static boolean checkHealth(Player player, int health) {
        double oldHealth = (int) (player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - 1);

        return !(oldHealth <= health);
    }

    public static double getHealth(Player player) {
        return player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
    }
}