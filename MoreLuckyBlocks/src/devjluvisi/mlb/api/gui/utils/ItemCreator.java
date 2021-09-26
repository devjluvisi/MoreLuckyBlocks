package devjluvisi.mlb.api.gui.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemCreator {

	private ItemStack item;

	public ItemCreator(Material material) {
		item = new ItemStack(material);
	}

	public ItemStack getItem() {
		return item;
	}

	public String getName() {
		return item.getItemMeta().getDisplayName();
	}

	public ItemCreator setName(String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return this;
	}
}