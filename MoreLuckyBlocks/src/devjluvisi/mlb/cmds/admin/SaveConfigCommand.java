package devjluvisi.mlb.cmds.admin;

import org.bukkit.command.CommandSender;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.util.Range;
import devjluvisi.mlb.util.SubCommand;
import net.md_5.bungee.api.ChatColor;

public class SaveConfigCommand implements SubCommand {
	
	private MoreLuckyBlocks plugin;
	public SaveConfigCommand(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "save";
	}

	@Override
	public String getDescription() {
		return "Saves lucky block changes to config.";
	}

	@Override
	public String getSyntax() {
		return "/mlb save";
	}

	@Override
	public String getPermission() {
		return "mlb.admin.save";
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
		sender.sendMessage(ChatColor.GREEN + "Configuration files have been updated according to unmodified changes.");
		for(int i = 0; i < plugin.getLuckyBlocks().size(); i++) {
			plugin.getLuckyBlocks().get(i).saveConfig(plugin.getBlocksYaml());
		}
		return ExecutionResult.PASSED;
	}

}
