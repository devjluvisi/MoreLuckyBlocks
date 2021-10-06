package devjluvisi.mlb.cmds.general;

import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;
import org.bukkit.command.CommandSender;

/**
 * "/mlb perms" Reveals to the user what permissions and commands they have
 * access to. If the user is OP, it will display to the user all commands and
 * permission nodes.
 *
 * @author jacob
 */
public class PermissionsCommand implements SubCommand {

    @Override
    public String getName() {
        return "perms";
    }

    @Override
    public String getDescription() {
        return "A list of MoreLuckyBlock permissions the user has.";
    }

    @Override
    public String getSyntax() {
        return "/mlb perms";
    }

    @Override
    public String getPermission() {
        return "mlb.perms";
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
