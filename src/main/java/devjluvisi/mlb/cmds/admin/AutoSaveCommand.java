package devjluvisi.mlb.cmds.admin;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class AutoSaveCommand implements SubCommand {

    private MoreLuckyBlocks plugin;

    public AutoSaveCommand(MoreLuckyBlocks plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "autosave";
    }

    @Override
    public String getDescription() {
        return "Information about autosave.";
    }

    @Override
    public String getSyntax() {
        return "/mlb autosave";
    }

    @Override
    public String getPermission() {
        return "mlb.admin.autosave";
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
    public CommandResult perform(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.GRAY + "MoreLuckyBlocks Auto Saving: ");
        if (plugin.getSettingsManager().isAutoSaveEnabled()) {
            sender.sendMessage(ChatColor.GREEN + "✔ Currently Enabled");
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Saving edits to lucky blocks whenever they happen.");
            if (plugin.getSettingsManager().getBlockDataSaveInterval() != 0) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "Saving lucky block world data every " + ChatColor.GRAY + (plugin.getSettingsManager().getBlockDataSaveInterval()) + ChatColor.LIGHT_PURPLE + " seconds.");
            } else {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "Currently not saving lucky block world data on an interval.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "✖ Currently Disabled");
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "No auto-saving is currently taking place.");
        }
        sender.sendMessage(ChatColor.GRAY + "Use \"/mlb settings\" to adjust the values of auto saving on the server.");
        return new CommandResult(ResultType.PASSED);
    }

}
