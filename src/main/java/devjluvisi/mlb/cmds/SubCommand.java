package devjluvisi.mlb.cmds;

import devjluvisi.mlb.util.Range;
import org.bukkit.command.CommandSender;

/**
 * Represents the individual methods that each sub command has. These methods
 * help the subcommands communicate with the CommandManager.
 *
 * @author jacob
 */
public interface SubCommand {

    String getName();

    String getDescription();

    String getSyntax();

    String getPermission();

    boolean isAllowConsole();

    Range getArgumentRange();

    /**
     * Performs a subcommand.
     *
     * @param sender The sender executing the command.
     * @param args   The arguments specified by the command.
     * @return The result which the subcommand returns.
     */
    CommandResult perform(CommandSender sender, String[] args);

}