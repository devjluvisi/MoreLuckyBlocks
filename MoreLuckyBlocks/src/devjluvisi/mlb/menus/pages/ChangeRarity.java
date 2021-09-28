package devjluvisi.mlb.menus.pages;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.menus.BasePage;
import devjluvisi.mlb.menus.LuckyMenu;
import devjluvisi.mlb.menus.LuckyMenu.View;
import net.md_5.bungee.api.ChatColor;

public class ChangeRarity extends BasePage {

	private static final float RARITY_STEP_INTERVAL = 5.0F;
	float rarity;

	public ChangeRarity(LuckyMenu menu) {
		super(menu);
		setMenuName("Change Rarity of Drop #" + getDropIndex());
		if (plugin.getLuckyBlocks().get(getBlockIndex()).getDroppableItems().size() <= getDropIndex()) {
			this.rarity = 50.0F; // default
			return;
		}
		this.rarity = plugin.getLuckyBlocks().get(getBlockIndex()).getDroppableItems().get(getDropIndex()).getRarity();
	}

	@Override
	public ItemStack[] getContent() {
		ItemStack[][] content = getPageType().getBlank2DArray();
		final ItemStack glassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);

		for (int i = 0; i < 3; i++) {
			Arrays.fill(content[i], glassPane);
		}
		content[1][1] = getSpecialItem(SpecialItem.INCREASE_RARITY);
		content[1][7] = getSpecialItem(SpecialItem.DECREASE_RARITY);
		content[1][4] = getPlaceholderItem(Material.EXPERIENCE_BOTTLE, ChatColor.YELLOW + "Save Rarity",
				Arrays.asList(ChatColor.GRAY + "Update the rarity of this",
						ChatColor.GRAY + "drop to " + ChatColor.GOLD + String.valueOf(rarity) + ChatColor.GRAY + "."));

		return getPageType().flatten(content);
	}

	@Override
	public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
		if (itemStack == null || itemStack.getType().isAir() || itemStack.getType() == Material.GRAY_STAINED_GLASS_PANE)
			return;

		if (itemStack.equals(getSpecialItem(SpecialItem.INCREASE_RARITY))) {
			if (rarity <= RARITY_STEP_INTERVAL) {
				rarity = 0.1F;
			} else {
				rarity -= RARITY_STEP_INTERVAL;
			}

			view.reopen();
			return;
		}
		if (itemStack.equals(getSpecialItem(SpecialItem.DECREASE_RARITY))) {
			if (rarity >= 95.0F) {
				rarity = 100.0F;
			} else {
				rarity += RARITY_STEP_INTERVAL;
			}

			view.reopen();
			return;
		}
		plugin.getLuckyBlocks().get(getBlockIndex()).getDroppableItems().get(getDropIndex()).setRarity(rarity);
		// User clicked on the "save" option
		traverse(view, View.LIST_LOOT);

	}

	@Override
	public View identity() {
		return View.CHANGE_RARITY;
	}

}
