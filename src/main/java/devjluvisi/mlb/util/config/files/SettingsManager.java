package devjluvisi.mlb.util.config.files;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.util.config.ConfigManager;

/**
 * Manages the "config.yml" file and pulls strings directly from it to indicate
 * values.
 *
 * @author jacob
 */
public class SettingsManager extends ConfigManager {

    /*
    Pseudo Configuration options:
    Edit common messages.
    Adjust common settings.
    Adjust permission nodes.
    Particle effects for lucky blocks.
    Get config version.
    Toggle join message.
    Adjust value of unopened lucky blocks warning.

    Disable lucky blocks.
     */

    public SettingsManager(MoreLuckyBlocks plugin) {
        super(plugin, "config.yml");
    }


}
