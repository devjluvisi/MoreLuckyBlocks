package devjluvisi.mlb.cmds;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.admin.*;
import devjluvisi.mlb.cmds.admin.exchanges.SetExchangeCommand;
import devjluvisi.mlb.cmds.admin.struct.ExitCommand;
import devjluvisi.mlb.cmds.admin.struct.SaveCommand;
import devjluvisi.mlb.cmds.admin.struct.StructureCommand;
import devjluvisi.mlb.cmds.general.*;
import devjluvisi.mlb.cmds.lb.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Class which manages the base command within the plugin "/mlb" and then goes
 * through all of the individual "subcommands" which have their own seperate
 * classes.
 * <p>
 * Every command in the plugin routes through this class during the execution
 * cycle.
 *
 * @author jacob
 */
public class CommandManager implements CommandExecutor {

    private final LinkedList<SubCommand> subcommands;

    public CommandManager(MoreLuckyBlocks plugin) {
        this.subcommands = new LinkedList<>();
        this.subcommands.addLast(new VersionCommand(plugin));
        this.subcommands.addLast(new InfoCommand(plugin));
        this.subcommands.addLast(new ListCommand(plugin));
        this.subcommands.addLast(new RedeemCommand(plugin));
        this.subcommands.addLast(new DropsCommand(plugin));
        this.subcommands.addLast(new UsageCommand(this));
        this.subcommands.addLast(new PermissionsCommand(this));
        this.subcommands.addLast(new LuckCommand(plugin));
        this.subcommands.addLast(new BriefCommand());
        this.subcommands.addLast(new ConfigCommand(plugin));
        this.subcommands.addLast(new CreateCommand(plugin));
        this.subcommands.addLast(new StructureCommand(plugin));
        this.subcommands.addLast(new DisableCommand(plugin));
        this.subcommands.addLast(new EditCommand(plugin));
        this.subcommands.addLast(new GiveCommand(plugin));
        this.subcommands.addLast(new TransformCommand(plugin));
        this.subcommands.addLast(new ItemCommand());
        this.subcommands.addLast(new ViewToolCommand(plugin));
        this.subcommands.addLast(new LuckSetCommand(plugin));
        this.subcommands.addLast(new SetExchangeCommand(plugin));
        this.subcommands.addLast(new PurgeCommand(plugin));
        this.subcommands.addLast(new ExitCommand(plugin));
        this.subcommands.addLast(new SaveCommand(plugin));
        this.subcommands.addLast(new ResetCommand(plugin));
        this.subcommands.addLast(new SettingsCommand(plugin));
        this.subcommands.addLast(new TrackCommand(plugin));
        this.subcommands.addLast(new TestCommand(plugin));
        this.subcommands.addLast(new HelpCommand(this));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!cmd.getName().equalsIgnoreCase("mlb")) {
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + "[MoreLuckyBlocks] " + ChatColor.RED
                    + "Unknown Command.\nType /mlb help to view a list of commands.");
            return true;
        }

        // Go through all of the sub commands and check if the argument matches.
        for (final SubCommand sub : this.subcommands) {
            if (args[0].equalsIgnoreCase(sub.getName())) {
                if (!(sender instanceof Player) && !sub.isAllowConsole()) {
                    sender.sendMessage(ChatColor.RED + "You must be a player to execute this command.");
                    return true;
                }

                if ((!sub.getPermission().isBlank()) && !sender.hasPermission(sub.getPermission())) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to do this.");
                    return true;
                }

                // If the arguments provided in the command match the exact range of the
                // subcommand.
                if (!sub.getArgumentRange().isInRange(args.length)) {
                    sender.sendMessage(ChatColor.RED + "Incorrect Usage.");
                    sender.sendMessage(ChatColor.RED + sub.getSyntax());
                    return true;
                }
                // Get the result that comes out after the subcommand is performed.
                final CommandResult result = sub.perform(sender, args);
                if (result.getResult() == ResultType.PASSED) {
                    // Play particles or sound.
                    return true;
                } else if (result.getResult() == ResultType.GENERAL_FAILURE) {
                    return true;
                }
                sender.sendMessage(ChatColor.RED + "There was a problem executing your command.");
                switch (result.getResult()) {
                    case INVALID_PLAYER -> {
                        sender.sendMessage(MessageFormat.format("{0}Could not find specified player {1}\"{2}\"" + ChatColor.RED + ".", ChatColor.RED, ChatColor.YELLOW, result.getBadArg()));
                    }
                    case BAD_ARGUMENT_TYPE -> {
                        if (!result.hasDetail()) {
                            sender.sendMessage("Invalid argument specified.");
                            break;
                        }
                        sender.sendMessage(ChatColor.RED + "Invalid argument type: \"" + result.getBadArg() + "\"" + ChatColor.RED + ".");
                        sender.sendMessage(ChatColor.RED + sub.getSyntax());
                    }
                    case BAD_USAGE -> {
                        sender.sendMessage(ChatColor.RED + "Invalid Usage.");
                        sender.sendMessage(ChatColor.RED + cmd.getUsage());
                    }
                    case BAD_REQUEST -> {
                        sender.sendMessage(ChatColor.RED + "You cannot execute this command right now.");
                    }
                    case INVALID_PERMISSION -> {
                        sender.sendMessage(ChatColor.RED + "You do not have permission to do this.");
                    }
                    case INVALID_LUCKY_BLOCK -> {
                        sender.sendMessage(ChatColor.RED + "Could not find lucky block \"" + result.getBadArg() + "\"" + ChatColor.RED + ".");
                    }
                    case INVALID_MATERIAL -> {
                        sender.sendMessage(ChatColor.RED + "Invalid material specified: \"" + result.getBadArg() + "\"" + ChatColor.RED + ".");
                    }
                    default -> {
                        return true;
                    }
                }
                return true;
            }
        }

        // This executes if the user attempted to enter a subcommand using /mlb but the
        // subcommand does not exist.
        sender.sendMessage(ChatColor.RED + "Unknown Command.\nmlb help for a list of commands.");
        return true;
    }

    public List<SubCommand> getSubcommands() {
        return this.subcommands;
    }

}
