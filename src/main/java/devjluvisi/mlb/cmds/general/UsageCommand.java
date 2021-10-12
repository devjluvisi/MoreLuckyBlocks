package devjluvisi.mlb.cmds.general;

import devjluvisi.mlb.cmds.CommandManager;
import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Displays the parameters and usage message for a command. "/mlb usage
 * <command>"
 * <p>
 * This is only if the sender has permissions for this command.
 *
 * @author jacob
 */
public class UsageCommand implements SubCommand {

    private final CommandManager cmdManager;

    public UsageCommand(CommandManager cmdManager) {
        this.cmdManager = cmdManager;
    }

    @Override
    public String getName() {
        return "usage";
    }

    @Override
    public String getDescription() {
        return "Display usage for a specific command.";
    }

    @Override
    public String getSyntax() {
        return "/mlb usage <subcommand>\nEx: /mlb usage give";
    }

    @Override
    public String getPermission() {
        return "mlb.usage";
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
    public CommandResult perform(CommandSender sender, String[] args) {
        for (SubCommand cmd : cmdManager.getSubcommands()) {
            if (cmd.getName().equalsIgnoreCase(args[1])) {
                sender.sendMessage(ChatColor.GRAY + "Usage of /mlb " + cmd.getName());
                sender.sendMessage(ChatColor.GREEN.toString() + cmd.getSyntax());
                sender.sendMessage(ChatColor.RESET.toString() + ChatColor.ITALIC.toString() + cmd.getDescription());
                return new CommandResult(ResultType.PASSED);
            }
        }
        sender.sendMessage(ChatColor.RED + "Could not find plugin command /mlb " + args[1]);
        return new CommandResult(ResultType.PASSED);
    }

}
