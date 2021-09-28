package devjluvisi.mlb.menus.pages;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
	}

	@Override
	public ItemStack[] getContent() {
		final ItemStack[][] content = this.getPageType().getBlank2DArray();

		for (int i = 0; i < 3; i++) {
			Arrays.fill(content[i], new ItemCreator(Material.GRAY_STAINED_GLASS_PANE).getItem());
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

		return this.getPageType().flatten(content);
	}

	@Override
	public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
		if ((itemStack == null) || itemStack.getType().equals(Material.GRAY_STAINED_GLASS_PANE)
				|| this.isPlayerSlot(slot)) {
			return;
		}
		if (this.getConfirmAction() == Action.DELETE_DROP) {
			if (itemStack.getType() == Material.GREEN_TERRACOTTA) {
				this.plugin.getLuckyBlocks().get(this.getBlockIndex()).getDroppableItems().remove(this.getDropIndex());
				view.getPlayer().sendMessage(ChatColor.GRAY + "Deleted the drop.");
				this.traverse(view, View.LIST_DROPS);
			} else {
				this.traverse(view, View.LIST_LOOT);
			}
			return;
		}
		if (this.getConfirmAction() == Action.REMOVE_ALL_DROPS) {
			if (itemStack.getType() == Material.GREEN_TERRACOTTA) {
				final LuckyBlockDrop firstIndex = this.plugin.getLuckyBlocks().get(this.getBlockIndex())
						.getDroppableItems().get(0);
				this.plugin.getLuckyBlocks().get(this.getBlockIndex()).getDroppableItems().clear();
				this.plugin.getLuckyBlocks().get(this.getBlockIndex()).getDroppableItems().add(firstIndex);
				view.getPlayer().sendMessage(ChatColor.GRAY + "Deleted All Drops (left one drop as required).");
				this.traverse(view, View.LIST_DROPS);
			} else {
				this.traverse(view, View.LIST_DROPS);
			}
			return;
		}
		if (this.getConfirmAction() == Action.REMOVE_LUCKY_BLOCK) {
			if (itemStack.getType() == Material.GREEN_TERRACOTTA) {
				this.plugin.getLuckyBlocks().remove(this.getBlockIndex());
				view.getPlayer().sendMessage(ChatColor.GRAY + "Deleted the lucky block.");
				view.close();
			} else {
				this.traverse(view, View.LIST_DROPS);
			}
			return;
		}

	}

	@Override
	public View identity() {
		return View.CONFIRM_ACTION;
	}

}
