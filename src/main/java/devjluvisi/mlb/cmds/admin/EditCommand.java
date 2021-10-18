package devjluvisi.mlb.cmds.admin;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuResource;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.admin.EditLuckyBlockMenu;
import devjluvisi.mlb.util.Range;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

/**
 * For editing attributes of lucky blocks. "/mlb edit [internal name]
 * [name|lore|default luck|break permission] [value]
 *
 * @author jacob
 */
public record EditCommand(MoreLuckyBlocks plugin) implements SubCommand {

    private static final HashSet<UUID> editingPlayers = new HashSet<>();

    @Override
    public String getName() {
        return "edit";
    }

    @Override
    public String getDescription() {
        return "Edit attributes of a specific lucky block.";
    }

    @Override
    public String getSyntax() {
        return "/mlb edit <internal-name>";
    }

    @Override
    public String getPermission() {
        return "mlb.admin.edit";
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

        // A list that represents the user arguments after the command.
        LuckyBlock lb = plugin.getLuckyBlocks().get(args[1]);
        MenuManager manager = new MenuManager(plugin);
        manager.setMenuData(new MenuResource().with(lb));
        manager.open((Player) sender, MenuType.EDIT_LUCKY_BLOCK_ATTRIBUTES);
        return new CommandResult(ResultType.PASSED);
    }

}
