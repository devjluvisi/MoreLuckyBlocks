package devjluvisi.mlb.cmds.admin.struct;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SaveCommand implements SubCommand {

    private final MoreLuckyBlocks plugin;

    public SaveCommand(MoreLuckyBlocks plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "save";
    }

    @Override
    public String getDescription() {
        return "Save the current structure for a lucky block.";
    }

    @Override
    public String getSyntax() {
        return "/mlb save";
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
        if (!this.plugin.getServerDropStructure().isUnsavedChanges()) {
            p.sendMessage(ChatColor.DARK_GRAY + "No save was made because you have not modified the world.");
            return new CommandResult(ResultType.PASSED);
        }
        p.sendMessage(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Saved.");
        this.plugin.getServerDropStructure().save();
        return new CommandResult(ResultType.PASSED);
    }

}
