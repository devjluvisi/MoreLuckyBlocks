package devjluvisi.mlb.util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.VersionCommand;
import net.md_5.bungee.api.ChatColor;

/**
 * A class which represents a command in the MoreLuckyBlocks plugin.
 * Automatically provides execution and main class access through abstraction.
 * 
 * @author jacob
 *
 */
public abstract class BaseCommand implements CommandExecutor {
	
	/**
	 * Represents different errors which can occur when attempting to execute commands.
	 *
	 */
	private enum CommandError {
		NOT_A_PLAYER, NO_PERMISSION, BAD_ARGUMENTS
	}
	
	// Compile Defined Variables
	
	private String commandName;
	private Range argsLength;
	private String usage;
	private boolean allowConsole;
	private MoreLuckyBlocks plugin;
	
	// Command Defined Variables
	
	private Player p;
	private CommandSender s;
	private String[] commandArguments;
	
	/**
	 * 
	 * The most basic command.
	 * <br />
	 * Example: /version, /spawn
	 * 
	 * @param plugin The main plugin class to reference.
	 * @param commandName The name of the command
	 */
	public BaseCommand(MoreLuckyBlocks plugin, String commandName) {
		this.plugin = plugin;
		this.commandName = commandName;
		this.argsLength = new Range(0, 0);
		this.usage = "/" + commandName;
		this.allowConsole = false;
	}
	
	/**
	 * A command with arguments and a usage case.
	 * <br />
	 * Example: /help 1, /gamemode 2
	 * 
	 * @param plugin The main plugin class to reference.
	 * @param commandName The name of the command.
	 * @param argsLength The number of arguments to execute.
	 * @param usage The proper command usage.
	 */
	public BaseCommand(MoreLuckyBlocks plugin, String commandName, int argsLength, String usage) {
		this.plugin = plugin;
		this.commandName = commandName;
		this.argsLength = new Range(argsLength, argsLength);
		this.usage = usage;
		this.allowConsole = false;
	}
	
	/**
	 * A command with arguments and a usage case which can be executed by the console as specified by the {@code allowConsole}.
	 * Example: /help 1, /difficulty peaceful
	 * 
	 * @param plugin The main plugin class to reference.
	 * @param commandName The name of the command.
	 * @param argsLength The number of arguments to execute.
	 * @param usage The proper command usage.
	 * @param allowConsole If the command can be executed from the console.
	 */
	public BaseCommand(MoreLuckyBlocks plugin, String commandName, int argsLength, String usage, boolean allowConsole) {
		this.plugin = plugin;
		this.commandName = commandName;
		this.argsLength = new Range(argsLength, argsLength);
		this.usage = usage;
		this.allowConsole = allowConsole;
	}
	
	/**
	 * The most advanced version of an FBCommand, allows a range of specified arguments.
	 * <br />
	 * The command will run execute() as long as the number of specified arguments is within the range.
	 * <br />
	 * Example: /tp foo (1 arg), /tp foo bar (2 args)
	 * 
	 * @param plugin The main plugin class to reference.
	 * @param commandName The name of the command.
	 * @param argsLengthSpan A range of arguments of which the command will accept.
	 * @param usage The proper command usage.
	 * @param allowConsole If the command can be executed from the console.
	 */
	public BaseCommand(MoreLuckyBlocks plugin, String commandName, Range argsLengthSpan, String usage, boolean allowConsole) {
		this.plugin = plugin;
		this.commandName = commandName;
		this.argsLength = argsLengthSpan;
		this.usage = usage;
		this.allowConsole = allowConsole;
	}
	
	/**
	 * Will throw an error to the console if player is null.
	 * @return The player who is executing the command if there is one.
	 */
	public Player getPlayer() {
		return p;
	}
	
	public CommandSender getSender() {
		return s;
	}
	
	/**
	 * @return Reference to the main plugin class.
	 */
	public MoreLuckyBlocks getPlugin() {
		return plugin;
	}
	
	/**
	 * @param index Which argument to get.
	 * @return The argument in the command. Null if the index is out of bounds.
	 */
	public String getArg(int index) {
		return index < commandArguments.length ? commandArguments[index] : null;
	}
	
	public int length() {
		return commandArguments.length;
	}
	
	public String name() {
		return this.commandName;
	}
	
	public String[] getArguments() {
		return this.commandArguments;
	}
	
	/**
	 * Sends an error to a sender with a message.
	 * The message for the error is dependent on the configuration file.
	 * 
	 * @param sender The sender who is executing the command.
	 * @param type The type of error to show.
	 */
	private void sendError(CommandSender sender, CommandError type) {
		if(sender == null) return;
		switch(type) {
		case BAD_ARGUMENTS:
			sender.sendMessage(ChatColor.RED + getPlugin().getMessagesYaml().getString("incorrect-usage").replaceAll("%usage%", this.usage));
			break;
		case NO_PERMISSION:
			sender.sendMessage(ChatColor.RED + getPlugin().getMessagesYaml().getString("no-permission-msg"));
			break;
		case NOT_A_PLAYER:
			sender.sendMessage(ChatColor.RED + getPlugin().getMessagesYaml().getString("must-be-player-msg"));
			break;
		default:
			sender.sendMessage(ChatColor.RED + "Could not execute this command.");
			break;
		}
		return;
	}
	
	/**
	 * @return The permission node for the specified command name as read from config.
	 */
	private String getPermissionNode() {
		return "test.tes";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase(commandName)) {
			if(!allowConsole) {
				// If the sender is a player.
				if(sender instanceof Player) {
					this.p = (Player) sender;
				}else {
					this.sendError(sender, CommandError.NOT_A_PLAYER);
					return true;
				}
			}
			if(sender instanceof Player) {
				this.p = (Player) sender;
			}
			this.s = sender;
			// If the user has the permission to execute the command.
			if(!sender.hasPermission(this.getPermissionNode())) {
				this.sendError(sender, CommandError.NO_PERMISSION);
				return true;
			}
			// Check if the amount of arguments specified is in range.
			if(!argsLength.isInRange(args.length)) {
				this.sendError(sender, CommandError.BAD_ARGUMENTS);
				return true;
			}
			// Execute the main command body.
			this.commandArguments = args;
			execute();
			return true;
		}
		return false;
	}
	
	
	/**
	 * The main command body which is executed if all of the checks of the command are valid.
	 */
	public abstract void execute();
	
	
}
