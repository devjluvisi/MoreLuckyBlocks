package devjluvisi.mlb.events.players;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import devjluvisi.mlb.MoreLuckyBlocks;

public class LeaveLuckEvent implements Listener {

	private final MoreLuckyBlocks plugin;

	public LeaveLuckEvent(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void leave(PlayerQuitEvent e) {
		this.plugin.getPlayerManager().remove(e.getPlayer());
	}

}
