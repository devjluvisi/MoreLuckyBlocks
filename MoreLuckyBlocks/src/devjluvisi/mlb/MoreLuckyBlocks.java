package devjluvisi.mlb;

import java.util.ArrayList;
import org.bukkit.plugin.java.JavaPlugin;

import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.helper.LuckyBlockHelper;
import devjluvisi.mlb.util.CommandManager;
import devjluvisi.mlb.util.ConfigManager;


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
	private ConfigManager configYaml;
	private ConfigManager messagesYaml;
	private ConfigManager blocksYaml;
	
	/**
	 * An array list to track all of the lucky blocks on the server.
	 * Tracks only the default values of the lucky blocks to be used as references rather then accessing config.
	 * Does NOT store individual data such as block placements, etc.
	 */
	private ArrayList<LuckyBlock> serverLuckyBlocks;
	
	
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
		
		serverLuckyBlocks = LuckyBlockHelper.getLuckyBlocks(blocksYaml);
		
		getLogger().info(serverLuckyBlocks.toString());
		
		super.onEnable();
	}
	
	public ArrayList<LuckyBlock> getLuckyBlocks() {
		return serverLuckyBlocks;
		
	}
	
	
	/**
	 * Sets up the configuration files for the plugin.
	 */
	private void setupConfig() {
		this.configYaml = new ConfigManager(this, "config.yml");
		this.messagesYaml = new ConfigManager(this, "messages.yml");
		this.blocksYaml = new ConfigManager(this, "blocks.yml");
	}
	
	public ConfigManager getConfigYaml() {
		return this.configYaml;
	}
	public ConfigManager getMessagesYaml() {
		return this.messagesYaml;
	}
	public ConfigManager getBlocksYaml() {
		return this.blocksYaml;
	}
	
	
	/**
	 * Registers all of the commands in the plugin.
	 */
	private void registerCommands() {
		getCommand("mlb").setExecutor(new CommandManager(this));
	}
	
	/**
	 * Registers all of the events in the plugin.
	 */
	private void registerEvents() {
		//getServer().getPluginManager().registerEvents(new CreateSubCommand(new MainCommand(this)), this);
	}
	
	@Override
	public void onDisable() {
		getLogger().info("*-----------------------------------------*");
		getLogger().info("MoreLuckyBlocks v" + this.getVersion() + " has been disabled!");
		getLogger().info("Server Version -> " + super.getServer().getVersion().substring(super.getServer().getVersion().indexOf('(')));
		getLogger().info("*-----------------------------------------*");
		super.onDisable();
	}
	
	public String getVersion() {
		return super.getDescription().getVersion() + " for Minecraft Version [" + super.getDescription().getAPIVersion() + "]";
	}
	
}
