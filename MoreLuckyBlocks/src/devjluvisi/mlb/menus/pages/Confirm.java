package devjluvisi.mlb.menus.pages;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.utils.ItemCreator;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import devjluvisi.mlb.menus.BasePage;
import devjluvisi.mlb.menus.LuckyMenu;
import devjluvisi.mlb.menus.LuckyMenu.View;

public class Confirm extends BasePage {
	

	public Confirm(LuckyMenu menu) {
		super(menu, "Confirm Action");
	}
	
	public enum Action {
		DELETE_DROP, REMOVE_ALL_DROPS, REMOVE_LUCKY_BLOCK
	};

	@Override
	public ItemStack[] getContent() {
		ItemStack[][] content = getPageType().getBlank2DArray();

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
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
	public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
		if(itemStack==null || itemStack.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;
		if(isPlayerSlot(slot)) {
			return;
		}
		if(getConfirmAction() == Action.DELETE_DROP) {
			if(itemStack.getType() == Material.GREEN_TERRACOTTA) {
				plugin.getLuckyBlocks().get(getBlockIndex()).getDroppableItems().remove(getDropIndex());
				view.getPlayer().sendMessage(ChatColor.GRAY + "Deleted the drop.");
				traverse(view, View.LIST_DROPS);
			}else {
				traverse(view, View.LIST_LOOT);
			}
			return;
		}
		if(getConfirmAction() == Action.REMOVE_ALL_DROPS) {
			if(itemStack.getType() == Material.GREEN_TERRACOTTA) {
				final LuckyBlockDrop firstIndex = plugin.getLuckyBlocks().get(getBlockIndex()).getDroppableItems().get(0);
				plugin.getLuckyBlocks().get(getBlockIndex()).getDroppableItems().clear();
				plugin.getLuckyBlocks().get(getBlockIndex()).getDroppableItems().add(firstIndex);
				view.getPlayer().sendMessage(ChatColor.GRAY + "Deleted All Drops (left one drop as required).");
				traverse(view, View.LIST_DROPS);
			}else {
				traverse(view, View.LIST_DROPS);
			}
			return;
		}
		if(getConfirmAction() == Action.REMOVE_LUCKY_BLOCK) {
			if(itemStack.getType() == Material.GREEN_TERRACOTTA) {
				plugin.getLuckyBlocks().remove(getBlockIndex());
				view.getPlayer().sendMessage(ChatColor.GRAY + "Deleted the lucky block.");
				view.close();
			}else {
				traverse(view, View.LIST_DROPS);
			}
			return;
		}
		
		
	}

	@Override
	public View identity() {
		return View.CONFIRM_ACTION;
	}
	
	
	
	

}
