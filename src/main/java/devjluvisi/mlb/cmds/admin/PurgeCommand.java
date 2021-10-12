package devjluvisi.mlb.cmds.admin;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.helper.Util;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuResource;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.admin.ConfirmMenu;
import devjluvisi.mlb.util.Range;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * "/mlb purge <internal name>" Removes a lucky block from the config.
 *
 * @author jacob
 */
public record PurgeCommand(MoreLuckyBlocks plugin) implements SubCommand {

    @Override
    public String getName() {
        return "purge";
    }

    @Override
    public String getDescription() {
        return "Deletes a lucky block. Requires confirmation.";
    }

    @Override
    public String getSyntax() {
        return "/mlb purge <name>";
    }

    @Override
    public String getPermission() {
        return "mlb.admin.purge";
    }

    @Override
    public boolean isAllowConsole() {
        return true; // No GUI is shown
    }

    @Override
    public Range getArgumentRange() {
        return new Range(2, 2);
    }

    @Override
    public CommandResult perform(CommandSender sender, String[] args) {

        if (!plugin.getLuckyBlocks().contains(Util.makeInternal(args[1]))) {
            return new CommandResult(ResultType.INVALID_LUCKY_BLOCK, args[1]);
        }
        if (sender instanceof Player) {
            MenuManager manager = new MenuManager(plugin);
            manager.setMenuData(new MenuResource().with(plugin.getLuckyBlocks().get(Util.makeInternal(args[1]))));
            manager.open((Player) sender, new ConfirmMenu(manager).request(ConfirmMenu.ConfirmAction.REMOVE_LUCKY_BLOCK).returnTo(MenuType.EMPTY));
            return new CommandResult(ResultType.PASSED);
        }
        plugin.getLuckyBlocks().remove(new LuckyBlock(Util.makeInternal(args[1])));
        sender.sendMessage(ChatColor.GREEN + "Lucky block has been removed.");

        return new CommandResult(ResultType.PASSED);
    }

}
