package me.imlukas.withdrawer.managers;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.events.WithdrawEvent;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.List;

public class HealthItem extends Manager {
    private final Withdrawer main;

    public HealthItem(Withdrawer main) {
        super(main, "heart");
        this.main = main;
    }

    public void give(Player player, double hp) {

        if (checkHealth(player)) {
            playWithdrawSound(player);
            sendMessages(player, 1, true);
            ItemStack item = setItemProperties(player, (int) hp);
            callEvent(player, hp, 1, WithdrawEvent.WithdrawType.HEALTH);
            player.getInventory().addItem(item);
            return;
        }
        sendMessages(player, 1, false);
    }

    @Override
    public ItemStack setItemProperties(Player player, int hp) {
        Material itemMaterial = Material.getMaterial(main.getConfig().getString("health.item").toUpperCase());
        if (itemMaterial == null) {
            itemMaterial = Material.BARRIER;
        }
        ItemStack finalItem = new ItemStack(itemMaterial);
        // nbt setup
        NBTItem nbtItem = new NBTItem(finalItem);
        nbtItem.setInteger("health-value", hp);
        nbtItem.applyNBT(finalItem);

        PotionMeta meta = (PotionMeta) finalItem.getItemMeta();
        // item setup
        List<String> lore = new ArrayList<>();
        for (String str : main.getConfig().getStringList("health.lore")) {
            String newText = str.replace("%hp%", "" + nbtItem.getDouble("health-value"));
            lore.add(textUtil.getColor(newText));
        }
        meta.addEnchant(Enchantment.LUCK, 123, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setLore(lore);
        finalItem.setItemMeta(meta);
        return finalItem;

    }

    private boolean checkHealth(Player player) {
        // todo
        return true;
    }
}
