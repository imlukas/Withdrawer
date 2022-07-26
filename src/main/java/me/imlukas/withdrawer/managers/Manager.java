package me.imlukas.withdrawer.managers;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.events.WithdrawEvent;
import me.imlukas.withdrawer.events.WithdrawType;
import me.imlukas.withdrawer.utils.EconomyUtil;
import me.imlukas.withdrawer.utils.ExpUtil;
import me.imlukas.withdrawer.utils.HealthUtil;
import me.imlukas.withdrawer.utils.TextUtil;
import me.imlukas.withdrawer.utils.illusion.item.ItemBuilder;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Manager {

    protected final Withdrawer main;
    protected final MessagesFile messages;
    protected final ExpUtil expUtil;
    protected final EconomyUtil economyUtil;
    protected final HealthUtil healthUtil;
    protected final TextUtil textUtil;
    private final String type;
    private final List<Material> consumables = new ArrayList<>(Arrays.asList(Material.POTION, Material.SPLASH_POTION, Material.LINGERING_POTION));

    public Manager(Withdrawer main, String type) {
        this.main = main;
        this.messages = main.getMessages();
        this.expUtil = main.getExpUtil();
        this.economyUtil = main.getEconomyUtil();
        this.healthUtil = main.getHealthUtil();
        this.textUtil = main.getTextUtil();
        this.type = type;

    }

    public boolean callEvent(Player player, double value, int quantity, WithdrawType type) {
        WithdrawEvent withdrawEvent = new WithdrawEvent(player, value, quantity, type);
        Bukkit.getServer().getPluginManager().callEvent(withdrawEvent);
        return withdrawEvent.isCancelled();
    }

    public void giveItem(Player player, double value, int amount) {
        ItemStack item = setItemProperties(player, value);
        if (amount > 1) {
            for (int i = 0; i < amount; i++) {
                player.getInventory().addItem(item);
            }
        } else {
            player.getInventory().addItem(item);
        }
    }

    public ItemStack setItemProperties(Player player, double value) {
        Material itemMaterial = getItemMaterial(type);

        ItemStack finalItem = new ItemBuilder(itemMaterial)
                .name(textUtil.getColorConfig(type + ".name"))
                .glowing(true)
                .build();
        // nbt setup
        NBTItem nbtItem = new NBTItem(finalItem);
        nbtItem.setDouble(type + "-value", value);
        nbtItem.applyNBT(finalItem);

        ItemMeta meta = finalItem.getItemMeta();
        // item setup
        List<String> lore = new ArrayList<>();
        for (String str : main.getConfig().getStringList(type + ".lore")) {
            String newText = str.replace("%value%", "" + nbtItem.getDouble(type + "-value"))
                    .replace("%owner%", player.getName());
            lore.add(textUtil.setColor(newText));
        }
        if (itemMaterial.equals(Material.BARRIER)) {
            lore.add(textUtil.setColor("&cThis item is a barrier because something is wrong in the config."));
            lore.add(textUtil.setColor("&cBut you can still use it :)."));
        }

        if (meta != null) {
            meta.setLore(lore);
        }
        finalItem.setItemMeta(meta);
        return finalItem;
    }

    public void sendActionBar(Player player, double value, boolean success, boolean console) {
        String currencySign = economyUtil.getCurrencySign();
        if (console && success) {
            messages.sendActionBarMessage(player, type + "-withdraw.actionbar-console", (message) -> message
                    .replace("%value%", String.valueOf(value))
                    .replace("%currency_sign%", currencySign));
            return;
        }
        if (success) {
            messages.sendActionBarMessage(player, type + "-withdraw.actionbar-success", (message) -> message
                    .replace("%value%", String.valueOf(value))
                    .replace("%currency_sign%", currencySign));
            return;
        }
        messages.sendActionBarMessage(player, type + "-withdraw.actionbar-error", (message) -> message
                .replace("%value%", String.valueOf(value)));
    }

    public void sendGiftMessage(Player target, int value, int quantity) {
        String currencySign = economyUtil.getCurrencySign();
        messages.sendMessage(target, type + "-withdraw.gifted", (message) -> message
                .replace("%type%", "banknote")
                .replace("%currency_sign%", currencySign)
                .replace("%amount%", String.valueOf(value * quantity))
                .replace("%quantity%", String.valueOf(quantity)));

    }

    public void sendMessages(Player player, double value, boolean success, boolean console) {
        String currencySign = economyUtil.getCurrencySign();
        String balance = String.valueOf(economyUtil.getMoney(player));
        String currentExp = String.valueOf(expUtil.getExp(player));
        String currentHealth = String.valueOf(healthUtil.getHealth(player) / 2);
        if (console) {
            messages.sendMessage(player, type + "-withdraw.console", (message) -> message
                    .replace("%value%", String.valueOf(value))
                    .replace("%balance%", balance)
                    .replace("%current_exp%", currentExp)
                    .replace("%current_health%", currentHealth)
                    .replace("%currency_sign%", currencySign));
            return;
        }
        if (success) {
            if (messages.getConfiguration().getBoolean("messages.less-intrusive")) {
                if (type.equalsIgnoreCase("expbottle")) {
                    messages.sendStringMessage(player, "&c-" + value + "EXP");
                } else if (type.equalsIgnoreCase("banknote")) {
                    messages.sendStringMessage(player, "&c-" + value + currencySign);
                } else {
                    messages.sendStringMessage(player, "&c-" + value + "HP");
                }
                return;
            }
            messages.sendMessage(player, type + "-withdraw.success", (message) -> message
                    .replace("%value%", String.valueOf(value))
                    .replace("%current_exp%", currentExp)
                    .replace("%balance%", balance)
                    .replace("%current_health%", currentHealth)
                    .replace("%currency_sign%", currencySign));
            return;
        }
        messages.sendMessage(player, type + "-withdraw.error", (message) -> message
                .replace("%current_exp%", "" + currentExp)
                .replace("%balance%", balance)
                .replace("%currency_sign%", currencySign)
                .replace("%current_health%", currentHealth));
    }

    public void playWithdrawSound(Player player) {
        if (main.getConfig().getBoolean(type + ".sounds.withdraw.enabled")) {
            player.playSound(player.getLocation(), Sound.valueOf(main.getConfig().getString(type + ".sounds.withdraw.sound").toUpperCase()), 0.8f, 1);
        }
    }

    private Material getItemMaterial(String type) {
        Material itemMaterial = Material.getMaterial(main.getConfig().getString(type + ".item").toUpperCase());
        if (itemMaterial == null || itemMaterial.isEdible() || consumables.contains(itemMaterial)) {
            itemMaterial = Material.BARRIER;
        }
        return itemMaterial;
    }
}
