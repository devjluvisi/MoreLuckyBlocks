package devjluvisi.mlb.cmds.admin;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.admin.ConfirmMenu;
import devjluvisi.mlb.util.Range;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * - "/mlb disable" - Disables the plugin for the current server instance (until
 * reload) Requires confirmation prompt.
 *
 * @author jacob
 */
public record DisableCommand(MoreLuckyBlocks plugin) implements SubCommand {

    @Override
    public String getName() {
        return "disable";
    }

    @Override
    public String getDescription() {
        return "Disable a specific lucky block or disable the entire plugin.";
    }

    @Override
    public String getSyntax() {
        return "/mlb disable <plugin|lucky-block>";
    }

    @Override
    public String getPermission() {
        return "mlb.admin.disable";
    }

    @Override
    public boolean isAllowConsole() {
        return true; // No Confirm
    }

    @Override
    public Range getArgumentRange() {
        return new Range(1, 1);
    }

    @Override
    public CommandResult perform(CommandSender sender, String[] args) {
        if (args[1].equalsIgnoreCase("plugin")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "MoreLuckyBlocks will disable in 5 seconds. All data will be saved.");
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> plugin.getServer().getPluginManager().disablePlugin(plugin), 20L * 5L);
                return new CommandResult(ResultType.PASSED);
            }
            MenuManager manager = new MenuManager(plugin);
            ConfirmMenu confirmMenu = new ConfirmMenu(manager).request(ConfirmMenu.ConfirmAction.DISABLE_PLUGIN).returnTo(MenuType.EMPTY);
            manager.open((Player) sender, confirmMenu);
        } else {
            if (!plugin.getLuckyBlocks().contains(args[1])) {
                return new CommandResult(ResultType.INVALID_LUCKY_BLOCK, args[1]);
            }
            // Open new confirm menu to disable lucky block.
        }

        return new CommandResult(ResultType.PASSED);
    }

}
