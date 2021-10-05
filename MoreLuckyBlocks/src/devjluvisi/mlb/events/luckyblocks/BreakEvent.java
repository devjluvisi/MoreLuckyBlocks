package devjluvisi.mlb.events.luckyblocks;

import java.util.LinkedList;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import devjluvisi.mlb.util.structs.RelativeBlock;
import net.md_5.bungee.api.ChatColor;

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
		if (Objects.isNull(lb)) {
			return;
		}
		if (lb.getDroppableItems().size() == 0) {
			e.getPlayer().sendMessage(ChatColor.RED
					+ "This was a lucky block but it did not drop anything because no drops were available.");
			e.setCancelled(true);
			return;
		}
		e.getPlayer().sendMessage(ChatColor.YELLOW + "You broke a lucky block!");
		LuckyBlockDrop drop = lb.generateDrop(this.plugin.getPlayerManager().getPlayer(e.getPlayer().getName()).getLuck());
		drop.applyTo(e.getBlock().getLocation(), e.getPlayer());
		
		if(drop.hasStructure()) {
			for(String s : plugin.getStructuresYaml().getConfig().getStringList("structures." + drop.getStructure().toString())) {
				RelativeBlock r = new RelativeBlock().deserialize(s);
				Location loc = e.getBlock().getLocation();
				//todo; get direction of player and adjust.
				r.setOffset(loc);
				r.place(e.getBlock().getLocation().getWorld());
			}
		}
		this.plugin.getAudit().remove(e.getBlock().getLocation());
	}

}
