package devjluvisi.mlb.api.items;

import java.util.UUID;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Preconditions;

public class NewCustomItemMeta implements CustomItemMeta {

	private final JavaPlugin javaPlugin;
	private final ItemMeta meta;

	NewCustomItemMeta(JavaPlugin javaPlugin, ItemMeta meta) {
		Preconditions.checkNotNull(javaPlugin);
		Preconditions.checkNotNull(meta);
		this.javaPlugin = javaPlugin;
		this.meta = meta;
	}

	@Override
	public void updateMeta(String label) {
		// Only <1.14 Versions
	}

	@Override
	public void updateMap() {
		// Only <1.14 Versions
	}

	@Override
	public ItemMeta getItemMeta() {
		return this.meta;
	}

	@Override
	public boolean containsKey(String key) {
		return this.meta.getPersistentDataContainer().has(new NamespacedKey(this.javaPlugin, key),
				PersistentDataType.STRING);
	}

	@Override
	public void remove(String key) {
		this.meta.getPersistentDataContainer().remove(new NamespacedKey(this.javaPlugin, key));
	}

	@Override
	public String getString(String key) {
		return this.meta.getPersistentDataContainer().get(new NamespacedKey(this.javaPlugin, key),
				PersistentDataType.STRING);
	}

	@Override
	public int getInt(String key) {
		return Integer.parseInt(this.getString(key));
	}

	@Override
	public double getDouble(String key) {
		return Double.parseDouble(this.getString(key));
	}

	@Override
	public float getFloat(String key) {
		return Float.parseFloat(this.getString(key));
	}

	@Override
	public long getLong(String key) {
		return Long.parseLong(this.getString(key));
	}

	@Override
	public short getShort(String key) {
		return Short.parseShort(this.getString(key));
	}

	@Override
	public byte getByte(String key) {
		return Byte.parseByte(this.getString(key));
	}

	@Override
	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(this.getString(key));
	}

	@Override
	public UUID getUniqueId(String key) {
		return UUID.fromString(this.getString(key));
	}

	@Override
	public void setString(String key, String value) {
		Preconditions.checkNotNull(value);
		this.meta.getPersistentDataContainer().set(new NamespacedKey(this.javaPlugin, key), PersistentDataType.STRING,
				value);
	}

	@Override
	public void setInt(String key, int value) {
		this.setString(key, value + "");
	}

	@Override
	public void setDouble(String key, double value) {
		this.setString(key, value + "");
	}

	@Override
	public void setFloat(String key, float value) {
		this.setString(key, value + "");
	}

	@Override
	public void setLong(String key, long value) {
		this.setString(key, value + "");
	}

	@Override
	public void setShort(String key, short value) {
		this.setString(key, value + "");
	}

	@Override
	public void setByte(String key, byte value) {
		this.setString(key, value + "");
	}

	@Override
	public void setBoolean(String key, boolean value) {
		this.setString(key, value + "");
	}

	@Override
	public void setUniqueId(String key, UUID value) {
		this.setString(key, value.toString());
	}

	@Override
	public String getOldLabel() {
		return "UNKNOWN"; // <1.14 Only
	}
}
