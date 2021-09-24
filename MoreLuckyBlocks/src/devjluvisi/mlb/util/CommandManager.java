package devjluvisi.mlb.util;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.admin.GiveCommand;
import devjluvisi.mlb.cmds.admin.ReloadConfigCommand;
import devjluvisi.mlb.cmds.general.HelpCommand;
import devjluvisi.mlb.cmds.general.VersionCommand;
import devjluvisi.mlb.cmds.lb.ListCommand;
import devjluvisi.mlb.util.SubCommand.ExecutionResult;
import net.md_5.bungee.api.ChatColor;

/**
 * Class which manages the base command within the plugin "/mlb" and then goes through all of the individual
 * "subcommands" which have their own seperate classes.
 * 
 * Every command in the plugin routes through this class during the execution cycle.
 * @author jacob
 */
public class CommandManager implements CommandExecutor {
	
	/** A list of all possible subcommands in the plugin arg[0]. */
	private ArrayList<SubCommand> subcommands;
	private MoreLuckyBlocks plugin;
	
	public CommandManager(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
		this.subcommands = new ArrayList<SubCommand>();
		
		subcommands.add(new VersionCommand(plugin));
		subcommands.add(new ListCommand(plugin));
		subcommands.add(new GiveCommand(plugin));
		subcommands.add(new ReloadConfigCommand(plugin));
		
		// Make sure this help command stays at the bottom other wise other commands will not show up in /mlb help
		subcommands.add(new HelpCommand(subcommands));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(ChatColor.GOLD + "[MoreLuckyBlocks] " + ChatColor.RED + "Unknown Command.\nType /mlb help to view a list of commands.");
			return true;
		}
		
		// Go through all of the sub commands and check if the argument matches.
		for(SubCommand sub : subcommands) {
			if(args[0].equalsIgnoreCase(sub.getName())) {
				
				if(!(sender instanceof Player) && !sub.isAllowConsole()) {
					sender.sendMessage(ChatColor.RED + "You must be a player to execute this command.");
					return true;
				}
				
				if(sub.getPermission() != null && !sender.hasPermission(sub.getPermission())) {
					sender.sendMessage(ChatColor.RED + "You do not have permission to do this.");
					return true;
				}
				
				// If the arguments provided in the command match the exact range of the subcommand.
				if(!sub.getArgumentRange().isInRange(args.length)) {
					sender.sendMessage(ChatColor.RED + "Incorrect Usage.");
					sender.sendMessage(ChatColor.RED + sub.getSyntax());
					return true;
				}
				// Get the result that comes out after the subcommand is performed.
				final ExecutionResult result = sub.perform(sender, args);
				
				switch(result) {
				case PASSED:
					break;
				case BAD_ARGUMENT_TYPE:
					sender.sendMessage(ChatColor.RED + "Arguments not in correct format.");
					break;
				case INVALID_PLAYER:
					sender.sendMessage(ChatColor.RED + "Player could not be found.");
					break;
				}
				return true;
			}
		}
		
		// This executes if the user attempted to enter a subcommand using /mlb but the subcommand does not exist.
		sender.sendMessage(ChatColor.RED + "Unknown Command.\nmlb help for a list of commands.");
		return true;
	}
	
	

}
