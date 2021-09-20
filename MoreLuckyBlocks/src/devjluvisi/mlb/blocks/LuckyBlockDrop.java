package devjluvisi.mlb.blocks;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

/**
 * An item which can be dropped from the block.
 * @author jacob
 *
 */
public class LuckyBlockDrop implements Comparable<LuckyBlockDrop> {
	
	private ArrayList<ItemStack> items; // Items to be dropped.
	private ArrayList<LuckyBlockCommand> commands; // Commands to be executed.
	private ArrayList<LuckyBlockPotionEffect> potionEffects; // Potion effects applied.
	private float rarity; // Rarity of this drop.
	
	public LuckyBlockDrop() {
		super();
	}
	
	public LuckyBlockDrop(ArrayList<ItemStack> items, ArrayList<LuckyBlockCommand> commands,
			ArrayList<LuckyBlockPotionEffect> potionEffects, float rarity) {
		super();
		this.items = items;
		this.commands = commands;
		this.potionEffects = potionEffects;
		this.rarity = rarity;
	}

	public ArrayList<ItemStack> getItems() {
		return items;
	}

	public void setItems(ArrayList<ItemStack> items) {
		this.items = items;
	}

	public ArrayList<LuckyBlockCommand> getCommands() {
		return commands;
	}

	public void setCommands(ArrayList<LuckyBlockCommand> commands) {
		this.commands = commands;
	}

	public ArrayList<LuckyBlockPotionEffect> getPotionEffects() {
		return potionEffects;
	}

	public void setPotionEffects(ArrayList<LuckyBlockPotionEffect> potionEffects) {
		this.potionEffects = potionEffects;
	}

	public float getRarity() {
		return rarity;
	}

	public void setRarity(float rarity) {
		this.rarity = rarity;
	}

	@Override
	public String toString() {
		return "LuckyBlockDrop [items=" + items + ", commands=" + commands + ", potionEffects=" + potionEffects
				+ ", rarity=" + rarity + "]";
	}

	@Override
	public int compareTo(LuckyBlockDrop o) {
		if(o.rarity > this.rarity) {
			return -1;
		}else if(o.rarity == this.rarity) {
			return 0;
		}else {
			return 1;
		}
	}
	
	
	
	
	
	
	

}
