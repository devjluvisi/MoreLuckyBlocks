package devjluvisi.mlb.blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.UUID;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.PluginConstants;
import devjluvisi.mlb.blocks.drops.LootProperty;
import devjluvisi.mlb.blocks.drops.LuckyBlockCommand;
import devjluvisi.mlb.blocks.drops.LuckyBlockItem;
import devjluvisi.mlb.blocks.drops.LuckyBlockPotionEffect;
import devjluvisi.mlb.util.config.ConfigManager;

/**
 * Class which manages the lucky blocks on the server. This class can be thought
 * of as the "main" class regarding how the plugin interacts with luckyblock
 * objects. <br />
 * This class IS an ArrayList itself, so it can be directly accessed and treated
 * like an ArrayList.
 *
 * @author jacob
 *
 */
public final class LuckyBlockManager extends ArrayList<LuckyBlock> {

	private static final long serialVersionUID = 3308170564891186269L;

	private final MoreLuckyBlocks plugin;

	public LuckyBlockManager(MoreLuckyBlocks plugin) {
		super(); // Call the parent ArrayList
		this.plugin = plugin;
		// this.loadExampleBlocks();
		this.upload();
	}

	/**
	 * Loads the example blocks.
	 */
	public void loadExampleBlocks() {
		final LuckyBlock lb = new LuckyBlock();
		lb.setInternalName("default");
		lb.setDefaultBlockLuck(0.0F);
		lb.setBlockMaterial(Material.SPONGE);
		lb.setBreakPermission("lb.default.break");
		lb.setLore(Arrays.asList(ChatColor.GRAY + "A custom lucky block."));
		lb.setName(ChatColor.GOLD + "Example Lucky Block");

		lb.addDrop(new LuckyBlockDrop(Arrays.asList(new LuckyBlockItem(new ItemStack(Material.IRON_SWORD))),
				Arrays.asList(new LuckyBlockCommand("/example")),
				Arrays.asList(new LuckyBlockPotionEffect(PotionEffectType.SPEED, 200, 1)), 50.0F));

		lb.saveConfig(this.plugin);
	}

	/**
	 * Reloads the cached-LuckyBlock ArrayList by reading from the config.
	 */
	public void upload() {
		this.clear();

		final ConfigManager blocksYaml = this.plugin.getBlocksYaml();
		String internalName;
		if(this.plugin.getBlocksYaml().getConfig().getConfigurationSection("lucky-blocks") == null) {
			this.loadExampleBlocks();
			this.save();
			this.upload();
			return;
		}
		// Go through all lucky blocks and add them.
		for (final String key : this.plugin.getBlocksYaml().getConfig().getConfigurationSection("lucky-blocks")
				.getKeys(false)) {
			final LuckyBlock block = new LuckyBlock();

			internalName = key;

			block.setInternalName(internalName);
			block.setName(ChatColor.translateAlternateColorCodes('&',
					blocksYaml.getConfig().getString("lucky-blocks." + internalName + ".item-name")));
			block.setBlockMaterial(
					Material.getMaterial(blocksYaml.getConfig().getString("lucky-blocks." + internalName + ".block")));
			block.setBreakPermission(blocksYaml.getConfig().getString("lucky-blocks." + internalName + ".permission"));
			block.setDefaultBlockLuck(
					(float) blocksYaml.getConfig().getDouble("lucky-blocks." + internalName + ".default-luck"));
			block.setBlockLuck(block.getDefaultBlockLuck());
			block.setLore(blocksYaml.getConfig().getStringList("lucky-blocks." + internalName + ".item-lore"));

			// Setup dropped items.
			final String itemDropKey = "lucky-blocks." + internalName + ".drops";
			if (blocksYaml.getConfig().getConfigurationSection(itemDropKey) == null) {
				break;
			}

			for (final String dropIndex : blocksYaml.getConfig().getConfigurationSection(itemDropKey).getKeys(false)) {

				final LuckyBlockDrop drop = new LuckyBlockDrop();
				drop.setRarity((float) blocksYaml.getConfig()
						.getDouble("lucky-blocks." + internalName + ".drops." + dropIndex + ".rarity"));
				if(blocksYaml.getConfig()
						.get("lucky-blocks." + internalName + ".drops." + dropIndex + ".structure") != null) {
					drop.setStructure(UUID.fromString((String)blocksYaml.getConfig()
							.get("lucky-blocks." + internalName + ".drops." + dropIndex + ".structure")));
				}
				
				// First get all of the items, commands, and potion effects.
				final ArrayList<LuckyBlockItem> items = new ArrayList<>();
				final ArrayList<LuckyBlockCommand> commands = new ArrayList<>();
				final ArrayList<LuckyBlockPotionEffect> potionEffects = new ArrayList<>();

				int index = 0;
				for (@SuppressWarnings("unused")
				final String s : blocksYaml.getConfig()
						.getConfigurationSection("lucky-blocks." + internalName + ".drops." + dropIndex + ".items")
						.getKeys(false)) {
					items.add(new LuckyBlockItem(blocksYaml.getConfig()
							.getItemStack("lucky-blocks." + internalName + ".drops." + dropIndex + ".items." + index)));
					index++;
				}

				for (final String s : blocksYaml.getConfig()
						.getStringList("lucky-blocks." + internalName + ".drops." + dropIndex + ".commands")) {
					commands.add(new LuckyBlockCommand(s));
				}
				for (final String s : blocksYaml.getConfig()
						.getStringList("lucky-blocks." + internalName + ".drops." + dropIndex + ".potions")) {
					potionEffects.add(LuckyBlockPotionEffect.parseFromFile(s));

				}

				drop.setCommands(commands);
				drop.setItems(items);
				drop.setPotionEffects(potionEffects);
				block.addDrop(drop);
			}

			this.add(block);
		}
		// Make sure all of the LuckyBlocks are working properly.
		if (!this.isValid()) {
			this.plugin.getServer().getLogger().severe("Could not start server due to invalid blocks.yml file.");
			this.plugin.getServer().getLogger()
					.severe("Please ensure that the plugin config file follows proper formatting.");
			this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
			return;
		}
	}

	/**
	 * Saves the current edits that have been made to the cached-LuckyBlocks to the
	 * configuration "blocks.yml" file.
	 */
	public void save() {
		int savedCount = 0;
		for (final LuckyBlock lb : this) {
			if (savedCount > PluginConstants.MAX_LUCKY_BLOCK_AMOUNT) {
				this.plugin.getServer().getLogger().warning(
						"The number of LuckyBlocks you have set exceeds the maximum amount allowed by the plugin ("
								+ PluginConstants.MAX_LUCKY_BLOCK_AMOUNT
								+ "). Lucky Blocks saved beyond this amount will not be read from.");
				return;
			}
			lb.saveConfig(this.plugin);
			savedCount++;
		}
	}

	/**
	 * Checks through the current set of lucky blocks and checks to see if any of
	 * them are "invalid". Invalid lucky blocks can occur from a LuckyBlock having a
	 * bad item, bad attributes, etc.
	 *
	 * @return
	 */
	private boolean isValid() {
		for (final LuckyBlock lb : this) {
			if (lb.getInternalName().contains(" ") || (lb.getBlockMaterial() == null)
					|| !lb.getBlockMaterial().isBlock()) {
				return false;
			}

			for (final LuckyBlockDrop drop : lb.getDroppableItems()) {
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

	/**
	 * Adds a new LuckyBlock to the LuckyBlockManager. Returns "false" if the lucky
	 * block cannot be added due to a similar name to a previously existing lucky
	 * block. O(N) Complexity
	 *
	 * @return If the lucky block was added successfully.
	 */
	@Override
	public boolean add(LuckyBlock lb) {
		if (this.contains(lb)) {
			return false;
		}
		// Run the base method.
		return super.add(lb);
	}
	
	public boolean contains(String internalName) {
		return super.contains(new LuckyBlock(internalName));
	}
	
	public LuckyBlock get(String internalName) {
		return super.get(super.indexOf(new LuckyBlock(internalName)));
	}

	@Override
	public void add(int index, LuckyBlock element) {
		throw new NotImplementedException(
				"This method is not implemented and should not be used. Used add(LuckyBlock obj) instead.");
	}

	/**
	 * Prints this classes toString() method to the plugin console.
	 */
	public void dumpLogger() {
		this.plugin.getServer().getConsoleSender().sendMessage(this.toString());
	}

	@Override
	public String toString() {
		final StringBuilder strBuild = new StringBuilder();
		strBuild.append("Server-Cached LuckyBlocks").append("\n");
		strBuild.append("  Size > ").append(this.size()).append("\n");
		strBuild.append("#################").append("\n");
		int index = 0;
		for (final LuckyBlock lb : this) {
			strBuild.append(index).append(" | ").append(lb.getInternalName()).append(" >> [")
					.append(lb.getBlockMaterial()).append(", ").append(lb.getDefaultBlockLuck()).append(", ")
					.append(lb.getDroppableItems().size()).append("]\n");
			index++;
		}
		strBuild.append("#################").append("\n");
		return strBuild.toString();
	}

}
