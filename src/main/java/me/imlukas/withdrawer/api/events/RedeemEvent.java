package me.imlukas.withdrawer.api.events;

import lombok.Getter;
import me.imlukas.withdrawer.item.WithdrawableItem;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class RedeemEvent extends Event implements Cancellable {

    private boolean cancelled = false;
    private final Player player;
    private final WithdrawableItem withdrawable;

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public RedeemEvent(Player player, WithdrawableItem withdrawable) {
        this.player = player;
        this.withdrawable = withdrawable;
    }

    public Player getPlayer() {
        return player;
    }

    public WithdrawableItem getWithdrawableItem() {
        return withdrawable;
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
