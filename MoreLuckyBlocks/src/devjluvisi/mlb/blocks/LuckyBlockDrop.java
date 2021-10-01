package devjluvisi.mlb.blocks;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.ShulkerBox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import devjluvisi.mlb.PluginConstants;
import devjluvisi.mlb.blocks.drops.LootProperty;
import devjluvisi.mlb.blocks.drops.LuckyBlockCommand;
import devjluvisi.mlb.blocks.drops.LuckyBlockItem;
import devjluvisi.mlb.blocks.drops.LuckyBlockPotionEffect;
import devjluvisi.mlb.helper.Util;
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

	public LuckyBlockDrop(ArrayList<LuckyBlockItem> items, ArrayList<LuckyBlockCommand> commands,
			ArrayList<LuckyBlockPotionEffect> potionEffects, float rarity) {
		super();
		this.rand = new Random();
		this.uniqueId = this.rand.nextLong();
		this.items = items;
		this.commands = commands;
		this.potionEffects = potionEffects;
		setRarity(rarity);
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
		
		if(rarity >= 100.0F) {
			this.rarity = 100.0F;
			return;
		}
		if(rarity <= 0.1F) {
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
		for(LuckyBlockItem item : this.items) {
			p.getWorld().dropItem(blockLocation, item.asItem());
		}
		for(LuckyBlockPotionEffect potion : this.potionEffects) {
			p.addPotionEffect(new PotionEffect(potion.getType(), potion.getDuration()*1000, potion.getAmplifier()));
		}
		for(LuckyBlockCommand command : this.commands) {
			p.getServer().dispatchCommand(p.getServer().getConsoleSender(), command.getCommand().replaceAll("%player%", p.getName()));
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
	public void saveConfig(ConfigManager blocksYaml, String internalName, String dropLabel) {
		final String path = "lucky-blocks." + internalName + ".drops." + dropLabel;
		blocksYaml.getConfig().set(path + ".rarity", this.rarity);

		// TODO: Written Books/Books and Quill, Enchanting Books, Attributes,
		// SAVING ITEMS
		int index = 0;
		for (final LuckyBlockItem item : this.getItems()) {
			blocksYaml.getConfig().set(path + ".items." + index + ".type", item.getItem().getType().name());

			if (item.getItem().getItemMeta().getDisplayName().isEmpty()
					|| (item.getItem().getItemMeta().getDisplayName() == null)) {
				blocksYaml.getConfig().set(path + ".items." + index + ".display-name", "");

			} else {
				blocksYaml.getConfig().set(path + ".items." + index + ".display-name",
						Util.asNormalColoredString(item.getItem().getItemMeta().getDisplayName()));
			}

			// Save Amount
			blocksYaml.getConfig().set(path + ".items." + index + ".amount", item.getItem().getAmount());
			// Save Enchants
			blocksYaml.getConfig().set(path + ".items." + index + ".enchants", item.enchantsConfigString());

			// Save leather armor
			if ((item.getItem().getItemMeta() instanceof LeatherArmorMeta)
					&& !(((LeatherArmorMeta) item.getItem().getItemMeta()).getColor() == Bukkit.getServer()
							.getItemFactory().getDefaultLeatherColor())) {
				blocksYaml.getConfig().set(path + ".items." + index + ".color", String.valueOf(
						Integer.toHexString(((LeatherArmorMeta) item.getItem().getItemMeta()).getColor().asRGB())));
			}

			// Save Shulker Boxes (uses default minecraft serialization)
			if (item.getItem().getItemMeta() instanceof BlockStateMeta) {
				final BlockStateMeta im = (BlockStateMeta) item.getItem().getItemMeta();
				if (im.getBlockState() instanceof ShulkerBox) {
					final ShulkerBox shulker = (ShulkerBox) im.getBlockState();
					int subIndex = 0;
					for (final ItemStack i : shulker.getInventory().getContents()) {
						if ((i == null) || i.getType().isAir()) {
							continue;
						}
						blocksYaml.getConfig().set(path + ".items." + index + ".inventory." + subIndex, i.serialize());
						subIndex++;
					}
				}
			}

			if (item.getItem().getItemMeta() instanceof BookMeta) {
				final BookMeta m = (BookMeta) item.getItem().getItemMeta();
				final String title = m.getTitle();
				final String author = m.getAuthor();
				final List<String> pages = m.getPages();
				blocksYaml.getConfig().set(path + ".items." + index + ".book." + "title",
						Util.asNormalColoredString(String.valueOf(title)));
				blocksYaml.getConfig().set(path + ".items." + index + ".book." + "author",
						Util.asNormalColoredString(String.valueOf(author)));
				blocksYaml.getConfig().set(path + ".items." + index + ".book." + "pages",
						Util.asNormalColoredString(pages));
			}

			if (item.getItem().getItemMeta() instanceof EnchantmentStorageMeta) {
				Bukkit.getServer().broadcastMessage("TRUE");
				final EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) item.getItem().getItemMeta();
				final List<String> enchants = new LinkedList<>();
				for (final Enchantment e : bookMeta.getStoredEnchants().keySet()) {
					enchants.add(String.valueOf(
							"[" + e.getKey().getKey().toUpperCase() + ":" + bookMeta.getStoredEnchantLevel(e) + "]"));
				}
				blocksYaml.getConfig().set(path + ".items." + index + ".stored-enchants", enchants);

			}

			// Save Minecraft Potions
			if (item.getItem().getItemMeta() instanceof PotionMeta) {
				final PotionMeta potionMeta = (PotionMeta) item.getItem().getItemMeta();
				if (!potionMeta.hasCustomEffects()) {
					blocksYaml.getConfig().set(path + ".items." + index + ".meta", potionMeta.serialize());
				} else {
					final List<String> potionItemType = new ArrayList<>();
					final List<String> types = new ArrayList<>();
					final List<String> amplifiers = new ArrayList<>();
					final List<String> durations = new ArrayList<>();

					for (final PotionEffect e : potionMeta.getCustomEffects()) {
						potionItemType.add(item.getItem().getType().name().toLowerCase().contains("splash") ? "SPLASH"
								: item.getItem().getType().name().toLowerCase().contains("lingering") ? "LINGERING"
										: "REGULAR");
						types.add(e.getType().getName().toUpperCase());
						amplifiers.add(String.valueOf(e.getAmplifier()));
						durations.add(String.valueOf(e.getDuration()));
					}
					for (byte i = 0; i < types.size(); i++) {
						blocksYaml.getConfig().set(path + ".items." + index + "type", types.get(i));
						blocksYaml.getConfig().set(path + ".items." + index + "." + types.get(i) + ".splash",
								potionItemType.get(i));
						blocksYaml.getConfig().set(path + ".items." + index + "." + types.get(i) + ".amplifier",
								amplifiers.get(i));
						blocksYaml.getConfig().set(path + ".items." + index + "." + types.get(i) + ".duration",
								durations.get(i));
					}
				}
			}

			// Save Damageable items like tools and armor.
			if ((item.getItem().getItemMeta() instanceof Damageable)
					&& (((Damageable) item.getItem().getItemMeta()).getDamage() != 0)) {
				blocksYaml.getConfig().set(path + ".items." + index + ".damage",
						((Damageable) item.getItem().getItemMeta()).getDamage());
				blocksYaml.getConfig().set(path + ".items." + index + ".unbreakable",
						(item.getItem().getItemMeta().isUnbreakable()));
			}

			// Save lore.
			if (item.getItem().getItemMeta().getLore() != null) {
				blocksYaml.getConfig().set(path + ".items." + index + ".lore",
						Util.asNormalColoredString(item.getItem().getItemMeta().getLore()));

			}

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
			return rand.nextBoolean() == true ? 1 : -1;
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
