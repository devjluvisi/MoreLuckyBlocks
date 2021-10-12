package devjluvisi.mlb.cmds.lb;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.util.Range;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public record ListCommand(MoreLuckyBlocks plugin) implements SubCommand {

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "A GUI list of all of the current lucky blocks on the server.";
    }

    @Override
    public String getSyntax() {
        return "/mlb list";
    }

    @Override
    public String getPermission() {
        return "";
    }

    @Override
    public boolean isAllowConsole() {
        return false;
    }

    @Override
    public Range getArgumentRange() {
        return new Range(1, 1);
    }

    @Override
    public CommandResult perform(CommandSender sender, String[] args) {
        if (plugin.getLuckyBlocks().size() == 0) {
            sender.sendMessage(ChatColor.RED + "Could not run this command because you have no lucky blocks on this server.");
            sender.sendMessage(ChatColor.GRAY + "Try /mlb create to create a new lucky block.");
            return new CommandResult(ResultType.PASSED);
        }
        MenuType type = MenuType.USER_LIST_LUCKY_BLOCKS;
        if (sender.hasPermission("mlb.admin")) {
            type = MenuType.LIST_LUCKY_BLOCKS;
        }
        new MenuManager(plugin).open((Player) sender, type);
        return new CommandResult(ResultType.PASSED);
    }

}
