package devjluvisi.mlb.cmds.admin;

import org.bukkit.command.CommandSender;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;

/**
 * TODO: Move the /mlb reload and /mlb save commands into this command. <br/>
 * - "/mlb config save" - Saves the config from what has been modified in game.
 * <br />
 * - "/mlb config reload" - Reloads the config and updates the server with the
 * data from the config. <br />
 * - "/mlb config regen" - Load the default config. <br />
 * - "/mlb config help" - Get a list of config help and commands.
 *
 * @author jacob
 *
 */
public class ConfigCommand implements SubCommand {

	private final MoreLuckyBlocks plugin;

	public ConfigCommand(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getName() {
		return "config";
	}

	@Override
	public String getDescription() {
		return "Perform actions on the config.";
	}

	@Override
	public String getSyntax() {
		return "/mlb config <save|reload|regen|help>";
	}

	@Override
	public String getPermission() {
		return "mlb.admin.config";
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
