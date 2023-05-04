package me.imlukas.withdrawer.api.events;

import lombok.Getter;
import me.imlukas.withdrawer.item.Withdrawable;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class WithdrawEvent extends Event implements Cancellable {

    private boolean cancelled = false;


    private final Player player;
    private final Withdrawable withdrawable;

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }


    public WithdrawEvent(Player player, Withdrawable withdrawable) {
        this.player = player;
        this.withdrawable = withdrawable;
    }

    public Player getPlayer() {
        return player;
    }

    public Withdrawable getWithdrawableItem() {
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
