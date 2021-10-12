package devjluvisi.mlb.cmds.admin;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * TODO: Move the /mlb reload and /mlb save commands into this command. <br/>
 * - "/mlb config save" - Saves the config from what has been modified in game.
 * <br />
 * - "/mlb config reload" - Reloads the config and updates the server with the
 * data from the config. <br />
 * - "/mlb config regen" - Load the default config. <br />
 * - "/mlb config help" - Get a list of config help and commands.
 *
 * @author jacob
 */
public record ConfigCommand(MoreLuckyBlocks plugin) implements SubCommand {

    @Override
    public String getName() {
        return "config";
    }

    @Override
    public String getDescription() {
        return "Perform actions on the config.";
    }

    @Override
    public String getSyntax() {
        return "/mlb config <save|reload|regen|help>";
    }

    @Override
    public String getPermission() {
        return "mlb.admin.config";
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
        if (args[1].equalsIgnoreCase("save")) {
            sender.sendMessage(
                    ChatColor.GREEN + "Configuration files have been updated according to unmodified changes.");
            this.plugin.getBlocksYaml().setValue("lucky-blocks", null);
            this.plugin.getLuckyBlocks().save();
            this.plugin.getAudit().writeAll();
            this.plugin.getPlayerManager().save();
            this.plugin.getBlocksYaml().save();
        } else if (args[1].equalsIgnoreCase("reload")) {
            sender.sendMessage(ChatColor.BLUE + "Configuration files have been saved and reloaded.");
            this.plugin.getBlocksYaml().save();
            this.plugin.getBlocksYaml().reload();

            this.plugin.getSettingsManager().save();
            this.plugin.getSettingsManager().reload();

            this.plugin.getMessagesManager().save();
            this.plugin.getMessagesManager().reload();

            this.plugin.getLuckyBlocks().upload();
        } else {
            return new CommandResult(ResultType.BAD_USAGE);
        }
        return new CommandResult(ResultType.PASSED);
    }

}
