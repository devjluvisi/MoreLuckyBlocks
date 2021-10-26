package devjluvisi.mlb.cmds.admin.struct;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.helper.Util;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuResource;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.util.Range;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class StructureCommand implements SubCommand {

    private final MoreLuckyBlocks plugin;

    public StructureCommand(MoreLuckyBlocks plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "struct";
    }

    @Override
    public String getDescription() {
        return "Configure a structure for a lucky block.";
    }

    @Override
    public String getSyntax() {
        return "/mlb struct";
    }

    @Override
    public String getPermission() {
        return "mlb.admin.struct";
    }

    @Override
    public boolean isAllowConsole() {
        return false;
    }

    /*
    /mlb struct
    /mlb struct help
    /mlb struct <name>
     */
    @Override
    public Range getArgumentRange() {
        return new Range(1, 2);
    }

    @Override
    public CommandResult perform(CommandSender sender, String[] args) {
        final Player p = (Player) sender;
        if (args.length == 1) {
            sender.sendMessage("");
            sender.sendMessage(ChatColor.DARK_GRAY + "-- " + ChatColor.GOLD + "LuckyBlock Structures"
                    + ChatColor.DARK_GRAY + " --");
            sender.sendMessage(
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "Click on a bolded message to view information.");

            final TextComponent t1 = new TextComponent(TextComponent.fromLegacyText(ChatColor.DARK_GRAY + "→ "
                    + ChatColor.BLUE + ChatColor.BOLD + "What are structures?"));
            final TextComponent t2 = new TextComponent(TextComponent.fromLegacyText(ChatColor.DARK_GRAY + "→ "
                    + ChatColor.DARK_GREEN + ChatColor.BOLD + "How do I make one?"));
            final TextComponent t3 = new TextComponent(TextComponent.fromLegacyText(ChatColor.DARK_GRAY + "→ "
                    + ChatColor.DARK_AQUA + ChatColor.BOLD + "How can I save and use one?"));
            final TextComponent t4 = new TextComponent(TextComponent.fromLegacyText(ChatColor.DARK_GRAY + "→ "
                    + ChatColor.DARK_PURPLE + ChatColor.BOLD + "How can I delete one?"));

            t1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new Text(TextComponent.fromLegacyText(ChatColor.ITALIC + "View more information."))));
            t2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new Text(TextComponent.fromLegacyText(ChatColor.ITALIC + "View more information."))));
            t3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new Text(TextComponent.fromLegacyText(ChatColor.ITALIC + "View more information."))));
            t4.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new Text(TextComponent.fromLegacyText(ChatColor.ITALIC + "View more information."))));

            t1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mlb struct h1"));
            t2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mlb struct h2"));
            t3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mlb struct h3"));
            t4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mlb struct h4"));

            p.spigot().sendMessage(t1);
            p.spigot().sendMessage(t2);
            p.spigot().sendMessage(t3);
            p.spigot().sendMessage(t4);
            sender.sendMessage(
                    ChatColor.LIGHT_PURPLE + "/mlb struct help " + ChatColor.GRAY + "for a list of commands.");
            sender.sendMessage("");
            return new CommandResult(ResultType.PASSED);
        }

        final ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
        final BookMeta meta = (BookMeta) book.getItemMeta();
        assert meta != null;
        meta.setAuthor("MoreLuckyBlocks");
        meta.setTitle("Struct Tutorial");

        if (args[1].equalsIgnoreCase("help")) {
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GRAY + "/mlb struct edit <lucky-block-name> <drop-number>");
            sender.sendMessage(
                    ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC + "Edit the structure of a specific drop.");
            sender.sendMessage(ChatColor.GRAY + "/mlb struct has <lucky-block-name> <drop-number>");
            sender.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC
                    + "Check if a lucky block drop has a structure defined with it.");
            sender.sendMessage(ChatColor.GRAY + "/mlb struct reset <lucky-block-name> <drop-number>");
            sender.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC
                    + "Delete the structure for a drop if it has one.");
            sender.sendMessage(ChatColor.GRAY + "/mlb save");
            sender.sendMessage(
                    ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC + "Save a structure while editing.");
            sender.sendMessage(ChatColor.GRAY + "/mlb exit");
            sender.sendMessage(
                    ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC + "Leave the structure editing world.");
            sender.sendMessage("");
        } else if (args[1].equalsIgnoreCase("h1")) {
            meta.addPage(Util.toColor(
                    "&lWhat are structures?\n----------------\n&rIn MoreLuckyBlocks, structures are a way to create physical modifications to the world when a player opens a lucky block. This greatly expands what lucky blocks can do."));
            meta.addPage(Util.toColor(
                    "For example, instead of just dropping items, a lucky block could also create blocks around it and spawn mobs.\nEach lucky block can have only a single structure, this structure can be removed and edited later."));
            meta.addPage(Util.toColor("To view more commands or information you can run &1/mlb struct help"));
            book.setItemMeta(meta);
            p.openBook(book);
        } else if (args[1].equalsIgnoreCase("h2")) {
            meta.addPage(Util.toColor(
                    "&lHow do I make one?\n----------------\n&rBy executing the &1/mlb struct edit <block name> <drop>&r you can create a structure for a specific lucky block drop."));
            meta.addPage(Util.toColor(
                    "Each lucky block drop can have only one structure.\nWhen you execute this command, you will be teleported into a new world 50x50x128 wide.\nThe lucky block you chose will be centered in the middle and serve as a reference point for the blocks you place."));
            meta.addPage(Util.toColor(
                    "Physics, motion, redstone, etc are all disabled in this world, meaning your creations are totally static until a player gets your drop. To check if a drop already has a structure, type &1/mlb struct has <lb-name> <drop #>"));
            meta.addPage(Util
                    .toColor("Remember that &lonly&r one player can be in the structure world editing at once."));
            book.setItemMeta(meta);
            p.openBook(book);
        } else if (args[1].equalsIgnoreCase("h3")) {
            meta.addPage(Util.toColor(
                    "&lHow can I save and use one?\n----------------\n&rWhen you have finished editing your structure, you can run the &1/mlb save&r command. This will save all of your progress into config."));
            meta.addPage(Util.toColor(
                    "You can exit the structure world by typing &1/mlb exit&r. If you have any unsaved changes, you will recieve a warning prior to exit.\nAfter Saving, you can return to view and edit your structure later."));
            book.setItemMeta(meta);
            p.openBook(book);
        } else if (args[1].equalsIgnoreCase("h4")) {
            meta.addPage(Util.toColor(
                    "&lHow can I delete one?\n----------------\n&rDelete a structure by typing the &1/mlb struct reset <lb-name> <drop>&r command.\nThis will delete any previous structure for the selected drop."));
            meta.addPage(Util.toColor(
                    "If you wish, you may also manually delete the structure by erasing any \"structure:\" tag in the blocks.yml configuration file.\nStructures can be viewed in the data/structures.yml directory but it is not suggested to modify them."));
            book.setItemMeta(meta);
            p.openBook(book);
        } else {
            if (!plugin.getLuckyBlocks().contains(args[1])) {
                return new CommandResult(ResultType.INVALID_LUCKY_BLOCK, args[1]);
            }
            LuckyBlock lb = plugin.getLuckyBlocks().get(args[1]);
            MenuManager manager = new MenuManager(plugin);
            manager.setMenuData(new MenuResource().with(lb));
            manager.open(p, MenuType.STRUCTURE);
            return new CommandResult(ResultType.PASSED);
        }


        return new CommandResult(ResultType.PASSED);
    }

}
