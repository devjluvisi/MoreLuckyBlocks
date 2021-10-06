package devjluvisi.mlb.cmds.admin;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;
import org.bukkit.command.CommandSender;

/**
 * "/mlb purge <internal name>" Removes a lucky block from the config.
 *
 * @author jacob
 */
public record PurgeCommand(MoreLuckyBlocks plugin) implements SubCommand {

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
