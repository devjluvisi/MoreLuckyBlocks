package devjluvisi.mlb.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.drops.LootProperty;
import devjluvisi.mlb.blocks.drops.LuckyBlockCommand;
import devjluvisi.mlb.blocks.drops.LuckyBlockItem;
import devjluvisi.mlb.blocks.drops.LuckyBlockPotionEffect;
import devjluvisi.mlb.util.config.ConfigManager;

/**
 * A "drop" that can be rewarded by breaking a lucky block. Note that a drop is
 * NOT an individual item. A drop is a group of items the user should get from
 * breaking the lucky block.
 *
 * @author jacob
 *
 */
public class LuckyBlockDrop implements Comparable<LuckyBlockDrop> {

	private final Random rand;
	private long uniqueId;

	private ArrayList<LuckyBlockItem> items; // Items to be dropped.
	private ArrayList<LuckyBlockCommand> commands; // Commands to be executed.
	private ArrayList<LuckyBlockPotionEffect> potionEffects; // Potion effects applied.

	private float rarity; // Rarity of this drop.

	public LuckyBlockDrop() {
		super();
		this.rand = new Random();

		this.uniqueId = this.rand.nextLong();
		this.rarity = 50.0F; // Default
		this.items = new ArrayList<>();
		this.commands = new ArrayList<>();
		this.potionEffects = new ArrayList<>();
	}

	public LuckyBlockDrop(List<LuckyBlockItem> items, List<LuckyBlockCommand> commands,
			List<LuckyBlockPotionEffect> potionEffects, float rarity) {
		super();
		this.rand = new Random();
		this.uniqueId = this.rand.nextLong();
		this.items = new ArrayList<>(items);
		this.commands = new ArrayList<>(commands);
		this.potionEffects = new ArrayList<>(potionEffects);
		this.setRarity(rarity);
	}

	public ArrayList<LuckyBlockItem> getItems() {
		return this.items;
	}

	public void setItems(ArrayList<LuckyBlockItem> items) {
		this.items = items;
	}

	public ArrayList<LuckyBlockCommand> getCommands() {
		return this.commands;
	}

	public void setCommands(ArrayList<LuckyBlockCommand> commands) {
		this.commands = commands;
	}

	public ArrayList<LuckyBlockPotionEffect> getPotionEffects() {
		return this.potionEffects;
	}

	public void setPotionEffects(ArrayList<LuckyBlockPotionEffect> potionEffects) {
		this.potionEffects = potionEffects;
	}

	public float getRarity() {
		return this.rarity;
	}

	public void setRarity(float rarity) {
		rarity = Math.abs(rarity);

		if (rarity >= 100.0F) {
			this.rarity = 100.0F;
			return;
		}
		if (rarity <= 0.1F) {
			this.rarity = 0.1F;
			return;
		}
		this.rarity = rarity;
	}

	/**
	 * @return An array list of all of the drops from the luckyblock under the
	 *         parent "DropProperty" interface.
	 */
	public ArrayList<LootProperty> getLoot() {
		final ArrayList<LootProperty> drops = new ArrayList<>();
		// TODO: Exclude potions which are still being edited.
		drops.addAll(this.items);
		drops.addAll(this.potionEffects);
		drops.addAll(this.commands);
		return drops;
	}

	public int indexOf(ItemStack lootAsItem) {
		final ArrayList<LootProperty> lootList = this.getLoot();
		for (int i = 0; i < lootList.size(); i++) {
			if (lootAsItem.equals(lootList.get(i).asItem())) {
				return i;
			}
		}
		return -1;
	}

	public void removeLoot(ItemStack lootAsItem) {
		final ArrayList<LootProperty> lootList = this.getLoot();
		final LootProperty loot = lootList.get(this.indexOf(lootAsItem));
		if (loot instanceof LuckyBlockItem) {
			this.items.remove(loot);
		}
		if (loot instanceof LuckyBlockPotionEffect) {
			this.potionEffects.remove(loot);
		}
		if (loot instanceof LuckyBlockCommand) {
			this.commands.remove(loot);
		}
	}

	public void applyTo(Location blockLocation, Player p) {
		for (final LuckyBlockItem item : this.items) {
			p.getWorld().dropItem(blockLocation, item.asItem());
		}
		for (final LuckyBlockPotionEffect potion : this.potionEffects) {
			p.addPotionEffect(new PotionEffect(potion.getType(), potion.getDuration() * 1000, potion.getAmplifier()));
		}
		for (final LuckyBlockCommand command : this.commands) {
			p.getServer().dispatchCommand(p.getServer().getConsoleSender(),
					command.getCommand().replaceAll("%player%", p.getName()));
		}
	}

	/**
	 * Saves the current drop information to the configuration file.
	 *
	 * @param blocksYaml   The block configuration file.
	 * @param internalName The internal name of the luckyblock.
	 * @param dropLabel    The label that specifies the specific drop, ex (0) or
	 *                     (1).
	 */
	public void saveConfig(MoreLuckyBlocks plugin, String internalName, String dropLabel) {
		final String path = "lucky-blocks." + internalName + ".drops." + dropLabel;
		final ConfigManager blocksYaml = plugin.getBlocksYaml();

		blocksYaml.getConfig().set(path + ".rarity", this.rarity);

		// SAVING ITEMS
		int index = 0;
		for (final LuckyBlockItem item : this.getItems()) {
			blocksYaml.getConfig().set(path + ".items." + index, item.getItem());
			index++;
		}

		// SAVING POTIONS
		final List<String> potionStringList = new ArrayList<>();

		for (final LuckyBlockPotionEffect effect : this.getPotionEffects()) {
			potionStringList.add(effect.asConfigString());
		}
		blocksYaml.getConfig().set(path + ".potions", potionStringList);

		// SAVING COMMANDS
		final List<String> commandStringList = new ArrayList<>();
		for (final LuckyBlockCommand cmd : this.getCommands()) {
			commandStringList.add(cmd.getCommand().toLowerCase());
		}
		blocksYaml.getConfig().set(path + ".commands", commandStringList);
	}

	@Override
	public String toString() {
		return "LuckyBlockDrop [items=" + this.items + ", commands=" + this.commands + ", potionEffects="
				+ this.potionEffects + ", rarity=" + this.rarity + "]";
	}

	/**
	 * Sort based on LuckyBlock rarity.
	 */
	@Override
	public int compareTo(LuckyBlockDrop o) {
		if (o.rarity > this.rarity) {
			return -1;
		} else if (o.rarity == this.rarity) {
			// Never let two drops be the "same" in rarity.
			return this.rand.nextBoolean() ? 1 : -1;
		} else {
			return 1;
		}
	}

	/**
	 * @return Copy of the current object with a different uniqueId.
	 *
	 */
	public LuckyBlockDrop ofUniqueCopy() {
		final LuckyBlockDrop d = new LuckyBlockDrop();
		d.uniqueId = this.rand.nextLong();
		d.commands.addAll(this.commands);
		d.items.addAll(this.items);
		d.potionEffects.addAll(this.potionEffects);
		d.rarity = this.rarity;
		return d;
	}

	@Override
	public int hashCode() {
		// Every lucky block drop has its own uniqueId.
		return String.valueOf(this.uniqueId).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LuckyBlockDrop)) {
			return false;
		}
		return ((LuckyBlockDrop) obj).hashCode() == this.hashCode();
	}

}
