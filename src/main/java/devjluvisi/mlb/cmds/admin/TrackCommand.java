package devjluvisi.mlb.cmds.admin;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.admin.TrackMenu;
import devjluvisi.mlb.util.Range;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TrackCommand implements SubCommand {

    public static final byte BLOCKS_PER_PAGE = 21;
    private final MoreLuckyBlocks plugin;

    public TrackCommand(MoreLuckyBlocks plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "track";
    }

    @Override
    public String getDescription() {
        return "Shows a GUI of how many lucky blocks have been placed on the server and their locations (pages).";
    }

    @Override
    public String getSyntax() {
        return "/mlb track\n/mlb track view";
    }

    @Override
    public String getPermission() {
        return "mlb.admin.track";
    }

    @Override
    public boolean isAllowConsole() {
        return false;
    }

    @Override
    public Range getArgumentRange() {
        return new Range(1, 2);
    }

    @Override
    public CommandResult perform(CommandSender sender, String[] args) {
        if (args.length == 1) {
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GRAY + "There are " + ChatColor.YELLOW + plugin.getAudit().getMap().size() + ChatColor.GRAY + " lucky blocks that have been placed on the server.");

            TextComponent textComponent = new TextComponent();
            textComponent.addExtra(ChatColor.GRAY + "To view an interactive menu,");

            TextComponent clickCommand = new TextComponent(TextComponent.fromLegacyText(net.md_5.bungee.api.ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + " CLICK HERE "));
            clickCommand.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Executes: /mlb track view.")));
            clickCommand.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mlb track view"));
            textComponent.addExtra(clickCommand);

            textComponent.addExtra(ChatColor.GRAY + "to show locations, remove, and teleport to lucky blocks.");

            sender.spigot().sendMessage(textComponent);
            sender.sendMessage("");
            sender.sendMessage(ChatColor.ITALIC + "To remove: ");
            sender.sendMessage("/mlb reset");
            sender.sendMessage("");
            return new CommandResult(ResultType.PASSED);
        }
        if (plugin.getAudit().getMap().size() == 0) {
            sender.sendMessage(ChatColor.RED + "No lucky blocks have been placed yet.");
            return new CommandResult(ResultType.GENERAL_FAILURE);
        }

        if (!args[1].equalsIgnoreCase("view")) {
            return new CommandResult(ResultType.BAD_USAGE);
        }

        MenuManager manager = new MenuManager(plugin);
        TrackMenu menu = new TrackMenu(manager);
        manager.open((Player) sender, menu);

        return new CommandResult(ResultType.PASSED);
    }

}
