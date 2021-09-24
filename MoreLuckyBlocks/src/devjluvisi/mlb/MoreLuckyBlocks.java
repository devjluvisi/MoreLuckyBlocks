package devjluvisi.mlb;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.plugin.java.JavaPlugin;

import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.helper.LuckyBlockHelper;
import devjluvisi.mlb.queries.Query;
import devjluvisi.mlb.util.CommandManager;
import devjluvisi.mlb.util.ConfigManager;
import devjluvisi.mlb.util.SubCommand;


/**
 * FortuneBlocks - 1.17
 * -> LuckyBlocks plugin for Minecraft Spigot 1.17
 * <br />
 * GitHub: https://github.com/devjluvisi/FortuneBlocks
 * 
 * @version UNREL
 * @author devjluvisi (Jacob Luvisi)
 * IGN: Interryne
 *
 */
public class MoreLuckyBlocks extends JavaPlugin {
	
	/**A reference to the config.yml file in the plugin. */
	private ConfigManager configYaml;
	/**A reference to the messages.yml file in the plugin. */
	private ConfigManager messagesYaml;
	/**A reference to the blocks.yml file in the plugin. */
	private ConfigManager blocksYaml;
	
	/**
	 * An array list to track all of the lucky blocks on the server.
	 * Tracks only the default values of the lucky blocks to be used as references rather then accessing config.
	 * Does NOT store individual data such as block placements, etc.
	 * 
	 * @see LuckyBlock
	 */
	private ArrayList<LuckyBlock> serverLuckyBlocks;
	
	/**
	 * Tracks all of the current queries the server has to handle.
	 * @see Query
	 */
	private HashSet<Query> requestQueries;
	
	
	/**
	 * Planned commands:
	 * - /mlbver -> Display Version
	 * - /mlb -> View Help
	 * - /mlb list -> List of all lucky block types.
	 * - /mlb purge <name> -> Remove a lucky block forever.
	 * - /mlb admin create -> Create a new category of luckyblock.
	 * - /mlb <name> -> Information about a lucky block.
	 * - /mlb edit <name> <name|perm|luck|block-type> <value> -> Adjust values of a luckyblock.
	 * - /mlb give <player> <block name> <block luck> <amount> -> Give a specific luckyblock to a player with block luck.
	 * - /mlb luck <player> set <value> -> Set luck of a player.
	 * - /mlb luck <player> default -> Restore player luck to the default.
	 * - /mlb luck <player> -> Get player luck.
	 * - /mlb luck -> set the luck value of the lucky block in hand.
	 * - /mlb redeem -> Redeem items in hand for a luckyblock (configurable). (Opens GUI)
	 * - /mlb editor <name> -> Opens an INTERACTIVE chest GUI which shows every drop that can be dropped. Each "droppableItems" section is a glass pane. When clicked on, the glass pane opens another chest GUI for that lucky blocks drops, effects, and commands.
	 *  			-> A user can add/remove drops right on the editor.
	 */
	
	@Override
	public void onEnable() {
		
		getLogger().info("*-----------------------------------------*");
		getLogger().info("MoreLuckyBlocks v" + this.getVersion() + " has started!");
		getLogger().info("Server Version -> " + super.getServer().getVersion().substring(super.getServer().getVersion().indexOf('(')));
		getLogger().info("*-----------------------------------------*");
		
		setupConfig();
		registerCommands();
		registerEvents();
		
		requestQueries = new HashSet<Query>();
		
		serverLuckyBlocks = LuckyBlockHelper.getLuckyBlocks(blocksYaml);
		
		// Check if the config is valid and has no errors.
		if(LuckyBlockHelper.validateBlocksYaml(serverLuckyBlocks) == false) {
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
	 * @return Get a list of all waiting queries on the server.
	 */
	public HashSet<Query> getRequests() {
		return this.requestQueries;
	}
	
	
	/**
	 * Sets up the configuration files for the plugin.
	 */
	private void setupConfig() {
		this.configYaml = new ConfigManager(this, "config.yml");
		this.messagesYaml = new ConfigManager(this, "messages.yml");
		this.blocksYaml = new ConfigManager(this, "blocks.yml");
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
	 * Registers all of the commands in the plugin.
	 * @see CommandManager
	 * @see SubCommand
	 */
	private void registerCommands() {
		getCommand("mlb").setExecutor(new CommandManager(this));
	}
	
	/**
	 * Registers all of the events in the plugin.
	 */
	private void registerEvents() {}
	
	@Override
	public void onDisable() {
		getLogger().info("*-----------------------------------------*");
		getLogger().info("MoreLuckyBlocks v" + this.getVersion() + " has been disabled!");
		getLogger().info("Server Version -> " + super.getServer().getVersion().substring(super.getServer().getVersion().indexOf('(')));
		getLogger().info("*-----------------------------------------*");
		super.onDisable();
	}
	
	/**
	 * @return String representation of the version.
	 */
	private String getVersion() {
		return super.getDescription().getVersion() + " for Minecraft Version [" + super.getDescription().getAPIVersion() + "]";
	}
	
}
