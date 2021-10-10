package devjluvisi.mlb.cmds.lb;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
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
    public ExecutionResult perform(CommandSender sender, String[] args) {
        if (!this.plugin.getLuckyBlocks().contains(Util.makeInternal(args[1]))) {
            sender.sendMessage("Could not find lucky block " + args[1] + ".");
            return ExecutionResult.PASSED;
        }

        final LuckyBlock lb = this.plugin.getLuckyBlocks().get(Util.makeInternal(args[1]));
        sender.sendMessage("");
        sender.sendMessage(ChatColor.DARK_AQUA + "Info for: " + ChatColor.BLUE + lb.getInternalName());
        sender.sendMessage(
                "  " + ChatColor.GRAY + "Item Name " + ChatColor.DARK_GRAY + "→ " + ChatColor.GREEN + lb.getName());
        sender.sendMessage("  " + ChatColor.GRAY + "Block Type " + ChatColor.DARK_GRAY + "→ " + ChatColor.GREEN
                + lb.getBlockMaterial().name());
        sender.sendMessage("  " + ChatColor.GRAY + "Default Luck " + ChatColor.DARK_GRAY + "→ " + ChatColor.GREEN
                + lb.getDefaultBlockLuck());
        sender.sendMessage("  " + ChatColor.GRAY + "# of Drops " + ChatColor.DARK_GRAY + "→ " + ChatColor.GREEN
                + lb.getDroppableItems().size());
        if (sender.hasPermission("mlb.admin")) {
            sender.sendMessage("  " + ChatColor.GRAY + "Break Permission " + ChatColor.DARK_GRAY + "→ "
                    + ChatColor.GREEN + lb.getBreakPermission());
            sender.sendMessage("  " + ChatColor.GRAY + "Lore Lines " + ChatColor.DARK_GRAY + "→ " + ChatColor.GREEN
                    + lb.getRefreshedLore().size());
        }
        sender.sendMessage(ChatColor.ITALIC + "-- Drops --");
        ArrayList<TextComponent> drops = new ArrayList<>();

        for(int i = 0; i < lb.getDroppableItems().size() && i < 50; i++) {
            TextComponent txtComponent = new TextComponent();
            txtComponent.addExtra("[");
            txtComponent.addExtra(String.valueOf(i));
            txtComponent.addExtra("]");
            txtComponent.setColor(ChatColor.BLUE);
            txtComponent.addExtra(" ");
            txtComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to view drop #" + i)));
            txtComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mlb drops " + lb.getInternalName() + " " + i));
            drops.add(txtComponent);
        }
        TextComponent finalComponent = new TextComponent();
        drops.forEach(finalComponent::addExtra);
        sender.spigot().sendMessage(finalComponent);

        if (sender.hasPermission(lb.getBreakPermission())) {
            sender.sendMessage(ChatColor.DARK_GREEN + "You have permission to break this lucky block.");
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to break this lucky block.");
        }

        return ExecutionResult.PASSED;
    }

}
