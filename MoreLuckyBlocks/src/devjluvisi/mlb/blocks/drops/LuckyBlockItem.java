package devjluvisi.mlb.blocks.drops;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class LuckyBlockItem implements DropProperty {
	
	private ItemStack item;

	public LuckyBlockItem(ItemStack item) {
		super();
		this.item = item;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}
	
	public String enchantsConfigString() {
		String str = "[";
		for(Enchantment e : item.getEnchantments().keySet()) {
			str += e.getKey().getKey().toString().toUpperCase() + ":" + item.getEnchantments().get(e) + ",";
		}
		// Remove trailing comma.
		str = str.substring(0, str.length()-1);
		str += "]";
		return str;
	}
	
	
	@Override
	public String toString() {
		return "LuckyBlockItem [item=" + item + "]";
	}

	@Override
	public ItemStack asItem() {
		return item;
	}

	@Override
	public boolean isValid() {
		if(item == null) return false;
		if(item.getAmount() < 1) return false;
		if(item.getType() == null) return false;
		return true;
	}
	
	

}
