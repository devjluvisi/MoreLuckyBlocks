package devjluvisi.mlb.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import devjluvisi.mlb.blocks.drops.DropProperty;
import devjluvisi.mlb.blocks.drops.LuckyBlockCommand;
import devjluvisi.mlb.blocks.drops.LuckyBlockPotionEffect;
import devjluvisi.mlb.util.Range;
import fr.dwightstudio.dsmapi.MenuView;
import fr.dwightstudio.dsmapi.pages.PageType;
import fr.dwightstudio.dsmapi.utils.ItemCreator;
import net.md_5.bungee.api.ChatColor;

public class LuckyBlockViewDropLootMenu extends BaseMenu {
	
	private LuckyBlockDrop blockDrop;
	private MoreLuckyBlocks plugin;
	private int dropIndex;

	public LuckyBlockViewDropLootMenu(MoreLuckyBlocks plugin, LuckyBlockDrop blockDrop, int drop) {
		super(plugin, ChatColor.BLACK + "Viewing Loot for Drop #" + drop, PageType.CHEST);
		this.plugin = plugin;
		this.blockDrop = blockDrop;
		this.dropIndex = drop;
	}

	@Override
	public ItemStack[] getContent() {
		ItemStack[][] content = getPageType().getBlank2DArray();

		ItemStack dropViewInfo = new ItemStack(Material.OAK_SIGN);
		ItemMeta dropViewMeta = dropViewInfo.getItemMeta();
		dropViewMeta.setDisplayName(ChatColor.GRAY + "Viewing Drop " + ChatColor.DARK_GRAY + "-> " + ChatColor.DARK_AQUA + dropIndex);
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
        
        ArrayList<DropProperty> dropList = blockDrop.getAllDrops();
        
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
		
	}

}
