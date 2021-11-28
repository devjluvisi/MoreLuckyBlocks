package devjluvisi.mlb.events;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.events.custom.DataChangedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * An event which saves all of the current data to the config,
 * similar to the /mlb config save command.
 * Does not include editing the world.
 */
public class SaveConfigEvent implements Listener {

    private MoreLuckyBlocks plugin;

    public SaveConfigEvent(MoreLuckyBlocks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void callSave(DataChangedEvent e) {
        plugin.getSavingManager().saveResources();
    }

}
