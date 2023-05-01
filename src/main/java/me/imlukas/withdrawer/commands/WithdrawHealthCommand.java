package me.imlukas.withdrawer.commands;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.economy.EconomyManager;
import me.imlukas.withdrawer.economy.IEconomy;
import me.imlukas.withdrawer.events.WithdrawEvent;
import me.imlukas.withdrawer.item.impl.HealthItem;
import me.imlukas.withdrawer.item.registry.WithdrawableItemsStorage;
import me.imlukas.withdrawer.utils.command.SimpleCommand;
import me.imlukas.withdrawer.utils.interactions.messages.MessagesFile;
import me.imlukas.withdrawer.utils.text.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class WithdrawHealthCommand implements SimpleCommand {

    private final Withdrawer plugin;

    public WithdrawHealthCommand(Withdrawer plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "withdraw.hp.*.*";
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

        HealthItem hpItem = new HealthItem(plugin, UUID.randomUUID(), value, amount);

        WithdrawEvent withdrawEvent = new WithdrawEvent(player, hpItem);
        Bukkit.getPluginManager().callEvent(withdrawEvent);

        if (withdrawEvent.isCancelled()) {
            return;
        }

        hpItem.withdraw(player);
    }


}
