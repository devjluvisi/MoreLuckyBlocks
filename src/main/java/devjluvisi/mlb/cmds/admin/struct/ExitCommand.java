package devjluvisi.mlb.cmds.admin.struct;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExitCommand implements SubCommand {

    private final MoreLuckyBlocks plugin;

    public ExitCommand(MoreLuckyBlocks plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public String getDescription() {
        return "Leave a structure editing mode for a lucky block.";
    }

    @Override
    public String getSyntax() {
        return "/mlb exit";
    }

    @Override
    public String getPermission() {
        return "mlb.admin.struct";
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
        final Player p = (Player) sender;
        if ((this.plugin.getServerDropStructure().getEditingPlayerUUID() == null)
                || !this.plugin.getServerDropStructure().getEditingPlayerUUID().equals(p.getUniqueId())) {
            p.sendMessage(ChatColor.RED + "You are not currently editing a lucky block structure.");
            p.sendMessage(ChatColor.BLUE + "/mlb struct help" + ChatColor.GRAY + " for more information.");
            return new CommandResult(ResultType.PASSED);
        }
        if (this.plugin.getServerDropStructure().isUnsavedChanges()
                && !this.plugin.getServerDropStructure().isConfirmExit()) {
            p.sendMessage(ChatColor.RED.toString() + ChatColor.ITALIC
                    + "You have unsaved changes. Are you sure you want to exit?\nType \"/mlb exit\" again to confirm.");
            this.plugin.getServerDropStructure().setConfirmExit(true);
            return new CommandResult(ResultType.PASSED);
        }

        this.plugin.getServerDropStructure().kick(p);
        p.sendMessage(ChatColor.GREEN + "Finished editing the lucky block structure. Returned to last known location.");
        return new CommandResult(ResultType.PASSED);
    }

}
