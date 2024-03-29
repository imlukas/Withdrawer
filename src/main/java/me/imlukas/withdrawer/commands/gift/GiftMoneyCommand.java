package me.imlukas.withdrawer.commands.gift;

import me.imlukas.withdrawer.WithdrawerPlugin;
import me.imlukas.withdrawer.api.events.GiftEvent;
import me.imlukas.withdrawer.config.ItemHandler;
import me.imlukas.withdrawer.economy.EconomyManager;
import me.imlukas.withdrawer.economy.Economy;
import me.imlukas.withdrawer.item.impl.MoneyItem;
import me.imlukas.withdrawer.utils.command.SimpleCommand;
import me.imlukas.withdrawer.utils.interactions.Messages;
import me.imlukas.withdrawer.utils.text.Placeholder;
import me.imlukas.withdrawer.utils.text.TextUtils;
import me.imlukas.withdrawer.v3.item.Withdrawable;
import me.imlukas.withdrawer.v3.item.impl.MoneyWithdrawableItem;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class GiftMoneyCommand implements SimpleCommand {

    private final WithdrawerPlugin plugin;
    private final Messages messages;
    private final ItemHandler itemsHandler;
    private final EconomyManager economyManager;

    public GiftMoneyCommand(WithdrawerPlugin plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMessages();
        this.itemsHandler = plugin.getItemHandler();
        this.economyManager = plugin.getEconomyManager();
    }

    @Override
    public Map<Integer, List<String>> tabCompleteWildcards() {
        return Map.of(1, List.of("10", "100", "1000"),
                3, economyManager.getEconomyIdentifiers());
    }


    @Override
    public String getPermission() {
        return "withdrawer.gift.money";
    }

    @Override
    public String getIdentifier() {
        return "wd.gift.money.*.*.*.*";
    }

    @Override
    public void execute(CommandSender sender, String... args) {
        Player gifter = (Player) sender;
        if (args[0].isEmpty()) {
            messages.sendMessage(gifter, "command.invalid-args");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            messages.sendMessage(gifter, "command.player-not-found");
            return;
        }


        int value = 1;
        if (!args[1].isEmpty()) {
            value = TextUtils.parseInt(args[1], (integer -> integer > 0));
        }

        int amount = 1;
        if (!args[2].isEmpty()) {
            amount = TextUtils.parseInt(args[2], (integer -> integer > 0));
        }

        Economy economy = economyManager.getFirstEconomy();
        if (!args[3].isEmpty()) {
            economy = getEconomy(args[3]);
        }

        if (amount > 64) {
            giveItem(gifter, target, amount / 64, value, 64, economy);
            return;
        }

        giveItem(gifter, target, 1, value, amount, economy);
    }

    private void giveItem(Player gifter, Player target, int iterations, int value, int amount, Economy economy) {
        for (int i = 0; i < iterations; i++) {
            Withdrawable moneyItem = new MoneyWithdrawableItem(plugin, value, amount, economy);

            if (!checkValues(gifter, value * amount, moneyItem.getConfigName())) {
                return;
            }

            GiftEvent giftEvent = new GiftEvent(gifter, target, moneyItem);
            Bukkit.getPluginManager().callEvent(giftEvent);

            if (giftEvent.isCancelled()) {
                return;
            }

            moneyItem.gift(gifter, target);
        }
    }

    private Economy getEconomy(String economy) {
        return economyManager.getEconomy(economy);
    }

    public boolean checkValues(Player player, int totalValue, String identifier) {
        if (player.hasPermission("withdrawer.bypass.minmax." + identifier)) {
            return true;
        }

        int min = itemsHandler.getMinValue(identifier);
        int max = itemsHandler.getMaxValue(identifier);

        List<Placeholder<Player>> placeholders = List.of(new Placeholder<>("min", String.valueOf(min)),
                new Placeholder<>("max", String.valueOf(max)));

        if (totalValue < min || totalValue > max) {
            messages.sendMessage(player, "command.invalid-values", placeholders);
            return false;
        }

        return true;
    }


}
