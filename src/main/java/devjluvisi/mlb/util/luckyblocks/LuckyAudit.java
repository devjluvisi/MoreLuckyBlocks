package devjluvisi.mlb.util.luckyblocks;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.util.config.ConfigManager;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * <h2>LuckyAudit</h2>
 * Class which manages the assertion that blocks being broken are lucky blocks.
 * Also manages on a global scale where every lucky block on the map is and
 * handles removing blocks from config when blocks are broken, etc.
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

    /**
     * @param checkingLocation Location on the server.
     * @return If there is a lucky block at the specified location.
     */
    public boolean isLuckyBlock(Location checkingLocation) {
        Validate.notNull(checkingLocation);
        return this.luckyBlockMap.containsKey(new MapLocation3D(checkingLocation)) && (checkingLocation.getBlock()
                .getType() == this.luckyBlockMap.get(new MapLocation3D(checkingLocation)).getBlockMaterial());
    }

    /**
     * Finds a lucky block at a given location.
     *
     * @param loc A specified location.
     * @return The lucky block as an object.
     */
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

    /**
     * Finds a lucky block at a given location.
     *
     * @param loc A specified location.
     * @return The lucky block as an object.
     */
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

    /**
     * Search all cached lucky blocks and find one by its hash code.
     *
     * @param hash The hash code of the block.
     * @return LuckyBlock found as a {@link LuckyBlock} object.
     */
    private LuckyBlock search(int hash) {
        for (final LuckyBlock lb : this.plugin.getLuckyBlocks()) {
            if (lb.hashCode() == hash) {
                return lb;
            }
        }
        return null;
    }

    /**
     * Converts a serialized string into a map entry for LuckyAudit.
     *
     * @param str The string which is serialized.
     * @return A HashMap entry as a {@link MapLocation3D} and a {@link LuckyValues}.
     */
    public Map.Entry<MapLocation3D, LuckyValues> deserializeEntry(String str) {
        try {
            final MapLocation3D entry = new MapLocation3D();
            final LuckyValues value = new LuckyValues();

            str = str.substring(1, str.length() - 1);
            // Replace brackets.
            final String[] pair = str.split(Pattern.quote("},{"));
            pair[0] = pair[0].replace("{", "");
            pair[1] = pair[1].replace("}", "");
            // Split on commas.
            final String[] keyVals = pair[0].split(",");

            // Parse data
            entry.setX(NumberUtils.toDouble(keyVals[0]));
            entry.setY(NumberUtils.toDouble(keyVals[1]));
            entry.setZ(NumberUtils.toDouble(keyVals[2]));
            entry.setWorld(UUID.fromString(keyVals[3]));

            final String[] entryVals = pair[1].split(",");

            value.setBlockLuck(NumberUtils.toFloat(entryVals[0]));
            value.setLuckyBlockHash(NumberUtils.toInt(entryVals[1]));

            value.setBlockMaterial(Objects.requireNonNull(this.search(value.getLuckyBlockHash())).getBlockMaterial());
            return Map.entry(entry, value);
        } catch (final Exception e) {
            return null;
        }

    }

    /**
     * Writes all data in the cached {@link LuckyAudit} to config.
     */
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

    /**
     * Pulls all of the block data from "block-data.yml" and converts it
     * into objects.
     */
    private void pullFromConfig() {
        long index = 0;
        while (this.worldDataYaml.getConfig().get("locations." + index) != null) {
            final String val = (String) this.worldDataYaml.getConfig().get("locations." + index);
            if (this.deserializeEntry(val) == null) {
                index++;
                continue;
            }
            this.luckyBlockMap.put(Objects.requireNonNull(this.deserializeEntry(val)).getKey(), Objects.requireNonNull(this.deserializeEntry(val)).getValue());
            index++;
        }
    }

    /**
     * Print information regarding lucky audit to the console sender.
     */
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
