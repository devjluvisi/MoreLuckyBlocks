package devjluvisi.mlb.cmds.lb;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;
import org.bukkit.command.CommandSender;

/**
 * "/mlb drops" Will display all possible drops to the user (GUI) based on the
 * lucky block in their hand. If a argument is specified, "/mlb drops <internal
 * name>" then a GUI will open for that lucky block.
 *
 * @author jacob
 */
public record DropsCommand(MoreLuckyBlocks plugin) implements SubCommand {

    @Override
    public String getName() {
        return "drops";
    }

    @Override
    public String getDescription() {
        return "Allows a user to see drops for a luckyblock.";
    }

    @Override
    public String getSyntax() {
        return "/mlb drops";
    }

    @Override
    public String getPermission() {
        return "mlb.drops";
    }

    @Override
    public boolean isAllowConsole() {
        return true;
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
