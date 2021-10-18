package devjluvisi.mlb.util.config.files;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum SettingType {

    // %enable% = Enabled/Disabled state of setting.
    // %value% = value of setting
    AUTO_SAVE_ENABLED(
            "Toggle Auto Save",
            "Toggle to automatically update blocks.yml file whenever you update a lucky block in game.\n\n" + SettingType.CURRENT_VALUE_PLACEHOLDER,
            "file-saving.auto-save-edits",
            ReturnType.BOOLEAN

    ),
    GET_BLOCK_DATA_INTERVAL(
            "Block Saving Interval",
            "Update the interval by which player lucky block data is saved persistently. (Ex. tracking breaking/placing lucky blocks).\n\n" + ChatColor.GRAY + "Value: " + ChatColor.GREEN + SettingType.CURRENT_VALUE_PLACEHOLDER,
            "file-saving.data-save-interval",
            Material.CLOCK,
            ReturnType.INT
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
