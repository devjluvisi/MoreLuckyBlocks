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
    Adjust auto save interval.
    Adjust auto save
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

    public float getMajorConfigVersion() {
        return getFloat("major-config-version");
    }

    public Object getGenericSetting(SettingType type) {
        return getConfig().get(type.getNode());
    }

    public int getWarningThreshold() {
        return getConfig().getInt(SettingType.LUCKY_BLOCK_WARNING_THRESHOLD.getNode());
    }

    public boolean isAutoSaveEnabled() {
        return getConfig().getBoolean(SettingType.AUTO_SAVE_ENABLED.getNode());
    }

    public boolean isFirstBoot() {
        return getConfig().getBoolean("first-boot");
    }

    public int getBlockDataSaveInterval() {
        return getConfig().getInt("file-saving.data-save-interval");
    }


}
