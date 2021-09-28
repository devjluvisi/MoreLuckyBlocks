package devjluvisi.mlb.blocks.drops;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;

public class LuckyBlockPotionEffect implements LootProperty {

	private PotionEffectType type;
	private int duration;
	private int amplifier;

	public LuckyBlockPotionEffect(PotionEffectType type, int duration, int amplifier) {
		super();
		this.type = type;
		this.duration = duration;
		this.amplifier = amplifier;
	}

	public PotionEffectType getType() {
		return type;
	}

	public void setType(PotionEffectType type) {
		this.type = type;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration2) {
		this.duration = duration2;
	}

	public int getAmplifier() {
		return amplifier;
	}

	public void setAmplifier(int amplifier2) {
		this.amplifier = amplifier2;
	}

	/**
	 * Parses a new instance of this object from a parseable string. Strings from
	 * potion effects are represented as follows: [TYPE, AMPLIFIER, DURATION].
	 *
	 * Example: [STRENGTH, 2, 30] -> Strength II for 30s
	 *
	 * @param raw String to parse from.
	 */
	public static LuckyBlockPotionEffect parseFromFile(String raw) {
		raw = raw.replace("[", "").replace("]", "");

		String[] cut = raw.split(",");
		PotionEffectType potionType = PotionEffectType.getByName(cut[0].toUpperCase().trim());
		byte amplifier;
		short duration;
		try {
			amplifier = Byte.parseByte(cut[1].trim());
			duration = Short.parseShort(cut[2].trim());
		} catch (NumberFormatException e) {
			amplifier = -1;
			duration = -1;
		}
		return new LuckyBlockPotionEffect(potionType, duration, amplifier);
	}

	public String asConfigString() {
		String s = "[";
		s += type.getName();
		s += ", ";
		s += String.valueOf(amplifier);
		s += ", ";
		s += String.valueOf(duration);
		s += "]";
		return s;
	}

	@Override
	public String toString() {
		return "LuckyBlockPotionEffect [type=" + type + ", duration=" + duration + ", amplifier=" + amplifier + "]";
	}

	@Override
	public ItemStack asItem() {
		ItemStack i = new ItemStack(Material.POTION);
		ItemMeta meta = i.getItemMeta();
		i.addUnsafeEnchantment(Enchantment.CHANNELING, 1);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		meta.setDisplayName(ChatColor.RED + "Add Effect");
		meta.setLore(Arrays.asList(ChatColor.GRAY + "The following potion effect", ChatColor.GRAY + "will be applied:",
				ChatColor.DARK_AQUA + type.getName() + ChatColor.DARK_GRAY + ", " + ChatColor.DARK_AQUA + amplifier
						+ ChatColor.DARK_GRAY + ", " + ChatColor.DARK_AQUA + duration + "s"));
		i.setItemMeta(meta);
		return i;
	}

	@Override
	public boolean isValid() {
		if ((type == null) || (duration < 1) || (amplifier < 0))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		return type.getName().hashCode() + duration + amplifier;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LuckyBlockPotionEffect)) {
			return false;
		}
		return ((LuckyBlockPotionEffect) obj).hashCode() == this.hashCode();
	}

}
