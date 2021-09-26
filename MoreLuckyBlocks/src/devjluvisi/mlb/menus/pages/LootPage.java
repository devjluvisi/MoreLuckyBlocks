package devjluvisi.mlb.menus.pages;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.blocks.drops.LootProperty;
import devjluvisi.mlb.menus.BasePage;
import devjluvisi.mlb.menus.LuckyMenu;
import devjluvisi.mlb.menus.BasePage.SpecialItem;
import devjluvisi.mlb.menus.LuckyMenu.View;
import devjluvisi.mlb.menus.pages.Confirm.Action;

public class LootPage extends BasePage {

	int blockIndex;
	int dropIndex;
	
	public LootPage(MoreLuckyBlocks plugin, int blockIndex, int dropIndex) {
		super(plugin, "Viewing Drop #" + dropIndex);
		this.blockIndex = blockIndex;
		this.dropIndex = dropIndex;
	}

	@Override
	public ItemStack[] getContent() {
		ItemStack[][] content = getPageType().getBlank2DArray();

		ItemStack dropViewInfo = new ItemStack(Material.OAK_SIGN);
		ItemMeta dropViewMeta = dropViewInfo.getItemMeta();
		dropViewMeta.setDisplayName(ChatColor.GRAY + "Viewing Drop " + ChatColor.DARK_GRAY + "-> " + ChatColor.DARK_AQUA + (dropIndex));
		dropViewMeta.setLore(Arrays.asList(ChatColor.GRAY + "You are currently viewing all of the loot", ChatColor.GRAY + "that can drop from this drop.", ChatColor.GOLD + "Click the Book & Quill to edit the drop."));
		dropViewInfo.setItemMeta(dropViewMeta);
		
        content[0][0] = dropViewInfo;
        content[0][1] = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        content[1][0] = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        content[1][1] = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        for(int i = 0; i < 5; i++) content[2][i] = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        content[2][5] = super.getSpecialItem(SpecialItem.EDIT_DROP);
        content[2][6] = super.getSpecialItem(SpecialItem.COPY_DROP);
        content[2][7] = super.getSpecialItem(SpecialItem.DELETE_DROP);
        content[2][8] = super.getSpecialItem(SpecialItem.EXIT_BUTTON);
        
        ArrayList<LootProperty> dropList = plugin.getLuckyBlocks().get(blockIndex).getDroppableItems().get(dropIndex).getLoot();
        
        int arrIndex = 0;
        
        for(int i = 0; i < 2; i++) {
        	for(int j = 2; j < 9; j++) {
        		if(arrIndex == dropList.size()) break;
        		content[i][j] = dropList.get(arrIndex).asItem();
        		arrIndex++;
        	}
        }
        return getPageType().flatten(content);
	}

	@Override
	public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
		if(itemStack==null) return;
		if(itemStack.equals(getSpecialItem(SpecialItem.EDIT_DROP))) {
			view.setPage(View.EDIT_DROP.toInt());
			return;
		}
		if(itemStack.equals(getSpecialItem(SpecialItem.COPY_DROP))) {
			plugin.getLuckyBlocks().get(blockIndex).getDroppableItems().add(plugin.getLuckyBlocks().get(blockIndex).getDroppableItems().get(dropIndex));
			view.setPage(View.LIST_DROPS.toInt());
			return;
		}
		if(itemStack.equals(getSpecialItem(SpecialItem.DELETE_DROP))) {
			new LuckyMenu(plugin, blockIndex, dropIndex, Action.DELETE_DROP).open(view.getPlayer(), View.CONFIRM_ACTION);
			return;
		}
		if(itemStack.equals(getSpecialItem(SpecialItem.EXIT_BUTTON))) {
			view.setPage(View.LIST_DROPS.toInt());
		}
        
		
	}
	
	
	
	

}
