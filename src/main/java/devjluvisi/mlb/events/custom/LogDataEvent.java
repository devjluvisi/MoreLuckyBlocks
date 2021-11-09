package devjluvisi.mlb.events.custom;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.time.Instant;
import java.time.LocalDateTime;

public class LogDataEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean isCancelled;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final String message;

    public LogDataEvent(String message) {
        this.message = Instant.now().toString().split("\\.")[0] + " (UTC) " + message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }
}
