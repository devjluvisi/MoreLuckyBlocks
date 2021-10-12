package devjluvisi.mlb.cmds.admin;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.PluginConstants;
import devjluvisi.mlb.api.items.CustomItemMeta;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.helper.Util;
import devjluvisi.mlb.util.Range;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * "/mlb set luck [luck]" - Adjust the luck of a lucky block in hand. "/mlb set
 * luck [player] [luck]" - Adjust the luck of a player. (online or offline).
 *
 * @author jacob
 */
public record LuckSetCommand(MoreLuckyBlocks plugin) implements SubCommand {

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescription() {
        return "Set the luck of a lucky block or player.";
    }

    @Override
    public String getSyntax() {
        return "/mlb set luck <player> <value>\n/mlb set luck <value>";
    }

    @Override
    public String getPermission() {
        return "mlb.admin.luck";
    }

    @Override
    public boolean isAllowConsole() {
        return true;
    }

    @Override
    public Range getArgumentRange() {
        return new Range(3, 4);
    }

    @Override
    public CommandResult perform(CommandSender sender, String[] args) {
        if (!args[1].equalsIgnoreCase("luck")) {
            return new CommandResult(ResultType.BAD_USAGE);
        }
        if (Util.isNumber(args[2])) {
            if (!Util.isNumber(args[2], new Range(PluginConstants.LUCK_MIN_VALUE, PluginConstants.LUCK_MAX_VALUE))) {
                sender.sendMessage("Luck values must be between -100 and 100.");
                return new CommandResult(ResultType.PASSED);
            }
            final float luck = NumberUtils.toFloat(args[2]);
            final ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
            if (item.getType().isAir()) {
                sender.sendMessage("You must hold a lucky block in your hand to do this!");
                return new CommandResult(ResultType.PASSED);
            }
            final ItemMeta meta = item.getItemMeta();
            final CustomItemMeta customMeta = this.plugin.getMetaFactory().createCustomMeta(meta);

            if (customMeta.getString(PluginConstants.LuckyIdentifier) == null) {
                sender.sendMessage("You are not holding a valid lucky block in your hand.");
                return new CommandResult(ResultType.PASSED);
            }

            for (final LuckyBlock lb : this.plugin.getLuckyBlocks()) {
                if (customMeta.getString(PluginConstants.LuckyIdentifier).equals(lb.getInternalName())) {
                    final int amount = item.getAmount();
                    ((Player) sender).getInventory().setItemInMainHand(lb.asItem(this.plugin, luck, amount));
                    sender.sendMessage("Updated the luck of the lucky block in your hand to " + luck);
                    return new CommandResult(ResultType.PASSED);
                }
            }

            sender.sendMessage("You are not holding a valid lucky block in your hand.");
            return new CommandResult(ResultType.PASSED);
        }
        // /mlb set luck <player> <value> now
        if ((args.length != 4) || !Util.isNumber(args[3])) {
            return new CommandResult(ResultType.BAD_ARGUMENT_TYPE, args[3]);
        }
        if (!Util.isNumber(args[3], new Range(PluginConstants.LUCK_MIN_VALUE, PluginConstants.LUCK_MAX_VALUE))) {
            sender.sendMessage("Luck values must be between -100 and 100.");
            return new CommandResult(ResultType.PASSED);
        }
        final float luck = NumberUtils.toFloat(args[3]);

        final Player target = this.plugin.getServer().getPlayerExact(args[2]);
        if (target == null) {
            if (this.plugin.getPlayerManager().getPlayer(args[2]).isNull()) {
                sender.sendMessage(ChatColor.RED + "Could not find player " + args[2]
                        + " as they have never logged onto the server before.");
                return new CommandResult(ResultType.PASSED);
            }
            final OfflinePlayer p = Bukkit
                    .getOfflinePlayer(this.plugin.getPlayerManager().getPlayer(args[2]).getUUID());
            this.plugin.getPlayerManager().updateOffline(p.getUniqueId(), luck);
            sender.sendMessage("Set Value Offline.");
            return new CommandResult(ResultType.PASSED);
        }

        this.plugin.getPlayerManager().update(target.getUniqueId(), luck);

        sender.sendMessage("Set Value.");

        return new CommandResult(ResultType.PASSED);
    }

}
