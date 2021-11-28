package devjluvisi.mlb.events;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.events.custom.LogDataEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class UpdateLogEvent implements Listener {

    private MoreLuckyBlocks plugin;

    public UpdateLogEvent(MoreLuckyBlocks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void process(LogDataEvent e) {
        plugin.getLoggingMessages().add(e.getMessage());
    }

}
