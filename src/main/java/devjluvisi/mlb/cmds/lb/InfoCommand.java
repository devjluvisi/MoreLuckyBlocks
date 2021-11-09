package devjluvisi.mlb.cmds.lb;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.helper.Util;
import devjluvisi.mlb.util.Range;
import devjluvisi.mlb.util.config.files.messages.Message;
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
        if (sender.hasPermission("mlb.admin")) {
            sender.sendMessage(Message.M38.format(lb.getInternalName(), lb.getName(), lb.getBlockMaterial().name(), lb.getDefaultBlockLuck(), lb.getDroppableItems().size(), lb.getBreakCooldown(), lb.getPlaceCooldown(), lb.isItemEnchanted(), lb.getBreakPermission(), lb.getRefreshedLore().size()));
        }else{
            sender.sendMessage(Message.M37.format(lb.getInternalName(), lb.getName(), lb.getBlockMaterial().name(), lb.getDefaultBlockLuck(), lb.getDroppableItems().size(), lb.getBreakCooldown(), lb.getPlaceCooldown(), lb.isItemEnchanted(), lb.getBreakPermission(), lb.getRefreshedLore().size()));
        }

        if (lb.hasRequiredTool()) {
            TextComponent txtComp = new TextComponent(TextComponent.fromLegacyText(Message.M39.get()));
            txtComp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to view item.")));
            txtComp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mlb tool " + lb.getInternalName()));
            sender.spigot().sendMessage(txtComp);
        }
        if (lb.hasParticles()) {
            sender.sendMessage(Message.M40.get());
        }
        if (lb.hasBreakSound()) {
            sender.sendMessage(Message.M41.get());
        }


        sender.sendMessage(Message.M44.get());
        ArrayList<TextComponent> drops = new ArrayList<>();

        for (int i = 0; i < lb.getDroppableItems().size() && i < MAX_DROPS_IN_CHAT; i++) {
            TextComponent txtComponent = new TextComponent();
            txtComponent.addExtra(Message.M45.format(String.valueOf(i)));
            txtComponent.addExtra(" ");
            txtComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to view drop #" + i)));
            txtComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mlb drops " + lb.getInternalName() + " " + i));
            drops.add(txtComponent);
            if (i == MAX_DROPS_IN_CHAT - 1) {
                drops.add(new TextComponent("and " + (lb.getDroppableItems().size() - MAX_DROPS_IN_CHAT) + " more..."));
            }
        }
        TextComponent finalComponent = new TextComponent();
        drops.forEach(finalComponent::addExtra);
        sender.spigot().sendMessage(finalComponent);

        if (sender.hasPermission(lb.getBreakPermission())) {
            sender.sendMessage(Message.M42.get());
        } else {
            sender.sendMessage(Message.M43.get());
        }

        return new CommandResult(ResultType.PASSED);
    }

}
