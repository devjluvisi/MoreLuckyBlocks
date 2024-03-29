package devjluvisi.mlb.blocks;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.PluginConstants;
import devjluvisi.mlb.blocks.drops.LootProperty;
import devjluvisi.mlb.blocks.drops.LuckyBlockCommand;
import devjluvisi.mlb.blocks.drops.LuckyBlockItem;
import devjluvisi.mlb.blocks.drops.LuckyBlockPotionEffect;
import devjluvisi.mlb.helper.Util;
import devjluvisi.mlb.util.config.ConfigManager;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.io.Serial;
import java.security.SecureRandom;
import java.util.*;

/**
 * Class which manages the lucky blocks on the server. This class can be thought
 * of as the "main" class regarding how the plugin interacts with luckyblock
 * objects. <br />
 * This class IS an ArrayList itself, so it can be directly accessed and treated
 * like an ArrayList.
 *
 * @author jacob
 */
public final class LuckyBlockManager extends ArrayList<LuckyBlock> {

    @Serial
    private static final long serialVersionUID = 3308170564891186269L;
    private static final int TEST_CASES = 250;
    private final MoreLuckyBlocks plugin;

    /* COOLDOWN MANAGEMENT */
    private final HashMap<UUID, LuckyBlock> placeCooldownMap;
    private final HashMap<UUID, LuckyBlock> breakCooldownMap;

    public LuckyBlockManager(MoreLuckyBlocks plugin) {
        super(); // Call the parent ArrayList
        this.plugin = plugin;
        this.upload();
        this.placeCooldownMap = new HashMap<>();
        this.breakCooldownMap = new HashMap<>();
    }

    /**
     * Reloads the cached-LuckyBlock ArrayList by reading from the config.
     */
    public void upload() {
        this.clear();

        final ConfigManager blocksYaml = this.plugin.getBlocksYaml();
        String internalName;
        if (this.plugin.getBlocksYaml().getConfig().getConfigurationSection("lucky-blocks") == null) {
            this.loadExampleBlocks();
            this.save();
            return;
        }
        // Go through all lucky blocks and add them.
        for (final String key : Objects.requireNonNull(this.plugin.getBlocksYaml().getConfig().getConfigurationSection("lucky-blocks"))
                .getKeys(false)) {
            final LuckyBlock block = new LuckyBlock();

            internalName = key;

            block.setInternalName(internalName);
            block.setName(ChatColor.translateAlternateColorCodes('&',
                    Objects.requireNonNull(blocksYaml.getConfig().getString("lucky-blocks." + internalName + ".item-name"))));
            block.setBlockMaterial(
                    Material.getMaterial(Objects.requireNonNull(blocksYaml.getConfig().getString("lucky-blocks." + internalName + ".block"))));
            block.setBreakPermission(blocksYaml.getConfig().getString("lucky-blocks." + internalName + ".permission"));
            block.setDefaultBlockLuck(
                    (float) blocksYaml.getConfig().getDouble("lucky-blocks." + internalName + ".default-luck"));
            block.setBlockLuck(block.getDefaultBlockLuck());
            block.setLore(blocksYaml.getConfig().getStringList("lucky-blocks." + internalName + ".item-lore"));
            block.setItemEnchanted(blocksYaml.getConfig().getBoolean("lucky-blocks." + internalName + ".enchanted"));

            if (!Objects.isNull(blocksYaml.getConfig().get("lucky-blocks." + internalName + ".required-tool"))) {
                block.setRequiredTool(blocksYaml.getConfig().getItemStack("lucky-blocks." + internalName + ".required-tool"));
            }
            if (!Objects.isNull(blocksYaml.getConfig().get("lucky-blocks." + internalName + ".break-sound"))) {
                block.setBreakSound(Sound.valueOf(blocksYaml.getConfig().getString("lucky-blocks." + internalName + ".break-sound")));
            }
            if (!Objects.isNull(blocksYaml.getConfig().get("lucky-blocks." + internalName + ".place-cooldown"))) {
                block.setPlaceCooldown(blocksYaml.getConfig().getInt("lucky-blocks." + internalName + ".place-cooldown"));
            }
            if (!Objects.isNull(blocksYaml.getConfig().get("lucky-blocks." + internalName + ".break-cooldown"))) {
                block.setBreakCooldown(blocksYaml.getConfig().getInt("lucky-blocks." + internalName + ".break-cooldown"));
            }
            if (!Objects.isNull(blocksYaml.getConfig().get("lucky-blocks." + internalName + ".particles"))) {
                String serialized = blocksYaml.getConfig().getString("lucky-blocks." + internalName + ".particles");
                serialized = StringUtils.replace(serialized, "[", StringUtils.EMPTY);
                serialized = StringUtils.replace(serialized, "]", StringUtils.EMPTY);
                String[] split = StringUtils.splitByWholeSeparator(serialized, ":");
                for (String s : split) {
                    Particle particle = Particle.valueOf(StringUtils.split(s, ",")[0]);
                    int amount = NumberUtils.toInt(StringUtils.split(s, ",")[1], 0);
                    block.getParticleMap().putIfAbsent(particle, amount);
                }
            }
            // Setup dropped items.
            final String itemDropKey = "lucky-blocks." + internalName + ".drops";
            if (blocksYaml.getConfig().getConfigurationSection(itemDropKey) == null) {
                break;
            }

            for (final String dropIndex : Objects.requireNonNull(blocksYaml.getConfig().getConfigurationSection(itemDropKey)).getKeys(false)) {

                final LuckyBlockDrop drop = new LuckyBlockDrop();
                drop.setRarity((float) blocksYaml.getConfig()
                        .getDouble("lucky-blocks." + internalName + ".drops." + dropIndex + ".rarity"));
                if (blocksYaml.getConfig()
                        .get("lucky-blocks." + internalName + ".drops." + dropIndex + ".structure") != null) {
                    drop.setStructure(UUID.fromString((String) Objects.requireNonNull(blocksYaml.getConfig()
                            .get("lucky-blocks." + internalName + ".drops." + dropIndex + ".structure"))));
                }

                // First get all of the items, commands, and potion effects.
                final ArrayList<LuckyBlockItem> items = new ArrayList<>();
                final ArrayList<LuckyBlockCommand> commands = new ArrayList<>();
                final ArrayList<LuckyBlockPotionEffect> potionEffects = new ArrayList<>();

                int index = 0;
                if (blocksYaml.getConfig()
                        .getConfigurationSection("lucky-blocks." + internalName + ".drops." + dropIndex + ".items")
                        != null) {
                    for (@SuppressWarnings("unused") final String s : Objects.requireNonNull(blocksYaml.getConfig()
                                    .getConfigurationSection("lucky-blocks." + internalName + ".drops." + dropIndex + ".items"))
                            .getKeys(false)) {
                        items.add(new LuckyBlockItem(Objects.requireNonNull(blocksYaml.getConfig()
                                .getItemStack("lucky-blocks." + internalName + ".drops." + dropIndex + ".items." + index))));
                        index++;
                    }
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
        }


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
        lb.setLore(List.of("A custom lucky block which", "is generated by the plugin automatically!", "\n", "&7Block Luck: &6%luck%"));
        lb.setName(ChatColor.GOLD + "Example Lucky Block");

        for (int i = 0; i < TEST_CASES; i++) {
            lb.addDrop(randomDrop());
        }

        lb.saveConfig(this.plugin);
    }

    private LuckyBlockDrop randomDrop() {

        LuckyBlockDrop d = new LuckyBlockDrop();
        SecureRandom rand = new SecureRandom();

        d.setRarity(rand.nextInt(100));
        final Material[] materials = new Material[] {
                Material.DIAMOND_HELMET, Material.DIAMOND_BLOCK, Material.RED_TERRACOTTA,
                Material.IRON_SWORD, Material.BOW, Material.LEATHER_HELMET, Material.TNT,
                Material.SPONGE, Material.GOLD_BLOCK, Material.IRON_BLOCK, Material.LEATHER_BOOTS,
                Material.LEATHER_CHESTPLATE, Material.IRON_HOE, Material.GOLDEN_PICKAXE, Material.GOLDEN_SHOVEL,
                Material.DIAMOND_SHOVEL, Material.BEDROCK, Material.OBSIDIAN, Material.LAVA_BUCKET, Material.WATER_BUCKET,
                Material.STONE, Material.DIAMOND_PICKAXE, Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE,
                Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_LEGGINGS
        };
        final String[] commands = new String[] { "/kill %player%", "/sethunger %player% 0", "/explode %player%", "/playsound random %player%", "/mimic %player% I am an idiot!", "/msg %player% Hello there.", "/blind %player%", "/troll %player% lava" };

        for (int i = 0; i < rand.nextInt(6); i++) {
            ItemStack item = new ItemStack(materials[rand.nextInt(materials.length)]);
            ItemMeta meta = item.getItemMeta();
            while (rand.nextBoolean()) {
                item.setAmount(item.getAmount() + 1);
            }

            while (rand.nextBoolean() && rand.nextBoolean()) {
                item.addUnsafeEnchantment(Enchantment.values()[rand.nextInt(Enchantment.values().length)], rand.nextInt(5));
            }

            if (rand.nextBoolean()) {
                assert meta != null;
                meta.setDisplayName(ChatColor.values()[rand.nextInt(ChatColor.values().length)] + StringUtils.upperCase(item.getType().name()));
            }

            if (rand.nextBoolean() && rand.nextBoolean() && rand.nextBoolean()) {
                assert meta != null;
                meta.setLore(List.of(ChatColor.values()[rand.nextInt(ChatColor.values().length)] + "This is a random number: " + (rand.nextFloat() + rand.nextInt(5000))));
            }

            item.setItemMeta(meta);
            d.getItems().add(new LuckyBlockItem(item));
        }
        for (int i = 0; i < rand.nextInt(4); i++) {
            LuckyBlockPotionEffect effect = new LuckyBlockPotionEffect(PotionEffectType.values()[rand.nextInt(PotionEffectType.values().length)], rand.nextInt(5000) + 2, rand.nextInt(250) + 2);
            d.getPotionEffects().add(effect);
        }
        for (int i = 0; i < rand.nextInt(4); i++) {
            LuckyBlockCommand command = new LuckyBlockCommand(commands[rand.nextInt(commands.length)]);
            d.getCommands().add(command);
        }

        return d;
    }

    /**
     * Saves the current edits that have been made to the cached-LuckyBlocks to the
     * configuration "blocks.yml" file.
     */
    public void save() {
        plugin.getBlocksYaml().setValue("lucky-blocks", null);
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

    @Override
    public void add(int index, LuckyBlock element) {
        throw new NotImplementedException(
                "This method is not implemented and should not be used. Used add(LuckyBlock obj) instead.");
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
                if ((drop.getLoot().size() > PluginConstants.MAX_LOOT_AMOUNT)) {
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

    public boolean contains(String internalName) {
        return super.contains(new LuckyBlock(Util.makeInternal(internalName)));
    }

    public LuckyBlock get(String internalName) {

        return stream().filter(e -> StringUtils.equals(e.getInternalName(), internalName)).findFirst().orElse(null);
    }

    public LuckyBlock find(String internalName) {
        return stream().filter(e -> e.getInternalName().equals(internalName)).findFirst().orElse(null);
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

    /**
     * Returns a unique hash code that represents all the lucky blocks.
     * Can take time to compute in larger quantities.
     * @return A unique hash code that represents every lucky block on the server.
     */
    public int getDetailedHash() {
        StringBuilder stringBuilder = new StringBuilder();
        for (LuckyBlock lb : this) {
            stringBuilder.append(lb.toString());
        }
        return stringBuilder.toString().hashCode();
    }

}
