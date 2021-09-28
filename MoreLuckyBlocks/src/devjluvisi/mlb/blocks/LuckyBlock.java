package devjluvisi.mlb.blocks;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.PluginConstants;
import devjluvisi.mlb.api.items.CustomItemMeta;
import devjluvisi.mlb.helper.Util;
import devjluvisi.mlb.util.config.ConfigManager;
import net.md_5.bungee.api.ChatColor;

/**
 * Represents a single lucky block with any range of values.
 *
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
	private LinkedList<LuckyBlockDrop> droppableItems;

	// Per-Item Fields (For individual lucky blocks)
	private Location blockLocation;
	private float blockLuck;

	public LuckyBlock() {
		super();
		this.internalName = StringUtils.EMPTY;
		this.name = StringUtils.EMPTY;
		this.breakPermission = StringUtils.EMPTY;
		this.blockMaterial = Material.AIR;
		this.lore = new ArrayList<>();
		this.defaultBlockLuck = PluginConstants.DEFAULT_BLOCK_LUCK;
		this.droppableItems = new LinkedList<>();
		this.blockLocation = null;
		this.blockLuck = PluginConstants.DEFAULT_BLOCK_LUCK;
	}

	public LuckyBlock(String internalName, String name, String breakPermission, Material blockMaterial,
			List<String> lore, float defaultBlockLuck, LinkedList<LuckyBlockDrop> droppableItems,
			Location blockLocation) {
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

	public ItemStack asItem(MoreLuckyBlocks plugin, float luck, int amount) {
		if (amount <= 0) {
			amount = 1;
		}
		final ItemStack luckyBlock = new ItemStack(this.blockMaterial, amount);
		final ItemMeta meta = luckyBlock.getItemMeta();
		final CustomItemMeta specialMeta = plugin.getMetaFactory().createCustomMeta(meta);

		this.setBlockLuck(luck);
		specialMeta.setString(PluginConstants.LuckyIdentifier, this.internalName);
		specialMeta.setFloat(PluginConstants.BlockLuckIdentifier, luck);
		specialMeta.updateMeta(PluginConstants.LuckyIdentifier);

		meta.setDisplayName(this.name);
		meta.setLore(this.getRefreshedLore());
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

		luckyBlock.setItemMeta(meta);
		return luckyBlock;

	}

	public ItemStack asItem(MoreLuckyBlocks plugin, int amount) {
		return this.asItem(plugin, this.defaultBlockLuck, amount);
	}

	public String getInternalName() {
		return this.internalName;
	}

	public void setInternalName(String internalName) {
		this.internalName = internalName.toLowerCase();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBreakPermission() {
		return this.breakPermission;
	}

	public void setBreakPermission(String breakPermission) {
		this.breakPermission = breakPermission;
	}

	public Material getBlockMaterial() {
		return this.blockMaterial;
	}

	public void setBlockMaterial(Material blockMaterial) {
		this.blockMaterial = blockMaterial;
	}

	public List<String> getLore() {
		return this.lore;
	}

	public List<String> getRefreshedLore() {
		final ArrayList<String> copy = new ArrayList<>();
		for (final String s : this.lore) {
			copy.add(ChatColor.translateAlternateColorCodes('&',
					s.replaceAll("%luck%", "" + this.blockLuck).replaceAll("%default_luck%", "" + this.defaultBlockLuck)
							.replaceAll("%break_perm%", this.breakPermission)
							.replaceAll("%internal_name%", this.internalName)));

		}
		return copy;
	}

	public void setLore(List<String> lore) {
		this.lore = lore;
	}

	public float getDefaultBlockLuck() {
		return this.defaultBlockLuck;
	}

	public void setDefaultBlockLuck(float defaultBlockLuck) {
		if (defaultBlockLuck > 100) {
			this.defaultBlockLuck = 100;
			return;
		}
		if (defaultBlockLuck < -100) {
			this.defaultBlockLuck = -100;
			return;
		}
		this.defaultBlockLuck = defaultBlockLuck;
	}

	public LinkedList<LuckyBlockDrop> getDroppableItems() {
		return this.droppableItems;
	}

	public void setDroppableItems(LinkedList<LuckyBlockDrop> arrayList) {
		this.droppableItems = arrayList;
	}

	public Location getBlockLocation() {
		return this.blockLocation;
	}

	public void setBlockLocation(Location blockLocation) {
		this.blockLocation = blockLocation;
	}

	public float getBlockLuck() {
		return this.blockLuck;
	}

	public void setBlockLuck(float blockLuck) {
		if (blockLuck > 100) {
			this.blockLuck = 100;
			return;
		}
		if (blockLuck < -100) {
			this.blockLuck = -100;
			return;
		}
		this.blockLuck = blockLuck;
	}

	/**
	 * Saves a lucky block and all of its drops.
	 *
	 * @param blocksYaml The config file to save at.
	 */
	public void saveConfig(ConfigManager blocksYaml) {
		final String path = "lucky-blocks." + this.internalName;
		blocksYaml.getConfig().set(path, null);
		blocksYaml.getConfig().set(path + ".item-name", Util.asNormalColoredString(this.name));
		blocksYaml.getConfig().set(path + ".block", this.blockMaterial.name());
		blocksYaml.getConfig().set(path + ".item-lore", Util.asNormalColoredString(this.lore));
		blocksYaml.getConfig().set(path + ".permission", this.breakPermission);
		blocksYaml.getConfig().set(path + ".default-luck", this.defaultBlockLuck);

		int index = 0;
		for (final LuckyBlockDrop drop : this.droppableItems) {
			drop.saveConfig(blocksYaml, this.internalName, String.valueOf(index));
			index++;
		}
		blocksYaml.save();
		blocksYaml.reload();
	}

	public void addDrop(LuckyBlockDrop drop) {
		this.droppableItems.add(drop);
	}

	public void removeDrop(LuckyBlockDrop drop) {
		this.droppableItems.remove(drop);
	}

	public int indexOf(LuckyBlockDrop drop) {
		for (int i = 0; i < this.droppableItems.size(); i++) {
			if (this.droppableItems.get(i).equals(drop)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LuckyBlock)) {
			return false;
		}
		return this.hashCode() == ((LuckyBlock) obj).hashCode();
	}

	@Override
	public int hashCode() {
		// There will never be another lucky block with the same internal name.
		return this.internalName.hashCode();
		// return this.toString().hashCode();
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("LuckyBlock [internalName=");
		builder.append(this.internalName);
		builder.append(", name=");
		builder.append(this.name);
		builder.append(", breakPermission=");
		builder.append(this.breakPermission);
		builder.append(", blockMaterial=");
		builder.append(this.blockMaterial);
		builder.append(", lore=");
		builder.append(this.lore);
		builder.append(", defaultBlockLuck=");
		builder.append(this.defaultBlockLuck);
		builder.append(", droppableItems=");
		builder.append(this.droppableItems);
		builder.append(", blockLocation=");
		builder.append(this.blockLocation);
		builder.append(", blockLuck=");
		builder.append(this.blockLuck);
		builder.append("]");
		return builder.toString();
	}
}
