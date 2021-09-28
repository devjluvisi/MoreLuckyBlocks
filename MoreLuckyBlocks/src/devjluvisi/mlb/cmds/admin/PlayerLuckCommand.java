package devjluvisi.mlb.cmds.admin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.players.LuckyPlayer;
import devjluvisi.mlb.util.Range;
import devjluvisi.mlb.util.SubCommand;import net.md_5.bungee.api.ChatColor;

public class PlayerLuckCommand implements SubCommand {
	
	private MoreLuckyBlocks plugin;
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
		return "/mlb setluck <value>\n/mlb setluck <player> <value>";
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
		if(args.length == 2) {
			float luck;
			try {
				luck = Float.parseFloat(args[2]);
			} catch (NumberFormatException e) {
				return ExecutionResult.BAD_ARGUMENT_TYPE;
			}
			LuckyPlayer target;
			if(plugin.getServer().getPlayerExact(args[1])==null) {
				
				//target = new LuckyPlayer(plugin).trackOffline()
			}
			//LuckyPlayer luck = new LuckyPlayer(args)
			// Add Metadata to block in hand
		}
		if(args.length == 3) {
			float luck;
			try {
				luck = Float.parseFloat(args[2]);
			} catch (NumberFormatException e) {
				return ExecutionResult.BAD_ARGUMENT_TYPE;
			}
			Player target = plugin.getServer().getPlayerExact(args[1]);
			if(target == null) {
				return ExecutionResult.INVALID_PLAYER;
			}
			
		}
		
		
		return ExecutionResult.PASSED;
	}
	
	

}
