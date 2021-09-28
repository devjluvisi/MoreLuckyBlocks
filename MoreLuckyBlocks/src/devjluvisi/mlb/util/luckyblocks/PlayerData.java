package devjluvisi.mlb.util.luckyblocks;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Class which manages players including individual player luck, how many lucky
 * blocks broken, how many lucky blocks placed, and the average drop rarity that
 * the user gets from a drop on a lucky block (how lucky they are)
 *
 * @author jacob
 *
 */
public class PlayerData {

	private UUID UUID;
	private float luck;

	public PlayerData(UUID uuid) {
		this.UUID = uuid;
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
		return Bukkit.getPlayer(this.UUID) == null;
	}

	public Player getPlayer() {
		if (!this.isOnline()) {
			return null;
		}
		return Bukkit.getPlayer(this.UUID);
	}

}
