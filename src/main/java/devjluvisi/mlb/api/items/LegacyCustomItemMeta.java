package devjluvisi.mlb.api.items;

import devjluvisi.mlb.api.items.itemdata.util.HiddenStringUtil;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class LegacyCustomItemMeta implements CustomItemMeta {

    private static final String SEQUENCE_HEADER = "CUSTOMMETAHEADER";
    private static final String SEQUENCE_FOOTER = "CUSTOMMETAFOOTER";
    private static final String SEQUENCE_SEPARATOR = "CUSTOMMETASEPARATOR";
    private static final String SEQUENCE_EQUALS_KEY = "CUSTOMMETAEQUALS";

    private final ItemMeta meta;
    private final Map<String, String> map = new HashMap<>();
    private String label;

    LegacyCustomItemMeta(ItemMeta meta) {
        this.meta = meta;
        this.updateMap();
    }

    @Override
    public void updateMeta(String label) {
        final List<String> lore = this.meta.getLore() == null ? new ArrayList<>() : this.meta.getLore();
        lore.removeIf(line -> HiddenStringUtil.hasHiddenString(line)
                && HiddenStringUtil.extractHiddenString(line).startsWith(SEQUENCE_HEADER));

        final StringBuilder dataBuilder = new StringBuilder();
        dataBuilder.append(SEQUENCE_HEADER);
        for (final Map.Entry<String, String> entry : this.map.entrySet()) {
            dataBuilder.append(entry.getKey()).append(SEQUENCE_EQUALS_KEY).append(entry.getValue())
                    .append(SEQUENCE_SEPARATOR);
        }

        final String data = dataBuilder.substring(0, dataBuilder.length() - SEQUENCE_SEPARATOR.length())
                + SEQUENCE_FOOTER;

        lore.add(lore.isEmpty() ? 0 : lore.size() - 1, HiddenStringUtil.encodeString(data) + label);
        this.meta.setLore(lore);
    }

    // TODO Refactor it!
    @Override
    public void updateMap() {
        this.map.clear();
        for (final String line : this.meta.getLore() == null ? new ArrayList<String>() : this.meta.getLore()) {
            if (!HiddenStringUtil.hasHiddenString(line)) {
                continue;
            }
            if (HiddenStringUtil.extractHiddenString(line).startsWith(SEQUENCE_HEADER)) {
                final String[] strings = HiddenStringUtil.extractHiddenString(line).substring(SEQUENCE_HEADER.length())
                        .split(SEQUENCE_FOOTER);
                final String extracted = strings[0];
                if (strings.length > 1) {
                    this.label = strings[1];
                }
                for (final String string : extracted.split(SEQUENCE_SEPARATOR)) {
                    final String[] data = string.split(SEQUENCE_EQUALS_KEY);
                    if (data.length < 2) {
                        continue;
                    }
                    this.map.put(data[0], data[1]);
                }
                break;
            }
        }
    }

    @Override
    public ItemMeta getItemMeta() {
        return this.meta;
    }

    @Override
    public boolean containsKey(String key) {
        return this.map.containsKey(key);
    }

    @Override
    public void remove(String key) {
        this.map.remove(key);
    }

    @Override
    public String getString(String key) {
        return this.map.get(key);
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
        this.map.put(key, value);
    }

    @Override
    public void setInt(String key, int value) {
        this.map.put(key, value + "");
    }

    @Override
    public void setDouble(String key, double value) {
        this.map.put(key, value + "");
    }

    @Override
    public void setFloat(String key, float value) {
        this.map.put(key, value + "");
    }

    @Override
    public void setLong(String key, long value) {
        this.map.put(key, value + "");
    }

    @Override
    public void setShort(String key, short value) {
        this.map.put(key, value + "");
    }

    @Override
    public void setByte(String key, byte value) {
        this.map.put(key, value + "");
    }

    @Override
    public void setBoolean(String key, boolean value) {
        this.map.put(key, value + "");
    }

    @Override
    public void setUniqueId(String key, UUID value) {
        this.map.put(key, value.toString());
    }

    @Override
    public String getOldLabel() {
        return this.label;
    }
}
