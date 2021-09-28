package devjluvisi.mlb.events.luck;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.players.LuckyPlayer;

public class LeaveLuckEvent implements Listener {
	
	private MoreLuckyBlocks plugin;
	
	public LeaveLuckEvent(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void leave(PlayerQuitEvent e) {
		LuckyPlayer p = new LuckyPlayer(plugin).track(e.getPlayer().getUniqueId());
		p.writeToConfig();
		plugin.getPlayerLuckMap().remove(p.getPlayerUUID());
	}

}
