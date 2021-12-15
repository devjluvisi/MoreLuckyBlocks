package devjluvisi.mlb.blocks;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.PluginConstants;
import devjluvisi.mlb.api.items.CustomItemMeta;
import devjluvisi.mlb.helper.Util;
import devjluvisi.mlb.util.config.ConfigManager;
import devjluvisi.mlb.util.config.files.SettingsManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.security.SecureRandom;
import java.util.*;

/**
 * Represents a single lucky block with any range of values.
 *
 * @author jacob
 */
public class LuckyBlock {

    private static Random rand;

    // Public Fields (Represent ALL blocks of this type)
    private String internalName;
    private String name;
    private String breakPermission;
    private Material blockMaterial;
    private List<String> lore;
    private float defaultBlockLuck;
    private List<LuckyBlockDrop> droppableItems;
    private ItemStack requiredTool;
    private EnumMap<Particle, Integer> particleMap;
    private Sound breakSound;
    private int placeCooldown;
    private int breakCooldown;
    private boolean itemEnchanted;


    // Per-Item Fields (For individual lucky blocks)
    // These fields are not saved to config.s
    private float blockLuck;

    public LuckyBlock() {
        super();
        this.internalName = StringUtils.EMPTY;
        this.name = StringUtils.EMPTY;
        this.breakPermission = StringUtils.EMPTY;
        this.blockMaterial = Material.AIR;
        this.lore = new ArrayList<>();
        this.defaultBlockLuck = PluginConstants.DEFAULT_BLOCK_LUCK;
        this.droppableItems = new ArrayList<>();
        this.blockLuck = PluginConstants.DEFAULT_BLOCK_LUCK;
        this.requiredTool = null;
        this.particleMap = new EnumMap<>(Particle.class);
        this.breakSound = null;
        this.placeCooldown = 0;
        this.breakCooldown = 0;
        this.itemEnchanted = false;
    }

    public LuckyBlock(String internalName) {
        super();
        this.internalName = internalName;
        this.name = StringUtils.EMPTY;
        this.breakPermission = StringUtils.EMPTY;
        this.blockMaterial = Material.AIR;
        this.lore = Collections.emptyList();
        this.defaultBlockLuck = PluginConstants.DEFAULT_BLOCK_LUCK;
        this.droppableItems = Collections.emptyList();
        this.blockLuck = PluginConstants.DEFAULT_BLOCK_LUCK;
    }

    public ItemStack asItem(MoreLuckyBlocks plugin, int amount) {
        return this.asItem(plugin, this.defaultBlockLuck, amount);
    }

    /**
     * Convert the LuckyBlock into a minecraft item which can be placed.
     *
     * @param plugin The main plugin.
     * @param luck   The luck to set.
     * @param amount The amount of lucky blocks wanted.
     * @return The Lucky block as {@link ItemStack}
     */
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

        assert meta != null;
        meta.setDisplayName(Util.toColor(name));
        meta.setLore(this.getRefreshedLore());
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        if (!itemEnchanted) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            luckyBlock.addUnsafeEnchantment(Enchantment.LUCK, 1);
        }

        luckyBlock.setItemMeta(meta);
        return luckyBlock;

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

    public String getInternalName() {
        return this.internalName;
    }

    public void setInternalName(String internalName) {
        if (!PluginConstants.INTERNAL_NAME_RANGE.isInRange(internalName.length())) {
            throw new IllegalArgumentException("Argument length for setting an internal name must be 2-28 characters in length.");
        }
        this.internalName = internalName;
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

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public float getDefaultBlockLuck() {
        return this.defaultBlockLuck;
    }

    public void setDefaultBlockLuck(float defaultBlockLuck) {
        if (defaultBlockLuck > 100) {
            this.defaultBlockLuck = 100.0F;
            return;
        }
        if (defaultBlockLuck < -100) {
            this.defaultBlockLuck = -100.0F;
            return;
        }
        this.defaultBlockLuck = defaultBlockLuck;
    }

    public ItemStack getRequiredTool() {
        return requiredTool;
    }

    public void setRequiredTool(ItemStack requiredTool) {
        this.requiredTool = requiredTool;
    }

    public EnumMap<Particle, Integer> getParticleMap() {
        return particleMap;
    }

    public void setParticleMap(EnumMap<Particle, Integer> particleMap) {
        this.particleMap = particleMap;
    }

    public Sound getBreakSound() {
        return breakSound;
    }

    public void setBreakSound(Sound breakSound) {
        this.breakSound = breakSound;
    }

    public int getPlaceCooldown() {
        return placeCooldown;
    }

    public void setPlaceCooldown(int placeCooldown) {
        this.placeCooldown = placeCooldown;
    }

    public int getBreakCooldown() {
        return breakCooldown;
    }

    public void setBreakCooldown(int breakCooldown) {
        this.breakCooldown = breakCooldown;
    }

    public boolean isItemEnchanted() {
        return itemEnchanted;
    }

    public void setItemEnchanted(boolean itemEnchanted) {
        this.itemEnchanted = itemEnchanted;
    }

    public boolean hasRequiredTool() {
        return !Objects.isNull(requiredTool);
    }

    public boolean hasParticles() {
        return !this.particleMap.isEmpty();
    }

    public boolean hasBreakSound() {
        return !Objects.isNull(breakSound);
    }

    public boolean hasCooldowns() {
        return this.breakCooldown != 0 || this.placeCooldown != 0;
    }

    public List<LuckyBlockDrop> getDroppableItems() {
        return this.droppableItems;
    }

    public void setDroppableItems(List<LuckyBlockDrop> arrayList) {
        this.droppableItems = arrayList;
    }

    public float getBlockLuck() {
        return this.blockLuck;
    }

    public void setBlockLuck(float blockLuck) {
        if (blockLuck > 100) {
            this.blockLuck = 100.0F;
            return;
        }
        if (blockLuck < -100) {
            this.blockLuck = -100.0F;
            return;
        }
        this.blockLuck = blockLuck;
    }

    public String getViewPermission() {
        return "mlb.lb." + internalName + ".view";
    }

    public String getExchangePermission() {
        return "mlb.lb." + internalName + ".exchange";
    }

    public boolean isAllowedToBreak(SettingsManager manager, Location loc, Player p) {
        if (manager.getDisabledLuckyBlocks().contains(internalName)) {
            return false;
        }
        if (StringUtils.isNotEmpty(breakPermission) && !p.hasPermission(breakPermission)) {
            return false;
        }
        if (manager.getBannedPlayers().contains(p.getName()) || manager.getBannedPlayers().contains(p.getUniqueId().toString())) {
            return false;
        }
        if (manager.getDisabledWorlds().contains(Objects.requireNonNull(loc.getWorld()).getName())) {
            return false;
        }
        if(!Objects.isNull(requiredTool) && !p.getInventory().getItemInMainHand().equals(requiredTool)) {
            return false;
        }
        // TODO: Add checks for break/place cooldowns
        return true;
    }

    public boolean isAllowedToPlace(SettingsManager manager, Location loc, Player p) {
        if (manager.getDisabledLuckyBlocks().contains(internalName)) {
            return false;
        }
        if (StringUtils.isNotEmpty(breakPermission) && !p.hasPermission(breakPermission)) {
            return false;
        }
        if (manager.getBannedPlayers().contains(p.getName()) || manager.getBannedPlayers().contains(p.getUniqueId().toString())) {
            return false;
        }
        if (manager.getDisabledWorlds().contains(Objects.requireNonNull(loc.getWorld()).getName())) {
            return false;
        }
        return true;
    }

    public boolean canExchange(SettingsManager manager, Player p) {
        if (manager.getDisabledLuckyBlocks().contains(internalName)) {
            return false;
        }
        if (manager.getBannedPlayers().contains(p.getName()) || manager.getBannedPlayers().contains(p.getUniqueId().toString())) {
            return false;
        }
        if (manager.getDisabledWorlds().contains(Objects.requireNonNull(p.getWorld()).getName())) {
            return false;
        }
        return p.hasPermission(getExchangePermission());
    }

    public boolean canView(SettingsManager manager, Player p) {
        if (manager.getDisabledLuckyBlocks().contains(internalName)) {
            return false;
        }
        if (manager.getBannedPlayers().contains(p.getName()) || manager.getBannedPlayers().contains(p.getUniqueId().toString())) {
            return false;
        }
        return p.hasPermission(getViewPermission());
    }


    /**
     * Saves a lucky block and all of its drops.
     */
    public void saveConfig(MoreLuckyBlocks plugin) {
        final ConfigManager blocksYaml = plugin.getBlocksYaml();
        final String path = "lucky-blocks." + this.internalName;
        blocksYaml.getConfig().set(path, null);
        blocksYaml.getConfig().set(path + ".item-name", Util.asNormalColoredString(this.name));
        blocksYaml.getConfig().set(path + ".block", this.blockMaterial.name());
        blocksYaml.getConfig().set(path + ".item-lore", Util.asNormalColoredString(this.lore));
        blocksYaml.getConfig().set(path + ".permission", this.breakPermission);
        blocksYaml.getConfig().set(path + ".enchanted", this.itemEnchanted);
        if (!Objects.isNull(requiredTool)) {
            blocksYaml.getConfig().set(path + ".required-tool", this.requiredTool);
        }

        if (!Objects.isNull(breakSound)) {
            blocksYaml.getConfig().set(path + ".break-sound", this.breakSound.name());
        }

        if (placeCooldown != 0 || breakCooldown != 0) {
            blocksYaml.getConfig().set(path + ".place-cooldown", this.placeCooldown);
            blocksYaml.getConfig().set(path + ".break-cooldown", this.breakCooldown);
        }

        if (!particleMap.isEmpty()) {
            StringBuilder str = new StringBuilder();
            particleMap.forEach((k, v) -> str.append(k.name()).append(",").append(v).append(":"));
            str.setLength(str.length() - 1); // Remove last ":"
            blocksYaml.getConfig().set(path + ".particles", "[" + str + "]");
        }

        blocksYaml.getConfig().set(path + ".default-luck", this.defaultBlockLuck);

        int index = 0;
        for (final LuckyBlockDrop drop : this.droppableItems) {
            drop.saveConfig(plugin, this.internalName, String.valueOf(index));
            index++;
        }
        blocksYaml.save();
        blocksYaml.reload();
    }

    public void addDrop(LuckyBlockDrop drop) {
        this.droppableItems.add(drop);
    }

    public int indexOf(LuckyBlockDrop drop) {
        for (int i = 0; i < this.droppableItems.size(); i++) {
            if (this.droppableItems.get(i).equals(drop)) {
                return i;
            }
        }
        return -1;
    }

    public LuckyBlockDrop generateDrop(float playerLuck) {
        rand = new SecureRandom();
        // Sorted drops by rarity.
        final TreeSet<LuckyBlockDrop> sortedDrops = new TreeSet<>(this.droppableItems);
        final float max = 100.0F;
        final float min = 0.0F;

        final float chance = min + (rand.nextFloat() * (max - min));

        final float offset = (playerLuck + this.blockLuck) / 16;

        final float offsetChance = (chance - offset);

        for (final LuckyBlockDrop d : sortedDrops) {

            if (offsetChance <= d.getRarity()) {
                return d;
            }
        }

        return sortedDrops.last();
    }

    public boolean isEmpty() {
        return this.droppableItems.size() == 0;
    }

    public void clean() {
        for (final LuckyBlockDrop d : this.droppableItems) {
            if (d.getLoot().size() == 0) {
                this.removeDrop(d);
            }
        }
    }

    public void removeDrop(LuckyBlockDrop drop) {
        this.droppableItems.remove(drop);
    }

    @Override
    public boolean equals(Object obj) {
        Validate.notNull(obj, "Object comparing to LuckyBlock equals() is null.");
        return this.hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        // There will never be another lucky block with the same internal name.
        return this.internalName.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LuckyBlock{");
        sb.append("internalName='").append(internalName).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", breakPermission='").append(breakPermission).append('\'');
        sb.append(", blockMaterial=").append(blockMaterial);
        sb.append(", lore=").append(lore);
        sb.append(", defaultBlockLuck=").append(defaultBlockLuck);
        sb.append(", droppableItems=").append(droppableItems);
        sb.append(", requiredTool=").append(requiredTool);
        sb.append(", particleMap=").append(particleMap);
        sb.append(", breakSound=").append(breakSound);
        sb.append(", placeCooldown=").append(placeCooldown);
        sb.append(", breakCooldown=").append(breakCooldown);
        sb.append(", itemEnchanted=").append(itemEnchanted);
        sb.append(", blockLuck=").append(blockLuck);
        sb.append('}');
        return sb.toString();
    }

}
