package devjluvisi.mlb.cmds.general;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

public record VersionCommand(MoreLuckyBlocks plugin) implements SubCommand {

    @Override
    public String getName() {
        return "version";
    }

    @Override
    public String getDescription() {
        return "Gives the version information for MoreLuckyBlocks";
    }

    @Override
    public String getSyntax() {
        return "/mlb version";
    }

    @Override
    public String getPermission() {
        return "mlb.version";
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
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + ChatColor.STRIKETHROUGH
                + "--------------------------------");
        sender.sendMessage(ChatColor.GOLD + "MoreLuckyBlocks " + ChatColor.GRAY + "(" + ChatColor.YELLOW
                + ChatColor.BOLD + "v" + this.plugin.getDescription().getVersion() + ChatColor.GRAY + ")");
        sender.sendMessage(
                ChatColor.GRAY + "API Version: " + ChatColor.RED + this.plugin.getDescription().getAPIVersion());
        sender.sendMessage(ChatColor.GRAY + "Server Version: " + ChatColor.RED
                + this.plugin.getServer().getVersion().substring(this.plugin.getServer().getVersion().indexOf('(')));
        sender.sendMessage(ChatColor.GRAY + "Author: " + ChatColor.RED + this.plugin.getDescription().getAuthors());
        sender.sendMessage("");
        sender.sendMessage(ChatColor.BLUE.toString() + ChatColor.UNDERLINE + this.plugin.getDescription().getWebsite());
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + ChatColor.STRIKETHROUGH
                + "--------------------------------");
        return new CommandResult(ResultType.PASSED);
    }

}
