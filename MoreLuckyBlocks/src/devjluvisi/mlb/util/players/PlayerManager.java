package devjluvisi.mlb.util.players;

import java.util.Objects;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import devjluvisi.mlb.MoreLuckyBlocks;

public class PlayerManager {

	private final MoreLuckyBlocks plugin;

	public PlayerManager(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
	}

	public void update(UUID uuid, float newLuck) {
		this.plugin.getPlayersYaml().getConfig().set("players." + uuid.toString() + ".name", String.valueOf(Bukkit.getServer().getPlayer(uuid).getName()));
		updateOffline(uuid, newLuck);
		this.save();
	}

	public void save() {
		plugin.getPlayersYaml().save();
		plugin.getPlayersYaml().reload();
	}

	public void updateOffline(UUID uuid, float luck) {
		this.plugin.getPlayersYaml().getConfig().set("players." + uuid.toString() + ".luck", String.valueOf(luck));
		save();
	}

	public PlayerData getPlayer(String name) {
		
		if (this.plugin.getPlayersYaml().getConfig().getConfigurationSection("players") == null) {
			return new PlayerData();
		}
		for (final String playerUUIDs : this.plugin.getPlayersYaml().getConfig().getConfigurationSection("players")
				.getKeys(false)) {
			if ((this.plugin.getPlayersYaml().getConfig().get("players." + playerUUIDs + ".name") != null)
					&& ((String) this.plugin.getPlayersYaml().getConfig().get("players." + playerUUIDs + ".name"))
							.equalsIgnoreCase(name)) {
				return new PlayerData(UUID.fromString(playerUUIDs)).withLuck(Float.parseFloat(
						String.valueOf(this.plugin.getPlayersYaml().getConfig().get("players." + playerUUIDs + ".luck"))));
			}
		}
		return new PlayerData();
	}
	
}
