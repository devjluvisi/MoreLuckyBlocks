package devjluvisi.mlb.util.config.files;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.util.config.ConfigManager;

/**
 * Manages all possible data which is accessed through the "messages.yml" file.
 * Communicates directly with the configuration file from the plugin. The values
 * from the messages manager are set AT STARTUP and cannot be changed once the
 * config file has been read.
 *
 * @author jacob
 */
public final class MessagesManager extends ConfigManager {

    public MessagesManager(MoreLuckyBlocks plugin) {
        super(plugin, "messages.yml");
    }


}
