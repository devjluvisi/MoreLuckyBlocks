package devjluvisi.mlb.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

/**
 * Represents a single lucky block with any range of values.
 * @author jacob
 *
 */
public class LuckyBlock {
	
	// Public Fields (Represent ALL blocks of this type)
	private String internalName;
	private String name;
	private String breakPermission;
	private Material blockMaterial;
	private List<String> lore;
	private float defaultBlockLuck;
	private ArrayList<LuckyBlockDrop> droppableItems;
	
	// Per-Item Fields (For individual lucky blocks)
	private Location blockLocation;
	private float blockLuck;
	
	public LuckyBlock() {
		super();
		this.blockLocation = null;
		this.blockLuck = 0.0F;
	}
	
	public LuckyBlock(String internalName, String name, String breakPermission, Material blockMaterial,
			List<String> lore, float defaultBlockLuck, ArrayList<LuckyBlockDrop> droppableItems, Location blockLocation) {
		super();
		this.internalName = internalName;
		this.name = name;
		this.breakPermission = breakPermission;
		this.blockMaterial = blockMaterial;
		this.lore = lore;
		this.defaultBlockLuck = defaultBlockLuck;
		this.droppableItems = droppableItems;
		this.blockLocation = blockLocation;
		this.blockLuck = defaultBlockLuck;
	}
	
	public ItemStack asItem(int amount) {
		if(amount <= 0) {
			amount = 1;
		}
		ItemStack luckyBlock = new ItemStack(this.blockMaterial, amount);
		ItemMeta meta = luckyBlock.getItemMeta();
		meta.setDisplayName(this.name);
		meta.setLore(getRefreshedLore());
		luckyBlock.setItemMeta(meta);
		return luckyBlock;
		
	}

	public String getInternalName() {
		return internalName;
	}

	public void setInternalName(String internalName) {
		this.internalName = internalName.toLowerCase();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBreakPermission() {
		return breakPermission;
	}

	public void setBreakPermission(String breakPermission) {
		this.breakPermission = breakPermission;
	}

	public Material getBlockMaterial() {
		return blockMaterial;
	}

	public void setBlockMaterial(Material blockMaterial) {
		this.blockMaterial = blockMaterial;
	}

	public List<String> getLore() {
		return lore;
	}
	
	public List<String> getRefreshedLore() {
		ArrayList<String> copy = new ArrayList<String>();
		for(String s : lore) {
			copy.add(ChatColor.translateAlternateColorCodes('&', 
					s
					.replaceAll("%luck%", ""+this.blockLuck)
					.replaceAll("%default_luck%", ""+this.defaultBlockLuck)
					.replaceAll("%break_perm%", this.breakPermission)
					.replaceAll("%internal_name%", this.internalName)
					));
			
		}
		return copy;
	}

	public void setLore(List<String> lore) {
		this.lore = lore;
	}

	public float getDefaultBlockLuck() {
		return defaultBlockLuck;
	}

	public void setDefaultBlockLuck(float defaultBlockLuck) {
		if(defaultBlockLuck > 100) {
			this.defaultBlockLuck = 100;
			return;
		}
		if(defaultBlockLuck < -100) {
			this.defaultBlockLuck = -100;
			return;
		}
		this.defaultBlockLuck = defaultBlockLuck;
	}

	public ArrayList<LuckyBlockDrop> getDroppableItems() {
		return droppableItems;
	}

	public void setDroppableItems(ArrayList<LuckyBlockDrop> arrayList) {
		this.droppableItems = arrayList;
	}

	public Location getBlockLocation() {
		return blockLocation;
	}

	public void setBlockLocation(Location blockLocation) {
		this.blockLocation = blockLocation;
	}
	
	
	public float getBlockLuck() {
		return blockLuck;
	}

	public void setBlockLuck(float blockLuck) {
		if(blockLuck > 100) {
			this.blockLuck = 100;
			return;
		}
		if(blockLuck < -100) {
			this.blockLuck = -100;
			return;
		}
		this.blockLuck = blockLuck;
	}
	
	public String getDroppableItemsAsString() {
		return "";
	}
	
	public void addDrop(LuckyBlockDrop drop) {
		this.droppableItems.add(drop);
	}
	
	public void removeDrop(LuckyBlockDrop drop) {
		this.droppableItems.remove(drop);
	}

	@Override
	public String toString() {
		return "LuckyBlock [internalName=" + internalName + ", name=" + name + ", breakPermission=" + breakPermission
				+ ", blockMaterial=" + blockMaterial + ", lore=" + lore + ", defaultBlockLuck=" + defaultBlockLuck
				+ ", droppableItems=" + droppableItems + ", blockLocation=" + blockLocation + ", blockLuck=" + blockLuck
				+ "]";
	}

	
	
	
	
}
