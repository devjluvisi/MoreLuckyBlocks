package devjluvisi.mlb.util.players;

import java.util.UUID;

import devjluvisi.mlb.MoreLuckyBlocks;

public class PlayerManager {

	private final MoreLuckyBlocks plugin;

	public PlayerManager(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
	}

	public void update(UUID uuid, float newLuck) {
		this.plugin.getPlayersYaml().getConfig().set("players." + uuid.toString() + ".name",
				String.valueOf(this.plugin.getServer().getPlayer(uuid).getName()));
		this.updateOffline(uuid, newLuck);
		this.save();
	}

	public void save() {
		this.plugin.getPlayersYaml().save();
		this.plugin.getPlayersYaml().reload();
	}

	public void updateOffline(UUID uuid, float luck) {
		this.plugin.getPlayersYaml().getConfig().set("players." + uuid.toString() + ".luck", String.valueOf(luck));
		this.save();
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
				return new PlayerData(UUID.fromString(playerUUIDs)).withLuck(Float.parseFloat(String
						.valueOf(this.plugin.getPlayersYaml().getConfig().get("players." + playerUUIDs + ".luck"))));
			}
		}
		return new PlayerData();
	}

}
