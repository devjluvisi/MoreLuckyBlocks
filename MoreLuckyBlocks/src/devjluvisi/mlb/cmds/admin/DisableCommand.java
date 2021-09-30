package devjluvisi.mlb.cmds.admin;

import org.bukkit.command.CommandSender;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;

/**
 * - "/mlb disable" - Disables the plugin for the current server instance (until
 * reload) Requires confirmation prompt.
 *
 * @author jacob
 *
 */
public class DisableCommand implements SubCommand {

	private final MoreLuckyBlocks plugin;

	public DisableCommand(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getName() {
		return "disable";
	}

	@Override
	public String getDescription() {
		return "Disable the plugin until the next reload/restart.";
	}

	@Override
	public String getSyntax() {
		return "/mlb disable";
	}

	@Override
	public String getPermission() {
		return "mlb.admin.disable";
	}

	@Override
	public boolean isAllowConsole() {
		return true; // No Confirm
	}

	@Override
	public Range getArgumentRange() {
		return new Range(1, 1);
	}

	@Override
	public ExecutionResult perform(CommandSender sender, String[] args) {
		return ExecutionResult.PASSED;
	}

}
