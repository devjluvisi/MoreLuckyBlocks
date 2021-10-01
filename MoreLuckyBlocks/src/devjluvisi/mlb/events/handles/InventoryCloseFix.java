package devjluvisi.mlb.events.handles;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.blocks.LuckyBlockDrop;

/**
 * Fix user errors related to inventory closing.
 *
 * @author jacob
 *
 */
public class InventoryCloseFix implements Listener {

	private final MoreLuckyBlocks plugin;

	public InventoryCloseFix(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void fixAddDrop(InventoryCloseEvent e) {

		// Only remove a drop from the luckyblock if the player has CLOSED ALL
		// INVENTORIES.
		// Removing this will cause index out of bounds exceptions when editing an added
		// item.
		if (!e.getPlayer().hasPermission("mlb.admin.edit") || !e.getView().getTitle().contains("Editing Drop")) {
			return;
		}
		if(plugin.getPlayersEditingDrop().containsKey(e.getPlayer().getUniqueId())) {
			return;
		}
		for (final LuckyBlock b : this.plugin.getLuckyBlocks()) {
			for (final LuckyBlockDrop d : b.getDroppableItems()) {
				if (d.getLoot().size() == 0) {
					b.removeDrop(d);
				}
			}
		}

	}

}
