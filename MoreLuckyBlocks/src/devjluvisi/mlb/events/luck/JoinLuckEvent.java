package devjluvisi.mlb.events.luck;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.players.LuckyPlayer;

public class JoinLuckEvent implements Listener {
	private MoreLuckyBlocks plugin;
	
	public JoinLuckEvent(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
	}
	
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		LuckyPlayer p = new LuckyPlayer(plugin).track(e.getPlayer().getUniqueId());
		
		if(plugin.getPlayersYaml().getConfig().get("players. " + p.getPlayerUUID().toString()) == null) {
			plugin.getPlayerLuckMap().put(p.getPlayerUUID(), MoreLuckyBlocks.DEFAULT_LUCK);
			p.writeToConfig();
			plugin.getPlayersYaml().save();
			plugin.getPlayersYaml().reload();
		}
		plugin.getPlayerLuckMap().put(e.getPlayer().getUniqueId(), (float)plugin.getPlayersYaml().getConfig().get(e.getPlayer().getUniqueId().toString()));
		
	}
}
