package devjluvisi.mlb.util;

import org.bukkit.command.CommandSender;

public interface SubCommand {
	
	enum ExecutionResult {
		PASSED, INVALID_PLAYER, BAD_ARGUMENT_TYPE;
	}
	
	public String getName();
	public String getDescription();
	public String getSyntax();
	public String getPermission();
	public boolean isAllowConsole();
	public Range getArgumentRange();
	
	public ExecutionResult perform(CommandSender sender, String[] args);
	
}