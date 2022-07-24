package me.imlukas.withdrawer.commands;

import me.imlukas.withdrawer.Withdrawer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Collections;
import java.util.List;

public class ExpBottleCommand implements CommandExecutor, TabCompleter {

    private final Withdrawer main;

    public ExpBottleCommand(Withdrawer main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
