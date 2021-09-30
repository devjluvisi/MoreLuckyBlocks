package devjluvisi.mlb.cmds.lb;

import org.bukkit.command.CommandSender;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;

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
		return ExecutionResult.PASSED;
	}

}
