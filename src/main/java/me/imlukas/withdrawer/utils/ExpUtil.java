package me.imlukas.withdrawer.utils;

import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class ExpUtil {

    public void setTotalExperience(Player player, int exp) {
        if (exp < 0) {
            return;
        }
        player.setExp(0.0F);
        player.setLevel(0);
        player.setTotalExperience(0);

        int amount = exp;
        while (amount > 0) {
            int expToLevel = getExpAtLevel(player);
            amount -= expToLevel;
            if (amount >= 0) {

                player.giveExp(expToLevel);
                continue;
            }
            amount += expToLevel;
            player.giveExp(amount);
            amount = 0;
        }
    }


    private int getExpAtLevel(Player player) {
        return getExpAtLevel(player.getLevel());
    }


    private int getExpAtLevel(int level) {
        if (level <= 15) {
            return 2 * level + 7;
        }
        if (level >= 16 && level <= 30) {
            return 5 * level - 38;
        }
        return 9 * level - 158;
    }


    private int getExpToLevel(int level) {
        int currentLevel = 0;
        int exp = 0;

        while (currentLevel < level) {
            exp += getExpAtLevel(currentLevel);
            currentLevel++;
        }
        if (exp < 0) {
            exp = Integer.MAX_VALUE;
        }
        return exp;
    }



    public int getTotalExperience(Player player) {
        int exp = Math.round(getExpAtLevel(player) * player.getExp());
        int currentLevel = player.getLevel();

        while (currentLevel > 0) {
            currentLevel--;
            exp += getExpAtLevel(currentLevel);
        }
        if (exp < 0) {
            exp = Integer.MAX_VALUE;
        }
        return exp;
    }

    private int getExpUntilNextLevel(Player player) {
        int exp = Math.round(getExpAtLevel(player) * player.getExp());
        int nextLevel = player.getLevel();
        return getExpAtLevel(nextLevel) - exp;
    }

    public void removeExp(Player player, double exp) {
    }
}
