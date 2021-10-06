package devjluvisi.mlb.cmds.admin;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;
import org.bukkit.command.CommandSender;

/**
 * "/mlb reset" Allows the user to delete all current lucky block data. Opens a
 * confirm GUI before execution.
 *
 * @author jacob
 */
public record ResetCommand(MoreLuckyBlocks plugin) implements SubCommand {

    @Override
    public String getName() {
        return "reset";
    }

    @Override
    public String getDescription() {
        return "Erases all user-placed lucky block data on the server (removes all luckyblocks from world).";
    }

    @Override
    public String getSyntax() {
        return "/mlb reset";
    }

    @Override
    public String getPermission() {
        return "mlb.admin.reset";
    }

    @Override
    public boolean isAllowConsole() {
        return true; // No Confirm GUI
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
