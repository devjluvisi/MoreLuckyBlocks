package devjluvisi.mlb.helper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import devjluvisi.mlb.PluginConstants;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import devjluvisi.mlb.blocks.drops.LootProperty;
import devjluvisi.mlb.blocks.drops.LuckyBlockCommand;
import devjluvisi.mlb.blocks.drops.LuckyBlockItem;
import devjluvisi.mlb.blocks.drops.LuckyBlockPotionEffect;
import devjluvisi.mlb.util.config.ConfigManager;
import net.md_5.bungee.api.ChatColor;

/**
 * Static methods to help with configuration and lucky blocks.
 *
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
	 *
	 * @param blocksYaml The config file.
	 * @param path       The path up until the item.
	 * @param item       The name of the item.
	 * @return The item stack made from the config path.
	 */
	public static ItemStack getItem(ConfigManager blocksYaml, String path) {
		final ItemStack itemObject = new ItemStack(Material.AIR);

		final String accessor = path;

		for (final String itemKeyValues : blocksYaml.getConfig().getConfigurationSection(accessor).getKeys(false)) {
			if (itemKeyValues.equalsIgnoreCase("type")) {
				itemObject.setType(Material.getMaterial(blocksYaml.getConfig().getString(accessor + "." + "type")));
			}
			if (itemKeyValues.equalsIgnoreCase("amount")) {
				itemObject.setAmount(blocksYaml.getConfig().getInt(accessor + "." + "amount"));
			}
			if (itemKeyValues.equalsIgnoreCase("enchants")) {
				String enchants = blocksYaml.getConfig().getString(accessor + ".enchants");
				if ((enchants != null) && !enchants.isEmpty()) {
					enchants = enchants.replace("[", "").replace("]", "");

					final String[] splitter = enchants.split(",");
					for (final String s : splitter) {
						final String[] enc = s.split(":");
						itemObject.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment
								.getByKey(NamespacedKey.minecraft(enc[0].toLowerCase())), Integer.parseInt(enc[1]));
					}
				}
			}

			if (itemKeyValues.equalsIgnoreCase("display-name")
					&& ((blocksYaml.getConfig().getString(accessor + ".display-name") != null)
							&& !blocksYaml.getConfig().getString(accessor + ".display-name").isEmpty())) {
				final ItemMeta meta = itemObject.getItemMeta();
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
						blocksYaml.getConfig().getString(accessor + ".display-name")));
				itemObject.setItemMeta(meta);
			}
			if (itemKeyValues.equalsIgnoreCase("lore")) {
				final ItemMeta meta = itemObject.getItemMeta();
				final ArrayList<String> lore = new ArrayList<>();
				blocksYaml.getConfig().getStringList(accessor + "." + "lore")
						.forEach(s -> lore.add(ChatColor.translateAlternateColorCodes('&', s)));
				meta.setLore(lore);
				itemObject.setItemMeta(meta);
			}
		}

		return itemObject;
	}

	/**
	 * Returns a lucky block object provided an internalName is specified.
	 *
	 * @param blocksYaml   Config "blocks.yml" file.
	 * @param internalName Internal name of the lucky block as specified in config.
	 * @return LuckyBlock if it exists.
	 */
	public static LuckyBlock getLuckyBlock(ConfigManager blocksYaml, String internalName) {
		final LuckyBlock block = new LuckyBlock();
		block.setInternalName(internalName);
		block.setName(ChatColor.translateAlternateColorCodes('&',
				blocksYaml.getConfig().getString("lucky-blocks." + internalName + ".item-name")));
		block.setBlockLocation(null);
		block.setBlockMaterial(
				Material.getMaterial(blocksYaml.getConfig().getString("lucky-blocks." + internalName + ".block")));
		block.setBreakPermission(blocksYaml.getConfig().getString("lucky-blocks." + internalName + ".permission"));
		block.setDefaultBlockLuck(
				(float) blocksYaml.getConfig().getDouble("lucky-blocks." + internalName + ".default-luck"));
		block.setBlockLuck(block.getDefaultBlockLuck());
		block.setLore(blocksYaml.getConfig().getStringList("lucky-blocks." + internalName + ".item-lore"));
		final ArrayList<LuckyBlockDrop> drops = new ArrayList<>();

		// Setup dropped items.

		final String itemDropKey = "lucky-blocks." + internalName + ".drops";
		if (blocksYaml.getConfig().getConfigurationSection(itemDropKey) == null) {
			return block;
		}
		for (final String key : blocksYaml.getConfig().getConfigurationSection(itemDropKey).getKeys(false)) {

			final LuckyBlockDrop drop = new LuckyBlockDrop();
			drop.setRarity((float) blocksYaml.getConfig()
					.getDouble("lucky-blocks." + internalName + ".drops." + key + ".rarity"));

			// First get all of the items, commands, and potion effects.
			final ArrayList<LuckyBlockItem> items = new ArrayList<>();
			final ArrayList<LuckyBlockCommand> commands = new ArrayList<>();
			final ArrayList<LuckyBlockPotionEffect> potionEffects = new ArrayList<>();

			int index = 0;
			for (@SuppressWarnings("unused")
			final String s : blocksYaml.getConfig()
					.getConfigurationSection("lucky-blocks." + internalName + ".drops." + key + ".items")
					.getKeys(false)) {
				items.add(new LuckyBlockItem(
						getItem(blocksYaml, "lucky-blocks." + internalName + ".drops." + key + ".items." + index)));
				index++;
			}

			for (final String s : blocksYaml.getConfig()
					.getStringList("lucky-blocks." + internalName + ".drops." + key + ".commands")) {
				commands.add(new LuckyBlockCommand(s));
			}
			for (final String s : blocksYaml.getConfig()
					.getStringList("lucky-blocks." + internalName + ".drops." + key + ".potions")) {
				potionEffects.add(LuckyBlockPotionEffect.parseFromFile(s));

			}

			drop.setCommands(commands);
			drop.setItems(items);
			drop.setPotionEffects(potionEffects);
			drops.add(drop);
		}

		block.setDroppableItems(new LinkedList<>(drops));
		return block;
	}

	public static boolean unique(ArrayList<LuckyBlock> arr, String internalName) {
		for (final LuckyBlock b : arr) {
			if (b.getInternalName().equalsIgnoreCase(internalName)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Ensures that the blocks.yml file is valid.
	 *
	 * @return If there are any errors in the blocks.yml file.
	 */
	public static boolean validateBlocksYaml(ArrayList<LuckyBlock> arr) {
		for (final LuckyBlock block : arr) {

			if (block.getInternalName().contains(" ") || (block.getBlockMaterial() == null)
					|| !block.getBlockMaterial().isBlock()) {
				return false;
			}

			for (final LuckyBlockDrop drop : block.getDroppableItems()) {
				if ((drop.getLoot().size() > PluginConstants.MAX_LOOT_AMOUNT) || (drop.getLoot().size() == 0)) {
					return false;
				}
				for (final LootProperty loot : drop.getLoot()) {
					if (!loot.isValid()) {
						return false;
					}
				}
			}

		}

		return true;

	}

	public static ArrayList<LuckyBlock> getLuckyBlocks(ConfigManager blocksYaml) {
		final ArrayList<LuckyBlock> luckyBlocks = new ArrayList<>();
		for (final String key : getLuckyBlockNames(blocksYaml)) {
			luckyBlocks.add(getLuckyBlock(blocksYaml, key));
		}
		return luckyBlocks;
	}

}
