package devjluvisi.mlb.menus;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
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

        return getPageType().flatten(content);
	}

	@Override
	public void onClick(MenuView arg0, ClickType arg1, int arg2, ItemStack arg3) {
		// TODO Auto-generated method stub
		
	}

}
