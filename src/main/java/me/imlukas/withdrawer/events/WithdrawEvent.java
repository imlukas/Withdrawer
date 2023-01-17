package me.imlukas.withdrawer.events;

import lombok.Getter;
import me.imlukas.withdrawer.constant.ItemType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class WithdrawEvent extends Event implements Cancellable {

    private boolean cancelled = false;


    private final Player player;
    private final double amount;
    private final int quantity;
    private final ItemType type;

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }


    public WithdrawEvent(Player player, double amount, int quantity, ItemType type) {
        this.player = player;
        this.amount = amount;
        this.quantity = Math.max(quantity, 1);
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
