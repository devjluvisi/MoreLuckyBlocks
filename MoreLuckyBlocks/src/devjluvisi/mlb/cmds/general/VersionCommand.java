package devjluvisi.mlb.cmds.general;

import org.bukkit.command.CommandSender;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.util.Range;
import devjluvisi.mlb.util.SubCommand;
import net.md_5.bungee.api.ChatColor;

public class VersionCommand implements SubCommand {
	
	private MoreLuckyBlocks plugin;
	public VersionCommand(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getName() {
		return "version";
	}

	@Override
	public String getDescription() {
		return "Gives the version information for MoreLuckyBlocks";
	}

	@Override
	public String getSyntax() {
		return "/mlb version";
	}

	@Override
	public String getPermission() {
		return "mlb.version";
	}

	@Override
	public boolean isAllowConsole() {
		return true;
	}

	@Override
	public Range getArgumentRange() {
		return new Range(1, 1);
	}

	@Override
	public ExecutionResult perform(CommandSender sender, String[] args) {
		sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + ChatColor.STRIKETHROUGH.toString() + "--------------------------------");
		sender.sendMessage(ChatColor.GOLD + "MoreLuckyBlocks " + ChatColor.GRAY + "(" + ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "v" + plugin.getDescription().getVersion() + ChatColor.GRAY + ")");
		sender.sendMessage(ChatColor.GRAY + "API Version: " + ChatColor.RED + plugin.getDescription().getAPIVersion());
		sender.sendMessage(ChatColor.GRAY + "Server Version: " + ChatColor.RED + plugin.getServer().getVersion().substring(plugin.getServer().getVersion().indexOf('(')));
		sender.sendMessage(ChatColor.GRAY + "Author: " + ChatColor.RED + plugin.getDescription().getAuthors());
		sender.sendMessage("");
		sender.sendMessage(ChatColor.BLUE.toString() + ChatColor.UNDERLINE + plugin.getDescription().getWebsite());
		sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + ChatColor.STRIKETHROUGH.toString() + "--------------------------------");
		
		return ExecutionResult.PASSED;
	}

}
