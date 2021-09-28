package devjluvisi.mlb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.plugin.java.JavaPlugin;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.events.EditDropInChatEvent;
import devjluvisi.mlb.events.handles.InventoryCloseFix;
import devjluvisi.mlb.events.luck.JoinLuckEvent;
import devjluvisi.mlb.events.luck.LeaveLuckEvent;
import devjluvisi.mlb.helper.LuckyBlockHelper;
import devjluvisi.mlb.util.CommandManager;
import devjluvisi.mlb.util.ConfigManager;
import devjluvisi.mlb.util.SubCommand;

/**
 * FortuneBlocks - 1.17 -> LuckyBlocks plugin for Minecraft Spigot 1.17 <br />
 * GitHub: https://github.com/devjluvisi/FortuneBlocks
 *
 * @version UNREL
 * @author devjluvisi (Jacob Luvisi) IGN: Interryne
 *
 */
public class MoreLuckyBlocks extends JavaPlugin {
	
	public static final float DEFAULT_LUCK = 0.0F;

	/*
	 * TODO:
	 * - Use StringUtils and StringBuilder
	 * - Cache variables
	 * - Add particle effects for lucky block break.
	 * - Add option to make lucky block break instant
	 */

	private ConfigManager configYaml;
	private ConfigManager messagesYaml;
	private ConfigManager blocksYaml;
	private ConfigManager worldDataYaml;
	private ConfigManager playersYaml;

	/**
	 * An array list to track all of the lucky blocks on the server. Tracks only the
	 * default values of the lucky blocks to be used as references rather then
	 * accessing config. Does NOT store individual data such as block placements,
	 * etc.
	 *
	 * @see LuckyBlock
	 */
	private ArrayList<LuckyBlock> serverLuckyBlocks;
	private ConcurrentHashMap<UUID, Float> playerLuckMap;
	/**
	 * Tracks which players are attempting to add commands to a lucky block in edit
	 * mode.
	 */
	private HashMap<UUID, MenuView> playersEditingDrop;

	@Override
	public void onEnable() {

		getLogger().info("*-----------------------------------------*");
		getLogger().info("MoreLuckyBlocks v" + this.getVersion() + " has started!");
		getLogger().info("Server Version -> "
				+ super.getServer().getVersion().substring(super.getServer().getVersion().indexOf('(')));
		getLogger().info("*-----------------------------------------*");

		setupConfig();
		registerCommands();
		registerEvents();

		this.serverLuckyBlocks = LuckyBlockHelper.getLuckyBlocks(blocksYaml);
		this.playerLuckMap = new ConcurrentHashMap<UUID, Float>();
		this.playersEditingDrop = new HashMap<>();

		// Check if the config is valid and has no errors.
		if (!LuckyBlockHelper.validateBlocksYaml(serverLuckyBlocks)) {
			getServer().getLogger().severe("Could not start server due to invalid blocks.yml file.");
			getServer().getLogger().severe("Please ensure that the plugin config file follows proper formatting.");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		super.onEnable();
	}

	/**
	 * @return Get a list of all lucky blocks on the server.
	 */
	public ArrayList<LuckyBlock> getLuckyBlocks() {
		return serverLuckyBlocks;
	}

	/**
	 * @return All of the players attempting to "edit" a drop in a luckyblock.
	 */
	public HashMap<UUID, MenuView> getPlayersEditingDrop() {
		return this.playersEditingDrop;
	}

	public synchronized ConcurrentHashMap<UUID, Float> getPlayerLuckMap() {
		return playerLuckMap;
	}


	/**
	 * Sets up the configuration files for the plugin.
	 */
	private void setupConfig() {
		this.configYaml = new ConfigManager(this, "config.yml");
		this.messagesYaml = new ConfigManager(this, "messages.yml");
		this.blocksYaml = new ConfigManager(this, "blocks.yml");
		this.playersYaml = new ConfigManager(this, "players.yml");
		this.worldDataYaml = new ConfigManager(this, "world-data.yml");
	}

	/**
	 * @return config.yml file.
	 */
	public ConfigManager getConfigYaml() {
		return this.configYaml;
	}

	/**
	 * @return messages.yml file.
	 */
	public ConfigManager getMessagesYaml() {
		return this.messagesYaml;
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
		return worldDataYaml;
	}

	/**
	 * @return The players.yml file
	 */
	public ConfigManager getPlayersYaml() {
		return playersYaml;
	}

	/**
	 * Registers all of the commands in the plugin.
	 *
	 * @see CommandManager
	 * @see SubCommand
	 */
	private void registerCommands() {
		getCommand("mlb").setExecutor(new CommandManager(this));
	}

	/**
	 * Registers all of the events in the plugin.
	 */
	private void registerEvents() {
		getServer().getPluginManager().registerEvents(new EditDropInChatEvent(this), this);
		getServer().getPluginManager().registerEvents(new InventoryCloseFix(this), this);
		getServer().getPluginManager().registerEvents(new JoinLuckEvent(this), this);
		getServer().getPluginManager().registerEvents(new LeaveLuckEvent(this), this);
	}

	@Override
	public void onDisable() {
		getLogger().info("*-----------------------------------------*");
		getLogger().info("MoreLuckyBlocks v" + this.getVersion() + " has been disabled!");
		getLogger().info("Server Version -> "
				+ super.getServer().getVersion().substring(super.getServer().getVersion().indexOf('(')));
		getLogger().info("*-----------------------------------------*");
		super.onDisable();
	}

	/**
	 * @return String representation of the version.
	 */
	private String getVersion() {
		return super.getDescription().getVersion() + " for Minecraft Version [" + super.getDescription().getAPIVersion()
				+ "]";
	}

}
