package devjluvisi.mlb.cmds.admin;

import org.bukkit.command.CommandSender;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;

/**
 * For editing attributes of lucky blocks. "/mlb edit [internal name]
 * [name|lore|default luck|break permission] [value]
 *
 * @author jacob
 *
 */
public class EditCommand implements SubCommand {

	private final MoreLuckyBlocks plugin;

	public EditCommand(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getName() {
		return "edit";
	}

	@Override
	public String getDescription() {
		return "Edit attributes of a specific lucky block.";
	}

	@Override
	public String getSyntax() {
		return "/mlb edit <internal-name> <name|lore|baseluck|breakperm> <value>";
	}

	@Override
	public String getPermission() {
		return "mlb.admin.edit";
	}

	@Override
	public boolean isAllowConsole() {
		return true;
	}

	@Override
	public Range getArgumentRange() {
		return new Range(4, 4);
	}

	@Override
	public ExecutionResult perform(CommandSender sender, String[] args) {
		return ExecutionResult.PASSED;
	}

}
