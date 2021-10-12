package devjluvisi.mlb.cmds.lb;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.PluginConstants;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.helper.Util;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuResource;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.util.Range;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * "/mlb drops" Will display all possible drops to the user (GUI) based on the
 * lucky block in their hand. If a argument is specified, "/mlb drops <internal
 * name>" then a GUI will open for that lucky block.
 *
 * @author jacob
 */
public record DropsCommand(MoreLuckyBlocks plugin) implements SubCommand {

    @Override
    public String getName() {
        return "drops";
    }

    @Override
    public String getDescription() {
        return "Allows a user to see drops for a luckyblock.";
    }

    @Override
    public String getSyntax() {
        return "/mlb drops\n/mlb drops <name>\n/mlb drops <name> <drop index>";
    }

    @Override
    public String getPermission() {
        return "mlb.drops";
    }

    @Override
    public boolean isAllowConsole() {
        return false;
    }

    @Override
    public Range getArgumentRange() {
        return new Range(1, 3);
    }

    @Override
    public CommandResult perform(CommandSender sender, String[] args) {

        Player p = (Player) sender;
        ItemStack item = p.getInventory().getItemInMainHand();
        LuckyBlock lb;
        MenuManager manager = new MenuManager(plugin);

        if (args.length == 1) {
            if (item.getItemMeta() == null || !plugin.getLuckyBlocks().contains(plugin.getMetaFactory().createCustomMeta(item.getItemMeta()).getString(PluginConstants.LuckyIdentifier))) {
                p.sendMessage(ChatColor.RED + "You must hold a valid lucky block in your hand.\nOr use: /mlb drops <name>");
                return new CommandResult(ResultType.PASSED);
            }
            String name = plugin.getMetaFactory().createCustomMeta(item.getItemMeta()).getString(PluginConstants.LuckyIdentifier);
            lb = plugin.getLuckyBlocks().get(name);
            manager.setMenuData(new MenuResource().with(lb));
            manager.open(p, MenuType.LIST_DROPS);

        } else if (args.length == 2) {
            if (!plugin.getLuckyBlocks().contains(Util.makeInternal(args[1]))) {
                return new CommandResult(ResultType.INVALID_LUCKY_BLOCK, args[1]);
            }
            lb = plugin.getLuckyBlocks().get(Util.makeInternal(args[1]));
            manager.setMenuData(new MenuResource().with(lb));
            manager.open(p, MenuType.LIST_DROPS);
        } else if (args.length == 3) {
            if (!plugin.getLuckyBlocks().contains(Util.makeInternal(args[1]))) {
                return new CommandResult(ResultType.INVALID_LUCKY_BLOCK, args[1]);
            }
            if (!Util.isNumber(args[2])) {
                return new CommandResult(ResultType.BAD_ARGUMENT_TYPE, args[2]);
            }
            if (!new Range(0, plugin.getLuckyBlocks().get(args[1]).getDroppableItems().size() - 1).isInRange(Util.toNumber(args[2]))) {
                sender.sendMessage(ChatColor.RED + "Lucky block " + args[1] + " only has " + (plugin.getLuckyBlocks().get(args[1]).getDroppableItems().size()) + " drops (including drop zero).");
                return new CommandResult(ResultType.PASSED);
            }
            lb = plugin.getLuckyBlocks().get(Util.makeInternal(args[1]));
            manager.setMenuData(new MenuResource().with(lb).with(lb.getDroppableItems().get(Integer.parseInt(args[2]))));
            manager.open(p, MenuType.LIST_LOOT);
        }
        return new CommandResult(ResultType.PASSED);
    }

}
