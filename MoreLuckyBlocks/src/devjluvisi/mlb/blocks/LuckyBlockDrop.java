package devjluvisi.mlb.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import devjluvisi.mlb.blocks.drops.LootProperty;
import devjluvisi.mlb.blocks.drops.LuckyBlockCommand;
import devjluvisi.mlb.blocks.drops.LuckyBlockItem;
import devjluvisi.mlb.blocks.drops.LuckyBlockPotionEffect;
import devjluvisi.mlb.util.ConfigManager;

/**
 * A "drop" that can be rewarded by breaking a lucky block.
 * Note that a drop is NOT an individual item. A drop is a group of items the user should get from breaking the
 * lucky block.
 * @author jacob
 *
 */
public class LuckyBlockDrop implements Comparable<LuckyBlockDrop> {
	
	/**
	 * The maximum allowed number of drops a single drop in a lucky block can have.
	 */
	public static final byte MAX_ALLOWED_LOOT = 7 * 2;
	
	private Random rand;
	private long uniqueId;
	
	private ArrayList<LuckyBlockItem> items; // Items to be dropped.
	private ArrayList<LuckyBlockCommand> commands; // Commands to be executed.
	private ArrayList<LuckyBlockPotionEffect> potionEffects; // Potion effects applied.
	
	private float rarity; // Rarity of this drop.
	
	public LuckyBlockDrop() {
		super();
		rand = new Random();
		
		uniqueId = rand.nextLong();
		Bukkit.getServer().getConsoleSender().sendMessage("New Instance created of unique id: "+ uniqueId);
		
		this.rarity = 50.0F; // Default
		items = new ArrayList<LuckyBlockItem>();
		commands = new ArrayList<LuckyBlockCommand>();
		potionEffects = new ArrayList<LuckyBlockPotionEffect>();
	}
	
	public LuckyBlockDrop(ArrayList<LuckyBlockItem> items, ArrayList<LuckyBlockCommand> commands,
			ArrayList<LuckyBlockPotionEffect> potionEffects, float rarity) {
		super();
		rand = new Random();
		uniqueId = rand.nextLong();
		this.items = items;
		this.commands = commands;
		this.potionEffects = potionEffects;
		this.rarity = rarity;
	}

	public ArrayList<LuckyBlockItem> getItems() {
		return items;
	}

	public void setItems(ArrayList<LuckyBlockItem> items) {
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
	
	/**
	 * @return An array list of all of the drops from the luckyblock under the parent "DropProperty" interface.
	 */
	public ArrayList<LootProperty> getLoot() {
		ArrayList<LootProperty> drops = new ArrayList<LootProperty>();
		//TODO: Exclude potions which are still being edited.
		drops.addAll(items);
		drops.addAll(potionEffects);
		drops.addAll(commands);
		return drops;
	}
	
	
	public int indexOf(ItemStack lootAsItem) {
		ArrayList<LootProperty> lootList = getLoot();
		for(int i = 0; i < lootList.size(); i++) {
			if(lootAsItem.equals(lootList.get(i).asItem())) {
				return i;
			}
		}
		return -1;
	}
	
	public void removeLoot(ItemStack lootAsItem) {
		ArrayList<LootProperty> lootList = getLoot();
		LootProperty loot = lootList.get(indexOf(lootAsItem));
			if(loot instanceof LuckyBlockItem) {
				items.remove((LuckyBlockItem)loot);
			}
			if(loot instanceof LuckyBlockPotionEffect) {
				potionEffects.remove((LuckyBlockPotionEffect)loot);
			}
			if(loot instanceof LuckyBlockCommand) {
				commands.remove((LuckyBlockCommand)loot);
			}
	}
	
	
	
	/**
	 * Saves the current drop information to the configuration file.
	 * @param blocksYaml The block configuration file.
	 * @param internalName The internal name of the luckyblock.
	 * @param dropLabel The label that specifies the specific drop, ex (0) or (1).
	 */
	public void saveConfig(ConfigManager blocksYaml, String internalName, String dropLabel) {
		final String path = "lucky-blocks." + internalName + ".drops."+dropLabel;
		blocksYaml.getConfig().set(path + ".rarity", rarity);
		
		// SAVING ITEMS
		for(LuckyBlockItem item : getItems()) {
			blocksYaml.getConfig().set(path + ".items." + item.getItem().getType().name() + ".amount", item.getItem().getAmount());
			blocksYaml.getConfig().set(path + ".items." + item.getItem().getType().name() + ".enchants", item.enchantsConfigString());
			if(item.getItem().getItemMeta().getDisplayName().isEmpty() || item.getItem().getItemMeta().getDisplayName() == null) {
				blocksYaml.getConfig().set(path + ".items." + item.getItem().getType().name() +".display-name", "");
				
			}else {
				blocksYaml.getConfig().set(path + ".items." + item.getItem().getType().name() +".display-name", item.getItem().getItemMeta().getDisplayName().toString());
				
			}
			
			blocksYaml.getConfig().set(path + ".items." + item.getItem().getType().name() +".lore", item.getItem().getItemMeta().getLore());
		}
		
		// SAVING POTIONS
		List<String> potionStringList = new ArrayList<String>();
		
		for(LuckyBlockPotionEffect effect : getPotionEffects()) {
			potionStringList.add(effect.asConfigString());
		}
		blocksYaml.getConfig().set(path + ".potions", potionStringList);
		
		// SAVING COMMANDS
		List<String> commandStringList = new ArrayList<String>();
		for(LuckyBlockCommand cmd : getCommands()) {
			commandStringList.add(cmd.getCommand().toLowerCase());
		}
		blocksYaml.getConfig().set(path + ".commands", commandStringList);
	}

	@Override
	public String toString() {
		return "LuckyBlockDrop [items=" + items + ", commands=" + commands + ", potionEffects=" + potionEffects
				+ ", rarity=" + rarity + "]";
	}

	/**
	 * Sort based on LuckyBlock rarity.
	 */
	@Override
	public int compareTo(LuckyBlockDrop o) {
		if(o.uniqueId > this.uniqueId) {
			return 0;
		}else if(o.uniqueId == this.uniqueId) {
			return 0;
		}else {
			return 0;
		}
	}
	
	

	/**
	 * @return Copy of the current object with a different uniqueId.
	 * 
	 */
	public LuckyBlockDrop ofUniqueCopy() {
		LuckyBlockDrop d = new LuckyBlockDrop();
		d.uniqueId = rand.nextLong();
		d.commands = commands;
		d.items = items;
		d.potionEffects = potionEffects;
		d.rarity = rarity;
		return d;
	}
	
	

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof LuckyBlockDrop)) {
			return false;
		}
		LuckyBlockDrop d = (LuckyBlockDrop)obj;
		if(d.uniqueId == this.uniqueId) {
			return true;
		}
		return false;
	}
	
	
	
	
	
	
	
	
	

}
