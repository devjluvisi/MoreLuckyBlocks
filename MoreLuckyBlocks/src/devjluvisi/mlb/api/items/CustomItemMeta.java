package devjluvisi.mlb.api.items;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.ItemMeta;

public interface CustomItemMeta {

	String DEFAULT_LABEL = ChatColor.BLUE + "+Custom Data";

	void updateMeta(String label);

	void updateMap();

	ItemMeta getItemMeta();

	boolean containsKey(String key);

	void remove(String key);

	String getString(String key);

	int getInt(String key);

	double getDouble(String key);

	float getFloat(String key);

	long getLong(String key);

	short getShort(String key);

	byte getByte(String key);

	boolean getBoolean(String key);

	UUID getUniqueId(String key);

	void setString(String key, String value);

	void setInt(String key, int value);

	void setDouble(String key, double value);

	void setFloat(String key, float value);

	void setLong(String key, long value);

	void setShort(String key, short value);

	void setByte(String key, byte value);

	void setBoolean(String key, boolean value);

	void setUniqueId(String key, UUID value);

	String getOldLabel();
}
