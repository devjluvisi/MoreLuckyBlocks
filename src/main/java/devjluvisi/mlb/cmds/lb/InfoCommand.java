package devjluvisi.mlb.cmds.lb;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.helper.Util;
import devjluvisi.mlb.util.Range;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

/**
 * Displays information related to the lucky block in chat. "/mlb info <internal
 * name>
 * <p>
 * Will display information depending on the permissions of the user.
 *
 * @author jacob
 */
public record InfoCommand(MoreLuckyBlocks plugin) implements SubCommand {

    private static final byte MAX_DROPS_IN_CHAT = 50;

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Get information about a specific lucky block.";
    }

    @Override
    public String getSyntax() {
        return "/mlb info <name>";
    }

    @Override
    public String getPermission() {
        return "mlb.info";
    }

    @Override
    public boolean isAllowConsole() {
        return true;
    }

    @Override
    public Range getArgumentRange() {
        return new Range(2, 2);
    }

    @Override
    public CommandResult perform(CommandSender sender, String[] args) {
        if (!this.plugin.getLuckyBlocks().contains(Util.makeInternal(args[1]))) {
            return new CommandResult(ResultType.INVALID_LUCKY_BLOCK, args[1]);
        }

        final LuckyBlock lb = this.plugin.getLuckyBlocks().get(Util.makeInternal(args[1]));
        sender.sendMessage("");
        sender.sendMessage(ChatColor.DARK_AQUA + "Info for: " + ChatColor.BLUE + lb.getInternalName());
        sender.sendMessage(ChatColor.WHITE + " ▶" + ChatColor.GRAY + " Item Name " + ChatColor.DARK_GRAY + "→ " + ChatColor.GREEN + lb.getName());
        sender.sendMessage(ChatColor.WHITE + " ▶" + ChatColor.GRAY + " Block Type " + ChatColor.DARK_GRAY + "→ " + ChatColor.GREEN
                + lb.getBlockMaterial().name());
        sender.sendMessage(ChatColor.WHITE + " ▶" + ChatColor.GRAY + " Default Luck " + ChatColor.DARK_GRAY + "→ " + ChatColor.GREEN
                + lb.getDefaultBlockLuck());
        sender.sendMessage(ChatColor.WHITE + " ▶" + ChatColor.GRAY + " # of Drops " + ChatColor.DARK_GRAY + "→ " + ChatColor.GREEN
                + lb.getDroppableItems().size());
        if(lb.getBreakCooldown() != 0) {
            sender.sendMessage(ChatColor.WHITE + " ▶" + ChatColor.GRAY + " Break Cooldown " + ChatColor.DARK_GRAY + "→ " + ChatColor.GREEN
                    + lb.getBreakCooldown());
        }
        if(lb.getPlaceCooldown() != 0) {
            sender.sendMessage(ChatColor.WHITE + " ▶" + ChatColor.GRAY + " Place Cooldown " + ChatColor.DARK_GRAY + "→ " + ChatColor.GREEN
                    + lb.getPlaceCooldown());
        }



        if (sender.hasPermission("mlb.admin")) {
            sender.sendMessage(ChatColor.RED + " ▶" + ChatColor.GRAY + " Enchanted " + ChatColor.DARK_GRAY + "→ "
                    + ChatColor.GREEN + lb.isItemEnchanted());
            sender.sendMessage(ChatColor.RED + " ▶" + ChatColor.GRAY + " Break Permission " + ChatColor.DARK_GRAY + "→ "
                    + ChatColor.GREEN + lb.getBreakPermission());
            sender.sendMessage(ChatColor.RED + " ▶" + ChatColor.GRAY + " Lore Lines " + ChatColor.DARK_GRAY + "→ " + ChatColor.GREEN
                    + lb.getRefreshedLore().size());
        }
        if(lb.hasRequiredTool()) {
            TextComponent txtComp = new TextComponent(  "  ⛏ Requires Special Tool ");
            txtComp.setColor(ChatColor.RED);
            txtComp.addExtra(ChatColor.DARK_GRAY + "(" + ChatColor.YELLOW.toString() + ChatColor.BOLD + "?" + ChatColor.DARK_GRAY + ")");
            txtComp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to view item.")));
            txtComp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mlb tool " + lb.getInternalName()));
            sender.spigot().sendMessage(txtComp);
        }
        if(lb.hasParticles()) {
            sender.sendMessage(ChatColor.GREEN + "  ✔ Has Particles");
        }
        if(lb.hasBreakSound()) {
            sender.sendMessage(ChatColor.GREEN + "  ✔ Plays sound when broken");
        }
        sender.sendMessage(ChatColor.ITALIC + "-- Drops --");
        ArrayList<TextComponent> drops = new ArrayList<>();

        for (int i = 0; i < lb.getDroppableItems().size() && i < MAX_DROPS_IN_CHAT; i++) {
            TextComponent txtComponent = new TextComponent();
            txtComponent.addExtra("[");
            txtComponent.addExtra(String.valueOf(i));
            txtComponent.addExtra("]");
            txtComponent.setColor(ChatColor.BLUE);
            txtComponent.addExtra(" ");
            txtComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to view drop #" + i)));
            txtComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mlb drops " + lb.getInternalName() + " " + i));
            drops.add(txtComponent);
            if(i == MAX_DROPS_IN_CHAT-1) {
                drops.add(new TextComponent("and " + (lb.getDroppableItems().size()-MAX_DROPS_IN_CHAT) + " more..."));
            }
        }
        TextComponent finalComponent = new TextComponent();
        drops.forEach(finalComponent::addExtra);
        sender.spigot().sendMessage(finalComponent);

        if (sender.hasPermission(lb.getBreakPermission())) {
            sender.sendMessage(ChatColor.DARK_GREEN + "You have permission to break this lucky block.");
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to break this lucky block.");
        }

        return new CommandResult(ResultType.PASSED);
    }

}
