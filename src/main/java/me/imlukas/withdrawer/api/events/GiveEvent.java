package me.imlukas.withdrawer.api.events;

import me.imlukas.withdrawer.v3.item.Withdrawable;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GiveEvent extends Event implements Cancellable {
    private boolean cancelled = false;

    private final CommandSender gifter;
    private final Player target;
    private final Withdrawable withdrawable;

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }


    public GiveEvent(CommandSender player, Player target, Withdrawable withdrawable) {
        this.gifter = player;
        this.target = target;
        this.withdrawable = withdrawable;
    }

    /**
     * Returns who gave the item, can be console or player
     * @return who gave the item
     */
    public CommandSender getGifter() {
        return gifter;
    }

    public Player getTarget() {
        return target;
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
