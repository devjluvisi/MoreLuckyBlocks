package devjluvisi.mlb.cmds.sub;

import java.util.Arrays;

import devjluvisi.mlb.cmds.MainCommand;
import devjluvisi.mlb.util.SubCommand;

public class HelpSubCommand extends SubCommand {

	public HelpSubCommand(MainCommand cmd) {
		super(cmd, Arrays.asList("help"), "mlb.help", "/mlb help", true);
	}

	@Override
	public void execute() {
		sender.sendMessage("Help Command!");
	}
	
	

}
