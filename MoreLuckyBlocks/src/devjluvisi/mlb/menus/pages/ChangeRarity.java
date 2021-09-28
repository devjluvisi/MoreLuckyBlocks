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
		this.setMenuName("Change Rarity of Drop #" + this.getDropIndex());
		if (this.plugin.getLuckyBlocks().get(this.getBlockIndex()).getDroppableItems().size() <= this.getDropIndex()) {
			this.rarity = 50.0F; // default
			return;
		}
		this.rarity = this.plugin.getLuckyBlocks().get(this.getBlockIndex()).getDroppableItems()
				.get(this.getDropIndex()).getRarity();
	}

	@Override
	public ItemStack[] getContent() {
		final ItemStack[][] content = this.getPageType().getBlank2DArray();
		final ItemStack glassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);

		for (int i = 0; i < 3; i++) {
			Arrays.fill(content[i], glassPane);
		}
		content[1][1] = this.getSpecialItem(SpecialItem.INCREASE_RARITY);
		content[1][7] = this.getSpecialItem(SpecialItem.DECREASE_RARITY);
		content[1][4] = this.getPlaceholderItem(Material.EXPERIENCE_BOTTLE, ChatColor.YELLOW + "Save Rarity",
				Arrays.asList(ChatColor.GRAY + "Update the rarity of this", ChatColor.GRAY + "drop to " + ChatColor.GOLD
						+ String.valueOf(this.rarity) + ChatColor.GRAY + "."));

		return this.getPageType().flatten(content);
	}

	@Override
	public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
		if ((itemStack == null) || itemStack.getType().isAir()
				|| (itemStack.getType() == Material.GRAY_STAINED_GLASS_PANE)) {
			return;
		}

		if (itemStack.equals(this.getSpecialItem(SpecialItem.INCREASE_RARITY))) {
			if (this.rarity <= RARITY_STEP_INTERVAL) {
				this.rarity = 0.1F;
			} else {
				this.rarity -= RARITY_STEP_INTERVAL;
			}

			view.reopen();
			return;
		}
		if (itemStack.equals(this.getSpecialItem(SpecialItem.DECREASE_RARITY))) {
			if (this.rarity >= 95.0F) {
				this.rarity = 100.0F;
			} else {
				this.rarity += RARITY_STEP_INTERVAL;
			}

			view.reopen();
			return;
		}
		this.plugin.getLuckyBlocks().get(this.getBlockIndex()).getDroppableItems().get(this.getDropIndex())
				.setRarity(this.rarity);
		// User clicked on the "save" option
		this.traverse(view, View.LIST_LOOT);

	}

	@Override
	public View identity() {
		return View.CHANGE_RARITY;
	}

}
