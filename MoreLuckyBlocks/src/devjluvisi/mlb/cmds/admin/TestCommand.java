package devjluvisi.mlb.cmds.admin;

import org.bukkit.command.CommandSender;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.util.Range;
import devjluvisi.mlb.util.SubCommand;

public class TestCommand implements SubCommand {

	private final MoreLuckyBlocks plugin;

	public TestCommand(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getName() {
		return "test";
	}

	@Override
	public String getDescription() {
		return "Test Command";
	}

	@Override
	public String getSyntax() {
		return "/mlb test";
	}

	@Override
	public String getPermission() {
		return "mlb.admin";
	}

	@Override
	public boolean isAllowConsole() {
		return true;
	}

	@Override
	public Range getArgumentRange() {
		return new Range(1, 1);
	}

	@Override
	public ExecutionResult perform(CommandSender sender, String[] args) {
		this.plugin.savePlayerLuckMap();
		sender.sendMessage("Running Test. Check logger");
		this.plugin.getAudit().dumpLogger();
		return ExecutionResult.PASSED;
	}

}
