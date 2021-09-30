package devjluvisi.mlb.menus.pages;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import devjluvisi.mlb.blocks.drops.LootProperty;
import devjluvisi.mlb.menus.BasePage;
import devjluvisi.mlb.menus.LuckyMenu;
import devjluvisi.mlb.menus.LuckyMenu.View;
import devjluvisi.mlb.menus.pages.Confirm.Action;

public class LootPage extends BasePage {

	public LootPage(LuckyMenu menu) {
		super(menu);
		this.setMenuName("Viewing loot for: " + menu.getDropIndex());
	}

	@Override
	public ItemStack[] getContent() {

		final ItemStack[][] content = this.getPageType().getBlank2DArray();

		final ItemStack dropViewInfo = new ItemStack(Material.OAK_SIGN);
		final ItemMeta dropViewMeta = dropViewInfo.getItemMeta();
		dropViewMeta.setDisplayName(ChatColor.GRAY + "Viewing Drop " + ChatColor.DARK_GRAY + "-> " + ChatColor.DARK_AQUA
				+ (this.getDropIndex()));
		dropViewMeta.setLore(Arrays.asList(ChatColor.GRAY + "You are currently viewing all of the loot",
				ChatColor.GRAY + "that can drop from this drop.",
				ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Rarity: " + ChatColor.BLUE
						+ this.plugin.getLuckyBlocks().get(this.getBlockIndex()).getDroppableItems()
								.get(this.getDropIndex()).getRarity(),
				ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "# of Items: " + ChatColor.BLUE
						+ this.plugin.getLuckyBlocks().get(this.getBlockIndex()).getDroppableItems()
								.get(this.getDropIndex()).getItems().size(),
				ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "# of Potions: " + ChatColor.BLUE
						+ this.plugin.getLuckyBlocks().get(this.getBlockIndex()).getDroppableItems()
								.get(this.getDropIndex()).getPotionEffects().size(),
				ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "# of Commands: " + ChatColor.BLUE
						+ this.plugin.getLuckyBlocks().get(this.getBlockIndex()).getDroppableItems()
								.get(this.getDropIndex()).getCommands().size(),
				ChatColor.GOLD + "Click the Book & Quill to edit the drop."));
		dropViewInfo.setItemMeta(dropViewMeta);

		content[0][0] = dropViewInfo;
		content[0][1] = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		content[1][0] = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		content[1][1] = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		for (int i = 0; i < 4; i++) {
			content[2][i] = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		}
		content[2][4] = super.getSpecialItem(SpecialItem.EDIT_DROP);
		content[2][5] = super.getSpecialItem(SpecialItem.CHANGE_RARITY);
		content[2][6] = super.getSpecialItem(SpecialItem.COPY_DROP);
		content[2][7] = super.getSpecialItem(SpecialItem.DELETE_DROP);
		content[2][8] = super.getSpecialItem(SpecialItem.EXIT_BUTTON);

		final ArrayList<LootProperty> dropList = this.plugin.getLuckyBlocks().get(this.getBlockIndex())
				.getDroppableItems().get(this.getDropIndex()).getLoot();

		int arrIndex = 0;

		for (int i = 0; i < 2; i++) {
			for (int j = 2; j < 9; j++) {
				if (arrIndex == dropList.size()) {
					break;
				}
				content[i][j] = dropList.get(arrIndex).asItem();
				arrIndex++;
			}
		}
		return this.getPageType().flatten(content);
	}

	@Override
	public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
		if (itemStack == null) {
			return;
		}
		if (itemStack.equals(this.getSpecialItem(SpecialItem.EDIT_DROP))) {
			this.traverse(view, View.EDIT_DROP);
			return;
		}
		if (itemStack.equals(this.getSpecialItem(SpecialItem.COPY_DROP))) {
			final LuckyBlockDrop drop = this.plugin.getLuckyBlocks().get(this.getBlockIndex()).getDroppableItems()
					.get(this.getDropIndex());
			this.plugin.getLuckyBlocks().get(this.getBlockIndex()).getDroppableItems().add(drop.ofUniqueCopy());
			this.traverse(view, View.LIST_DROPS);
			return;
		}
		if (itemStack.equals(this.getSpecialItem(SpecialItem.DELETE_DROP))) {
			if (this.plugin.getLuckyBlocks().get(this.getBlockIndex()).getDroppableItems().size() == 1) {
				view.getPlayer().sendMessage(ChatColor.RED + "You must have at least one drop in this lucky block.");
				return;
			}
			this.setConfirmAction(Action.DELETE_DROP);
			this.traverse(view, View.CONFIRM_ACTION);
			return;
		}
		if (itemStack.equals(this.getSpecialItem(SpecialItem.CHANGE_RARITY))) {
			this.traverse(view, View.CHANGE_RARITY);
			return;
		}
		if (itemStack.equals(this.getSpecialItem(SpecialItem.EXIT_BUTTON))) {
			this.traverse(view, View.LIST_DROPS);
			return;
		}

	}

	@Override
	public View identity() {
		return View.LIST_LOOT;
	}

}
