package devjluvisi.mlb.cmds.general;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;
import net.md_5.bungee.api.ChatColor;

public class LuckCommand implements SubCommand {

	private final MoreLuckyBlocks plugin;

	public LuckCommand(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getName() {
		return "luck";
	}

	@Override
	public String getDescription() {
		return "Get the luck of yourself or another player.";
	}

	@Override
	public String getSyntax() {
		return "/mlb luck\n/mlb luck <player>";
	}

	@Override
	public String getPermission() {
		return "mlb.luck";
	}

	@Override
	public boolean isAllowConsole() {
		return true;
	}

	@Override
	public Range getArgumentRange() {
		return new Range(1, 2);
	}

	@Override
	public ExecutionResult perform(CommandSender sender, String[] args) {
		if (args.length == 1) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "We could not find a luck value for you.");
				return ExecutionResult.PASSED;
			}
			sender.sendMessage("");
			sender.sendMessage("Your Luck: " + this.plugin.getPlayerManager()
					.getPlayer(this.plugin.getServer().getPlayerExact(sender.getName()).getName()).getLuck());
			sender.sendMessage("");
			return ExecutionResult.PASSED;
		}
		if (args.length == 2) {
			if (this.plugin.getPlayerManager().getPlayer(args[1]).isNull()) {
				sender.sendMessage("");
				sender.sendMessage("Player \"" + args[1] + "\" has never logged in before so they have no luck value.");
				sender.sendMessage("");
				return ExecutionResult.PASSED;
			}
			sender.sendMessage("");
			sender.sendMessage(args[1] + "'s Luck: " + this.plugin.getPlayerManager().getPlayer(args[1]).getLuck());
			sender.sendMessage("");
		}
		return ExecutionResult.PASSED;
	}

}
