package devjluvisi.mlb.cmds.general;

import devjluvisi.mlb.cmds.CommandManager;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.helper.Util;
import devjluvisi.mlb.util.Range;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;

import java.util.LinkedList;
import java.util.List;

/**
 * Lists the commands available to the user. Will not display commands the user
 * does not have permissions for.
 *
 * @author jacob
 */
public class HelpCommand implements SubCommand {

    /**
     * The maximum amount of commands the help command is allowed to display between
     * each page.
     */
    private static final byte MAX_COMMANDS_PER_PAGE = 6;
    private List<SubCommand> commands;

    public HelpCommand(CommandManager cmdManager) {
        this.commands = cmdManager.getSubcommands();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Sends a message describing all of the possible commands in the plugin.";
    }

    @Override
    public String getSyntax() {
        return "/mlb help <[?]page>";
    }

    @Override
    public String getPermission() {
        return "";
    }

    @Override
    public boolean isAllowConsole() {
        return true;
    }

    @Override
    public Range getArgumentRange() {
        return new Range(1, 2);
    }

    private TextComponent asInteractable(SubCommand cmd) {
        TextComponent cmp = new TextComponent(TextComponent.fromLegacyText(Util.toColor("&d/mlb " + cmd.getName() + "&7 - &a" + cmd.getDescription())));
        cmp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(TextComponent.fromLegacyText(Util.toColor("&oClick to paste.")))));
        cmp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cmd.getSyntax().split("\n")[0]));
        return cmp;
    }

    private TextComponent goForward(int currPage) {
        TextComponent cmp = new TextComponent(TextComponent.fromLegacyText(Util.toColor("→")));
        cmp.setBold(true);
        cmp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(TextComponent.fromLegacyText(Util.toColor("&aGo to page " + (currPage + 1))))));
        cmp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mlb help " + (currPage + 1)));
        return cmp;
    }

    private TextComponent goBack(int currPage) {
        if (currPage == 1) {
            return new TextComponent();
        }
        TextComponent cmp = new TextComponent(TextComponent.fromLegacyText(Util.toColor("←")));
        cmp.setBold(true);
        cmp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(TextComponent.fromLegacyText(Util.toColor("&aGo to page " + (currPage - 1))))));
        cmp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mlb help " + (currPage - 1)));
        return cmp;
    }

    private TextComponent pageSelector(int currPage, int maxPage) {
        TextComponent cmp = new TextComponent();
        cmp.addExtra(goBack(currPage));
        cmp.addExtra(" Page " + currPage + "/" + maxPage + " ");
        if (currPage != maxPage) {
            cmp.addExtra(goForward(currPage));
        }

        cmp.setColor(ChatColor.BLUE);
        return cmp;
    }

    @Override
    public ExecutionResult perform(CommandSender sender, String[] args) {
        // "/mlb help <page>
        commands = commands.stream().filter(e -> e.getPermission().isBlank() || sender.hasPermission(e.getPermission())).toList();

        int page = 1;

        if (args.length != 1) {
            try {
                page = Integer.parseInt(args[1]);
                if (page < 1) {
                    throw new NumberFormatException("Bad Integer. Must be greater then 1.");
                }
            } catch (final NumberFormatException e) {
                return ExecutionResult.BAD_ARGUMENT_TYPE;
            }
            // Make sure the page is not over the limit.
            if (page > ((this.commands.size() / MAX_COMMANDS_PER_PAGE)
                    + ((this.commands.size() % MAX_COMMANDS_PER_PAGE) != 0 ? 1 : 0))) {
                sender.sendMessage(ChatColor.RED + "There are no commands to view on this page.");
                return ExecutionResult.PASSED;
            }
        }
        // Get the range of commands to show the player.
        final int commandRangeMin = (page * MAX_COMMANDS_PER_PAGE) - MAX_COMMANDS_PER_PAGE;
        final int commandRangeMax = page * MAX_COMMANDS_PER_PAGE;

        sender.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------"
                + ChatColor.RESET + ChatColor.GRAY + ChatColor.BOLD + "["
                + ChatColor.RESET + ChatColor.GOLD + ChatColor.BOLD + "MoreLuckyBlocks"
                + ChatColor.RESET + ChatColor.GRAY + ChatColor.BOLD + "]"
                + ChatColor.RESET + ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH
                + "-----------------");
        for (int i = commandRangeMin; (i < commandRangeMax) && (i < this.commands.size()); i++) {
            sender.spigot().sendMessage(asInteractable(commands.get(i)));
        }

        sender.spigot().sendMessage(pageSelector(page, ((this.commands.size() / MAX_COMMANDS_PER_PAGE)
                + ((this.commands.size() % MAX_COMMANDS_PER_PAGE) != 0 ? 1 : 0))));

        sender.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH
                + "----------------------------------------------------");
        return ExecutionResult.PASSED;

    }

}
