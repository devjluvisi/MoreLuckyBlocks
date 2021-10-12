package devjluvisi.mlb.cmds.admin.exchanges;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuResource;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.util.Range;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class SetExchangeCommand implements SubCommand, Listener {

    private MoreLuckyBlocks plugin;

    public SetExchangeCommand(MoreLuckyBlocks plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public String getName() {
        return "exchange";
    }

    @Override
    public String getDescription() {
        return "Configure item exchanges for a lucky block.";
    }

    @Override
    public String getSyntax() {
        return "/mlb exchange <luckyblock>";
    }

    @Override
    public String getPermission() {
        return "mlb.admin";
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
        Player p = (Player) sender;
        LuckyBlock lb = plugin.getLuckyBlocks().get(args[1]);
        if (lb == null) {
            return new CommandResult(ResultType.INVALID_LUCKY_BLOCK, args[1]);
        }

        MenuManager manager = new MenuManager(plugin);
        manager.setMenuData(new MenuResource().with(lb));
        manager.open(p, MenuType.VIEW_EXCHANGE);

        return new CommandResult(ResultType.PASSED);
    }
}
