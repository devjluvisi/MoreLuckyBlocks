package devjluvisi.mlb.menus.util;

import devjluvisi.mlb.helper.Util;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class MenuItem {

    private final List<ItemFlag> flags;
    private Material m;
    private String displayName;
    private List<String> lore;

    public MenuItem() {
        m = Material.AIR;
        displayName = StringUtils.EMPTY;
        lore = new LinkedList<>();
        flags = new ArrayList<>();
    }

    public MenuItem(SpecialItem specialItem) {
        lore = new LinkedList<>();
        flags = new ArrayList<>();
        of(specialItem);
    }

    public final MenuItem of(SpecialItem specialItem) {
        switch (specialItem) {
            case EXIT_BUTTON -> {
                m = Material.BARRIER;
                displayName = "&c&lExit";
                lore.add("&7Click to exit the menu.");
                lore.add("&7Will return to previous menu if");
                lore.add("&7possible.");
            }
            case ADD_NEW_DROP -> {
                m = Material.GLOWSTONE_DUST;
                displayName = "&aAdd New Drop";
                lore.add("&7Add a new drop to this lucky block.");
            }
            case REMOVE_ALL_DROPS -> {
                m = Material.LAVA_BUCKET;
                displayName = "&4Remove all Drops";
                lore.add("&7Removes all drops from LuckyBlock.");
            }
            case EDIT_DROP -> {
                m = Material.BOOK;
                displayName = "&aEdit Drop";
                lore.add("&7Edit the contents of");
                lore.add("&7the drop.");
            }
            case COPY_DROP -> {
                m = Material.DIAMOND;
                displayName = "&9Duplicate Drop";
                lore.add("&7Copy the contents of");
                lore.add("&7this drop and add it as");
                lore.add("&7a new drop.");
            }
            case DELETE_DROP -> {
                m = Material.TNT;
                displayName = "&4Delete Drop";
                lore.add("&7Delete this drop and");
                lore.add("&7all of its contents.");
            }
            case ADD_POTION_EFFECT -> {
                m = Material.POTION;
                displayName = "&eAdd Potion Effect";
                lore.add("&7Add a potion effect to");
                lore.add("&7be applied to this player!");
                flags.add(ItemFlag.HIDE_POTION_EFFECTS);
            }
            case ADD_COMMAND -> {
                m = Material.OAK_SIGN;
                displayName = "&eAdd Command";
                lore.add("&7Add a command to be");
                lore.add("&7executed by console.");
            }
            case SAVE_BUTTON -> {
                m = Material.EMERALD;
                displayName = "&2Save";
                lore.add("&7Save this edit to config.");
                lore.add("&7Use &3/mlb settings&7 to enable");
                lore.add("&7auto-saving for edits.");
            }
            case CHANGE_RARITY -> {
                m = Material.EXPERIENCE_BOTTLE;
                flags.add(ItemFlag.HIDE_ATTRIBUTES);
                displayName = "&3Change Rarity";
                lore.add("&7Adjust the rarity of this drop.");
            }
            case INCREASE_RARITY -> {
                m = Material.SLIME_BALL;
                displayName = "&2Increase Rarity";
                lore.add("&7Increase the rarity of");
                lore.add("&7this drop by &3&l5&7 points.");
                lore.add("&8(Less Common)");
            }
            case DECREASE_RARITY -> {
                m = Material.MAGMA_CREAM;
                displayName = "&2Decrease Rarity";
                lore.add("&7Decrease the rarity of");
                lore.add("&7this drop by &3&l5&7 points.");
                lore.add("&8(More Common)");
            }
            case DELETE_LUCKY_BLOCK -> {
                m = Material.TNT;
                displayName = "&4&lDelete Lucky Block";
                lore.add("&7Delete this lucky block");
                lore.add("&7and all of its contents.");
            }
            case ADD_STRUCTURE -> {
                m = Material.STONE_BRICKS;
                displayName = "&eAdd/Edit Structure";
                lore.add("&7Add or edit the structure");
                lore.add("&7associated with this drop.");
                lore.add("&bStructures are physical modifications");
                lore.add("&bto the world that occur when a user breaks");
                lore.add("&ba lucky block and gets this drop.");
                lore.add("&e&l/mlb struct &7 for more info.");
            }
            case DELETE_EXCHANGE -> {
                m = Material.TNT;
                displayName = "&cDelete";
                lore.add("&7Delete the exchange for");
                lore.add("&7the current lucky block.");
            }
            case SAVE_EXCHANGE -> {
                m = Material.EMERALD;
                displayName = "&2Save";
                lore.add("&7Save your changes");
                lore.add("&7to this exchange.");
            }
            case EDIT_NAME -> {
                m = Material.NAME_TAG;
                displayName = "&eEdit Name";
                lore.add("&7Edit the name of the lucky block.");
                lore.add("&7This edits both the name of the");
                lore.add("&7item as well as the internal name.");
            }
            case EDIT_LORE -> {
                m = Material.MAP;
                displayName = "&eEdit Item Lore";
                lore.add("&7Edit the lore of the lucky block.");
            }
            case EDIT_ENCHANTED -> {
                m = Material.ENCHANTED_BOOK;
                displayName = "&eToggle Enchanted";
                lore.add("&7Edit if the lucky block should");
                lore.add("&7be enchanted.");
            }
            case EDIT_BREAK_COOLDOWN -> {
                m = Material.REDSTONE;
                displayName = "&eEdit Break Cooldown";
                lore.add("&7Edit the cooldown for");
                lore.add("&7breaking this lucky block.");
            }
            case EDIT_PLACE_COOLDOWN -> {
                m = Material.GLOWSTONE_DUST;
                displayName = "&eEdit Place Cooldown";
                lore.add("&7Edit the cooldown for");
                lore.add("&7placing this lucky block.");
            }
            case EDIT_BREAK_PERMISSION -> {
                m = Material.BEDROCK;
                displayName = "&eEdit Break Permission";
                lore.add("&7Edit the permission required");
                lore.add("&7to break and place this lucky block.");
            }
            case EDIT_MATERIAL -> {
                m = Material.GLASS;
                displayName = "&eEdit Item Material";
                lore.add("&7Edit the material of the");
                lore.add("&7lucky block.");
                lore.add("&3&oWill disable all placed lucky blocks");
                lore.add("&3&oof the previous type.");
            }
            case EDIT_PARTICLES -> {
                m = Material.NETHER_WART;
                displayName = "&eEdit Particles";
                lore.add("&7Edit the particles that");
                lore.add("&7surround the lucky block");
                lore.add("&7when a player gets close.");
            }
            case EDIT_SOUND -> {
                m = Material.NOTE_BLOCK;
                displayName = "&eEdit Break Sound";
                lore.add("&7Edit the sound the lucky block");
                lore.add("&7plays when it is broken.");
            }
            case EDIT_TOOL -> {
                m = Material.DIAMOND_PICKAXE;
                displayName = "&eEdit Required Tool";
                lore.add("&7Edit the tool required to");
                lore.add("&7break the lucky block.");
                lore.add("&3&oRight-Click to view current tool.");
                lore.add("&3&oLeft-Click to edit required tool.");
            }
            case EDIT_ATTRIBUTES -> {
                m = Material.ANVIL;
                displayName = "&9Edit Attributes";
                lore.add("&7Edit the attributes of this");
                lore.add("&7lucky block.");
                lore.add("&f&oEx: Particles, Sounds, Name, Lore, ...");
            }
            case AUTO_SAVING -> {
                m = Material.NETHER_STAR;
                displayName = "&2Auto Save";
                lore.add("&7Your edits are automatically");
                lore.add("&7saved to config.");
                lore.add("&7Use &3/mlb settings&7 to enable or disable");
                lore.add("&7this feature.");
            }
            case EDIT_STRUCTURE -> {
                m = Material.FEATHER;
                displayName = "&eEdit Structure";
                lore.add("&7Edit the structure");
                lore.add("&7for this drop.");
                lore.add("&8Will teleport you to structure world.");
            }
            case DELETE_STRUCTURE -> {
                m = Material.LAVA_BUCKET;
                displayName = "&eDelete Structure";
                lore.add("&7Delete the structure for");
                lore.add("&7for this drop.");
            }
            case NEW_STRUCTURE -> {
                m = Material.FEATHER;
                displayName = "&eAdd Structure";
                lore.add("&7Add a new structure");
                lore.add("&7for this drop.");
                lore.add("&8Will teleport you to structure world.");
            }
        }
        return this;
    }

    public MenuItem(Material m) {
        this.m = m;
        this.displayName = StringUtils.EMPTY;
        this.lore = Collections.emptyList();
        flags = Collections.emptyList();
    }

    public MenuItem(Material m, String displayName) {
        this.m = m;
        this.displayName = displayName;
        this.lore = Collections.emptyList();
        flags = Collections.emptyList();
    }

    public MenuItem(Material m, String displayName, List<String> lore) {
        this.m = m;
        this.displayName = displayName;
        this.lore = lore;
        flags = Collections.emptyList();
    }

    public static MenuItem blackPlaceholder() {
        return new MenuItem(Material.BLACK_STAINED_GLASS_PANE);
    }

    public static MenuItem whitePlaceholder() {
        return new MenuItem(Material.WHITE_STAINED_GLASS_PANE);
    }

    public static MenuItem redPlaceholder() {
        return new MenuItem(Material.RED_STAINED_GLASS_PANE);
    }

    public MenuItem with(Material m) {
        this.m = m;
        return this;
    }

    public MenuItem with(String name) {
        this.displayName = name;
        return this;
    }

    public MenuItem with(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public MenuItem addLine(String line) {
        this.lore.add(line);
        return this;
    }

    public MenuItem addAllLine(String[] lines) {
        this.lore.addAll(Arrays.stream(lines).toList());
        return this;
    }

    public boolean equals(ItemStack item) {
        final ItemStack i = asItem();
        return item.getItemMeta() != null && item.getType() == i.getType() && item.getItemMeta().getDisplayName().equals(Objects.requireNonNull(i.getItemMeta()).getDisplayName()) && Objects.equals(item.getItemMeta().getLore(), i.getItemMeta().getLore());
    }

    public ItemStack asItem() {
        ItemStack item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Util.toColor(displayName));
        meta.setLore(Util.listToColor(lore));
        for (ItemFlag flag : flags) {
            meta.addItemFlags(flag);
        }
        item.setItemMeta(meta);
        return item;
    }


    /**
     * Special items are menu items that are used frequently or perform certain major actions.
     */
    public enum SpecialItem {
        EXIT_BUTTON, ADD_NEW_DROP, REMOVE_ALL_DROPS, EDIT_DROP, DELETE_DROP, COPY_DROP, CHANGE_RARITY,
        EDIT_ATTRIBUTES,
        ADD_POTION_EFFECT, ADD_COMMAND, SAVE_BUTTON, CANCEL_BUTTON, CONFIRM_BUTTON, INCREASE_RARITY, DECREASE_RARITY,
        DELETE_LUCKY_BLOCK, ADD_STRUCTURE, DELETE_EXCHANGE, SAVE_EXCHANGE, EDIT_NAME, EDIT_LORE, EDIT_MATERIAL, EDIT_BREAK_PERMISSION,
        EDIT_TOOL, EDIT_PARTICLES, EDIT_SOUND, EDIT_PLACE_COOLDOWN, EDIT_BREAK_COOLDOWN, EDIT_ENCHANTED,
        AUTO_SAVING, NEW_STRUCTURE, EDIT_STRUCTURE, DELETE_STRUCTURE
    }
}
