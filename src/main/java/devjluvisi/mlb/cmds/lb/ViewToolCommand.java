package devjluvisi.mlb.cmds.lb;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.shared.ItemViewMenu;
import devjluvisi.mlb.util.Range;
import devjluvisi.mlb.util.config.files.messages.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ViewToolCommand implements SubCommand {

    private MoreLuckyBlocks plugin;

    public ViewToolCommand(MoreLuckyBlocks plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "tool";
    }

    @Override
    public String getDescription() {
        return "View a required tool of a lucky block.";
    }

    @Override
    public String getSyntax() {
        return "/mlb tool <name>";
    }

    @Override
    public String getPermission() {
        return "mlb.tool";
    }

    @Override
    public boolean isAllowConsole() {
        return false;
    }

    @Override
    public Range getArgumentRange() {
        return new Range(2, 2);
    }

    @Override
    public CommandResult perform(CommandSender sender, String[] args) {
        if (!plugin.getLuckyBlocks().contains(args[1])) {
            return new CommandResult(ResultType.INVALID_LUCKY_BLOCK, args[1]);
        }
        if (!plugin.getLuckyBlocks().get(args[1]).hasRequiredTool()) {
            sender.sendMessage(Message.M33.get());
            return new CommandResult(ResultType.GENERAL_FAILURE);
        }
        MenuManager manager = new MenuManager(plugin);
        ItemViewMenu menu = new ItemViewMenu(manager, plugin.getLuckyBlocks().get(args[1]).getRequiredTool());
        manager.open((Player) sender, menu);
        return new CommandResult(ResultType.PASSED);
    }

}
