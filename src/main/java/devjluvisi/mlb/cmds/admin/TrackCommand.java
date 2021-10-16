package devjluvisi.mlb.cmds.admin;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.api.gui.Menu;
import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.helper.Util;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.admin.TrackMenu;
import devjluvisi.mlb.util.Range;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TrackCommand implements SubCommand {

    private MoreLuckyBlocks plugin;
    public TrackCommand(MoreLuckyBlocks plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "track";
    }

    @Override
    public String getDescription() {
        return "Shows a GUI of how many lucky blocks have been placed on the server and their locations (pages).";
    }

    @Override
    public String getSyntax() {
        return "/mlb track\n/mlb track view";
    }

    @Override
    public String getPermission() {
        return "mlb.admin.track";
    }

    @Override
    public boolean isAllowConsole() {
        return false;
    }

    @Override
    public Range getArgumentRange() {
        return new Range(1, 2);
    }

    public static final byte BLOCKS_PER_PAGE = 21;

    @Override
    public CommandResult perform(CommandSender sender, String[] args) {
        if(args.length==1) {
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GRAY + "There are " + ChatColor.YELLOW + plugin.getAudit().getMap().size() + ChatColor.GRAY + " lucky blocks that have been placed on the server.");
            sender.sendMessage(ChatColor.GRAY + "Use the command " + ChatColor.LIGHT_PURPLE + " /mlb track view " + ChatColor.GRAY + " to view the locations of these lucky blocks (pages of " + BLOCKS_PER_PAGE + ").");
            sender.sendMessage("");
            sender.sendMessage(ChatColor.ITALIC + "To remove: ");
            sender.sendMessage("/mlb reset");
            sender.sendMessage("");
            return new CommandResult(ResultType.PASSED);
        }
        if(plugin.getAudit().getMap().size()==0) {
            sender.sendMessage(ChatColor.RED + "No lucky blocks have been placed yet.");
            return new CommandResult(ResultType.GENERAL_FAILURE);
        }

        if(!args[1].equalsIgnoreCase("view")) {
            return new CommandResult(ResultType.BAD_USAGE);
        }

        MenuManager manager = new MenuManager(plugin);
        TrackMenu menu = new TrackMenu(manager);
        manager.open((Player)sender, menu);

        return new CommandResult(ResultType.PASSED);
    }
}
