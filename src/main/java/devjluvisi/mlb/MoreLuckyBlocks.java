package devjluvisi.mlb;

import devjluvisi.mlb.api.items.CustomMetaFactory;
import devjluvisi.mlb.blocks.LuckyBlockManager;
import devjluvisi.mlb.cmds.CommandManager;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.events.EditDropInChatEvent;
import devjluvisi.mlb.events.SaveConfigEvent;
import devjluvisi.mlb.events.UpdateLogEvent;
import devjluvisi.mlb.events.custom.LogDataEvent;
import devjluvisi.mlb.events.luck.JoinLuckEvent;
import devjluvisi.mlb.events.luckyblocks.BreakEvent;
import devjluvisi.mlb.events.luckyblocks.PlaceEvent;
import devjluvisi.mlb.events.player.JoinEvent;
import devjluvisi.mlb.menus.admin.EditDropMenu;
import devjluvisi.mlb.menus.admin.EditLuckyBlockMenu;
import devjluvisi.mlb.util.config.ConfigManager;
import devjluvisi.mlb.util.config.SavingManager;
import devjluvisi.mlb.util.config.files.ExchangesManager;
import devjluvisi.mlb.util.config.files.SettingsManager;
import devjluvisi.mlb.util.config.files.messages.Message;
import devjluvisi.mlb.util.config.files.messages.MessagesManager;
import devjluvisi.mlb.util.luckyblocks.LuckyAudit;
import devjluvisi.mlb.util.players.PlayerManager;
import devjluvisi.mlb.util.structs.DropStructure;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.*;

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
     * - Use StringUtils and StringBuilder
     * - Cache temporary variables
     * - Add particle effects for lucky block break. (config.yml)
     * - Add option to make lucky block break instant (config.yml)
     * - Make an option to break certain lucky blocks with only a certain tool
     * (config.yml)
     * - Make classes, instance variables, and methods final where
     * possible
     * - Documentation and additional comments.
     * - Use Optionals for methods which can return null
     * - Validate.<...> should be used liberally.
     * - Replace String "null" with just an empty string StringUtils.EMPTY. Then only check isBlank() on the
     * strings.
     * - Any class which is not inherited from should be "final"
     * - Return empty collections instead of null
     * - Implement Config AutoSave for LuckyBlock (worldData.yml) (configurable in config.yml)
     * - Add hotbar notification for when a player breaks a lucky
     * block (Tells them the luck)
     * /mlb event spawnmob to allow lucky blocks to do more things then
     * - Add event commands to make lucky blocks more interesting (/explode, /tprandom, /boost (launch into air).
     * - Convert long iteration of lists to "stream()"
     * - Handle try-catches for command arguments in a seperate class/util.java
     * - Reduce if/else branch scoping as much as possible.
     * - Follow sonar lint standards.
     * - Use modern (Java11+) tech when possible
     * - JavaDoc & Comment all methods.
     * - GUI for structures.
     * - Fix bug. Stone blocks in structure editor above 50% height are not counted.
     * - Allow structures to have placed fireworks
     */
    private static final long INITIAL_LOG_DELAY = 40L;
    private static final long LOG_INTERVAL = 240L;

    private ConfigManager blocksYaml;
    private ConfigManager worldDataYaml;
    private ConfigManager playersYaml;
    private ConfigManager structuresYaml;

    private SettingsManager settingsManager;
    private ExchangesManager exchangesManager;
    private MessagesManager messagesManager;

    private SavingManager savingManager;


    private LuckyBlockManager lbManager; // Manage all types of lucky blocks and drops.
    private HashMap<UUID, EditDropMenu> playersEditingDrop; // Players editing a drop.
    private LinkedList<String> loggingMessages;

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
    public Map<UUID, EditDropMenu> getPlayersEditingDrop() {
        return this.playersEditingDrop;
    }

    /**
     * @return The custom meta factory object (Items API) for managing custom item tags (PersistentDataContainer)
     */
    public CustomMetaFactory getMetaFactory() {
        return this.metaFactory;
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

    public SavingManager getSavingManager() {
        return this.savingManager;
    }

    public LinkedList<String> getLoggingMessages() {
        return this.loggingMessages;
    }

    /**
     * @return The "structures.yml" file.
     */
    public ConfigManager getStructuresYaml() {
        return this.structuresYaml;
    }

    @Override
    public void onDisable() {
        if (getSettingsManager().isFirstBoot()) {
            getSettingsManager().setValue("first-boot", false);
        }

        try {
            this.audit.writeAll();
            this.playerManager.save();
        } catch(NullPointerException e) {
            getLogger().severe("Could not save resource files due to plugin error.");
        }

        this.getServer().getScheduler().cancelTasks(this);
        this.getLogger().info("*-----------------------------------------*");
        this.getLogger().info("MoreLuckyBlocks v" + this.getVersion() + " has been disabled!");
        this.getLogger().info(MessageFormat.format("Server Version -> {0}", super.getServer().getVersion().substring(super.getServer().getVersion().indexOf('('))));
        this.getLogger().info("By Paroxi, (Jacob)");
        this.getLogger().info("https://github.com/devjluvisi/MoreLuckyBlocks");
        this.getLogger().info("*-----------------------------------------*");
        super.onDisable();
    }

    /**
     * --> FEATURE ADDITIONS <--
     * <p>
     * Also: Fix issue where a breaking of a lucky block with a structure might not appear infront of a player.
     * TODO: 10/11/2021
     * - With autosave
     * - Implement SettingsCommand
     * - Implement parts of messages.
     * <p>
     * TODO: 10/12/2021 (+)
     * - Implement particle effects for lucky blocks.
     * - Implement multiple players in a structure.
     * - Implement LuckyAPI
     * - Implement a custom pre made template
     * - Implement player stats for number of blocks placed, broken, average rarity of drop
     * - Implement /explosion command
     * - Implement /particles command
     * - Implement /playsound command
     * - Implement many types of clickable and hoverable text.
     * - Implement permission nodes per lucky block.
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
        this.getLogger().info(MessageFormat.format("Server Version -> {0}", super.getServer().getVersion().substring(super.getServer().getVersion().indexOf('('))));
        this.getLogger().info("By Paroxi, (Jacob)");
        this.getLogger().info("https://github.com/devjluvisi/MoreLuckyBlocks");
        this.getLogger().info("*-----------------------------------------*");


        this.setupConfig();
        this.registerCommands();
        this.registerEvents();


        this.metaFactory = new CustomMetaFactory(this);
        this.lbManager = new LuckyBlockManager(this);
        this.playersEditingDrop = new HashMap<>();
        this.loggingMessages = new LinkedList<>();

        this.lbStructure = new DropStructure(this);
        this.getServer().getPluginManager().registerEvents(this.lbStructure, this);

        this.audit = new LuckyAudit(this);
        this.playerManager = new PlayerManager(this);
        this.savingManager = new SavingManager(this);

        // Update all players currently on the server (for reload)
        for (final Player p : Bukkit.getOnlinePlayers()) {
            this.playerManager.update(p.getUniqueId(), this.playerManager.getPlayer(p.getName()).getLuck());
        }

        Message.register(this);
        getServer().getPluginManager().callEvent(new LogDataEvent("Server Start."));

        new BukkitRunnable() {
            @Override
            public void run() {
                saveLog();
            }
        }.runTaskTimerAsynchronously(this, INITIAL_LOG_DELAY, LOG_INTERVAL); // Every 12 Seconds

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
        this.getServer().getPluginManager().registerEvents(new SaveConfigEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new EditLuckyBlockMenu(this), this);
        this.getServer().getPluginManager().registerEvents(new UpdateLogEvent(this), this);
    }

    private void saveLog() {
        if (!settingsManager.isLoggingEvents()) {
            return;
        }

        File logDir = new File(getDataFolder().getAbsolutePath() + "/logs");
        if (!logDir.exists()) {
            logDir.mkdir();
        }

        File file = new File(getDataFolder().getAbsolutePath() + "/logs", "log-" + Instant.now().toString().split("T")[0] + ".txt");

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedWriter out = null;
        try {

            out = new BufferedWriter(new FileWriter(file, true));

        } catch (IOException e) {
            e.printStackTrace();
        }
        assert out != null;
        PrintWriter printWriter = new PrintWriter(out, false);
        for (String s : loggingMessages) {
            printWriter.println(s);
        }
        printWriter.close();
        loggingMessages.clear();
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return config.yml file.
     */
    public SettingsManager getSettingsManager() {
        return this.settingsManager;
    }

    /**
     * @return String representation of the version.
     */
    private String getVersion() {
        return MessageFormat.format("{0} for Minecraft Version [{1}]", super.getDescription().getVersion(), super.getDescription().getAPIVersion());
    }

}
