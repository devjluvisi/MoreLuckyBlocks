package devjluvisi.mlb.blocks.drops;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

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

    /**
     * Parses a new instance of this object from a parseable string. Strings from
     * potion effects are represented as follows: [TYPE, AMPLIFIER, DURATION].
     * <p>
     * Example: [STRENGTH, 2, 30] -> Strength II for 30s
     *
     * @param raw String to parse from.
     */
    public static LuckyBlockPotionEffect parseFromFile(String raw) {
        raw = raw.replace("[", "").replace("]", "");

        final String[] cut = raw.split(",");
        final PotionEffectType potionType = PotionEffectType.getByName(cut[0].toUpperCase().trim());
        int amplifier;
        int duration;
        try {
            amplifier = Integer.parseInt(cut[1].trim());
            duration = Integer.parseInt(cut[2].trim());
        } catch (final NumberFormatException e) {
            amplifier = -1;
            duration = -1;
        }
        return new LuckyBlockPotionEffect(potionType, duration, amplifier);
    }

    public PotionEffectType getType() {
        return this.type;
    }

    public void setType(PotionEffectType type) {
        this.type = type;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration2) {
        this.duration = duration2;
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public void setAmplifier(int amplifier2) {
        this.amplifier = amplifier2;
    }

    public String asConfigString() {
        final StringBuilder s = new StringBuilder("[");
        s.append(this.type.getName());
        s.append(", ");
        s.append(this.amplifier);
        s.append(", ");
        s.append(this.duration);
        s.append("]");
        return s.toString();
    }

    @Override
    public ItemStack asItem() {
        final ItemStack i = new ItemStack(Material.POTION);
        final ItemMeta meta = i.getItemMeta();
        i.addUnsafeEnchantment(Enchantment.CHANNELING, 1);
        assert meta != null;
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.setDisplayName(ChatColor.RED + "Add Effect");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "The following potion effect", ChatColor.GRAY + "will be applied:",
                ChatColor.DARK_AQUA + this.type.getName() + ChatColor.DARK_GRAY + ", " + ChatColor.DARK_AQUA
                        + this.amplifier + ChatColor.DARK_GRAY + ", " + ChatColor.DARK_AQUA + this.duration + "s"));
        i.setItemMeta(meta);
        return i;
    }

    @Override
    public boolean isValid() {
        return (this.type != null) && (this.duration >= 1) && (this.amplifier >= 0);
    }

    @Override
    public String toString() {
        return "LuckyBlockPotionEffect [type=" + this.type + ", duration=" + this.duration + ", amplifier="
                + this.amplifier + "]";
    }


    @Override
    public int hashCode() {
        return this.type.getName().hashCode() + this.duration + this.amplifier;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LuckyBlockPotionEffect)) {
            return false;
        }
        return obj.hashCode() == this.hashCode();
    }

}
