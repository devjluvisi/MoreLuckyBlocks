package devjluvisi.mlb.players;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;

public class LuckyPlayer {
	
	private final MoreLuckyBlocks plugin;
	private UUID playerUUID;
	private float playerLuck;
	
	public LuckyPlayer(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
		this.playerLuck = MoreLuckyBlocks.DEFAULT_LUCK;
	}
	
	public LuckyPlayer trackOffline(UUID u) {
		this.playerUUID = u;
		if(plugin.getPlayersYaml().getConfig().get(u.toString()) == null) {
			throw new IllegalArgumentException("Specified UUID of " + u.toString() + " could not be found in the configuration files.");
		}
		this.playerLuck = (float)plugin.getPlayersYaml().getConfig().get(u.toString());
		return this; 
	}
	
	public LuckyPlayer track(UUID u) {
		this.playerUUID = u;
		this.playerLuck = plugin.getPlayerLuckMap().get(playerUUID);
		return this;
	}
	/*
	public LuckyPlayer byNameOffline(String name) {
		for(String players : plugin.getPlayersYaml().getConfig().getConfigurationSection("players").getKeys(false)) {
			if(players.equalsIgnoreCase("name")) {
				if(((String)plugin.getPlayersYaml().getConfig().get("players.name")).equalsIgnoreCase(name)) {
					this.playerUUID = UUID.fromString((String)plugin.getPlayersYaml().getConfig().get("players." + plugin.getPlayersYaml().getConfig().get("players.name")));
					//this.playerLuck = plugin.getPlayersYaml().getConfig().get("players.uuid")
					//return new LuckyPlayer(plugin, )
				}
			}
		}
	}
	*/
	public void changeTarget(UUID u) {
		this.playerUUID = u;
		this.playerLuck = plugin.getPlayerLuckMap().get(playerUUID);
	}
	
	public Player getPlayer() {
		return plugin.getServer().getPlayer(playerUUID);
	}
	
	public boolean isOnline() {
		return plugin.getServer().getPlayer(playerUUID) != null;
	}
	
	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public void setPlayerUUID(UUID playerUUID) {
		this.playerUUID = playerUUID;
		this.playerLuck = plugin.getPlayerLuckMap().get(playerUUID);
	}

	public float getPlayerLuck() {
		return playerLuck;
	}

	public void setPlayerLuck(float playerLuck) {
		this.playerLuck = playerLuck;
		plugin.getPlayerLuckMap().put(playerUUID, this.playerLuck);
	}

	public void writeToConfig() {
		plugin.getPlayersYaml().getConfig().set("players." + playerUUID.toString() + ".name", Bukkit.getPlayer(playerUUID).getName());
		plugin.getPlayersYaml().getConfig().set("players." + playerUUID.toString() + ".luck", playerLuck);
		plugin.getPlayersYaml().save();
		plugin.getPlayersYaml().reload();
	}
	
	/**
	 * Calculates the player luck in tandom with the luck of a lucky block.
	 * @param block
	 * @return
	 */
	public double calculate(LuckyBlock block) {
		return -1;
	}
	
	
	
	

}
