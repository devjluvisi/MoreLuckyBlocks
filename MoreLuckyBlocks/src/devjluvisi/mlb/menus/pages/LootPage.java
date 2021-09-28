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
		setMenuName("Viewing loot for: " + menu.getDropIndex());
	}

	@Override
	public ItemStack[] getContent() {
		ItemStack[][] content = getPageType().getBlank2DArray();

		ItemStack dropViewInfo = new ItemStack(Material.OAK_SIGN);
		ItemMeta dropViewMeta = dropViewInfo.getItemMeta();
		dropViewMeta.setDisplayName(ChatColor.GRAY + "Viewing Drop " + ChatColor.DARK_GRAY + "-> " + ChatColor.DARK_AQUA
				+ (getDropIndex()));
		dropViewMeta
				.setLore(
						Arrays.asList(ChatColor.GRAY + "You are currently viewing all of the loot",
								ChatColor.GRAY + "that can drop from this drop.",
								ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Rarity: " + ChatColor.BLUE
										+ plugin.getLuckyBlocks().get(getBlockIndex()).getDroppableItems()
												.get(getDropIndex()).getRarity(),
								ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "# of Items: " + ChatColor.BLUE
										+ plugin.getLuckyBlocks().get(getBlockIndex()).getDroppableItems()
												.get(getDropIndex()).getItems().size(),
								ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "# of Potions: " + ChatColor.BLUE
										+ plugin.getLuckyBlocks().get(getBlockIndex()).getDroppableItems()
												.get(getDropIndex()).getPotionEffects().size(),
								ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "# of Commands: " + ChatColor.BLUE
										+ plugin.getLuckyBlocks().get(getBlockIndex()).getDroppableItems()
												.get(getDropIndex()).getCommands().size(),
								ChatColor.GOLD + "Click the Book & Quill to edit the drop."));
		dropViewInfo.setItemMeta(dropViewMeta);

		content[0][0] = dropViewInfo;
		content[0][1] = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		content[1][0] = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		content[1][1] = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		for (int i = 0; i < 4; i++)
			content[2][i] = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		content[2][4] = super.getSpecialItem(SpecialItem.EDIT_DROP);
		content[2][5] = super.getSpecialItem(SpecialItem.CHANGE_RARITY);
		content[2][6] = super.getSpecialItem(SpecialItem.COPY_DROP);
		content[2][7] = super.getSpecialItem(SpecialItem.DELETE_DROP);
		content[2][8] = super.getSpecialItem(SpecialItem.EXIT_BUTTON);

		ArrayList<LootProperty> dropList = plugin.getLuckyBlocks().get(getBlockIndex()).getDroppableItems()
				.get(getDropIndex()).getLoot();

		int arrIndex = 0;

		for (int i = 0; i < 2; i++) {
			for (int j = 2; j < 9; j++) {
				if (arrIndex == dropList.size())
					break;
				content[i][j] = dropList.get(arrIndex).asItem();
				arrIndex++;
			}
		}
		return getPageType().flatten(content);
	}

	@Override
	public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
		if (itemStack == null)
			return;
		if (itemStack.equals(getSpecialItem(SpecialItem.EDIT_DROP))) {
			traverse(view, View.EDIT_DROP);
			return;
		}
		if (itemStack.equals(getSpecialItem(SpecialItem.COPY_DROP))) {
			final LuckyBlockDrop drop = plugin.getLuckyBlocks().get(getBlockIndex()).getDroppableItems()
					.get(getDropIndex());
			plugin.getLuckyBlocks().get(getBlockIndex()).getDroppableItems().add(drop.ofUniqueCopy());
			traverse(view, View.LIST_DROPS);
			return;
		}
		if (itemStack.equals(getSpecialItem(SpecialItem.DELETE_DROP))) {
			if (plugin.getLuckyBlocks().get(getBlockIndex()).getDroppableItems().size() == 1) {
				view.getPlayer().sendMessage(ChatColor.RED + "You must have at least one drop in this lucky block.");
				return;
			}
			setConfirmAction(Action.DELETE_DROP);
			traverse(view, View.CONFIRM_ACTION);
			return;
		}
		if (itemStack.equals(getSpecialItem(SpecialItem.CHANGE_RARITY))) {
			traverse(view, View.CHANGE_RARITY);
			return;
		}
		if (itemStack.equals(getSpecialItem(SpecialItem.EXIT_BUTTON))) {
			traverse(view, View.LIST_DROPS);
			return;
		}

	}

	@Override
	public View identity() {
		return View.LIST_LOOT;
	}

}
