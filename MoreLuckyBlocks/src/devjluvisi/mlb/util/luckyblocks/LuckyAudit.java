package devjluvisi.mlb.util.luckyblocks;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.Validate;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.util.config.ConfigManager;

/**
 * Class which manages the assertion that blocks being broken are lucky blocks.
 * Also manages on a global scale where every lucky block on the map is and
 * handles removing blocks from config when blocks are broken, etc.
 *
 * @author jacob
 *
 *         TODO: Make binary search tree?
 *
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

	public LuckyBlock find(Location loc) {
		Validate.notNull(loc);
		final LuckyValues lv = this.luckyBlockMap.get(new MapLocation3D(loc));

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
	}

	public void remove(Location l) {
		Validate.notNull(l);
		this.luckyBlockMap.remove(new MapLocation3D(l));
	}

	public void removeAll(LuckyBlock block) {

	}

	public void writeNew(Location loc, LuckyBlock block) {

	}

	public void writeAll() {

	}

	private void pullFromConfig() {

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
