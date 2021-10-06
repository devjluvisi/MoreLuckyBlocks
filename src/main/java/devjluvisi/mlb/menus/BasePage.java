package devjluvisi.mlb.menus;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.Page;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.menus.LuckyMenu.View;
import devjluvisi.mlb.menus.pages.Confirm.Action;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public abstract class BasePage implements Page {

    protected final MoreLuckyBlocks plugin;

    private final LuckyMenu baseMenu;
    private final PageType pageType;
    private String menuName;

    public BasePage(LuckyMenu menu) {
        this.baseMenu = menu;
        this.plugin = menu.getPlugin();
        this.menuName = "UNKNOWN";
        this.pageType = PageType.CHEST;
    }

    public BasePage(LuckyMenu menu, String menuName) {
        this.baseMenu = menu;
        this.plugin = menu.getPlugin();
        this.menuName = menuName;
        this.pageType = PageType.CHEST;
    }

    public BasePage(LuckyMenu menu, String menuName, PageType pageType) {
        this.baseMenu = menu;
        this.plugin = menu.getPlugin();
        this.menuName = menuName;
        this.pageType = pageType;
    }

    @Override
    public String getName() {
        return this.menuName;
    }

    @Override
    public PageType getPageType() {
        return this.pageType;
    }

    public boolean isPlayerSlot(int rawSlot) {
        return !((this.pageType.getSize() - rawSlot) > 0);
    }

    public abstract View identity();

    public ItemStack getSpecialItem(SpecialItem type) {
        ItemStack i = new ItemStack(Material.STONE);
        ItemMeta meta = i.getItemMeta();
        assert meta != null;
        switch (type) {
            case EXIT_BUTTON -> {
                i.setType(Material.BARRIER);
                meta.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD + "Exit");
                meta.setLore(List.of(ChatColor.GRAY + "Click to exit the menu."));
            }
            case ADD_NEW_DROP -> {
                i.setType(Material.GLOWSTONE_DUST);
                meta.setDisplayName(ChatColor.GREEN + "Add New Drop");
                meta.setLore(List.of(ChatColor.GRAY + "Add a new drop to this lucky block."));
            }
            case REMOVE_ALL_DROPS -> {
                i.setType(Material.LAVA_BUCKET);
                meta.setDisplayName(ChatColor.DARK_RED + "Remove All Drops");
                meta.setLore(Arrays.asList(ChatColor.GRAY + "Remove all drops from the lucky block.",
                        ChatColor.DARK_GRAY + "Will leave the first drop as at least",
                        ChatColor.DARK_GRAY + "one drop is required per block."));
            }
            case EDIT_DROP -> {
                i.setType(Material.BOOK);
                meta.setDisplayName(ChatColor.GREEN + "Edit Drop");
                meta.setLore(Arrays.asList(ChatColor.GRAY + "Enable edit mode and edit",
                        ChatColor.GRAY + "the content of the lucky block."));
            }
            case COPY_DROP -> {
                i.setType(Material.DIAMOND);
                meta.setDisplayName(ChatColor.BLUE + "Duplicate Drop");
                meta.setLore(Arrays.asList(ChatColor.GRAY + "Copies the contents of this",
                        ChatColor.GRAY + "drop and adds it as a new drop", ChatColor.GRAY + "to the lucky block."));
            }
            case DELETE_DROP -> {
                i.setType(Material.TNT);
                meta.setDisplayName(ChatColor.DARK_RED + "Delete Drop");
                meta.setLore(
                        Arrays.asList(ChatColor.GRAY + "Delete this drop and", ChatColor.GRAY + "all of its contents."));
            }
            case ADD_POTION_EFFECT -> {
                i.setType(Material.POTION);
                meta.setDisplayName(ChatColor.YELLOW + "Add Potion Effect");
                meta.setLore(Arrays.asList(ChatColor.GRAY + "Add a potion effect to",
                        ChatColor.GRAY + "be applied to the player."));
                meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            }
            case ADD_COMMAND -> {
                i.setType(Material.OAK_SIGN);
                meta.setDisplayName(ChatColor.YELLOW + "Add Command");
                meta.setLore(
                        Arrays.asList(ChatColor.GRAY + "Add a command to be", ChatColor.GRAY + "executed by console."));
            }
            case SAVE_BUTTON -> {
                i.setType(Material.EMERALD);
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
                meta.setDisplayName(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Save");
                meta.setLore(List.of(ChatColor.GRAY + "Will save this edit to config."));
            }
            case CHANGE_RARITY -> {
                i.setType(Material.EXPERIENCE_BOTTLE);
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
                meta.setDisplayName(ChatColor.DARK_AQUA + "Change Rarity");
                meta.setLore(List.of(ChatColor.GRAY + "Edit the rarity of this drop."));
            }
            case INCREASE_RARITY -> {
                i.setType(Material.SLIME_BALL);
                meta.setDisplayName(ChatColor.DARK_GREEN + "Increase Rarity");
                meta.setLore(Arrays.asList(ChatColor.GRAY + "Increase the rarity of this",
                        ChatColor.GRAY + "drop by " + ChatColor.GREEN + "5" + ChatColor.GRAY + " points.",
                        ChatColor.DARK_GRAY + "(Less Common)"));
            }
            case DECREASE_RARITY -> {
                i.setType(Material.MAGMA_CREAM);
                meta.setDisplayName(ChatColor.RED + "Decrease Rarity");
                meta.setLore(Arrays.asList(ChatColor.GRAY + "Decrease the rarity of this",
                        ChatColor.GRAY + "drop by " + ChatColor.RED + "5" + ChatColor.GRAY + " points.",
                        ChatColor.DARK_GRAY + "(More Common)"));
            }
            case DELETE_LUCKY_BLOCK -> {
                i.setType(Material.TNT);
                meta.setDisplayName(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Delete Lucky Block");
                meta.setLore(Arrays.asList(ChatColor.GRAY + "Delete this lucky block and",
                        ChatColor.GRAY + "all of its contents.", ChatColor.DARK_GRAY + "(Cannot be undone)"));
            }
            case ADD_STRUCTURE -> {
                i.setType(Material.STONE_BRICKS);
                meta.setDisplayName(ChatColor.YELLOW + "Add/Edit Structure");
                meta.setLore(Arrays.asList(ChatColor.GRAY + "Add or edit a structure to this",
                        ChatColor.GRAY + "lucky block. Structures are physical",
                        ChatColor.GRAY + "modifications to the world that", ChatColor.GRAY + "happen when a user breaks a",
                        ChatColor.GRAY + "luckyblock.", ChatColor.GRAY + "Do " + ChatColor.DARK_AQUA
                                + ChatColor.BOLD + "/mlb struct" + ChatColor.GRAY + " for more info."));
            }
            default -> {
                Bukkit.getLogger().severe("Could not read specific value of getSpecialItem method.");
                return null;
            }
        }
        i.setItemMeta(meta);
        return i;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    protected int getBlockIndex() {
        return this.baseMenu.getBlockIndex();
    }

    protected void setBlockIndex(int blockIndex) {
        this.baseMenu.setBlockIndex(blockIndex);
    }

    protected int getDropIndex() {
        return this.baseMenu.getDropIndex();
    }

    protected void setDropIndex(int dropIndex) {
        this.baseMenu.setDropIndex(dropIndex);
    }

    protected Action getConfirmAction() {
        return this.baseMenu.getConfirmAction();
    }

    protected void setConfirmAction(Action confirmAction) {
        this.baseMenu.setConfirmAction(confirmAction);
    }

    /**
     * Goes through all of the pages and finds a page with the view requested.
     *
     * @param view Current GUI.
     * @param v    View to open.
     */
    public void traverse(MenuView view, View v) {
        for (byte i = 0; i < this.baseMenu.getPages().length; i++) {
            if (((BasePage) this.baseMenu.getPage(i)).identity().equals(v)) {
                this.baseMenu.refresh();
                view.setPage(i);
                return;
            }
        }
    }

    /**
     * A "placeholder" item to be stored in a GUI. Usually used to send a message to
     * the user.
     *
     * @param m     The material of the item.
     * @param dName The display name to set for the item.
     * @param lore  Lore for the item.
     * @return A placeholder itemstack.
     */
    public ItemStack getPlaceholderItem(Material m, String dName, List<String> lore) {
        final ItemStack i = new ItemStack(m);
        final ItemMeta meta = i.getItemMeta();
        assert meta != null;
        meta.setDisplayName(dName);
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }

    public enum SpecialItem {
        EXIT_BUTTON, ADD_NEW_DROP, REMOVE_ALL_DROPS, EDIT_DROP, DELETE_DROP, COPY_DROP, CHANGE_RARITY,
        ADD_POTION_EFFECT, ADD_COMMAND, SAVE_BUTTON, CANCEL_BUTTON, CONFIRM_BUTTON, INCREASE_RARITY, DECREASE_RARITY,
        DELETE_LUCKY_BLOCK, ADD_STRUCTURE
    }

}
