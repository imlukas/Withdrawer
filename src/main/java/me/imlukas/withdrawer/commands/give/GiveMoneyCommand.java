package me.imlukas.withdrawer.commands.give;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.economy.EconomyManager;
import me.imlukas.withdrawer.economy.IEconomy;
import me.imlukas.withdrawer.item.WithdrawableItem;
import me.imlukas.withdrawer.item.impl.MoneyItem;
import me.imlukas.withdrawer.utils.command.SimpleCommand;
import me.imlukas.withdrawer.utils.interactions.messages.MessagesFile;
import me.imlukas.withdrawer.utils.text.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

public class GiveMoneyCommand implements SimpleCommand {

    private final Withdrawer plugin;
    private final EconomyManager economyManager;
    private final MessagesFile messages;

    public GiveMoneyCommand(Withdrawer plugin) {
        this.plugin = plugin;
        this.economyManager = plugin.getEconomyManager();
        this.messages = plugin.getMessages();
    }

    @Override
    public String getIdentifier() {
        return "give.money.*.*.*.*";
    }

    @Override
    public void execute(CommandSender sender, String... args) {
        if (args[0].isEmpty()) {
            messages.sendMessage(sender, "command.invalid-arguments");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            messages.sendMessage(sender, "command.player-not-found");
            return;
        }

        int value = 1;
        if (!args[1].isEmpty()) {
            value = TextUtils.parseInt(args[0], (integer -> integer > 0));
        }

        int amount = 1;
        if (!args[2].isEmpty()) {
            amount = TextUtils.parseInt(args[1], (integer -> integer > 0));
        }

        IEconomy economy = economyManager.getFirstEconomy();
        if (!args[3].isEmpty()) {
            economy = getEconomy(args[2]);
        }

        while (amount > 64) {
            amount -= 64;
            giveItem(target, value, 64, economy);
        }

        giveItem(target, value, amount, economy);
    }

    private void giveItem(Player target, int value, int amount, IEconomy economy) {
        MoneyItem moneyItem = new MoneyItem(plugin, value, amount, economy);
        moneyItem.give(target);
    }

    private IEconomy getEconomy(String economy) {
        return economyManager.getEconomy(economy);
    }
}
