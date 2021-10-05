package devjluvisi.mlb.cmds.lb;

import org.bukkit.command.CommandSender;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;
import net.md_5.bungee.api.ChatColor;
import devjluvisi.mlb.helper.Util;
/**
 * Displays information related to the lucky block in chat. "/mlb info <internal
 * name>
 *
 * Will display information depending on the permissions of the user.
 *
 * @author jacob
 *
 */
public class InfoCommand implements SubCommand {

	private final MoreLuckyBlocks plugin;

	public InfoCommand(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getName() {
		return "info";
	}

	@Override
	public String getDescription() {
		return "Get information about a specific lucky block.";
	}

	@Override
	public String getSyntax() {
		return "/mlb info <name>";
	}

	@Override
	public String getPermission() {
		return "mlb.info";
	}

	@Override
	public boolean isAllowConsole() {
		return true;
	}

	@Override
	public Range getArgumentRange() {
		return new Range(2, 2);
	}

	@Override
	public ExecutionResult perform(CommandSender sender, String[] args) {
		if(!plugin.getLuckyBlocks().contains(Util.makeInternal(args[1]))) {
			sender.sendMessage("Could not find lucky block " + args[1] + ".");
			return ExecutionResult.PASSED;
		}
		LuckyBlock lb = plugin.getLuckyBlocks().get(Util.makeInternal(args[1]));
		sender.sendMessage(ChatColor.DARK_AQUA + "Info for: " + ChatColor.BLUE + lb.getInternalName());
		sender.sendMessage("  " + ChatColor.GRAY + "Item Name " + ChatColor.DARK_GRAY + "» " + ChatColor.GREEN + lb.getName());
		sender.sendMessage("  " + ChatColor.GRAY + "Block Type " + ChatColor.DARK_GRAY + "» " + ChatColor.GREEN + lb.getBlockMaterial().name());
		sender.sendMessage("  " + ChatColor.GRAY + "Default Luck " + ChatColor.DARK_GRAY + "» " + ChatColor.GREEN + lb.getDefaultBlockLuck());
		sender.sendMessage("  " + ChatColor.GRAY + "# of Drops " + ChatColor.DARK_GRAY + "» " + ChatColor.GREEN + lb.getDroppableItems().size());
		if(sender.hasPermission("mlb.admin")) {
			sender.sendMessage("  " + ChatColor.GRAY + "Break Permission " + ChatColor.DARK_GRAY + "» " + ChatColor.GREEN + lb.getBreakPermission());
			sender.sendMessage("  " + ChatColor.GRAY + "Lore Lines " + ChatColor.DARK_GRAY + "» " + ChatColor.GREEN + lb.getRefreshedLore().size());
		}
		if(sender.hasPermission(lb.getBreakPermission())) {
			sender.sendMessage(ChatColor.DARK_GREEN + "You have permission to break this lucky block.");
		}else {
			sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to break this lucky block.");
		}
		
		return ExecutionResult.PASSED;
	}

}
