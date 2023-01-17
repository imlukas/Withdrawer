package me.imlukas.withdrawer.utils;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;


public class HealthUtil {
    public void addHealth(Player player, int health) {

        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute == null) {
            return;
        }

        double oldHealth = attribute.getValue();
        attribute.setBaseValue(oldHealth + health);
    }


    public void removeHealth(Player player, int health) {

        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute == null) {
            return;
        }

        double oldHealth = attribute.getValue();
        attribute.setBaseValue(oldHealth - health);
    }

    public boolean checkHealth(Player player, int health) {
        int oldHealth = (int) (player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - 1);

        return !(oldHealth <= health);
    }

    public double getHealth(Player player) {
        return player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
    }
}
