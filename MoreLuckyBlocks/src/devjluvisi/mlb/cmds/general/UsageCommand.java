package devjluvisi.mlb.cmds.general;

import org.bukkit.command.CommandSender;

import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;

/**
 * Displays the parameters and usage message for a command. "/mlb usage
 * <command>"
 *
 * This is only if the sender has permissions for this command.
 *
 * @author jacob
 *
 */
public class UsageCommand implements SubCommand {

	@Override
	public String getName() {
		return "usage";
	}

	@Override
	public String getDescription() {
		return "Display usage for a specific command.";
	}

	@Override
	public String getSyntax() {
		return "/mlb usage <subcommand>\nEx: /mlb usage give";
	}

	@Override
	public String getPermission() {
		return "mlb.usage";
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
		return ExecutionResult.PASSED;
	}

}
