package devjluvisi.mlb.menus;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.queries.Query;
import fr.dwightstudio.dsmapi.MenuView;
import fr.dwightstudio.dsmapi.pages.PageType;
import fr.dwightstudio.dsmapi.utils.ItemCreator;

public class ConfirmMenu extends BaseMenu {

	private BaseMenu prevMenu;
	
	public ConfirmMenu(MoreLuckyBlocks plugin, BaseMenu menu) {
		super(plugin, "Confirm Action", PageType.CHEST);
		this.prevMenu = menu;
	}

	@Override
	public ItemStack[] getContent() {
		ItemStack[][] content = getPageType().getBlank2DArray();

        for(int i = 0; i < 3; i++) {
        	for(int j = 0; j < 9; j++) {
        		content[i][j] = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE).getItem();
        	}
        }
        
        ItemStack i;
        ItemMeta meta;
        
        i = new ItemStack(Material.GREEN_TERRACOTTA);
        meta = i.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Confirm");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Are you sure you want", ChatColor.GRAY + "to do this?"));
        i.setItemMeta(meta);
        content[1][2] = i;
        i = new ItemStack(Material.RED_TERRACOTTA);
        meta = i.getItemMeta();
        meta.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD + "Cancel");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Leave without confirming."));
        i.setItemMeta(meta);
        content[1][6] = i;
        

        return getPageType().flatten(content);
	}

	@Override
	public void onClick(MenuView view, ClickType type, int slot, ItemStack item) {
		if(item==null || item.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;
		if(item.getType().equals(Material.GREEN_TERRACOTTA)) {
			for(Query q : plugin.getRequests()) {
				if(q.getPlayerUUID() == view.getPlayer().getUniqueId()) {
					q.runProcess();
					return;
				}
			}
		}else {
			prevMenu.open(view.getPlayer());
		}
	}
	
	

}
