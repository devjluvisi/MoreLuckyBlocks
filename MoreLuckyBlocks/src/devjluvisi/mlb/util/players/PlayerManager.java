package devjluvisi.mlb.util.players;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.entity.Player;

import devjluvisi.mlb.MoreLuckyBlocks;

public class PlayerManager {

	private final MoreLuckyBlocks plugin;
	private final HashSet<PlayerData> playerLuckMap;

	public PlayerManager(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
		this.playerLuckMap = new HashSet<>();
		this.init();
	}

	private void init() {
		for (final Player p : this.plugin.getServer().getOnlinePlayers()) {
			if (!this.getPlayer(p.getName()).isNull()) {
				this.playerLuckMap.add(this.getPlayer(p.getName()));
			}
		}
	}

	public void add(Player p, float luck) {
		this.playerLuckMap.add(new PlayerData(p).withLuck(luck));
		this.save();
	}

	public void add(Player p) {
		this.playerLuckMap.add(new PlayerData(p));
		this.save();
	}

	public void remove(Player p) {
		// You dont need luck as it does not matter when comparing players.
		this.playerLuckMap.remove(new PlayerData(p));
		this.save();
	}

	public void update(UUID uuid, float newLuck) {
		this.playerLuckMap.remove(new PlayerData(uuid));
		this.add(this.plugin.getServer().getPlayer(uuid), newLuck);
		this.save();
	}

	public void save() {
		for (final PlayerData pData : this.playerLuckMap) {
			pData.save(this.plugin.getPlayersYaml());
		}
	}

	public void updateOffline(UUID uuid, float luck) {
		this.plugin.getPlayersYaml().getConfig().set("players." + uuid.toString() + ".luck", String.valueOf(luck));
		this.plugin.getPlayersYaml().save();
		this.plugin.getPlayersYaml().reload();
	}

	public PlayerData getPlayer(String name) {
		if (!(Objects.isNull(this.plugin.getServer().getPlayer(name)))) {
			for (final PlayerData p : this.playerLuckMap) {
				if (p.getUUID().equals(this.plugin.getServer().getPlayer(name).getUniqueId())) {
					return p;
				}
			}
		}

		if (this.plugin.getPlayersYaml().getConfig().getConfigurationSection("players") == null) {
			return new PlayerData();
		}
		for (final String playerUUIDs : this.plugin.getPlayersYaml().getConfig().getConfigurationSection("players")
				.getKeys(false)) {
			if ((this.plugin.getPlayersYaml().getConfig().get("players." + playerUUIDs + ".name") != null)
					&& ((String) this.plugin.getPlayersYaml().getConfig().get("players." + playerUUIDs + ".name"))
							.equalsIgnoreCase(name)) {
				return new PlayerData(UUID.fromString(playerUUIDs)).withLuck(Float.parseFloat(
						(String) this.plugin.getPlayersYaml().getConfig().get("players." + playerUUIDs + ".luck")));
			}
		}
		return new PlayerData();
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("[ ");
		for (final PlayerData p : this.playerLuckMap) {
			builder.append("{");
			builder.append(p.getUUID());
			builder.append(", ");
			builder.append(p.getLuck());
			builder.append("} ");
		}
		builder.append("]");
		return builder.toString();
	}

}
