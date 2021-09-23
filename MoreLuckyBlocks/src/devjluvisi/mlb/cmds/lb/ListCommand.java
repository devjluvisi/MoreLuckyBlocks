package devjluvisi.mlb.cmds.lb;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.menus.LuckyBlockListMenu;
import devjluvisi.mlb.util.Range;
import devjluvisi.mlb.util.SubCommand;

public class ListCommand implements SubCommand {
	
	private MoreLuckyBlocks plugin;
	public ListCommand(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getName() {
		return "list";
	}

	@Override
	public String getDescription() {
		return "A GUI list of all of the current lucky blocks on the server.";
	}

	@Override
	public String getSyntax() {
		return "/mlb list";
	}

	@Override
	public String getPermission() {
		return "mlb.list";
	}

	@Override
	public boolean isAllowConsole() {
		return false;
	}

	@Override
	public Range getArgumentRange() {
		return new Range(1, 1);
	}

	@Override
	public ExecutionResult perform(CommandSender sender, String[] args) {
		new LuckyBlockListMenu(plugin).open((Player)sender, 0);
		return ExecutionResult.PASSED;
	}
	

}
