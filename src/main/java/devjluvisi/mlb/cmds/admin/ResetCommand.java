package devjluvisi.mlb.cmds.admin;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.api.gui.Menu;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuResource;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.admin.ConfirmMenu;
import devjluvisi.mlb.util.Range;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * "/mlb reset" Allows the user to delete all current lucky block data. Opens a
 * confirm GUI before execution.
 *
 * @author jacob
 */
public record ResetCommand(MoreLuckyBlocks plugin) implements SubCommand {

    @Override
    public String getName() {
        return "reset";
    }

    @Override
    public String getDescription() {
        return "Removes all placed lucky blocks of a certain (or all) types from the world.";
    }

    @Override
    public String getSyntax() {
        return "/mlb reset <name>\n/mlb reset all";
    }

    @Override
    public String getPermission() {
        return "mlb.admin.reset";
    }

    @Override
    public boolean isAllowConsole() {
        return true; // No Confirm GUI
    }

    @Override
    public Range getArgumentRange() {
        return new Range(2, 2);
    }

    @Override
    public ExecutionResult perform(CommandSender sender, String[] args) {

        if(args[1].equalsIgnoreCase("all")) {
            if(sender instanceof Player) {
                MenuManager manager = new MenuManager(plugin);
                ConfirmMenu menu = new ConfirmMenu(manager).request(ConfirmMenu.ConfirmAction.DELETE_BLOCK_DATA_ALL).returnTo(MenuType.EMPTY);
                manager.open((Player)sender, menu);

                return ExecutionResult.PASSED;
            }
            plugin.getAudit().removeAll();
        }else{
            if(!plugin.getLuckyBlocks().contains(args[1])) {
                sender.sendMessage(ChatColor.RED + "Could not find lucky block: " + args[1]);
                return ExecutionResult.PASSED;
            }

            if(sender instanceof Player) {
                MenuManager manager = new MenuManager(plugin);
                manager.setMenuData(new MenuResource().with(plugin.getLuckyBlocks().get(args[1])));
                ConfirmMenu menu = new ConfirmMenu(manager).request(ConfirmMenu.ConfirmAction.DELETE_BLOCK_DATA_SPECIFIC).returnTo(MenuType.EMPTY);
                manager.open((Player)sender, menu);
                return ExecutionResult.PASSED;
            }
            plugin.getAudit().removeAll(new LuckyBlock(args[1]));
        }

        sender.sendMessage(ChatColor.DARK_RED + "You have deleted all player lucky block data for lucky block type: " + args[1]);
        return ExecutionResult.PASSED;
    }

}
