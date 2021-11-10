package devjluvisi.mlb.util.config.files;

import org.bukkit.Material;

public enum SettingType {

    // %enable% = Enabled/Disabled state of setting.
    // %value% = value of setting
    AUTO_SAVE_ENABLED(
            "Toggle Auto Save",
            "Toggle to automatically update blocks.yml file whenever you update a lucky block in game.",
            "file-saving.auto-save-edits",
            ReturnType.BOOLEAN

    ),
    JOIN_MESSAGE(
            "Toggle Join Message",
            "Enable or disable the join message when an admin logs into the server. Note that disabling will also disable out-of-date notifications.",
            "join-message",
            ReturnType.BOOLEAN
    ),
    SHOW_SAVING_MESSAGE(
            "Toggle Save Alert",
            "Toggle whether or not to alert staff when the world is saving block data from server to config.",
            "show-saving-messages",
            ReturnType.BOOLEAN
    ),
    EXTRA_PLAYER_DATA(
            "Toggle Extra Player Data",
            "Toggle whether or not additional data about players should be saved into config.",
            "use-extra-player-data",
            ReturnType.BOOLEAN
    ),
    GET_BLOCK_DATA_INTERVAL(
            "Block Saving Interval",
            "Update the interval by which player lucky block data is saved persistently. (Ex. tracking breaking/placing lucky blocks)",
            "file-saving.data-save-interval",
            Material.CLOCK,
            ReturnType.INT
    ),
    LUCKY_BLOCK_WARNING_THRESHOLD(
            "Warning Threshold",
            "Update the maximum amount of unopened lucky blocks on a server before a warning is given.",
            "lucky-block-warning-threshold",
            Material.REDSTONE_TORCH,
            ReturnType.INT
    ),
    LOG_EVENTS(
            "Toggle Event Logging",
            "Log when players place and break lucky blocks to a configuration file which can be viewed in game as well.",
            "log-events",
            ReturnType.BOOLEAN
    );

    public static final String CURRENT_VALUE_PLACEHOLDER = "%value%";
    private final String node;
    private final String name;
    private final String description;
    private final Material material;
    private final ReturnType returnType;

    SettingType(String name, String description, String node, ReturnType returnType) {
        this.node = node;
        this.name = name;
        this.description = description;
        this.returnType = returnType;
        this.material = Material.BARRIER;
    }

    SettingType(String name, String description, String node, Material m, ReturnType returnType) {
        this.node = node;
        this.name = name;
        this.description = description;
        this.returnType = returnType;
        this.material = m;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getNode() {
        return this.node;
    }

    public ReturnType getReturnType() {
        return this.returnType;
    }

    public Material getMaterial() {
        return this.material;
    }

    public enum ReturnType {
        BOOLEAN, INT, DECIMAL, STRING, LIST
    }
}
