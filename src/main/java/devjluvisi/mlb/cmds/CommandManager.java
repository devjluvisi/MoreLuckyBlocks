package devjluvisi.mlb.cmds;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.admin.*;
import devjluvisi.mlb.cmds.admin.exchanges.SetExchangeCommand;
import devjluvisi.mlb.cmds.admin.struct.ExitCommand;
import devjluvisi.mlb.cmds.admin.struct.SaveCommand;
import devjluvisi.mlb.cmds.admin.struct.StructureCommand;
import devjluvisi.mlb.cmds.general.*;
import devjluvisi.mlb.cmds.lb.*;
import devjluvisi.mlb.events.custom.LogDataEvent;
import devjluvisi.mlb.util.config.files.messages.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

    private final MoreLuckyBlocks plugin;
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
        this.subcommands.addLast(new AutoSaveCommand(plugin));
        this.subcommands.addLast(new TrackCommand(plugin));
        this.subcommands.addLast(new TestCommand(plugin));
        this.subcommands.addLast(new HelpCommand(this));
        this.subcommands.removeIf(sub -> plugin.getSettingsManager().getDisabledCommands().contains(sub.getName()));
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("mlb")) {
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage(Message.UNKNOWN_COMMAND.get());
            return true;
        }

        // Go through all of the sub commands and check if the argument matches.
        for (final SubCommand sub : this.subcommands) {
            if (args[0].equalsIgnoreCase(sub.getName())) {
                if (!(sender instanceof Player) && !sub.isAllowConsole()) {
                    sender.sendMessage(Message.MUST_BE_PLAYER.get());
                    return true;
                }

                if ((!sub.getPermission().isBlank()) && !sender.hasPermission(sub.getPermission())) {
                    sender.sendMessage(Message.NO_PERMISSION.get());
                    return true;
                }

                // If the arguments provided in the command match the exact range of the
                // subcommand.
                if (!sub.getArgumentRange().isInRange(args.length)) {
                    sender.sendMessage(Message.BAD_COMMAND_USAGE.format(sub.getSyntax()));
                    return true;
                }

                // If the command is disabled.
                if(plugin.getSettingsManager().getDisabledCommands().contains(sub.getName())) {
                    sender.sendMessage(Message.DISABLED_COMMAND.get());
                    return true;
                }

                // Get the result that comes out after the subcommand is performed.
                final CommandResult result = sub.perform(sender, args);
                if (result.getResult() == ResultType.GENERAL_FAILURE) {
                    return true;
                }
                if (result.getResult() != ResultType.PASSED) {
                    sender.sendMessage(Message.GENERAL_COMMAND_ERROR.get());
                }
                switch (result.getResult()) {
                    case INVALID_PLAYER -> {
                        sender.sendMessage(Message.UNKNOWN_PLAYER.format(result.getBadArg()));
                    }
                    case BAD_ARGUMENT_TYPE -> {
                        if (!result.hasDetail()) {
                            sender.sendMessage(Message.BAD_ARGUMENT_1.get());
                            break;
                        }
                        sender.sendMessage(Message.BAD_ARGUMENT_2.format(result.getBadArg()));
                    }
                    case BAD_USAGE -> {
                        sender.sendMessage(Message.BAD_COMMAND_USAGE.format(sub.getSyntax()));
                    }
                    case BAD_REQUEST -> {
                        sender.sendMessage(Message.BAD_REQUEST.get());
                    }
                    case INVALID_PERMISSION -> {
                        sender.sendMessage(Message.NO_PERMISSION.get());
                    }
                    case INVALID_LUCKY_BLOCK -> {
                        sender.sendMessage(Message.UNKNOWN_LUCKY_BLOCK.format(result.getBadArg()));
                    }
                    case INVALID_MATERIAL -> {
                        sender.sendMessage(Message.BAD_MATERIAL.format(result.getBadArg()));
                    }
                    default -> {
                        StringBuilder linkedArgs = new StringBuilder();
                        for (String s : args) {
                            linkedArgs.append(s).append(" ");
                        }
                        plugin.getServer().getPluginManager().callEvent(new LogDataEvent(sender.getName() + " executed command /" + cmd.getName() + " " + linkedArgs));
                        return true;
                    }
                }
                return true;
            }
        }

        // This executes if the user attempted to enter a subcommand using /mlb but the
        // subcommand does not exist.
        sender.sendMessage(Message.UNKNOWN_COMMAND.get());
        return true;
    }

    public List<SubCommand> getSubcommands() {
        return this.subcommands;
    }

}
