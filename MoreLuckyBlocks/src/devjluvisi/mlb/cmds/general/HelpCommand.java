package devjluvisi.mlb.cmds.general;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

import devjluvisi.mlb.util.Range;
import devjluvisi.mlb.util.SubCommand;
import net.md_5.bungee.api.ChatColor;

/**
 * Lists the commands available to the user. Will not display commands the user
 * does not have permissions for.
 *
 * @author jacob
 *
 */
public class HelpCommand implements SubCommand {

	private final ArrayList<SubCommand> commands;

	/**
	 * The maximum amount of commands the help command is allowed to display between
	 * each page.
	 */
	private static final byte MAX_COMMANDS_PER_PAGE = 6;

	public HelpCommand(ArrayList<SubCommand> list) {
		this.commands = list;
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
		if (args.length == 1) {
			sender.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "-----------------"
					+ ChatColor.RESET.toString() + ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + "["
					+ ChatColor.RESET + ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "MoreLuckyBlocks"
					+ ChatColor.RESET.toString() + ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + "]"
					+ ChatColor.RESET + ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH.toString()
					+ "-----------------");
			for (int i = 0; (i < MAX_COMMANDS_PER_PAGE) && (i < this.commands.size()); i++) {
				sender.sendMessage(ChatColor.LIGHT_PURPLE + "/mlb " + this.commands.get(i).getName() + ChatColor.GRAY
						+ " - " + ChatColor.GREEN + this.commands.get(i).getDescription());
			}
			sender.sendMessage(
					ChatColor.BLUE.toString() + ChatColor.BOLD + "<< " + ChatColor.RESET + ChatColor.BLUE + "Page 1/"
							+ ((this.commands.size() / MAX_COMMANDS_PER_PAGE)
									+ ((this.commands.size() % MAX_COMMANDS_PER_PAGE) != 0 ? 1 : 0))
							+ ChatColor.BOLD + " >>");
			sender.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH.toString()
					+ "----------------------------------------------------");
			return ExecutionResult.PASSED;
		}
		// "/mlb help <page>
		int page;
		try {
			page = Integer.parseInt(args[1]);
			if (page < 1) {
				throw new NumberFormatException("Bad Integer. Must be greater then 1.");
			}
		} catch (final NumberFormatException e) {
			return ExecutionResult.BAD_ARGUMENT_TYPE;
		}
		// Make sure the page is not over the limit.
		if (page > ((this.commands.size() / MAX_COMMANDS_PER_PAGE)
				+ ((this.commands.size() % MAX_COMMANDS_PER_PAGE) != 0 ? 1 : 0))) {
			sender.sendMessage(ChatColor.RED + "There are no commands to view on this page.");
			return ExecutionResult.PASSED;
		}
		// Get the range of commands to show the player.
		final int commandRangeMin = (page * MAX_COMMANDS_PER_PAGE) - MAX_COMMANDS_PER_PAGE;
		final int commandRangeMax = page * MAX_COMMANDS_PER_PAGE;

		sender.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "-----------------"
				+ ChatColor.RESET.toString() + ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + "["
				+ ChatColor.RESET + ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "MoreLuckyBlocks"
				+ ChatColor.RESET.toString() + ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + "]"
				+ ChatColor.RESET + ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH.toString()
				+ "-----------------");
		for (int i = commandRangeMin; (i < commandRangeMax) && (i < this.commands.size()); i++) {
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "/mlb " + this.commands.get(i).getName() + ChatColor.GRAY
					+ " - " + ChatColor.GREEN + this.commands.get(i).getDescription());
		}
		sender.sendMessage(
				ChatColor.BLUE.toString() + ChatColor.BOLD + "<< " + ChatColor.RESET + ChatColor.BLUE + "Page " + page
						+ "/"
						+ ((this.commands.size() / MAX_COMMANDS_PER_PAGE)
								+ ((this.commands.size() % MAX_COMMANDS_PER_PAGE) != 0 ? 1 : 0))
						+ ChatColor.BOLD + " >>");
		sender.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH.toString()
				+ "----------------------------------------------------");
		return ExecutionResult.PASSED;

	}

}
