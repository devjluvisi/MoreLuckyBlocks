package devjluvisi.mlb.menus.pages;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.api.gui.utils.ItemCreator;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.helper.Util;
import devjluvisi.mlb.menus.BasePage;
import devjluvisi.mlb.menus.LuckyMenu;
import devjluvisi.mlb.menus.LuckyMenu.View;

public class ListPage extends BasePage {

	private Random rand;

	public ListPage(LuckyMenu menu) {
		super(menu, "Lucky Block List", PageType.DOUBLE_CHEST);
		this.rand = new Random();
	}

	@Override
	public ItemStack[] getContent() {

		final ItemStack[][] content = this.getPageType().getBlank2DArray();

		// Add the items
		for (int i = 0; i < 9; i++) {
			content[0][i] = new ItemCreator(Material.BLACK_STAINED_GLASS_PANE).setName("").getItem();
		}
		for (int i = 0; i < 9; i++) {
			content[5][i] = new ItemCreator(Material.BLACK_STAINED_GLASS_PANE).setName("").getItem();
		}
		for (int i = 0; i < 9; i++) {
			content[1][i] = new ItemCreator(this.randomPane()).setName("").getItem();
		}
		for (int i = 0; i < 9; i++) {
			content[4][i] = new ItemCreator(this.randomPane()).setName("").getItem();
		}
		content[2][0] = new ItemCreator(this.randomPane()).setName("").getItem();
		content[3][0] = new ItemCreator(this.randomPane()).setName("").getItem();
		content[2][8] = new ItemCreator(this.randomPane()).setName("").getItem();
		content[3][8] = new ItemCreator(this.randomPane()).setName("").getItem();
		int luckyBlockIndex = 0;

		for (int i = 2; i != 4; i++) {
			for (int j = 1; (j < 9) && (luckyBlockIndex != this.plugin.getLuckyBlocks().size()); j++) {
				final ItemStack luckyBlock = new ItemStack(
						this.plugin.getLuckyBlocks().get(luckyBlockIndex).getBlockMaterial());
				final ItemMeta meta = luckyBlock.getItemMeta();
				meta.setDisplayName(
						ChatColor.WHITE + this.plugin.getLuckyBlocks().get(luckyBlockIndex).getInternalName());
				meta.setLore(Arrays.asList(
						ChatColor.DARK_AQUA + "Item Name" + ChatColor.GRAY + ": "
								+ this.plugin.getLuckyBlocks().get(luckyBlockIndex).getName(),
						ChatColor.DARK_AQUA + "Break Permission" + ChatColor.GRAY + ": "
								+ this.plugin.getLuckyBlocks().get(luckyBlockIndex).getBreakPermission(),
						ChatColor.DARK_AQUA + "Material" + ChatColor.GRAY + ": "
								+ this.plugin.getLuckyBlocks().get(luckyBlockIndex).getBlockMaterial().name(),
						ChatColor.DARK_AQUA + "Default Luck" + ChatColor.GRAY + ": "
								+ this.plugin.getLuckyBlocks().get(luckyBlockIndex).getDefaultBlockLuck(),
						ChatColor.DARK_AQUA + "# of Droppable Items" + ChatColor.GRAY + ": "
								+ this.plugin.getLuckyBlocks().get(luckyBlockIndex).getDroppableItems().size(),
						ChatColor.DARK_AQUA + "Lore Length" + ChatColor.GRAY + ": "
								+ this.plugin.getLuckyBlocks().get(luckyBlockIndex).getRefreshedLore().size(),
						"", ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "CLICK TO CONFIGURE"));

				luckyBlock.setItemMeta(meta);

				content[i][j] = luckyBlock;
				luckyBlockIndex++;
			}
		}
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 9; j++) {
				if (content[i][j] == null) {
					final ItemStack emptyBlock = new ItemStack(Material.HOPPER, 1);
					final ItemMeta meta = emptyBlock.getItemMeta();
					meta.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "Empty");
					meta.setLore(Arrays.asList(
							ChatColor.GRAY + "You can add a lucky block here!", ChatColor.GRAY + "Up to "
									+ ChatColor.YELLOW + "14" + ChatColor.GRAY + " custom lucky blocks can be added!",
							ChatColor.YELLOW + "/mlb create"));
					emptyBlock.setItemMeta(meta);
					content[i][j] = emptyBlock;
				}
			}
		}
		return this.getPageType().flatten(content);

	}

	private Material randomPane() {
		final Material[] glassPanes = { Material.BLUE_STAINED_GLASS_PANE, Material.BROWN_STAINED_GLASS_PANE,
				Material.CYAN_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS_PANE,
				Material.LIGHT_BLUE_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS_PANE,
				Material.RED_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS_PANE };
		this.rand = new Random();
		final int max = 8;
		final int min = 1;

		final int paneIndex = (int) ((Math.random() * ((max - min) + 1)) + min);
		return glassPanes[paneIndex - 1];
	}

	@Override
	public void onClick(MenuView view, ClickType clickType, int slot, ItemStack item) {
		if (item == null) {
			return;
		}
	
		int index = plugin.getLuckyBlocks().indexOf(new LuckyBlock(Util.makeInternal(item.getItemMeta().getDisplayName())));
		if(index==-1) return;
		this.setBlockIndex(index);
		this.traverse(view, View.LIST_DROPS);
	}

	@Override
	public View identity() {
		return View.LIST_LUCKYBLOCKS;
	}

}
