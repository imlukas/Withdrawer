package me.imlukas.withdrawer.utils;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;


public class HealthUtil {

    public void addHealth(Player player, int health){
        double oldHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(oldHealth + health);
    }
    public void removeHealth(Player player, int health){
        double oldHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(oldHealth - health);
    }
    public boolean checkHealth(Player player, int health){
        int oldHealth = (int) (player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - 1);

        return !(oldHealth <= health);
    }
    public double getHealth(Player player){
        return player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
    }
}
