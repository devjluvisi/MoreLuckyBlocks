package devjluvisi.mlb;

import devjluvisi.mlb.api.items.CustomMetaFactory;
import devjluvisi.mlb.blocks.LuckyBlockManager;
import devjluvisi.mlb.cmds.CommandManager;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.events.EditDropInChatEvent;
import devjluvisi.mlb.events.luck.JoinLuckEvent;
import devjluvisi.mlb.events.luckyblocks.BreakEvent;
import devjluvisi.mlb.events.luckyblocks.PlaceEvent;
import devjluvisi.mlb.events.player.JoinEvent;
import devjluvisi.mlb.menus.admin.EditDropMenu;
import devjluvisi.mlb.util.config.ConfigManager;
import devjluvisi.mlb.util.config.files.ExchangesManager;
import devjluvisi.mlb.util.config.files.MessagesManager;
import devjluvisi.mlb.util.config.files.PermissionsManager;
import devjluvisi.mlb.util.config.files.SettingsManager;
import devjluvisi.mlb.util.luckyblocks.LuckyAudit;
import devjluvisi.mlb.util.players.PlayerManager;
import devjluvisi.mlb.util.structs.DropStructure;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * MoreLuckyBlocks
 * <p>
 * Lucky Blocks plugin for Minecraft Spigot 1.17.X and higher.
 * Fully customizable in game without the need to edit configuration files.
 *
 * @author devjluvisi
 * <p>
 * Website: https://github.com/devjluvisi/MoreLuckyBlocks
 * @version 1.0-SNAPSHOT
 */
public final class MoreLuckyBlocks extends JavaPlugin {

    /*
     * TODO:
     *
     *  - Use StringUtils and StringBuilder
     * - Cache variables
     * - Add particle effects for lucky block break.
     * - Add option to make lucky block break instant
     * - Make an option to break certain lucky blocks with only a certain tool
     * (config.yml)
     * - Make classes, instance variables, and methods final where
     * possible
     * - Documentation and additional comments. - Use Optionals for methods
     * which can return null
     * - Validate.<...> should be used liberally.
     * - Replace String "null" with just an empty string "". Then only check isBlank() on the
     * strings.
     * - Any class which is not inherited from should be "final"
     * - Return empty collections instead of null
     * - Implement Config AutoSave for LuckyBlock (worldData.yml)
     * - Add hotbar notification for when a player breaks a lucky
     * block (Tells them the luck)
     * - Add "event" commands like /mlb event spawnblock
     * <block>, /mlb event spawnmob to allow lucky blocks to do more things then
     * just drop items.
     * - Convert long iteration of lists to "stream()"
     * - Handle try-catches for command arguments in a seperate class/util.java
     * -
     */

    // Setup resource files for the plugin to use.

    /**
     * "blocks.yml" resource file.
     */
    private ConfigManager blocksYaml;
    /**
     * "data/world-data.yml" resource file.
     */
    private ConfigManager worldDataYaml;
    /**
     * "data/players.yml" resource file.
     */
    private ConfigManager playersYaml;
    /**
     * "data/structures.yml" resource file.
     */
    private ConfigManager structuresYaml;

    private SettingsManager settingsManager;
    private PermissionsManager permissionsManager;
    private ExchangesManager exchangesManager;
    private MessagesManager messagesManager;

    private LuckyBlockManager lbManager; // Manage all types of lucky blocks and drops.
    private HashMap<UUID, EditDropMenu> playersEditingDrop; // Players editing a drop.

    private DropStructure lbStructure; // An object to manage structure editing for lucky blocks.

    private PlayerManager playerManager; // Manage players.
    private LuckyAudit audit; // Manage lucky block locations.
    private CustomMetaFactory metaFactory; // Special items.

    /**
     * @return The current server drop structure object.
     * @see DropStructure
     */
    public DropStructure getServerDropStructure() {
        return this.lbStructure;
    }

    /**
     * @return The server lucky block manager object.
     * @see LuckyBlockManager
     */
    public LuckyBlockManager getLuckyBlocks() {
        return this.lbManager;
    }

    /**
     * @return The lucky block audit object.
     * @see LuckyAudit
     */
    public LuckyAudit getAudit() {
        return this.audit;
    }

    /**
     * @return Get the server player manager object to manage players.
     * @see PlayerManager
     * @see devjluvisi.mlb.util.players.PlayerData
     */
    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    /**
     * @return All of the players attempting to "edit" a drop in a luckyblock.
     */
    public HashMap<UUID, EditDropMenu> getPlayersEditingDrop() {
        return this.playersEditingDrop;
    }

    /**
     * @return The custom meta factory object (Items API) for managing custom item tags (PersistentDataContainer)
     */
    public final CustomMetaFactory getMetaFactory() {
        return this.metaFactory;
    }

    /**
     * @return config.yml file.
     */
    public SettingsManager getSettingsManager() {
        return this.settingsManager;
    }

    /**
     * @return messages.yml file.
     */
    public MessagesManager getMessagesManager() {
        return this.messagesManager;
    }

    /**
     * @return blocks.yml file.
     */
    public ConfigManager getBlocksYaml() {
        return this.blocksYaml;
    }

    /**
     * @return The "world-data.yml" file.
     */
    public ConfigManager getWorldDataYaml() {
        return this.worldDataYaml;
    }

    /**
     * @return The players.yml file
     */
    public ConfigManager getPlayersYaml() {
        return this.playersYaml;
    }

    /**
     * @return The "exchanges.yml" file.
     */
    public ExchangesManager getExchangesManager() {
        return this.exchangesManager;
    }

    public PermissionsManager getPermissionsManager() {
        return this.permissionsManager;
    }

    /**
     * @return The "structures.yml" file.
     */
    public ConfigManager getStructuresYaml() {
        return this.structuresYaml;
    }

    @Override
    public void onDisable() {
        this.audit.writeAll();
        this.playerManager.save();
        this.getServer().getScheduler().cancelTasks(this);
        this.getLogger().info("*-----------------------------------------*");
        this.getLogger().info("MoreLuckyBlocks v" + this.getVersion() + " has been disabled!");
        this.getLogger().info("Server Version -> "
                + super.getServer().getVersion().substring(super.getServer().getVersion().indexOf('(')));
        this.getLogger().info("By Paroxi, (Jacob)");
        this.getLogger().info("https://github.com/devjluvisi/MoreLuckyBlocks");
        this.getLogger().info("*-----------------------------------------*");
        super.onDisable();
    }

    /**
     * --> FEATURE ADDITIONS <--
     * <p>
     * Also: Fix issue where a breaking of a lucky block with a structure might not appear infront of a player.
     * <p>
     * TODO: 10/10/2021
     * - Implement /mlb edit for block type
     * - Implement blocking out editing for /mlb drops for players with no perms.
     * - Implement exchanges
     * - Implement brief command
     * <p>
     * TODO: 10/11/2021
     * - Implement config.yml file.
     *  - With autosave
     * - Implement SettingsCommand
     * - Implement parts of messages.
     * <p>
     * TODO: 10/12/2021 (+)
     * - Implement particle effects for lucky blocks.
     * - Implement multiple players in a structure.
     * - Implement LuckyAPI
     * - Implement a custom pre made template
     * - Implement /give all command
     * - Implement /mlb reset [name] [amount]
     * - Implement player stats for number of blocks placed, broken, average rarity of drop
     * - Implement /explosion command
     * - Implement /particles command
     * - Implement /playsound command
     * - Implement many types of clickable and hoverable text.
     * - Implement permission nodes per lucky block.
     * - Implement user-only GUI for /mlb list.
     * - Implement scheduler saving for world-data.yml
     * - Implement out of date notification (refer to spigot)
     * - Implement numerous Validate.(...) functions to verify parts of the program.
     * - Implement try-catch for config to ward off errors.
     * - Finish optimizations
     * <p>
     * TODO: Future (after first release)
     * - Implement option for SQL
     */

    @Override
    public void onEnable() {

        this.getLogger().info("*-----------------------------------------*");
        this.getLogger().info("MoreLuckyBlocks v" + this.getVersion() + " has started!");
        this.getLogger().info("Server Version -> "
                + super.getServer().getVersion().substring(super.getServer().getVersion().indexOf('(')));
        this.getLogger().info("By Paroxi, (Jacob)");
        this.getLogger().info("https://github.com/devjluvisi/MoreLuckyBlocks");
        this.getLogger().info("*-----------------------------------------*");

        this.setupConfig();
        this.registerCommands();
        this.registerEvents();

        this.metaFactory = new CustomMetaFactory(this);
        this.lbManager = new LuckyBlockManager(this);
        this.playersEditingDrop = new HashMap<>();

        this.lbStructure = new DropStructure(this);
        this.getServer().getPluginManager().registerEvents(this.lbStructure, this);

        this.audit = new LuckyAudit(this);
        this.playerManager = new PlayerManager(this);

        // Update all players currently on the server (for reload)
        for (final Player p : Bukkit.getOnlinePlayers()) {
            this.playerManager.update(p.getUniqueId(), this.playerManager.getPlayer(p.getName()).getLuck());
        }

        super.onEnable();
    }

    /**
     * Sets up the configuration files for the plugin.
     */
    private void setupConfig() {
        this.settingsManager = new SettingsManager(this);
        this.messagesManager = new MessagesManager(this);
        this.blocksYaml = new ConfigManager(this, "blocks.yml");
        this.playersYaml = new ConfigManager(this, "data/players.yml");
        this.worldDataYaml = new ConfigManager(this, "data/world-data.yml");
        this.exchangesManager = new ExchangesManager(this);
        this.structuresYaml = new ConfigManager(this, "data/structures.yml");
    }

    /**
     * Registers all of the commands in the plugin.
     *
     * @see CommandManager
     * @see SubCommand
     */
    private void registerCommands() {
        Objects.requireNonNull(this.getCommand("mlb")).setExecutor(new CommandManager(this));

    }

    /**
     * Registers all events in the plugin.
     */
    private void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new EditDropInChatEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new JoinLuckEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new BreakEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new PlaceEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new JoinEvent(this), this);
    }

    /**
     * @return String representation of the version.
     */
    private final String getVersion() {
        return super.getDescription().getVersion() + " for Minecraft Version [" + super.getDescription().getAPIVersion()
                + "]";
    }

}
