package devjluvisi.mlb.helper;

import java.util.ArrayList;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import devjluvisi.mlb.blocks.drops.DropProperty;
import devjluvisi.mlb.blocks.drops.LuckyBlockCommand;
import devjluvisi.mlb.blocks.drops.LuckyBlockItem;
import devjluvisi.mlb.blocks.drops.LuckyBlockPotionEffect;
import devjluvisi.mlb.util.ConfigManager;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Static methods to help with configuration and lucky blocks.
 * @author jacob
 *
 */
public final class LuckyBlockHelper {
	
	public static Set<String> getLuckyBlockNames(ConfigManager blocksYaml) {
		return blocksYaml.getConfig().getConfigurationSection("lucky-blocks").getKeys(false);
	}
	
	public static boolean doesExist(ConfigManager blocksYaml, String internalName) {
		return blocksYaml.getConfig().get("lucky-blocks." + internalName) != null;
	}
	
	/**
	 * Converts an item from the config into an ItemStack.
	 * @param blocksYaml The config file.
	 * @param path The path up until the item.
	 * @param item The name of the item.
	 * @return The item stack made from the config path.
	 */
	public static ItemStack getItem(ConfigManager blocksYaml, String path, String item) {
		ItemStack itemObject = null;
		final String accessor = path + "." + item;
		
			itemObject = new ItemStack(Material.getMaterial(item));
			
			for(String itemKeyValues : blocksYaml.getConfig().getConfigurationSection(accessor).getKeys(false)) {
				if(itemKeyValues.equalsIgnoreCase("amount")) {
					itemObject.setAmount(blocksYaml.getConfig().getInt(accessor + "." + "amount"));
				}
				if(itemKeyValues.equalsIgnoreCase("enchants")) {
					String enchants = blocksYaml.getConfig().getString(accessor + ".enchants");
					enchants = enchants.replace("[", "").replace("]", "");
					
					String[] splitter = enchants.split(",");
					for(String s : splitter) {
						String[] enc = s.split(":");
						itemObject.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.getByKey(NamespacedKey.minecraft(enc[0].toLowerCase())), Integer.parseInt(enc[1]));
					}
				}
				if(itemKeyValues.equalsIgnoreCase("display-name")) {
					ItemMeta meta = itemObject.getItemMeta();
					meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', blocksYaml.getConfig().getString(accessor + ".display-name")));
					itemObject.setItemMeta(meta);
				}
				if(itemKeyValues.equalsIgnoreCase("lore")) {
					ItemMeta meta = itemObject.getItemMeta();
					ArrayList<String> lore = new ArrayList<String>();
					blocksYaml.getConfig().getStringList(accessor + "." + "lore").forEach(s -> lore.add(ChatColor.translateAlternateColorCodes('&', s)));
					meta.setLore(lore);
					itemObject.setItemMeta(meta);
				}
			}
		
		return itemObject;
	}

	/**
	 * Returns a lucky block object provided an internalName is specified.
	 * @param blocksYaml Config "blocks.yml" file.
	 * @param internalName Internal name of the lucky block as specified in config.
	 * @return LuckyBlock if it exists.
	 */
	public static LuckyBlock getLuckyBlock(ConfigManager blocksYaml, String internalName) {
			LuckyBlock block = new LuckyBlock();
			block.setInternalName(internalName);
			block.setName(ChatColor.translateAlternateColorCodes('&', blocksYaml.getConfig().getString("lucky-blocks." + internalName + ".item-name")));
			block.setBlockLocation(null);
			block.setBlockMaterial(Material.getMaterial(blocksYaml.getConfig().getString("lucky-blocks." + internalName + ".block")));
			block.setBreakPermission(blocksYaml.getConfig().getString("lucky-blocks." + internalName + ".permission"));
			block.setDefaultBlockLuck((float)blocksYaml.getConfig().getDouble("lucky-blocks." + internalName + ".default-luck"));
			block.setBlockLuck(block.getDefaultBlockLuck());
			block.setLore(blocksYaml.getConfig().getStringList("lucky-blocks." + internalName + ".item-lore"));
			ArrayList<LuckyBlockDrop> drops = new ArrayList<LuckyBlockDrop>();
			
			// Setup dropped items.
			
			final String itemDropKey = "lucky-blocks." + internalName + ".drops";
			
			for(String key : blocksYaml.getConfig().getConfigurationSection(itemDropKey).getKeys(false)) {
				
				LuckyBlockDrop drop = new LuckyBlockDrop();
				drop.setRarity((float) blocksYaml.getConfig().getDouble("lucky-blocks." + internalName + ".drops." + key + ".rarity"));
				
				// First get all of the items, commands, and potion effects.
				ArrayList<LuckyBlockItem> items = new ArrayList<LuckyBlockItem>();
				ArrayList<LuckyBlockCommand> commands = new ArrayList<LuckyBlockCommand>();
				ArrayList<LuckyBlockPotionEffect> potionEffects = new ArrayList<LuckyBlockPotionEffect>();
				

				for(String s : blocksYaml.getConfig().getConfigurationSection("lucky-blocks." + internalName + ".drops." + key + ".items").getKeys(false)) {
					items.add(new LuckyBlockItem(getItem(blocksYaml, "lucky-blocks." + internalName + ".drops." + key + ".items", s)));
				}
				
				for(String s : blocksYaml.getConfig().getStringList("lucky-blocks." + internalName + ".drops." + key + ".commands")) {
					commands.add(new LuckyBlockCommand(s));
				}
				for(String s : blocksYaml.getConfig().getStringList("lucky-blocks." + internalName + ".drops." + key + ".potions")) {
					potionEffects.add(LuckyBlockPotionEffect.parseFromFile(s));
					
				}
				
				drop.setCommands(commands);
				drop.setItems(items);
				drop.setPotionEffects(potionEffects);
				drops.add(drop);
			}
			
			block.setDroppableItems(new ArrayList<LuckyBlockDrop>(drops));
			return block;
	}
	
	/**
	 * Ensures that the blocks.yml file is valid.
	 * @return If there are any errors in the blocks.yml file.
	 */
	public static boolean validateBlocksYaml(ArrayList<LuckyBlock> arr) {
		for(LuckyBlock block : arr) {
			
			if(block.getInternalName().contains(" ")) return false;
			if(block.getBlockMaterial() == null || !block.getBlockMaterial().isBlock()) return false;
			if(block.getDroppableItems().size() < 1) return false;
			
			for(LuckyBlockDrop drop : block.getDroppableItems()) {
				if(drop.getAllDrops().size() > LuckyBlockDrop.MAX_ALLOWED_LOOT || drop.getAllDrops().size() == 0) {
					return false;
				}
				for(DropProperty loot : drop.getAllDrops()) {
					if(!loot.isValid()) {
						return false;
					}
				}
			}
			
		}
		
		return true;
		
	}
	
	
	public static ArrayList<LuckyBlock> getLuckyBlocks(ConfigManager blocksYaml) {
		ArrayList<LuckyBlock> luckyBlocks = new ArrayList<LuckyBlock>();
		for(String key : getLuckyBlockNames(blocksYaml)) {
			luckyBlocks.add(getLuckyBlock(blocksYaml, key));
		}
		return luckyBlocks;
	}
	

}
