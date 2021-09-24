package devjluvisi.mlb.cmds.general;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

import devjluvisi.mlb.util.Range;
import devjluvisi.mlb.util.SubCommand;
import net.md_5.bungee.api.ChatColor;

public class HelpCommand implements SubCommand {
	
	private ArrayList<SubCommand> commands;
	
	/**
	 * The maximum amount of commands the help command is allowed to display
	 * between each page.
	 */
	private static final byte MAX_COMMANDS_PER_PAGE = 6;
	
	public HelpCommand(ArrayList<SubCommand> list) {
		commands = list;
	}

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getDescription() {
		return "Sends a message describing all of the possible commands in the plugin.";
	}

	@Override
	public String getSyntax() {
		return "/mlb help <[?]page>";
	}

	@Override
	public String getPermission() {
		return "mlb.help";
	}

	@Override
	public boolean isAllowConsole() {
		return true;
	}

	@Override
	public Range getArgumentRange() {
		return new Range(1, 2);
	}

	@Override
	public ExecutionResult perform(CommandSender sender, String[] args) {
		if(args.length == 1) {
			sender.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "-----------------" + ChatColor.RESET.toString() + ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + "[" + ChatColor.RESET + ChatColor.GOLD.toString()+ChatColor.BOLD.toString()+"MoreLuckyBlocks" + ChatColor.RESET.toString() + ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + "]" + ChatColor.RESET + ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "-----------------");
			for(int i = 0; i < MAX_COMMANDS_PER_PAGE && i < commands.size(); i++) {
				sender.sendMessage(ChatColor.LIGHT_PURPLE + "/mlb " + commands.get(i).getName() + ChatColor.GRAY + " - " + ChatColor.GREEN + commands.get(i).getDescription());
			}
			sender.sendMessage(ChatColor.BLUE.toString() + ChatColor.BOLD + "<< " + ChatColor.RESET + ChatColor.BLUE + "Page 1/"+((commands.size()/MAX_COMMANDS_PER_PAGE)+commands.size()%MAX_COMMANDS_PER_PAGE != 0 ? 1 : 0) + ChatColor.BOLD + " >>");
			sender.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "----------------------------------------------------");
			return ExecutionResult.PASSED;
		}
		int page;
		try {
			page = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			return ExecutionResult.BAD_ARGUMENT_TYPE;
		}
		if(page > ((commands.size()/MAX_COMMANDS_PER_PAGE)+commands.size()%MAX_COMMANDS_PER_PAGE != 0 ? 1 : 0)) {
			sender.sendMessage(ChatColor.RED + "There are no commands to view on this page.");
			return ExecutionResult.PASSED;
		}
		final int commandRangeMin = (page * MAX_COMMANDS_PER_PAGE) - MAX_COMMANDS_PER_PAGE;
		final int commandRangeMax = page * MAX_COMMANDS_PER_PAGE;
		sender.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "-----------------" + ChatColor.RESET.toString() + ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + "[" + ChatColor.RESET + ChatColor.GOLD.toString()+ChatColor.BOLD.toString()+"MoreLuckyBlocks" + ChatColor.RESET.toString() + ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + "]" + ChatColor.RESET + ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "-----------------");
		for(int i = commandRangeMin; i < commandRangeMax && i < commands.size(); i++) {
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "/mlb " + commands.get(i).getName() + ChatColor.GRAY + " - " + ChatColor.GREEN + commands.get(i).getDescription());
		}
		sender.sendMessage(ChatColor.BLUE.toString() + ChatColor.BOLD + "<< " + ChatColor.RESET + ChatColor.BLUE + "Page " + page + "/"+((commands.size()/MAX_COMMANDS_PER_PAGE)+commands.size()%MAX_COMMANDS_PER_PAGE != 0 ? 1 : 0) + ChatColor.BOLD + " >>");
		sender.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "----------------------------------------------------");
		return ExecutionResult.PASSED;
		
	}

}