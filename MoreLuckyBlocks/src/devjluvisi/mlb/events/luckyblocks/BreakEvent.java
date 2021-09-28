package devjluvisi.mlb.events.luckyblocks;

import org.apache.commons.lang.Validate;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;

public class BreakEvent implements Listener {

	private final MoreLuckyBlocks plugin;

	public BreakEvent(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void breakLuckyBlockEvent(BlockBreakEvent e) {
		if (!this.plugin.getAudit().isLuckyBlock(e.getBlock().getLocation())) {
			return;
		}
		final LuckyBlock lb = this.plugin.getAudit().find(e.getBlock().getLocation());
		Validate.notNull(lb);

		e.getPlayer().sendMessage("You broke a lucky block -> " + lb.getInternalName());
		e.getPlayer().sendMessage("Its Luck -> " + lb.getBlockLuck());
		e.getPlayer()
				.sendMessage("Your Player Luck -> " + this.plugin.getPlayerLuckMap().get(e.getPlayer().getUniqueId()));
		this.plugin.getAudit().remove(e.getBlock().getLocation());
	}

}
