package devjluvisi.mlb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.items.CustomMetaFactory;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.events.EditDropInChatEvent;
import devjluvisi.mlb.events.handles.InventoryCloseFix;
import devjluvisi.mlb.events.luckyblocks.BreakEvent;
import devjluvisi.mlb.events.luckyblocks.PlaceEvent;
import devjluvisi.mlb.events.players.JoinLuckEvent;
import devjluvisi.mlb.events.players.LeaveLuckEvent;
import devjluvisi.mlb.helper.LuckyBlockHelper;
import devjluvisi.mlb.util.CommandManager;
import devjluvisi.mlb.util.SubCommand;
import devjluvisi.mlb.util.config.ConfigManager;
import devjluvisi.mlb.util.luckyblocks.LuckyAudit;

/**
 * FortuneBlocks - 1.17 -> LuckyBlocks plugin for Minecraft Spigot 1.17 <br />
 * GitHub: https://github.com/devjluvisi/FortuneBlocks
 *
 * @version UNREL
 * @author devjluvisi (Jacob Luvisi) IGN: Interryne
 *
 */
public final class MoreLuckyBlocks extends JavaPlugin {

	/*
	 * TODO: - Use StringUtils and StringBuilder - Cache variables - Add particle
	 * effects for lucky block break. - Add option to make lucky block break instant
	 * - Make an option to break certain lucky blocks with only a certain tool
	 * (config.yml) - Make classes, instance variables, and methods final where
	 * possible - Documentation and additional comments.
	 */

	private ConfigManager configYaml;
	private ConfigManager messagesYaml;
	private ConfigManager blocksYaml;
	private ConfigManager worldDataYaml;
	private ConfigManager playersYaml;
	private ConfigManager exchangesYaml;

	private ArrayList<LuckyBlock> serverLuckyBlocks;

	private HashMap<UUID, Float> playerLuckMap;
	private HashMap<UUID, MenuView> playersEditingDrop;

	private CustomMetaFactory metaFactory;
	private LuckyAudit audit;

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
		this.playerLuckMap = new HashMap<>();
		this.playersEditingDrop = new HashMap<>();
		this.metaFactory = new CustomMetaFactory(this);
		this.audit = new LuckyAudit(this);

		// Check if the config is valid and has no errors.
		if (!LuckyBlockHelper.validateBlocksYaml(this.serverLuckyBlocks)) {
			this.getServer().getLogger().severe("Could not start server due to invalid blocks.yml file.");
			this.getServer().getLogger().severe("Please ensure that the plugin config file follows proper formatting.");
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}

		// For Reloads, we ensure player luck remains
		for (final Player p : this.getServer().getOnlinePlayers()) {
			if (this.getPlayerFromConfig(p.getName()) != null) {
				this.getPlayerLuckMap().put(p.getUniqueId(), this.getPlayerFromConfig(p.getName()).getValue());
			}
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

	/**
	 * @return All of the players attempting to "edit" a drop in a luckyblock.
	 */
	public HashMap<UUID, MenuView> getPlayersEditingDrop() {
		return this.playersEditingDrop;
	}

	public final CustomMetaFactory getMetaFactory() {
		return this.metaFactory;
	}

	public HashMap<UUID, Float> getPlayerLuckMap() {
		return this.playerLuckMap;
	}

	public void savePlayerLuckMap() {

		for (final Map.Entry<UUID, Float> entry : this.playerLuckMap.entrySet()) {
			String name;
			if (Bukkit.getPlayer(entry.getKey()) == null) {
				name = Bukkit.getOfflinePlayer(entry.getKey()).getName();
			} else {
				this.getServer().getConsoleSender().sendMessage("Saving player online.");
				name = Bukkit.getPlayer(entry.getKey()).getName();
			}
			final String uuid = entry.getKey().toString();
			this.playersYaml.getConfig().set("players." + uuid + ".name", name);
			this.playersYaml.getConfig().set("players." + uuid + ".luck", entry.getValue().toString());
		}
		this.playersYaml.save();
		this.playersYaml.reload();
	}

	public Map.Entry<UUID, Float> getPlayerFromConfig(String name) {
		if (this.playersYaml.getConfig().getConfigurationSection("players") == null) {
			return null;
		}
		for (final String playerUUIDs : this.playersYaml.getConfig().getConfigurationSection("players")
				.getKeys(false)) {
			if ((this.playersYaml.getConfig().get("players." + playerUUIDs + ".name") != null)
					&& ((String) this.playersYaml.getConfig().get("players." + playerUUIDs + ".name"))
							.equalsIgnoreCase(name)) {
				return new Map.Entry<>() {
					@Override
					public UUID getKey() {
						return UUID.fromString(playerUUIDs);
					}

					@Override
					public Float getValue() {
						return Float.parseFloat((String) MoreLuckyBlocks.this.playersYaml.getConfig()
								.get("players." + playerUUIDs + ".luck"));
					}

					@Override
					public Float setValue(Float value) {
						MoreLuckyBlocks.this.updateOfflinePlayer(this.getKey(), value);
						return value;
					}
				};
			}
		}
		return null;
	}

	public void updateOfflinePlayer(UUID uuid, float luck) {
		this.playersYaml.getConfig().set("players." + uuid.toString() + ".luck", String.valueOf(luck));
		this.playersYaml.save();
		this.playersYaml.reload();
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
		this.getServer().getPluginManager().registerEvents(new LeaveLuckEvent(this), this);
		this.getServer().getPluginManager().registerEvents(new BreakEvent(this), this);
		this.getServer().getPluginManager().registerEvents(new PlaceEvent(this), this);
	}

	@Override
	public void onDisable() {
		this.savePlayerLuckMap();
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
