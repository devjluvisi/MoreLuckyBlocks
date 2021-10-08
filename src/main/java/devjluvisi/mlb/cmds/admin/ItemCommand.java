package devjluvisi.mlb.cmds.admin;

import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;

public class ItemCommand implements SubCommand {

    @Override
    public String getName() {
        return "item";
    }

    @Override
    public String getDescription() {
        return "Useful tools for editing item attributes like name and lore.";
    }

    @Override
    public String getSyntax() {
        return "/mlb item <name|lore> <values>\nSeperate lore by commas for each line.";
    }

    @Override
    public String getPermission() {
        return "mlb.admin.item";
    }

    @Override
    public boolean isAllowConsole() {
        return false;
    }

    @Override
    public Range getArgumentRange() {
        return new Range(3, Integer.MAX_VALUE);
    }

    @Override
    public ExecutionResult perform(CommandSender sender, String[] args) {
        final Player p = (Player) sender;
        p.getInventory().getItemInMainHand();
        if (p.getInventory().getItemInMainHand().getType().isAir()) {
            p.sendMessage(ChatColor.RED + "You must hold an item to set.");
            return ExecutionResult.PASSED;
        }
        final ItemStack i = p.getInventory().getItemInMainHand();
        final ItemMeta meta = i.getItemMeta();

        if (args[1].equalsIgnoreCase("name")) {
            final StringBuilder argStr = new StringBuilder();
            for (int j = 2; j < args.length; j++) {
                argStr.append(args[j]).append(" ");
            }
            assert meta != null;
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', argStr.toString().trim()));
            p.sendMessage(ChatColor.AQUA + "Name has been set.");
        } else if (args[1].equalsIgnoreCase("lore")) {
            final List<String> lore = new LinkedList<>();
            final StringBuilder argStr = new StringBuilder();
            for (int j = 2; j < args.length; j++) {
                argStr.append(args[j]).append(" ");
            }
            final String[] splitString = StringUtils.split(argStr.toString(), ",");
            for (final String element : splitString) {
                lore.add(ChatColor.translateAlternateColorCodes('&', element.trim()));
            }
            assert meta != null;
            meta.setLore(lore);
            p.sendMessage(ChatColor.AQUA + "Lore has been set.");
        } else {
            return ExecutionResult.BAD_ARGUMENT_TYPE;
        }
        i.setItemMeta(meta);
        return ExecutionResult.PASSED;
    }

}
