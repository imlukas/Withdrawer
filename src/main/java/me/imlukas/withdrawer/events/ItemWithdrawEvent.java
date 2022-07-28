package me.imlukas.withdrawer.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
@Getter
public class ItemWithdrawEvent extends Event implements Cancellable {
    public enum WithdrawType {
        BANKNOTE,
        EXPBOTTLE
    }
    private boolean cancelled = false;


    private Player player;
    private double amount;
    private int quantity;
    private WithdrawType type;

    private static final HandlerList HANDLERS = new HandlerList();
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public ItemWithdrawEvent(Player player, double amount, WithdrawType type) {
        this.player = player;
        this.amount = amount;
        this.type = type;

    }
    public ItemWithdrawEvent(Player player, double amount, int quantity,  WithdrawType type) {
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
