package devjluvisi.mlb.cmds.admin;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.PluginConstants;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.helper.Util;
import devjluvisi.mlb.util.Range;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

/**
 * For editing attributes of lucky blocks. "/mlb edit [internal name]
 * [name|lore|default luck|break permission] [value]
 *
 * @author jacob
 */
public record EditCommand(MoreLuckyBlocks plugin) implements SubCommand {

    @Override
    public String getName() {
        return "edit";
    }

    @Override
    public String getDescription() {
        return "Edit attributes of a specific lucky block.";
    }

    @Override
    public String getSyntax() {
        return "/mlb edit <internal-name> <name|lore|baseluck|breakperm> <value>";
    }

    @Override
    public String getPermission() {
        return "mlb.admin.edit";
    }

    @Override
    public boolean isAllowConsole() {
        return true;
    }

    @Override
    public Range getArgumentRange() {
        return new Range(4, Integer.MAX_VALUE);
    }

    @Override
    public ExecutionResult perform(CommandSender sender, String[] args) {
        if(!plugin.getLuckyBlocks().contains(args[1])) {
            sender.sendMessage(ChatColor.RED + "Could not find lucky block: " + args[1]);
            return ExecutionResult.PASSED;
        }

        // A list that represents the user arguments after the command.
        LuckyBlock lb = plugin.getLuckyBlocks().get(args[1]);
        String[] valueArgs = Arrays.stream(args).toList().subList(3, args.length).toArray(String[]::new);

        // name|lore|baseluck|breakperm
        switch(StringUtils.lowerCase(args[2])) {
            case "name":
            {
                String name = StringUtils.join(valueArgs, " ");
                lb.setName(name);
                lb.setInternalName(Util.makeInternal(name));
                sender.sendMessage(ChatColor.GREEN + "Updated the name of " + ChatColor.GOLD + args[1] + ChatColor.GREEN + " to " + ChatColor.GOLD + Util.toColor(name));
                break;
            }
            case "lore":
            {
                String lore = StringUtils.join(valueArgs, " ");
                lb.setLore(Arrays.stream(lore.split(",")).toList());
                sender.sendMessage(ChatColor.GREEN + "Updated the lore of " + ChatColor.GOLD + args[1] + ChatColor.GREEN + ".");
                break;
            }
            case "baseluck":
            {
                if(args.length > 4)
                    return ExecutionResult.BAD_USAGE;
                if(!Util.isNumber(args[3]))
                    return ExecutionResult.BAD_ARGUMENT_TYPE;
                int arg = Integer.parseInt(args[3]);
                if(arg > PluginConstants.LUCK_MAX_VALUE || arg < PluginConstants.LUCK_MIN_VALUE)
                    sender.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC.toString() + "Your number exceeded the maximum luck range so it was set to the highest/lowest possible.");
                lb.setDefaultBlockLuck(arg);
                sender.sendMessage(ChatColor.GREEN + "Updated the default luck of " + ChatColor.GOLD + args[1] + ChatColor.GREEN + " to " + ChatColor.GOLD + lb.getDefaultBlockLuck());
                break;
            }
            case "breakperm":
            {
                if(args.length > 4)
                    return ExecutionResult.BAD_USAGE;
                lb.setBreakPermission(args[3]);
                sender.sendMessage(ChatColor.GREEN + "Updated the break permission of " + ChatColor.GOLD + args[1] + ChatColor.GREEN + " to " + ChatColor.GOLD + lb.getBreakPermission());
                break;
            }
            default:
                return ExecutionResult.BAD_USAGE;
        }
        return ExecutionResult.PASSED;
    }

}
