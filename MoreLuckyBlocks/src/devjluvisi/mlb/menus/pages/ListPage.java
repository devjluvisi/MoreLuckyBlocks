package devjluvisi.mlb.menus.pages;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.api.gui.utils.ItemCreator;
import devjluvisi.mlb.menus.BasePage;
import devjluvisi.mlb.menus.LuckyMenu;
import devjluvisi.mlb.menus.LuckyMenu.View;
import devjluvisi.mlb.menus.pages.Confirm.Action;

public class ListPage extends BasePage {
	
	private Random rand;

	public ListPage(LuckyMenu menu) {
		super(menu, "Lucky Block List", PageType.DOUBLE_CHEST);
		rand = new Random();
	}

	@Override
	public ItemStack[] getContent() {
		
		ItemStack[][] content = getPageType().getBlank2DArray();
		
		// Add the items
        for(int i = 0; i < 9; i++) content[0][i] = new ItemCreator(Material.BLACK_STAINED_GLASS_PANE).setName("").getItem();
        for(int i = 0; i < 9; i++) content[5][i] = new ItemCreator(Material.BLACK_STAINED_GLASS_PANE).setName("").getItem();
        for(int i = 0; i < 9; i++) content[1][i] = new ItemCreator(randomPane()).setName("").getItem();
        for(int i = 0; i < 9; i++) content[4][i] = new ItemCreator(randomPane()).setName("").getItem();
        content[2][0] =new ItemCreator(randomPane()).setName("").getItem();
        content[3][0] =new ItemCreator(randomPane()).setName("").getItem();
        content[2][8] =new ItemCreator(randomPane()).setName("").getItem();
        content[3][8] =new ItemCreator(randomPane()).setName("").getItem();
        int luckyBlockIndex = 0;

        
        for(int i = 2; i != 4; i++) {
        	for(int j = 1; j < 9 && luckyBlockIndex != plugin.getLuckyBlocks().size(); j++) {
        		ItemStack luckyBlock = new ItemStack(plugin.getLuckyBlocks().get(luckyBlockIndex).getBlockMaterial());
            	ItemMeta meta = luckyBlock.getItemMeta();
            	meta.setDisplayName(ChatColor.WHITE + plugin.getLuckyBlocks().get(luckyBlockIndex).getInternalName());
            	meta.setLore(Arrays.asList(
            			ChatColor.DARK_AQUA + "Item Name" + ChatColor.GRAY + ": " + plugin.getLuckyBlocks().get(luckyBlockIndex).getName(),
            			ChatColor.DARK_AQUA + "Break Permission" + ChatColor.GRAY + ": " + plugin.getLuckyBlocks().get(luckyBlockIndex).getBreakPermission(),
            			ChatColor.DARK_AQUA + "Material" + ChatColor.GRAY + ": " + plugin.getLuckyBlocks().get(luckyBlockIndex).getBlockMaterial().name(),
            			ChatColor.DARK_AQUA + "Default Luck" + ChatColor.GRAY + ": " + plugin.getLuckyBlocks().get(luckyBlockIndex).getDefaultBlockLuck(),
            			ChatColor.DARK_AQUA + "# of Droppable Items" + ChatColor.GRAY + ": " + plugin.getLuckyBlocks().get(luckyBlockIndex).getDroppableItems().size(),
            			ChatColor.DARK_AQUA + "Lore Length" + ChatColor.GRAY + ": " + plugin.getLuckyBlocks().get(luckyBlockIndex).getRefreshedLore().size(),
            			"",
            			ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "CLICK TO CONFIGURE"
            			));
            	
            	luckyBlock.setItemMeta(meta);
            	
            	content[i][j] = luckyBlock;
            	luckyBlockIndex++;
        	}
        }
        for(int i = 0; i < 6; i++) {
        	for(int j = 0; j < 9; j++) {
        		if(content[i][j] == null) {
        			ItemStack emptyBlock = new ItemStack(Material.HOPPER, 1);
        			ItemMeta meta = emptyBlock.getItemMeta();
        			meta.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "Empty");
        			meta.setLore(Arrays.asList(ChatColor.GRAY + "You can add a lucky block here!", ChatColor.GRAY + "Up to " + ChatColor.YELLOW + "14" + ChatColor.GRAY + " custom lucky blocks can be added!", ChatColor.YELLOW + "/mlb create"));
        			emptyBlock.setItemMeta(meta);
        			content[i][j] = emptyBlock;
        		}
        	}
        }
        return getPageType().flatten(content);
		
	}
	
	private Material randomPane() {
    	Material[] glassPanes = new Material[] {
    			Material.BLUE_STAINED_GLASS_PANE, Material.BROWN_STAINED_GLASS_PANE, Material.CYAN_STAINED_GLASS_PANE,
    			Material.YELLOW_STAINED_GLASS_PANE, Material.LIGHT_BLUE_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS_PANE,
    			Material.RED_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS_PANE
    	};
    	rand = new Random();
    	final int max = 8;
    	final int min = 1;
    	
    	int paneIndex = (int)(Math.random()*(max-min+1)+min);
    	return glassPanes[paneIndex-1];
    }

	@Override
	public void onClick(MenuView view, ClickType clickType, int slot, ItemStack item) {
		if(item == null) return;
		for(int i = 0; i < plugin.getLuckyBlocks().size(); i++) {
        	if(ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(plugin.getLuckyBlocks().get(i).getInternalName()))) {
        		
        		setBlockIndex(i);
        		traverse(view, View.LIST_DROPS);
        	}
        }
	}

	@Override
	public View identity() {
		return View.LIST_LUCKYBLOCKS;
	}

	
	

}
