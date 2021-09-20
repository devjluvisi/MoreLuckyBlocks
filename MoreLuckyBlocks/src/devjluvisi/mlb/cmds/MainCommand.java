package devjluvisi.mlb.cmds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.sub.CreateSubCommand;
import devjluvisi.mlb.cmds.sub.GiveSubCommand;
import devjluvisi.mlb.cmds.sub.HelpSubCommand;
import devjluvisi.mlb.cmds.sub.ListSubCommand;
import devjluvisi.mlb.util.BaseCommand;
import devjluvisi.mlb.util.Range;
import devjluvisi.mlb.util.SubCommand;
import net.md_5.bungee.api.ChatColor;

/**
 * Main Command for the /mlb command.
 * Note that in order to reduce the size of the class other static methods are used.
 * @author jacob
 *
 */
public class MainCommand extends BaseCommand {

	private final List<SubCommand> subCommands = Arrays.asList(
			new HelpSubCommand(this),
			new GiveSubCommand(this),
			new ListSubCommand(this),
			new CreateSubCommand(this));
	
	public MainCommand(MoreLuckyBlocks plugin) {
		super(plugin, "mlb", new Range(0, Integer.MAX_VALUE), "/mlb", true);
	}

	@Override
	public void execute() {
		for(SubCommand s: subCommands) {
			if(s.validate()) return;
		}
		getSender().sendMessage(ChatColor.RED + "Unknown Command! Try /mlb help for a list of commands.");
	}
	
	

}
