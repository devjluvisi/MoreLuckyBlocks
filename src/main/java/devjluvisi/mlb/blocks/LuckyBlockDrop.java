package devjluvisi.mlb.blocks;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.drops.LootProperty;
import devjluvisi.mlb.blocks.drops.LuckyBlockCommand;
import devjluvisi.mlb.blocks.drops.LuckyBlockItem;
import devjluvisi.mlb.blocks.drops.LuckyBlockPotionEffect;
import devjluvisi.mlb.util.config.ConfigManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

/**
 * A "drop" that can be rewarded by breaking a lucky block. Note that a drop is
 * NOT an individual item. A drop is a group of items the user should get from
 * breaking the lucky block.
 *
 * @author jacob
 */
public class LuckyBlockDrop implements Comparable<LuckyBlockDrop> {

    private final Random rand;
    private long uniqueId;

    private ArrayList<LuckyBlockItem> items; // Items to be dropped.
    private ArrayList<LuckyBlockCommand> commands; // Commands to be executed.
    private ArrayList<LuckyBlockPotionEffect> potionEffects; // Potion effects applied.

    private float rarity; // Rarity of this drop.
    private UUID structure;

    public LuckyBlockDrop() {
        super();
        this.rand = new Random();

        this.uniqueId = this.rand.nextLong();
        this.rarity = 50.0F; // Default
        this.items = new ArrayList<>();
        this.commands = new ArrayList<>();
        this.potionEffects = new ArrayList<>();
        this.structure = null;
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
        this.structure = null;
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

    public final UUID getStructure() {
        return this.structure;
    }

    public final void setStructure(UUID structure) {
        this.structure = structure;
    }

    public boolean hasStructure() {
        return !Objects.isNull(this.structure);
    }

    /**
     * @return An array list of all of the drops from the luckyblock under the
     * parent "DropProperty" interface.
     */
    public ArrayList<LootProperty> getLoot() {
        final ArrayList<LootProperty> drops = new ArrayList<>();
        // TODO: Exclude potions which are still being edited.
        drops.addAll(this.items);
        drops.addAll(this.potionEffects);
        drops.addAll(this.commands);
        return drops;
    }

    public LootProperty getDrop(ItemStack lootAsItem) {
        return getLoot().stream().filter(e -> e.asItem().equals(lootAsItem)).findFirst().orElse(null);
    }

    public void removeLoot(ItemStack lootAsItem) {
        Validate.notNull(lootAsItem, "Attempted to remove \"null\" from a loot list (removeLoot)");
        final LootProperty loot = getDrop(lootAsItem);

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
        items.forEach(e -> p.getWorld().dropItem(blockLocation, e.asItem()));
        potionEffects.forEach(e -> new PotionEffect(e.getType(), e.getDuration() * 1000, e.getAmplifier()));
        commands.forEach(e -> p.getServer().dispatchCommand(p.getServer().getConsoleSender(),
                e.getCommand().replaceAll("%player%", p.getName()).replaceFirst("/", StringUtils.EMPTY)));
    }

    /**
     * Saves the current drop information to the configuration file.
     *
     * @param internalName The internal name of the luckyblock.
     * @param dropLabel    The label that specifies the specific drop, ex (0) or
     *                     (1).
     */
    public void saveConfig(MoreLuckyBlocks plugin, String internalName, String dropLabel) {
        final String path = "lucky-blocks." + internalName + ".drops." + dropLabel;
        final ConfigManager blocksYaml = plugin.getBlocksYaml();

        blocksYaml.getConfig().set(path + ".rarity", this.rarity);

        if (this.structure != null) {
            blocksYaml.getConfig().set(path + ".structure", this.structure.toString());
        }

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
        return obj.hashCode() == this.hashCode();
    }

}
