package devjluvisi.mlb.cmds.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.PluginConstants;
import devjluvisi.mlb.api.items.CustomItemMeta;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.util.Range;
import devjluvisi.mlb.util.SubCommand;

public class PlayerLuckCommand implements SubCommand {

	private final MoreLuckyBlocks plugin;

	public PlayerLuckCommand(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getName() {
		return "luck";
	}

	@Override
	public String getDescription() {
		return "Set the luck values of lucky blocks or players.";
	}

	@Override
	public String getSyntax() {
		return "\n/mlb luck <player>\n/mlb luck <value>\n/mlb luck <player> <value>";
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
		return new Range(2, 3);
	}

	@Override
	public ExecutionResult perform(CommandSender sender, String[] args) {
		if (args.length == 2) {
			float luck;
			try {
				luck = Float.parseFloat(args[1]);
			} catch (final NumberFormatException e) {
				// TODO: Find player if the float is not parsed.
				return ExecutionResult.BAD_ARGUMENT_TYPE;
			}
			if ((luck < -100) || (luck > 100)) {
				sender.sendMessage(ChatColor.RED + "Luck values must be between -100 and 100.");
				return ExecutionResult.PASSED;
			}

			final ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
			if ((item == null) || item.getType().isAir()) {
				sender.sendMessage("You must hold a lucky block in your hand to do this!");
				return ExecutionResult.PASSED;
			}
			final ItemMeta meta = item.getItemMeta();
			final CustomItemMeta customMeta = this.plugin.getMetaFactory().createCustomMeta(meta);

			if (customMeta.getString(PluginConstants.LuckyIdentifier) == null) {
				sender.sendMessage("You are not holding a valid lucky block in your hand.");
				return ExecutionResult.PASSED;
			}

			for (final LuckyBlock lb : this.plugin.getLuckyBlocks()) {
				if (customMeta.getString(PluginConstants.LuckyIdentifier).equals(lb.getInternalName())) {
					final int amount = item.getAmount();
					((Player) sender).getInventory().setItemInMainHand(lb.asItem(this.plugin, luck, amount));
					sender.sendMessage("Updated the luck of the lucky block in your hand to " + luck);
					return ExecutionResult.PASSED;
				}
			}

			sender.sendMessage("You are not holding a valid lucky block in your hand.");
			return ExecutionResult.PASSED;
		}

		if (args.length == 3) {
			float luck;
			try {
				luck = Float.parseFloat(args[2]);
			} catch (final NumberFormatException e) {
				return ExecutionResult.BAD_ARGUMENT_TYPE;
			}
			if ((luck < -100) || (luck > 100)) {
				sender.sendMessage(ChatColor.RED + "Luck values must be between -100 and 100.");
				return ExecutionResult.PASSED;
			}
			final Player target = this.plugin.getServer().getPlayerExact(args[1]);
			if (target == null) {
				if (this.plugin.getPlayerFromConfig(args[1]) == null) {
					sender.sendMessage(ChatColor.RED + "Could not find player " + args[1]
							+ " as they have never logged onto the server before.");
					return ExecutionResult.PASSED;
				}
				final OfflinePlayer p = Bukkit.getOfflinePlayer(this.plugin.getPlayerFromConfig(args[1]).getKey());
				this.plugin.updateOfflinePlayer(p.getUniqueId(), luck);
				sender.sendMessage("Set Value Offline.");
				return ExecutionResult.PASSED;
			}
			this.plugin.getPlayerLuckMap().put(target.getUniqueId(), luck);
			this.plugin.savePlayerLuckMap();
			sender.sendMessage("Set Value.");
		}

		return ExecutionResult.PASSED;
	}

}
