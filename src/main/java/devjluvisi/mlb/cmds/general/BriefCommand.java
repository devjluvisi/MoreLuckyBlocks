package devjluvisi.mlb.cmds.general;

import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;
import org.bukkit.command.CommandSender;

/**
 * "/mlb brief" Shows the user a book and quill with information about how the
 * plugin works and how the lucky blocks work.
 *
 * @author jacob
 */
public class BriefCommand implements SubCommand {

    @Override
    public String getName() {
        return "brief";
    }

    @Override
    public String getDescription() {
        return "Displays a Book & Quill with information about Lucky Blocks.";
    }

    @Override
    public String getSyntax() {
        return "/mlb brief";
    }

    @Override
    public String getPermission() {
        return "mlb.brief";
    }

    @Override
    public boolean isAllowConsole() {
        return true; // No Book & Quill, a text message is directly sent instead.
    }

    @Override
    public Range getArgumentRange() {
        return new Range(1, 1);
    }

    @Override
    public CommandResult perform(CommandSender sender, String[] args) {

        return new CommandResult(ResultType.PASSED);

    }

}
