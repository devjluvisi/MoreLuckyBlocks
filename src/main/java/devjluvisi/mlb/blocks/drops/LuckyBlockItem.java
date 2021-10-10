package devjluvisi.mlb.blocks.drops;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class LuckyBlockItem implements LootProperty {

    private ItemStack item;

    public LuckyBlockItem(ItemStack item) {
        super();
        this.item = item.clone();
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void setItem(ItemStack item) {
        this.item = item.clone();
    }

    public String enchantsConfigString() {
        if (this.item.getEnchantments().size() == 0) {
            return "";
        }
        StringBuilder str = new StringBuilder("[");
        for (final Enchantment e : this.item.getEnchantments().keySet()) {
            str.append(e.getKey().getKey().toUpperCase()).append(":").append(this.item.getEnchantments().get(e)).append(",");
        }
        // Remove trailing comma.
        str = new StringBuilder(str.substring(0, str.length() - 1));
        str.append("]");
        return str.toString();
    }

    @Override
    public String toString() {
        return "LuckyBlockItem [item=" + this.item + "]";
    }

    @Override
    public ItemStack asItem() {
        return this.item;
    }

    @Override
    public boolean isValid() {
        return (this.item != null) && (this.item.getAmount() >= 1);
    }

    @Override
    public boolean equals(Object obj) {
        return this.item.equals(((LuckyBlockItem) obj).item);
    }

}
