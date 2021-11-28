package devjluvisi.mlb.api.gui.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemCreator {

    private final ItemStack item;

    public ItemCreator(Material material) {
        this.item = new ItemStack(material);
    }

    public ItemStack getItem() {
        return this.item;
    }

    public String getName() {
        return this.item.getItemMeta().getDisplayName();
    }

    public ItemCreator setName(String name) {
        final ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(name);
        this.item.setItemMeta(meta);
        return this;
    }

}