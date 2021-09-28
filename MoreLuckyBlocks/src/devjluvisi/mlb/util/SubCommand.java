package devjluvisi.mlb.util;

import org.bukkit.command.CommandSender;

/**
 * Represents the individual methods that each sub command has. These methods
 * help the subcommands communicate with the CommandManager.
 *
 * @author jacob
 *
 */
public interface SubCommand {

	/**
	 * A "result" which is returned by the command. Results are returned because
	 * sometimes individual commands might want additional checks not provided in
	 * {@link CommandManager} so this enum represents specific (but still common)
	 * errors a command can return.
	 */
	enum ExecutionResult {
		PASSED, INVALID_PLAYER, BAD_ARGUMENT_TYPE;
	}

	public String getName();

	public String getDescription();

	public String getSyntax();

	public String getPermission();

	public boolean isAllowConsole();

	public Range getArgumentRange();

	/**
	 * Performs a subcommand.
	 *
	 * @param sender The sender executing the command.
	 * @param args   The arguments specified by the command.
	 * @return The result which the subcommand returns.
	 */
	public ExecutionResult perform(CommandSender sender, String[] args);

}