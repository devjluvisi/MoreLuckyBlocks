package devjluvisi.mlb.cmds.admin;

import org.bukkit.command.CommandSender;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;

/**
 * "/mlb settings" Will display a GUI of settings the user can change. These
 * settings are basically just the values stored in Config.yml that can be
 * edited in real time.
 *
 * @author jacob
 *
 */
public class SettingsCommand implements SubCommand {

	private final MoreLuckyBlocks plugin;

	public SettingsCommand(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getName() {
		return "settings";
	}

	@Override
	public String getDescription() {
		return "Adjust config settings in game using a GUI.";
	}

	@Override
	public String getSyntax() {
		return "/mlb settings";
	}

	@Override
	public String getPermission() {
		return "mlb.admin.settings";
	}

	@Override
	public boolean isAllowConsole() {
		return false;
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
