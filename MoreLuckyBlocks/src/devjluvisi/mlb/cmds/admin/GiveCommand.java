package devjluvisi.mlb.cmds.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;
import net.md_5.bungee.api.ChatColor;

/**
 * - "/mlb give [player] [name] [amount]" - "/mlb give [player] [name] [luck]
 * [amount]"
 *
 * @author jacob
 *
 */
public class GiveCommand implements SubCommand {

	private final MoreLuckyBlocks plugin;

	public GiveCommand(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
	}

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
	public ExecutionResult perform(CommandSender sender, String[] args) {

		final Player p = Bukkit.getPlayerExact(args[1]);

		if (p == null) {
			return ExecutionResult.INVALID_PLAYER;
		}

		final int index = this.plugin.getLuckyBlocks().indexOf(new LuckyBlock(args[2].toLowerCase()));
		if (index == -1) {
			sender.sendMessage(ChatColor.RED + "Lucky block does not exist.");
			return ExecutionResult.PASSED;
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
			return ExecutionResult.BAD_ARGUMENT_TYPE;
		}

		block.setBlockLuck(luck);
		p.getInventory().addItem(block.asItem(this.plugin, luck, amount));
		sender.sendMessage(ChatColor.GREEN + "Success!");
		return ExecutionResult.PASSED;
	}

}
