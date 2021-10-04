package devjluvisi.mlb.util.luckyblocks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.Validate;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.math.NumberUtils;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.util.config.ConfigManager;

/**
 * Class which manages the assertion that blocks being broken are lucky blocks.
 * Also manages on a global scale where every lucky block on the map is and
 * handles removing blocks from config when blocks are broken, etc. Todo:
 * Optimize saving to config.
 *
 * @author jacob
 */
public final class LuckyAudit {

	/**
	 * A complete key-value set of the location of every lucky block on the entire
	 * server.
	 */
	private final HashMap<MapLocation3D, LuckyValues> luckyBlockMap;
	private final ConfigManager worldDataYaml;
	private final MoreLuckyBlocks plugin;

	public LuckyAudit(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
		this.worldDataYaml = plugin.getWorldDataYaml();
		this.luckyBlockMap = new HashMap<>();
		plugin.getServer().getConsoleSender()
				.sendMessage("MoreLuckyBlocks -> Attempting to fetch all saved lucky blocks...");
		this.pullFromConfig();
	}

	public boolean isLuckyBlock(Location checkingLocation) {
		Validate.notNull(checkingLocation);
		return this.luckyBlockMap.containsKey(new MapLocation3D(checkingLocation)) && (checkingLocation.getBlock()
				.getType() == this.luckyBlockMap.get(new MapLocation3D(checkingLocation)).getBlockMaterial());
	}

	public LuckyBlock find(MapLocation3D loc) {
		final LuckyValues lv = this.luckyBlockMap.get(loc);

		final int hash = lv.getLuckyBlockHash();
		final float blockLuck = lv.getBlockLuck();

		for (final LuckyBlock lb : this.plugin.getLuckyBlocks()) {
			if (lb.hashCode() == hash) {
				lb.setBlockLuck(blockLuck);
				return lb;
			}
		}
		return null;
	}

	public LuckyBlock find(Location loc) {
		Validate.notNull(loc);
		return this.find(new MapLocation3D(loc));
	}

	public float getBlockLuck(Location loc) {
		Validate.notNull(loc);
		return this.luckyBlockMap.get(new MapLocation3D(loc)).getBlockLuck();
	}

	public HashMap<MapLocation3D, LuckyValues> getMap() {
		return this.luckyBlockMap;
	}

	public void put(Location l, LuckyBlock lb) {
		Validate.notNull(l);
		Validate.notNull(lb);
		this.luckyBlockMap.put(new MapLocation3D(l),
				new LuckyValues(lb.getBlockMaterial(), lb.hashCode(), lb.getBlockLuck()));
		// this.writeAll();
	}

	public void remove(Location l) {
		Validate.notNull(l);
		this.luckyBlockMap.remove(new MapLocation3D(l));
		// this.writeAll();
	}

//	public void removeAll(LuckyBlock block) {
//
//	}
//
//	public void writeNew(Location loc, LuckyBlock block) {
//
//	}

	public String serializeEntry(MapLocation3D key, LuckyValues value) {
		final StringBuilder str = new StringBuilder();
		str.append("[{");
		str.append(key.getX());
		str.append(",");
		str.append(key.getY());
		str.append(",");
		str.append(key.getZ());
		str.append(",");
		str.append(key.getWorld());
		str.append("},{");
		str.append(value.getBlockLuck());
		str.append(",");
		str.append(value.getLuckyBlockHash());
		str.append("}]");
		return str.toString();
	}

	private LuckyBlock search(int hash) {
		for (final LuckyBlock lb : this.plugin.getLuckyBlocks()) {
			if (lb.hashCode() == hash) {
				return lb;
			}
		}
		return null;
	}

	public Map.Entry<MapLocation3D, LuckyValues> deserializeEntry(String str) {
		try {
			final MapLocation3D entry = new MapLocation3D();
			final LuckyValues value = new LuckyValues();

			str = str.substring(1, str.length() - 1);
			final String[] pair = str.split(Pattern.quote("},{"));
			pair[0] = pair[0].replace("{", "");
			pair[1] = pair[1].replace("}", "");
			final String[] keyVals = pair[0].split(",");

			entry.setX(NumberUtils.toDouble(keyVals[0]));
			entry.setY(NumberUtils.toDouble(keyVals[1]));
			entry.setZ(NumberUtils.toDouble(keyVals[2]));
			entry.setWorld(UUID.fromString(keyVals[3]));

			final String[] entryVals = pair[1].split(",");

			value.setBlockLuck(NumberUtils.toFloat(entryVals[0]));
			value.setLuckyBlockHash(NumberUtils.toInt(entryVals[1]));

			value.setBlockMaterial(this.search(value.getLuckyBlockHash()).getBlockMaterial());
			return Map.entry(entry, value);
		} catch (final Exception e) {
			return null;
		}

	}

	public void writeAll() {
		this.worldDataYaml.getConfig().set("locations", null);
		long index = 0;
		for (final Map.Entry<MapLocation3D, LuckyValues> entry : this.luckyBlockMap.entrySet()) {
			this.worldDataYaml.getConfig().set("locations." + index,
					this.serializeEntry(entry.getKey(), entry.getValue()));
			index++;
		}
		this.worldDataYaml.save();
		this.worldDataYaml.reload();
	}

	private void pullFromConfig() {
		long index = 0;
		while (this.worldDataYaml.getConfig().get("locations." + index) != null) {
			final String val = (String) this.worldDataYaml.getConfig().get("locations." + index);
			if (this.deserializeEntry(val) == null) {
				index++;
				continue;
			}
			this.luckyBlockMap.put(this.deserializeEntry(val).getKey(), this.deserializeEntry(val).getValue());
			index++;
		}
	}

	public void dumpLogger() {
		this.plugin.getServer().getConsoleSender().sendMessage("LuckyAudit Mapping Dump:");
		this.plugin.getServer().getConsoleSender().sendMessage(" Size ---> " + this.luckyBlockMap.size());
		this.plugin.getServer().getConsoleSender().sendMessage("********");
		int index = 0;
		for (final Map.Entry<MapLocation3D, LuckyValues> entry : this.luckyBlockMap.entrySet()) {
			this.plugin.getServer().getConsoleSender()
					.sendMessage(index + " | " + entry.getKey().toString() + " <-> " + entry.getValue().toString());
			index++;
		}
		this.plugin.getServer().getConsoleSender().sendMessage("<----- END ----->");
	}

}
