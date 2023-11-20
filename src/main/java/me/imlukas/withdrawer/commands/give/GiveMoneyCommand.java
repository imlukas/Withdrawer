package me.imlukas.withdrawer.commands.give;

import me.imlukas.withdrawer.WithdrawerPlugin;
import me.imlukas.withdrawer.economy.EconomyManager;
import me.imlukas.withdrawer.economy.Economy;
import me.imlukas.withdrawer.item.impl.MoneyItem;
import me.imlukas.withdrawer.utils.command.SimpleCommand;
import me.imlukas.withdrawer.utils.interactions.Messages;
import me.imlukas.withdrawer.utils.text.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class GiveMoneyCommand implements SimpleCommand {

    private final WithdrawerPlugin plugin;
    private final EconomyManager economyManager;
    private final Messages messages;

    public GiveMoneyCommand(WithdrawerPlugin plugin) {
        this.plugin = plugin;
        this.economyManager = plugin.getEconomyManager();
        this.messages = plugin.getMessages();
    }

    @Override
    public Map<Integer, List<String>> tabCompleteWildcards() {
        return Map.of(1, List.of("10", "100", "1000"),
                3, economyManager.getEconomyIdentifiers());
    }

    @Override
    public String getIdentifier() {
        return "wd.give.money.*.*.*.*";
    }

    @Override
    public void execute(CommandSender sender, String... args) {
        if (args[0].isEmpty()) {
            messages.sendMessage(sender, "command.invalid-arguments");
            return;
        }

        if (sender instanceof Player player) {
            if (!player.hasPermission("withdrawer.give.*")) {
                if (!player.hasPermission("withdrawer.give.money")) {
                    messages.sendMessage(player, "command.no-permission");
                    return;
                }
            }
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            messages.sendMessage(sender, "command.player-not-found");
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

        while (amount > 64) {
            amount -= 64;
            giveItem(target, value, 64, economy);
        }

        giveItem(target, value, amount, economy);
    }

    private void giveItem(Player target, int value, int amount, Economy economy) {
        MoneyItem moneyItem = new MoneyItem(plugin, value, amount, economy);
        moneyItem.give(target, value * amount);
    }

    private Economy getEconomy(String economy) {
        return economyManager.getEconomy(economy);
    }
}
