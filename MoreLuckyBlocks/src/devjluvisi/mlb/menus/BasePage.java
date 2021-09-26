package devjluvisi.mlb.menus;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.Page;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.menus.LuckyMenu.View;
import net.md_5.bungee.api.ChatColor;

public abstract class BasePage implements Page {
	
	protected MoreLuckyBlocks plugin;
	private String menuName;
	private PageType pageType;
	
	public BasePage(MoreLuckyBlocks plugin, String menuName) {
		this.plugin = plugin;
		this.menuName = menuName;
		this.pageType = PageType.CHEST;
	}
	
	public BasePage(MoreLuckyBlocks plugin, String menuName, PageType pageType) {
		this.plugin = plugin;
		this.menuName = menuName;
		this.pageType = pageType;
	}


	@Override
	public String getName() {
		return menuName;
	}

	@Override
	public PageType getPageType() {
		return pageType;
	}
	
	public enum SpecialItem {
		EXIT_BUTTON, ADD_NEW_DROP, REMOVE_ALL_DROPS, PREVIOUS_PAGE, NEXT_PAGE, EDIT_DROP, DELETE_DROP, COPY_DROP, ADD_POTION_EFFECT, ADD_COMMAND, SAVE_BUTTON, CANCEL_BUTTON, CONFIRM_BUTTON; 
	}
	
	public boolean isPlayerSlot(int rawSlot) {
		return !(pageType.getSize()-rawSlot > 0);
	}
	
	
	public ItemStack getSpecialItem(SpecialItem type) {
		ItemStack i;
		ItemMeta meta;
		switch(type) {
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
	    	meta.setLore(Arrays.asList(ChatColor.GRAY + "Remove all drops from the lucky block."));
	    	break;
		case PREVIOUS_PAGE:
	    	i = new ItemStack(Material.FEATHER);
	    	meta = i.getItemMeta();
	    	meta.setDisplayName(ChatColor.YELLOW + "Previous Page");
	    	break;
		case NEXT_PAGE:
	    	i = new ItemStack(Material.ARROW);
	    	meta = i.getItemMeta();
	    	meta.setDisplayName(ChatColor.YELLOW + "Next Page");
	    	break;
		case EDIT_DROP:
			i = new ItemStack(Material.WRITABLE_BOOK);
			meta = i.getItemMeta();
			meta.setDisplayName(ChatColor.GREEN + "Edit Drop");
			meta.setLore(Arrays.asList(
					ChatColor.GRAY + "Enable edit mode and edit",
					ChatColor.GRAY + "the content of the lucky block."
					));
			break;
		case COPY_DROP:
			i = new ItemStack(Material.DIAMOND);
			meta = i.getItemMeta();
			meta.setDisplayName(ChatColor.BLUE + "Duplicate Drop");
			meta.setLore(Arrays.asList(
					ChatColor.GRAY + "Copies the contents of this",
					ChatColor.GRAY + "drop and adds it as a new drop",
					ChatColor.GRAY + "to the lucky block."
					));
			break;
		case DELETE_DROP:
			i = new ItemStack(Material.TNT);
			meta = i.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_RED + "Delete Drop");
			meta.setLore(Arrays.asList(ChatColor.GRAY + "Delete this drop and", ChatColor.GRAY + "all of its contents."));
			break;
		case ADD_POTION_EFFECT:
			i = new ItemStack(Material.POTION);
			meta = i.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW + "Add Potion Effect");
			meta.setLore(Arrays.asList(ChatColor.GRAY + "Add a potion effect to", ChatColor.GRAY + "be applied to the player."));
			meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
			break;
		case ADD_COMMAND:
			i = new ItemStack(Material.OAK_SIGN);
			meta = i.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW + "Add Command");
			meta.setLore(Arrays.asList(ChatColor.GRAY + "Add a command to be", ChatColor.GRAY + "executed by console."));
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
		default:
			Bukkit.getLogger().severe("Could not read specific value of getSpecialItem method.");
			return null;
		}
		i.setItemMeta(meta);
		return i;
	}
	
	/**
	 * A "placeholder" item to be stored in a GUI. Usually used to send a message to the user.
	 * @param m The material of the item.
	 * @param dName The display name to set for the item.
	 * @param lore Lore for the item.
	 * @return A placeholder itemstack.
	 */
	public ItemStack getPlaceholderItem(Material m, String dName, List<String> lore) {
		ItemStack i = new ItemStack(m);
    	ItemMeta meta = i.getItemMeta();
    	meta.setDisplayName(dName);
    	meta.setLore(lore);
    	i.setItemMeta(meta);
    	return i;
	}
	
	
	
	

}
