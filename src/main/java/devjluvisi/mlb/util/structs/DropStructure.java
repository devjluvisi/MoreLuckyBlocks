package devjluvisi.mlb.util.structs;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import devjluvisi.mlb.util.config.files.messages.Message;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import java.util.*;

import static devjluvisi.mlb.util.structs.DropStructure.BUILD_LIMIT;

/**
 * <h2>DropStructure</h2>
 * The drop structure object represents a world "mlb-world" on the server where users
 * can edit the structures of LuckyBlocks.
 * <p>
 * The DropStructure allows only one player at a time in the world.
 * The plugin will track everything the player modifies in the world in relation to blocks and spawning mobs.
 * When the player exists, the data is saved for a specific lucky block drop.
 */
public class DropStructure implements Listener {

    /**
     * Maximum allowed build limit in the world.
     */
    static final int BUILD_LIMIT = 128;
    /**
     * Size of the world border from the origin.
     */
    static final byte WORLD_BORDER_SIZE = 50;

    /**
     * Default name of the world to be created in the world directory on the server folder.
     */
    private static final String defaultName = "mlb-world";
    private final MoreLuckyBlocks plugin;
    private final World structWorld;
    private final HashMap<Location, EntityType> spawnableEntities;
    /**
     * If the user has any unsaved changes.
     */
    private boolean unsavedChanges;
    /**
     * If the user has attempted to exit with unsaved changes.
     */
    private boolean confirmExit;
    private UUID editingPlayerUUID;

    // Track information about the player before they were teleported to this world.
    private Location lastKnownLocation;
    private ItemStack[] lastKnownInventory;
    private Collection<PotionEffect> lastKnownPotionEffects;

    /**
     * Luckyblock being edited.
     */
    private LuckyBlock lb;
    /**
     * Drop being edited.
     */
    private LuckyBlockDrop drop;

    public DropStructure(MoreLuckyBlocks plugin) {

        this.plugin = plugin;
        this.unsavedChanges = false;
        this.confirmExit = false;
        this.spawnableEntities = new HashMap<>();

        // If the world does not exist, create one.
        if (plugin.getServer().getWorld(defaultName) == null) {
            this.structWorld = plugin.getServer()
                    .createWorld(new WorldCreator(defaultName).type(WorldType.FLAT).generateStructures(false).generator(new EmptyChunkGenerator()));
        } else {
            // If the world does exist, kick everyone because then the plugin has been reloaded.
            this.structWorld = plugin.getServer().getWorld(defaultName);

            assert this.structWorld != null;

            for (final Player p : this.structWorld.getPlayers()) {
                // get(0) just represents the first world, usually the main one "world".
                p.teleport(plugin.getServer().getWorlds().get(0).getSpawnLocation());
                p.kickPlayer(Message.M4.get());
            }
            for (LivingEntity e : structWorld.getLivingEntities()) {
                e.setHealth(0.0F);
                e.remove();
            }

        }
        // Set the spawn location at the origin.
        assert this.structWorld != null;
        this.structWorld
                .setSpawnLocation(new Location(this.structWorld, 0, this.structWorld.getHighestBlockYAt(0, 0) + 1, 0));

        // Set the attributes the world should have.
        this.structWorld.setDifficulty(Difficulty.PEACEFUL);
        this.structWorld.setPVP(false);
        this.structWorld.setAutoSave(false);
        this.structWorld.setSpawnFlags(false, false);
        this.structWorld.setKeepSpawnInMemory(false);
        this.structWorld.setTime(10000);
        this.structWorld.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        this.structWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        this.structWorld.setGameRule(GameRule.DO_FIRE_TICK, false);
        this.structWorld.setGameRule(GameRule.DO_ENTITY_DROPS, false);
        this.structWorld.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        this.structWorld.setGameRule(GameRule.DO_MOB_LOOT, false);
        this.structWorld.setGameRule(GameRule.SPAWN_RADIUS, 0);
        this.structWorld.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
        this.structWorld.setGameRule(GameRule.MOB_GRIEFING, false);
        this.structWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        this.structWorld.setGameRule(GameRule.KEEP_INVENTORY, true);
        this.structWorld.setGameRule(GameRule.FALL_DAMAGE, false);
        this.structWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        this.structWorld.setGameRule(GameRule.FIRE_DAMAGE, false);
        this.structWorld.setGameRule(GameRule.DROWNING_DAMAGE, false);

        // Define the world border.
        final WorldBorder border = this.structWorld.getWorldBorder();
        border.setSize(WORLD_BORDER_SIZE);
        border.setCenter(0.0, 0.0);
    }

    public static final String getDefaultName() {
        return defaultName;
    }

    /**
     * Move a player to the structure world.
     *
     * @param lb          The lucky block to edit.
     * @param editingDrop The drop within the lucky block to edit.
     * @param p           The player to move to the structure world.
     */
    public void update(LuckyBlock lb, LuckyBlockDrop editingDrop, Player p) {
        this.lb = lb;
        this.drop = editingDrop;
        this.editingPlayerUUID = p.getUniqueId();

        // Remove all entities in the world.
        for (LivingEntity e : structWorld.getLivingEntities()) {
            e.setHealth(0.0F);
            e.remove();
        }


        // Reset the world to defaults.
        for (int x = -(WORLD_BORDER_SIZE / 2); x < (WORLD_BORDER_SIZE / 2); x++) {
            for (int z = -(WORLD_BORDER_SIZE / 2); z < (WORLD_BORDER_SIZE / 2); z++) {
                for (int y = 0; y < BUILD_LIMIT; y++) {
                    // Set the bottom half to stone and the top half to air.
                    this.structWorld.getBlockAt(x, y, z).setType(((y < BUILD_LIMIT / 2) ? Material.STONE : Material.AIR));
                }
            }
        }

        // Build a structure if the lucky block already has a previous struture.
        if (editingDrop.hasStructure()) {
            final LinkedList<String> blockList = new LinkedList<>(this.plugin.getStructuresYaml().getConfig()
                    .getStringList("structures." + editingDrop.getStructure().toString()));
            for (final String s : blockList) {
                final RelativeObject r = new RelativeObject().deserialize(s);
                r.place(this.structWorld);
            }
        }
        if (structWorld.getBlockAt(0, BUILD_LIMIT / 2, 0).isEmpty()) {
// Set the base lucky block at the origin.
            this.structWorld.getBlockAt(0, BUILD_LIMIT / 2, 0).setType(lb.getBlockMaterial());
        }

        this.lastKnownInventory = p.getInventory().getContents();
        this.lastKnownLocation = p.getLocation();
        this.lastKnownPotionEffects = p.getActivePotionEffects();

        // Pause all entities.
        for (LivingEntity e : structWorld.getLivingEntities()) {
            e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30000, 127));
            e.setInvulnerable(true);
            e.setSilent(true);
            e.setRotation(0, 0);
            e.setAI(false);
        }

        // Finally teleport the player to the world.
        this.teleport(p);
    }

    /**
     * Teleport a player to the structure world.
     *
     * @param p Player to teleport.
     */
    private void teleport(Player p) {

        if (!(this.structWorld.getPlayers().size() > 0)) {

            p.teleport(new Location(this.structWorld, 0.0D, this.structWorld.getHighestBlockYAt(0, 0) + 1, 0.0D));
            p.sendMessage(Message.M4.get());
            if (this.drop.hasStructure()) {
                p.sendMessage(Message.M5.get());
            }
            p.getInventory().clear();

            for (final PotionEffect effect : p.getActivePotionEffects()) {
                p.removePotionEffect(effect.getType());
            }

            p.setGameMode(GameMode.CREATIVE);
            p.setHealth(20);
            p.setFoodLevel(20);
            p.setInvulnerable(true);
            // Hotbar message.
            new EditingStructureTask(this).runTaskTimerAsynchronously(this.plugin, 10, 20);
            p.sendTitle(ChatColor.GREEN + "Editing", "Now editing structure for drop.", 10, 70, 20);
        }
    }

    public final boolean isConfirmExit() {
        return this.confirmExit;
    }

    public final void setConfirmExit(boolean confirmExit) {
        this.confirmExit = confirmExit;
    }

    public final boolean isUnsavedChanges() {
        return this.unsavedChanges;
    }

    public final void setUnsavedChanges(boolean unsavedChanges) {
        this.unsavedChanges = unsavedChanges;
    }

    /**
     * @return UUID of the player currently in the structure world.
     */
    public final UUID getEditingPlayerUUID() {
        return this.editingPlayerUUID;
    }

    public World getWorld() {
        return this.structWorld;
    }

    /**
     * @return If there is more then one player in the structure world.
     */
    public boolean hasEditingPlayer() {
        return this.structWorld.getPlayers().size() > 0;
    }

    @EventHandler
    public void interact(PlayerInteractEvent e) {
        // Manage spawn egg placing.

        if (!(Objects.requireNonNull(e.getPlayer().getLocation().getWorld()).getUID().equals(this.structWorld.getUID()))) {
            return;
        }
        if (e.getClickedBlock() == null || e.getClickedBlock().getType().isAir() || !Objects.requireNonNull(e.getItem()).getType().name().contains("_SPAWN_EGG")) {
            return;
        }
        EntityType entityType = EntityType.valueOf(e.getItem().getType().name().split("_SPAWN_EGG")[0]);
        double x = e.getClickedBlock().getLocation().getX();
        double y = e.getClickedBlock().getLocation().getY();
        double z = e.getClickedBlock().getLocation().getZ();
        e.getPlayer().sendMessage(Message.M6.format(entityType, x, y, z));
        this.spawnableEntities.put(e.getClickedBlock().getLocation(), entityType);
    }

    @EventHandler
    public void preventPlace(BlockPlaceEvent e) {
        // Prevent placing above build limit.

        if (!(e.getBlock().getLocation().getWorld().getUID().equals(this.structWorld.getUID()))) {
            return;
        }
        if (e.getBlock().getLocation().getY() > BUILD_LIMIT) {
            e.getPlayer().sendMessage(Message.M7.format(BUILD_LIMIT));
            e.setCancelled(true);
            return;
        }
        this.unsavedChanges = true;
        this.confirmExit = false;
    }

    @EventHandler
    public void blockFlow(BlockSpreadEvent e) {
        if (!(e.getBlock().getLocation().getWorld().getUID().equals(this.structWorld.getUID()))) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void blockChange(EntityChangeBlockEvent e) {
        if (!(e.getBlock().getLocation().getWorld().getUID().equals(this.structWorld.getUID()))) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void blockPhysics(BlockPhysicsEvent e) {
        if (!(e.getBlock().getLocation().getWorld().getUID().equals(this.structWorld.getUID()))) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void blockToBlock(BlockFromToEvent e) {
        if (!(e.getBlock().getLocation().getWorld().getUID().equals(this.structWorld.getUID()))) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void blockPiston(BlockPistonExtendEvent e) {
        if (!(e.getBlock().getLocation().getWorld().getUID().equals(this.structWorld.getUID()))) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void blockRedstone(BlockRedstoneEvent e) {
        if (!(e.getBlock().getLocation().getWorld().getUID().equals(this.structWorld.getUID()))) {
            return;
        }
        e.setNewCurrent(0);
    }

    @EventHandler
    public void preventExplode(EntityExplodeEvent e) {
        if (!(e.getLocation().getWorld().getUID().equals(this.structWorld.getUID()))) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void preventBreak(BlockBreakEvent e) {
        if (!(e.getBlock().getLocation().getWorld().getUID().equals(this.structWorld.getUID()))) {
            return;
        }

        if (e.getBlock().getLocation().equals(new Location(this.structWorld, 0, BUILD_LIMIT / 2, 0)) && structWorld.getBlockAt(0, BUILD_LIMIT / 2, 0).getType() == lb.getBlockMaterial()) {
            e.getPlayer().sendMessage(Message.M8.get());
            return;
        }


        this.unsavedChanges = true;
        this.confirmExit = false;
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent e) {
        if (!(e.getPlayer().getLocation().getWorld().getUID().equals(this.structWorld.getUID()))) {
            return;
        }
        this.kick(e.getPlayer());
        this.reset();
    }

    /**
     * Removes a player from the structure world.
     * Teleports them to their last known location.
     *
     * @param p The player to kick.
     */
    public void kick(Player p) {
        p.teleport(this.lastKnownLocation);
        p.getInventory().setContents(this.lastKnownInventory);
        for (final PotionEffect effect : this.lastKnownPotionEffects) {
            p.addPotionEffect(effect);
        }
        p.sendMessage(Message.M2.get());
        this.reset();
    }

    /**
     * Resets all instance variables to default or null values.
     */
    private void reset() {
        this.spawnableEntities.clear();
        this.lb = null;
        this.drop = null;
        this.editingPlayerUUID = null;
        this.lastKnownInventory = null;
        this.lastKnownLocation = null;
        this.lastKnownPotionEffects = null;
        this.confirmExit = false;
        this.unsavedChanges = false;
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        if (!(e.getPlayer().getLocation().getWorld().getUID().equals(this.structWorld.getUID()))) {
            return;
        }
        e.getPlayer().setHealth(0);
        e.getPlayer().setInvulnerable(false);
    }

    /**
     * Save the data modified in the structure world to the
     * "structures.yml" configuration file.
     */
    public void save() {

        if (this.drop.getStructure() == null) {
            this.drop.setStructure(UUID.randomUUID());
        }

        final LinkedList<String> itemList = new LinkedList<>();

        for (int x = -(WORLD_BORDER_SIZE / 2); x < (WORLD_BORDER_SIZE / 2); x++) {
            for (int z = -(WORLD_BORDER_SIZE / 2); z < (WORLD_BORDER_SIZE / 2); z++) {
                for (int y = 0; y < BUILD_LIMIT; y++) {
                    // Ignore Lucky Block
                    if ((x == 0) && (z == 0) && (y == (BUILD_LIMIT / 2)) && structWorld.getBlockAt(x, y, z).getType() == lb.getBlockMaterial()) {
                        continue;
                    }
                    final Material m = this.structWorld.getBlockAt(x, y, z).getType();

                    if(y < BUILD_LIMIT / 2) {
                        if(m == Material.STONE) {
                            continue;
                        }
                    }else{
                        if(structWorld.getBlockAt(x, y, z).isEmpty()) {
                            continue;
                        }
                    }
                    final RelativeObject block = new RelativeObject(m, x, y, z);
                    itemList.add(block.serialize());
                }
            }
        }

        // Create relative objects for each entity.
        for (Map.Entry<Location, EntityType> entry : this.spawnableEntities.entrySet()) {
            itemList.add(new RelativeObject(entry.getValue(), (int) entry.getKey().getX(), (int) entry.getKey().getY(), (int) entry.getKey().getZ()).serialize());
        }

        this.plugin.getStructuresYaml().getConfig().set("structures." + this.drop.getStructure().toString(), itemList);
        this.plugin.getStructuresYaml().save();
        this.plugin.getStructuresYaml().reload();
        this.unsavedChanges = false;
    }

    public final MoreLuckyBlocks getPlugin() {
        return this.plugin;
    }

    public final LuckyBlock getLb() {
        return this.lb;
    }

    public final LuckyBlockDrop getDrop() {
        return this.drop;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("DropStructure [structUUID=");
        builder.append(defaultName);
        builder.append(", editingPlayerUUID=");
        builder.append(this.editingPlayerUUID);
        builder.append("]");
        return builder.toString();
    }

}

/**
 * A task for constantly showing a hot bar message to a player
 * while they are editing in the structure world.
 */
class EditingStructureTask extends BukkitRunnable {

    private final DropStructure struct;
    private final MoreLuckyBlocks plugin;
    private final Player p;

    public EditingStructureTask(DropStructure struct) {
        this.plugin = struct.getPlugin();
        this.p = this.plugin.getServer().getPlayer(struct.getEditingPlayerUUID());
        this.struct = struct;
    }

    @Override
    public void run() {
        if (!this.struct.getWorld().getPlayers().contains(this.p) || (this.struct.getLb() == null)
                || (this.struct.getDrop() == null)) {
            this.cancel();
            return;
        }
        struct.getWorld().spawnParticle(Particle.GLOW, 0.5, BUILD_LIMIT / 2, 0.5, 50);
        struct.getWorld().spawnParticle(Particle.GLOW, 0.25, BUILD_LIMIT / 2, 0.5, 50);
        struct.getWorld().spawnParticle(Particle.GLOW, 0.5, BUILD_LIMIT / 2, 0.25, 50);
        struct.getWorld().spawnParticle(Particle.GLOW, 0.75, BUILD_LIMIT / 2, 0.5, 50);
        struct.getWorld().spawnParticle(Particle.GLOW, 0.5, BUILD_LIMIT / 2, 0.75, 50);
        this.p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent
                        .fromLegacyText(Message.M1.format(this.struct.getLb().getInternalName(), this.struct.getLb().indexOf(this.struct.getDrop()))));

    }

}
class EmptyChunkGenerator extends ChunkGenerator {

    @Override
    @Nonnull
    public ChunkData generateChunkData(@Nonnull World world, @Nonnull Random random, int x, int z, @Nonnull BiomeGrid biome) {
        return createChunkData(world);
    }

}
