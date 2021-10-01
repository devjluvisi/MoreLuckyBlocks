package devjluvisi.mlb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.items.CustomMetaFactory;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.cmds.CommandManager;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.events.EditDropInChatEvent;
import devjluvisi.mlb.events.handles.InventoryCloseFix;
import devjluvisi.mlb.events.luckyblocks.BreakEvent;
import devjluvisi.mlb.events.luckyblocks.PlaceEvent;
import devjluvisi.mlb.events.players.JoinLuckEvent;
import devjluvisi.mlb.helper.LuckyBlockHelper;
import devjluvisi.mlb.util.config.ConfigManager;
import devjluvisi.mlb.util.luckyblocks.LuckyAudit;
import devjluvisi.mlb.util.players.PlayerManager;

/**
 * FortuneBlocks - 1.17 -> LuckyBlocks plugin for Minecraft Spigot 1.17 <br />
 * GitHub: https://github.com/devjluvisi/FortuneBlocks
 *
 * @version UNREL
 * @author devjluvisi (Jacob Luvisi) IGN: Interryne
 *
 */
public final class MoreLuckyBlocks extends JavaPlugin {
	//TODO: Potentially, ditch data structures and read directly from the config files ratheer then using maps, etc.
	/*
	 * TODO: - Use StringUtils and StringBuilder - Cache variables - Add particle
	 * effects for lucky block break. - Add option to make lucky block break instant
	 * - Make an option to break certain lucky blocks with only a certain tool
	 * (config.yml) - Make classes, instance variables, and methods final where
	 * possible - Documentation and additional comments. - Use Optionals for methods
	 * which can return null - Validate.<...> should be used liberally. - Replace
	 * String "null" with just an empty string "". Then only check isBlank() on the
	 * strings. - Any class which is not inherited from should be "final" - Return
	 * empty collections instead of null
	 * - Implement Config AutoSave for LuckyBlock (worldData.yml)
	 * - Add hotbar notification for when a player breaks a lucky block (Tells them the luck)
	 */
	private ConfigManager configYaml;
	private ConfigManager messagesYaml;
	private ConfigManager blocksYaml;
	private ConfigManager worldDataYaml;
	private ConfigManager playersYaml;
	private ConfigManager exchangesYaml;

	private ArrayList<LuckyBlock> serverLuckyBlocks;

	private HashMap<UUID, MenuView> playersEditingDrop;

	private PlayerManager playerManager;
	private LuckyAudit audit;
	private CustomMetaFactory metaFactory;
	

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

		this.serverLuckyBlocks = LuckyBlockHelper.getLuckyBlocks(this.blocksYaml);
		this.playersEditingDrop = new HashMap<>();
		this.metaFactory = new CustomMetaFactory(this);
		this.audit = new LuckyAudit(this);
		this.playerManager = new PlayerManager(this);
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			playerManager.update(p.getUniqueId(), playerManager.getPlayer(p.getName()).getLuck());
		}

		// Check if the config is valid and has no errors.
		if (!LuckyBlockHelper.validateBlocksYaml(this.serverLuckyBlocks)) {
			this.getServer().getLogger().severe("Could not start server due to invalid blocks.yml file.");
			this.getServer().getLogger().severe("Please ensure that the plugin config file follows proper formatting.");
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}

		super.onEnable();
	}

	/**
	 * @return Get a list of all lucky blocks on the server.
	 */
	public ArrayList<LuckyBlock> getLuckyBlocks() {
		return this.serverLuckyBlocks;
	}

	public void setServerLuckyBlocks(ArrayList<LuckyBlock> serverLuckyBlocks) {
		this.serverLuckyBlocks = serverLuckyBlocks;
	}

	public LuckyAudit getAudit() {
		return this.audit;
	}

	public PlayerManager getPlayerManager() {
		return this.playerManager;
	}

	/**
	 * @return All of the players attempting to "edit" a drop in a luckyblock.
	 */
	public HashMap<UUID, MenuView> getPlayersEditingDrop() {
		return this.playersEditingDrop;
	}

	public final CustomMetaFactory getMetaFactory() {
		return this.metaFactory;
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
		this.exchangesYaml = new ConfigManager(this, "exchanges.yml");
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
		return this.worldDataYaml;
	}

	/**
	 * @return The players.yml file
	 */
	public ConfigManager getPlayersYaml() {
		return this.playersYaml;
	}

	public ConfigManager getExchangesYaml() {
		return this.exchangesYaml;
	}

	/**
	 * Registers all of the commands in the plugin.
	 *
	 * @see CommandManager
	 * @see SubCommand
	 */
	private void registerCommands() {
		this.getCommand("mlb").setExecutor(new CommandManager(this));
	}

	/**
	 * Registers all of the events in the plugin.
	 */
	private void registerEvents() {
		this.getServer().getPluginManager().registerEvents(new EditDropInChatEvent(this), this);
		this.getServer().getPluginManager().registerEvents(new InventoryCloseFix(this), this);
		this.getServer().getPluginManager().registerEvents(new JoinLuckEvent(this), this);
		this.getServer().getPluginManager().registerEvents(new BreakEvent(this), this);
		this.getServer().getPluginManager().registerEvents(new PlaceEvent(this), this);
	}

	@Override
	public void onDisable() {
		this.audit.writeAll();
		this.playerManager.save();
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
	 * @return String representation of the version.
	 */
	private final String getVersion() {
		return super.getDescription().getVersion() + " for Minecraft Version [" + super.getDescription().getAPIVersion()
				+ "]";
	}

}
