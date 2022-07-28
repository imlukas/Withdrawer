package me.imlukas.withdrawer.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
@Getter
public class ItemRedeemEvent extends Event implements Cancellable {
    public enum ReedemType {
        BANKNOTE,
        EXPBOTTLE
    }

    private boolean cancelled = false;
    Player player;
    double amount;
    ReedemType type;
    int quantity = 1;

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public ItemRedeemEvent(Player player, double amount, ReedemType type) {
        this.player = player;
        this.amount = amount;
        this.type = type;
    }
    public ItemRedeemEvent(Player player, double amount, ReedemType type, int quantity) {
        this.player = player;
        this.amount = amount;
        this.type = type;
        this.quantity = quantity;


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
