package me.imlukas.withdrawer.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class WithdrawEvent extends Event implements Cancellable {
    public enum WithdrawType {
        BANKNOTE,
        EXPBOTTLE
    }

    private boolean cancelled = false;


    private final Player player;
    private final double amount;
    private int quantity;
    private final WithdrawType type;

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public WithdrawEvent(Player player, double amount, WithdrawType type) {
        this.player = player;
        this.amount = amount;
        this.type = type;

    }

    public WithdrawEvent(Player player, double amount, int quantity, WithdrawType type) {
        this.player = player;
        this.amount = amount;
        this.quantity = quantity;
        this.type = type;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

}
