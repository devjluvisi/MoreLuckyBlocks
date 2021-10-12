package devjluvisi.mlb.cmds.lb;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.util.Range;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * "/mlb redeem" Opens a GUI where the user can redeem items (if they have them
 * in their inventory) for lucky blocks. All of the data which operates this
 * command is configurable in the exchanges.yml file. If the server admin
 * wishes, the command can be directly disabled from config.yml
 *
 * Needs: mlb.lb.redeem.[internal name] permission
 *
 * @author jacob
 */
public record RedeemCommand(MoreLuckyBlocks plugin) implements SubCommand {

    @Override
    public String getName() {
        return "redeem";
    }

    @Override
    public String getDescription() {
        return "Allows a user to redeem items for a lucky block.";
    }

    @Override
    public String getSyntax() {
        return "/mlb redeem"; // Opens GUI
    }

    @Override
    public String getPermission() {
        return "mlb.redeem"; // Requires break permission to redeem specific luckyblock.s
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
        new MenuManager(plugin).open((Player) sender, MenuType.USER_REDEEM_LIST);
        return new CommandResult(ResultType.PASSED);
    }

}
