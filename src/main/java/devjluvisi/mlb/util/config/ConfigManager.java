package devjluvisi.mlb.util.config;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * <h2>Config Manager</h2>
 * <p>
 * A class which manages all of the different configuration files in
 * MoreLuckyBlocks.
 * </p>
 */
public class ConfigManager {

    private final MoreLuckyBlocks plugin;
    private final File configFile;
    private final String name;
    private YamlConfiguration yamlConfig;

    /**
     * Define a new config manager instance with the "Plugin" and the config file.
     */
    public ConfigManager(MoreLuckyBlocks plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        this.configFile = new File(plugin.getDataFolder(), name);

        if (!this.configFile.exists()) {
            plugin.getLogger().warning("Configuration File could not be located... Generating new.");
            // Save a version of the file with comments.
            plugin.saveResource(name, false);
        }
        // Load the new configuration.
        this.yamlConfig = YamlConfiguration.loadConfiguration(this.configFile);

    }

    /**
     * Saves the YAML configuration file and reloads its contents. Note that this
     * method DOES NOT save comments below the header comment in the file. As a
     * result, this method should not be called on config files it should only be
     * called on data files that do not need comments.
     */
    public void save() {
        try {
            this.yamlConfig.save(this.configFile);
            this.reload(); // Reload the config on the server now that the file has been saved.
        } catch (final IOException e) {
            this.plugin.getLogger()
                    .severe("Could not save config file: " + this.configFile.getName() + ". No data was written.");
        }
    }

    /**
     * Reloads the YAML configuration file. Keeps Comments.
     */
    public void reload() {
        this.yamlConfig = YamlConfiguration.loadConfiguration(new File(this.plugin.getDataFolder(), this.name));
    }

    /**
     * @return The configuration object for Spigot.
     */
    public YamlConfiguration getConfig() {
        return this.yamlConfig;
    }

    /**
     * Get a string from a node in the configuration file. Parses '&' color codes
     * automatically.
     *
     * @param node The configuration node to search.
     * @return The string value at the config node.
     */
    public String getString(String node) {
        try {
            return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.yamlConfig.getString(node)));
        } catch (final NullPointerException e) {
            this.plugin.getLogger().severe("CONFIG EXCEPTION: Could not read String Value at node: " + node);
            return ChatColor.RED + "Error with message. Contact a server administrator for more help.";
        }
    }

    /**
     * Get a string from a node in the configuration file. Allows the declaration of
     * placeholders and a string to replace them with. Placeholders are replaced
     * using the fast {@link StringUtils} class. Parses '&' color codes
     * automatically.
     *
     * @param node         The configuration node to search
     * @param placeHolders A map of all placeholders.
     * @return The new string to send to the player.
     */
    public String getString(String node, Map<String, String> placeHolders) {
        return StringUtils.replaceEachRepeatedly(this.getString(node), placeHolders.keySet().toArray(new String[0]),
                placeHolders.values().toArray(new String[0]));
    }

    /**
     * Get a float value from a node in the configuration file.
     *
     * @param node The node in the config to check for.
     * @return The floating point value at the node.
     */
    public float getFloat(String node) {
        try {
            return (float) this.yamlConfig.getDouble(node);
        } catch (final NullPointerException e) {
            this.plugin.getLogger().severe("CONFIG EXCEPTION: Could not read Float Value at node: " + node);
            return -1;
        } catch (final ClassCastException e) {
            this.plugin.getLogger().severe("CONFIG EXCEPTION: Float value at node \"" + node
                    + "\" is not a valid floating point value. (Bad Type Cast)");
            return -1;
        }
    }

    public LuckyBlock getLuckyBlock(String name) {
        return null;

    }

    /**
     * Set a specific value in the configuration file given a node and a value. This
     * method "saves" the configuration file but does NOT reload it.
     *
     * @param node  The path to set "value".
     * @param value The value to set.
     */
    public void setValue(String node, Object value) {
        try {
            this.yamlConfig.set(node, value);
            this.yamlConfig.save(this.configFile);
        } catch (final IOException e) {
            this.plugin.getLogger().severe("ERROR: Could not save value to configuration file.");
            this.plugin.getLogger().severe("Could not write data to " + node);
            this.plugin.getLogger().severe(e.getMessage());
        }
    }

}