package devjluvisi.fb;

import org.bukkit.plugin.java.JavaPlugin;

import devjluvisi.fb.cmds.VersionCommand;


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
public class FortuneBlocks extends JavaPlugin {
	
	@Override
	public void onEnable() {
		
		getLogger().info("*-----------------------------------------*");
		getLogger().info("FortuneBlocks v" + this.getVersion() + " has started!");
		getLogger().info("Server Version -> " + super.getServer().getVersion().substring(super.getServer().getVersion().indexOf('(')));
		getLogger().info("*-----------------------------------------*");
		
		setupConfig();
		registerCommands();
		registerEvents();
		
		super.onEnable();
	}
	
	/**
	 * Sets up the configuration files for the plugin.
	 */
	private void setupConfig() {
		
	}
	
	/**
	 * Registers all of the commands in the plugin.
	 */
	private void registerCommands() {
		getCommand(new VersionCommand(this).name()).setExecutor(new VersionCommand(this));
	}
	
	/**
	 * Registers all of the events in the plugin.
	 */
	private void registerEvents() {
		
	}
	
	@Override
	public void onDisable() {
		getLogger().info("*-----------------------------------------*");
		getLogger().info("FortuneBlocks v" + this.getVersion() + " has been disabled!");
		getLogger().info("Server Version -> " + super.getServer().getVersion().substring(super.getServer().getVersion().indexOf('(')));
		getLogger().info("*-----------------------------------------*");
		super.onDisable();
	}
	
	public String getVersion() {
		return super.getDescription().getVersion() + " for Minecraft Version [" + super.getDescription().getAPIVersion() + "]";
	}
	
}
