package devjluvisi.mlb.cmds.admin;

import org.bukkit.command.CommandSender;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;

/**
 * "/mlb purge <internal name>" Removes a lucky block from the config.
 *
 * @author jacob
 *
 */
public class PurgeCommand implements SubCommand {

	private final MoreLuckyBlocks plugin;

	public PurgeCommand(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getName() {
		return "purge";
	}

	@Override
	public String getDescription() {
		return "Deletes a lucky block. Requires confirmation.";
	}

	@Override
	public String getSyntax() {
		return "/mlb purge <name>";
	}

	@Override
	public String getPermission() {
		return "mlb.admin.purge";
	}

	@Override
	public boolean isAllowConsole() {
		return true; // No GUI is shown
	}

	@Override
	public Range getArgumentRange() {
		return new Range(2, 2);
	}

	@Override
	public ExecutionResult perform(CommandSender sender, String[] args) {

		return ExecutionResult.PASSED;
	}

}
