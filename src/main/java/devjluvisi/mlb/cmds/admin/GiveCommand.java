package devjluvisi.mlb.cmds.admin;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * - "/mlb give [player] [name] [amount]" - "/mlb give [player] [name] [luck]
 * [amount]"
 *
 * @author jacob
 */
public record GiveCommand(MoreLuckyBlocks plugin) implements SubCommand {

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getDescription() {
        return "Give a lucky block to a specific player.";
    }

    @Override
    public String getSyntax() {
        return "/mlb give <player> <block-name> <[?]block-luck> <amount>";
    }

    @Override
    public String getPermission() {
        return "mlb.admin.give";
    }

    @Override
    public boolean isAllowConsole() {
        return true;
    }

    @Override
    public Range getArgumentRange() {
        return new Range(4, 5);
    }

    @Override
    public CommandResult perform(CommandSender sender, String[] args) {

        final Player p = Bukkit.getPlayerExact(args[1]);

        if (p == null) {
            return new CommandResult(ResultType.INVALID_PLAYER, args[1]);
        }

        final int index = this.plugin.getLuckyBlocks().indexOf(new LuckyBlock(args[2].toLowerCase()));
        if (index == -1) {
            return new CommandResult(ResultType.INVALID_LUCKY_BLOCK, args[2]);
        }
        final LuckyBlock block = this.plugin.getLuckyBlocks().get(index);

        float luck = block.getDefaultBlockLuck();
        int amount = 1;

        try {
            if (args.length == 5) {
                luck = Float.parseFloat(args[3]);
                amount = Integer.parseInt(args[4]);
            } else {
                amount = Integer.parseInt(args[3]);
            }

        } catch (final NumberFormatException e) {
            return new CommandResult(ResultType.BAD_ARGUMENT_TYPE);
        }

        block.setBlockLuck(luck);
        p.getInventory().addItem(block.asItem(this.plugin, luck, amount));
        sender.sendMessage(ChatColor.GREEN + "Success!");
        return new CommandResult(ResultType.PASSED);
    }

}
