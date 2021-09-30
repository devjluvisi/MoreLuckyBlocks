package devjluvisi.mlb.util.players;

import java.util.Objects;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import devjluvisi.mlb.PluginConstants;
import devjluvisi.mlb.util.config.ConfigManager;

/**
 * Class which manages players including individual player luck, how many lucky
 * blocks broken, how many lucky blocks placed, and the average drop rarity that
 * the user gets from a drop on a lucky block (how lucky they are)
 *
 * Todo: optimize saving to config.
 *
 * @author jacob
 *
 */
public class PlayerData {

	private UUID UUID;
	private float luck;

	public PlayerData() {
		this.UUID = null;
		this.luck = PluginConstants.DEFAULT_PLAYER_LUCK;
	}

	public PlayerData(OfflinePlayer p) {
		if (p == null) {
			this.UUID = null;
			return;
		}
		this.UUID = p.getUniqueId();
		this.luck = PluginConstants.DEFAULT_PLAYER_LUCK;
	}

	public PlayerData(Player p) {
		this.UUID = p.getUniqueId();
		this.luck = PluginConstants.DEFAULT_PLAYER_LUCK;
	}

	public PlayerData(UUID uuid) {
		this.UUID = uuid;
		this.luck = PluginConstants.DEFAULT_BLOCK_LUCK;
	}

	public PlayerData withLuck(float luck) {
		this.luck = luck;
		return this;
	}

	public boolean isNull() {
		return Objects.isNull(this.UUID);
	}

	public final float getLuck() {
		return this.luck;
	}

	public final void setLuck(float luck) {
		this.luck = luck;
	}

	public final UUID getUUID() {
		return this.UUID;
	}

	public final void setUUID(UUID uUID) {
		this.UUID = uUID;
	}

	public boolean isOnline() {
		if (this.isNull()) {
			return true;
		}
		return Objects.isNull(Bukkit.getPlayer(this.UUID));
	}

	public Player getPlayer() {
		if (!this.isOnline()) {
			return null;
		}
		return Bukkit.getPlayer(this.UUID);
	}

	public OfflinePlayer getOfflinePlayer() {
		if (this.isOnline()) {
			return null;
		}
		return Bukkit.getOfflinePlayer(this.UUID);
	}

	public void save(ConfigManager playersYaml) {
		if (this.isNull()) {
			Bukkit.getLogger().severe("Could not set luck of this player as their UUID is not present.");
			return;
		}
		if (this.isOnline()) {
			playersYaml.getConfig().set("players." + this.UUID + ".name", this.getPlayer().getName());
			playersYaml.getConfig().set("players." + this.UUID + ".luck", String.valueOf(this.luck));
			playersYaml.save();
			playersYaml.reload();
			return;
		}
		playersYaml.getConfig().set("players." + this.UUID + ".name", Bukkit.getOfflinePlayer(this.UUID).getName());
		playersYaml.getConfig().set("players." + this.UUID + ".luck", String.valueOf(this.luck));
		playersYaml.save();
		playersYaml.reload();
	}

	@Override
	public int hashCode() {
		return this.UUID.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PlayerData)) {
			return false;
		}
		return ((PlayerData) obj).hashCode() == this.hashCode();
	}

}
