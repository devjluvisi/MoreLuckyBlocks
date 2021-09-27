package devjluvisi.mlb.menus.pages;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.utils.ItemCreator;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import devjluvisi.mlb.menus.BasePage;
import devjluvisi.mlb.menus.BasePage.SpecialItem;
import devjluvisi.mlb.menus.LuckyMenu;
import devjluvisi.mlb.menus.LuckyMenu.View;
import devjluvisi.mlb.menus.pages.Confirm.Action;
import net.md_5.bungee.api.ChatColor;

public class DropsPage extends BasePage {
	
	private int startingIndex;

	public DropsPage(LuckyMenu menu) {
		super(menu);
		setMenuName(ChatColor.DARK_PURPLE + "Viewing: " + plugin.getLuckyBlocks().get(menu.getBlockIndex()).getInternalName());
		startingIndex = 0;
	}
	
	
    
	@Override
	public ItemStack[] getContent() {
		ItemStack[][] content = getPageType().getBlank2DArray();
        int rowCount = 0;
        int colCount = 0;
        for(int i = startingIndex; i < plugin.getLuckyBlocks().get(super.getBlockIndex()).getDroppableItems().size(); i++) {
        	if(colCount == 9) {
        		colCount = 0;
        		rowCount++;
        	}
        	ItemStack dropSlot = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        	ItemMeta meta = dropSlot.getItemMeta();
        	meta.setDisplayName(ChatColor.GREEN + "Drop: " + (i));
        	int size = plugin.getLuckyBlocks().get(getBlockIndex()).getDroppableItems().get(i).getItems().size() + plugin.getLuckyBlocks().get(getBlockIndex()).getDroppableItems().get(i).getCommands().size() + plugin.getLuckyBlocks().get(getBlockIndex()).getDroppableItems().get(i).getPotionEffects().size();
        	meta.setLore(Arrays.asList(ChatColor.AQUA + "You have a drop set for this item.", ChatColor.GRAY.toString()+ChatColor.BOLD + "Total Drops: " + ChatColor.GREEN + size, ChatColor.GRAY.toString()+ChatColor.BOLD.toString() + "Rarity: " + ChatColor.LIGHT_PURPLE + plugin.getLuckyBlocks().get(getBlockIndex()).getDroppableItems().get(i).getRarity(), ChatColor.GRAY.toString() + ChatColor.ITALIC + "Click to configure/delete drop."));
        	dropSlot.setItemMeta(meta);
        	if(rowCount == 3)
        		continue;
        	content[rowCount][colCount] = dropSlot;
        	colCount++;
        }
        for(int i = 0; i < 3; i++) {
        	for(int j = 0; j < 9; j++) {
        		if(content[i][j] == null) {
        			content[i][j] = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE).setName(ChatColor.GRAY + "You have no drop set for this slot.").getItem();
        		}
        	}
        }
        content[2][1] =  super.getSpecialItem(SpecialItem.EXIT_BUTTON);
        content[2][0] = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        content[2][6] = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        content[2][2] = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        content[2][3] = super.getSpecialItem(SpecialItem.ADD_NEW_DROP);
        content[2][4] = super.getSpecialItem(SpecialItem.REMOVE_ALL_DROPS);
        content[2][5] = super.getSpecialItem(SpecialItem.DELETE_LUCKY_BLOCK);
        int maxPages = (plugin.getLuckyBlocks().get(getBlockIndex()).getDroppableItems().size() / 18) + 1;
        int currPage = (startingIndex/18) + 1;
        content[2][7] = super.getPlaceholderItem(Material.FEATHER, ChatColor.YELLOW + "Previous Page " + ChatColor.GRAY + "(" + currPage + "/" + maxPages + ")", Arrays.asList(ChatColor.GRAY + "Go back to the previous page."));
        content[2][8] = super.getPlaceholderItem(Material.ARROW, ChatColor.YELLOW + "Next Page " + ChatColor.GRAY + "(" + currPage + "/" + maxPages + ")", Arrays.asList(ChatColor.GRAY + "Go to the next page."));
        return getPageType().flatten(content);
	}

	@Override
	public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
		if(itemStack == null) return;
			if(itemStack.getItemMeta().getDisplayName().contains("Drop:")) {
        	
        	int dropIndex = Integer.parseInt(ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()).split(": ")[1]);
        	
        	setDropIndex(dropIndex);
        	traverse(view, View.LIST_LOOT);
        	return;
        }
		if(itemStack.equals(getSpecialItem(SpecialItem.ADD_NEW_DROP))) {
			plugin.getLuckyBlocks().get(getBlockIndex()).getDroppableItems().add(new LuckyBlockDrop());
			setDropIndex(plugin.getLuckyBlocks().get(getBlockIndex()).getDroppableItems().size()-1);
			traverse(view, View.EDIT_DROP);
			return;
		}
		if(itemStack.equals(getSpecialItem(SpecialItem.REMOVE_ALL_DROPS))) {
			if(plugin.getLuckyBlocks().get(getBlockIndex()).getDroppableItems().size() == 1) {
				view.getPlayer().sendMessage(ChatColor.RED + "You must have more then one drop in this lucky block.");
				return;
			}
			setConfirmAction(Action.REMOVE_ALL_DROPS);
			traverse(view, View.CONFIRM_ACTION);
			return;
		}
		if(itemStack.equals(getSpecialItem(SpecialItem.DELETE_LUCKY_BLOCK))) {
			if(plugin.getLuckyBlocks().size() == 1) {
				view.getPlayer().sendMessage(ChatColor.RED + "You must have at least one lucky block.");
				return;
			}
			setConfirmAction(Action.REMOVE_LUCKY_BLOCK);
			traverse(view, View.CONFIRM_ACTION);
			return;
			
		}
		if(itemStack.getType().equals(Material.FEATHER)) {
			if(startingIndex - 18 <= 0) {
				startingIndex = 0;
			}else {
				startingIndex -= 18;
			}
			
			view.reopen();
			return;
		}
		if(itemStack.getType().equals(Material.ARROW)) {
			int maxPages = (plugin.getLuckyBlocks().get(getBlockIndex()).getDroppableItems().size() / 18) + 1;
		    int currPage = (startingIndex/18) + 1;
			if(currPage == maxPages) return;
			startingIndex += 18;
			view.reopen();
			return;
		}
		if(itemStack.equals(getSpecialItem(SpecialItem.EXIT_BUTTON))) {
			traverse(view, View.LIST_LUCKYBLOCKS);
		}
	}

	@Override
	public View identity() {
		return View.LIST_DROPS;
	}
	
	
	
	

}
