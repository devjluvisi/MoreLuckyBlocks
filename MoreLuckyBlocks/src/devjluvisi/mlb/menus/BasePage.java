package devjluvisi.mlb.menus;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.Page;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.menus.LuckyMenu.View;
import devjluvisi.mlb.menus.pages.Confirm.Action;
import net.md_5.bungee.api.ChatColor;

public abstract class BasePage implements Page {

	protected final MoreLuckyBlocks plugin;

	private final LuckyMenu baseMenu;

	private String menuName;
	private PageType pageType;

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

	public enum SpecialItem {
		EXIT_BUTTON, ADD_NEW_DROP, REMOVE_ALL_DROPS, EDIT_DROP, DELETE_DROP, COPY_DROP, CHANGE_RARITY,
		ADD_POTION_EFFECT, ADD_COMMAND, SAVE_BUTTON, CANCEL_BUTTON, CONFIRM_BUTTON, INCREASE_RARITY, DECREASE_RARITY,
		DELETE_LUCKY_BLOCK;
	}

	public boolean isPlayerSlot(int rawSlot) {
		return !((this.pageType.getSize() - rawSlot) > 0);
	}

	public abstract View identity();

	public ItemStack getSpecialItem(SpecialItem type) {
		ItemStack i;
		ItemMeta meta;
		switch (type) {
		case EXIT_BUTTON:
			i = new ItemStack(Material.BARRIER);
			meta = i.getItemMeta();
			meta.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD + "Exit");
			meta.setLore(Arrays.asList(ChatColor.GRAY + "Click to exit the menu."));
			break;
		case ADD_NEW_DROP:
			i = new ItemStack(Material.GLOWSTONE_DUST);
			meta = i.getItemMeta();
			meta.setDisplayName(ChatColor.GREEN + "Add New Drop");
			meta.setLore(Arrays.asList(ChatColor.GRAY + "Add a new drop to this lucky block."));
			break;
		case REMOVE_ALL_DROPS:
			i = new ItemStack(Material.LAVA_BUCKET);
			meta = i.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_RED + "Remove All Drops");
			meta.setLore(Arrays.asList(ChatColor.GRAY + "Remove all drops from the lucky block.",
					ChatColor.DARK_GRAY + "Will leave the first drop as at least",
					ChatColor.DARK_GRAY + "one drop is required per block."));

			break;
		case EDIT_DROP:
			i = new ItemStack(Material.WRITABLE_BOOK);
			meta = i.getItemMeta();
			meta.setDisplayName(ChatColor.GREEN + "Edit Drop");
			meta.setLore(Arrays.asList(ChatColor.GRAY + "Enable edit mode and edit",
					ChatColor.GRAY + "the content of the lucky block."));
			break;
		case COPY_DROP:
			i = new ItemStack(Material.DIAMOND);
			meta = i.getItemMeta();
			meta.setDisplayName(ChatColor.BLUE + "Duplicate Drop");
			meta.setLore(Arrays.asList(ChatColor.GRAY + "Copies the contents of this",
					ChatColor.GRAY + "drop and adds it as a new drop", ChatColor.GRAY + "to the lucky block."));
			break;
		case DELETE_DROP:
			i = new ItemStack(Material.TNT);
			meta = i.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_RED + "Delete Drop");
			meta.setLore(
					Arrays.asList(ChatColor.GRAY + "Delete this drop and", ChatColor.GRAY + "all of its contents."));
			break;
		case ADD_POTION_EFFECT:
			i = new ItemStack(Material.POTION);
			meta = i.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW + "Add Potion Effect");
			meta.setLore(Arrays.asList(ChatColor.GRAY + "Add a potion effect to",
					ChatColor.GRAY + "be applied to the player."));
			meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
			break;
		case ADD_COMMAND:
			i = new ItemStack(Material.OAK_SIGN);
			meta = i.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW + "Add Command");
			meta.setLore(
					Arrays.asList(ChatColor.GRAY + "Add a command to be", ChatColor.GRAY + "executed by console."));
			break;
		case SAVE_BUTTON:
			i = new ItemStack(Material.WRITTEN_BOOK);
			meta = i.getItemMeta();
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
			meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
			meta.setDisplayName(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Save");
			meta.setLore(Arrays.asList(ChatColor.GRAY + "Will save this edit to config."));
			break;
		case CHANGE_RARITY:
			i = new ItemStack(Material.EXPERIENCE_BOTTLE);
			meta = i.getItemMeta();
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
			meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
			meta.setDisplayName(ChatColor.DARK_AQUA.toString() + "Change Rarity");
			meta.setLore(Arrays.asList(ChatColor.GRAY + "Edit the rarity of this drop."));
			break;
		case INCREASE_RARITY:
			i = new ItemStack(Material.SLIME_BALL);
			meta = i.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_GREEN + "Increase Rarity");
			meta.setLore(Arrays.asList(ChatColor.GRAY + "Increase the rarity of this",
					ChatColor.GRAY + "drop by " + ChatColor.GREEN + "5" + ChatColor.GRAY + " points.",
					ChatColor.DARK_GRAY + "(Less Common)"));
			break;
		case DECREASE_RARITY:
			i = new ItemStack(Material.MAGMA_CREAM);
			meta = i.getItemMeta();
			meta.setDisplayName(ChatColor.RED + "Decrease Rarity");
			meta.setLore(Arrays.asList(ChatColor.GRAY + "Decrease the rarity of this",
					ChatColor.GRAY + "drop by " + ChatColor.RED + "5" + ChatColor.GRAY + " points.",
					ChatColor.DARK_GRAY + "(More Common)"));
			break;
		case DELETE_LUCKY_BLOCK:
			i = new ItemStack(Material.TNT);
			meta = i.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString() + "Delete Lucky Block");
			meta.setLore(Arrays.asList(ChatColor.GRAY + "Delete this lucky block and",
					ChatColor.GRAY + "all of its contents.", ChatColor.DARK_GRAY + "(Cannot be undone)"));
			break;
		default:
			Bukkit.getLogger().severe("Could not read specific value of getSpecialItem method.");
			return null;
		}
		i.setItemMeta(meta);
		return i;
	}

	public String getMenuName() {
		return this.menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public void setPageType(PageType pageType) {
		this.pageType = pageType;
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
				Bukkit.getServer().getConsoleSender().sendMessage("moved to view: " + v.name());
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
		meta.setDisplayName(dName);
		meta.setLore(lore);
		i.setItemMeta(meta);
		return i;
	}

}
