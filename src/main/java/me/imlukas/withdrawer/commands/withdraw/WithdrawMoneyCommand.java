package me.imlukas.withdrawer.commands.withdraw;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.api.events.WithdrawEvent;
import me.imlukas.withdrawer.config.ItemHandler;
import me.imlukas.withdrawer.economy.EconomyManager;
import me.imlukas.withdrawer.economy.IEconomy;
import me.imlukas.withdrawer.item.impl.MoneyItem;
import me.imlukas.withdrawer.utils.command.SimpleCommand;
import me.imlukas.withdrawer.utils.interactions.messages.MessagesFile;
import me.imlukas.withdrawer.utils.text.Placeholder;
import me.imlukas.withdrawer.utils.text.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class WithdrawMoneyCommand implements SimpleCommand {

    private final Withdrawer plugin;
    private final MessagesFile messages;
    private final ItemHandler itemsHandler;
    private final EconomyManager economyManager;

    public WithdrawMoneyCommand(Withdrawer plugin) {
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
    public String getIdentifier() {
        return "withdraw.money.*.*.*";
    }

    @Override
    public void execute(CommandSender sender, String... args) {
        Player player = (Player) sender;
        int value = 1;
        if (!args[0].isEmpty()) {
            value = TextUtils.parseInt(args[0], (integer -> integer > 0));
        }

        int amount = 1;
        if (!args[1].isEmpty()) {
            amount = TextUtils.parseInt(args[1], (integer -> integer > 0));
        }

        IEconomy economy = economyManager.getFirstEconomy();
        if (!args[2].isEmpty()) {
            economy = getEconomy(args[2]);
        }

        while (amount > 64) {
            amount -= 64;
            giveItem(player, value, 64, economy);
        }

        giveItem(player, value, amount, economy);
    }

    private void giveItem(Player player, int value, int amount, IEconomy economy) {
        MoneyItem moneyItem = new MoneyItem(plugin, value, amount, economy);

        if (!checkValues(player, value * amount, moneyItem.getConfigName())) {
            return;
        }

        WithdrawEvent withdrawEvent = new WithdrawEvent(player, moneyItem);
        Bukkit.getPluginManager().callEvent(withdrawEvent);

        if (withdrawEvent.isCancelled()) {
            return;
        }

        moneyItem.withdraw(player);
    }

    private IEconomy getEconomy(String economy) {
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
