package devjluvisi.mlb.cmds.general;

import devjluvisi.mlb.cmds.CommandManager;
import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * "/mlb perms" Reveals to the user what permissions and commands they have
 * access to. If the user is OP, it will display to the user all commands and
 * permission nodes.
 *
 * @author jacob
 */
public class PermissionsCommand implements SubCommand {

    public List<SubCommand> commandList;

    public PermissionsCommand(CommandManager manager) {
        this.commandList = manager.getSubcommands();
    }

    @Override
    public String getName() {
        return "perms";
    }

    @Override
    public String getDescription() {
        return "A list of MoreLuckyBlock permissions the user has.";
    }

    @Override
    public String getSyntax() {
        return "/mlb perms";
    }

    @Override
    public String getPermission() {
        return "mlb.perms";
    }

    @Override
    public boolean isAllowConsole() {
        return true;
    }

    @Override
    public Range getArgumentRange() {
        return new Range(1, 1);
    }

    @Override
    public CommandResult perform(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.GRAY + "Your Permissions:");

        for (SubCommand cmd : commandList) {
            if (sender.hasPermission(cmd.getPermission())) {
                TextComponent txtcomp = new TextComponent(TextComponent.fromLegacyText(ChatColor.GRAY + "/mlb " + cmd.getName()));
                txtcomp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(cmd.getPermission())));
                txtcomp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cmd.getSyntax().split("\n")[0]));
                sender.spigot().sendMessage(txtcomp);
            }
        }
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "Hover over commands to give permission nodes.");
        return new CommandResult(ResultType.PASSED);
    }

}
