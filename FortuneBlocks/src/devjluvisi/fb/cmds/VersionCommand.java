package devjluvisi.fb.cmds;

import devjluvisi.fb.FortuneBlocks;
import devjluvisi.fb.util.FBCommand;

public class VersionCommand extends FBCommand {

	public VersionCommand(FortuneBlocks plugin) {
		super(plugin, "fortuneblocks");
	}
	
	@Override
	public void execute() {
		getPlayer().sendMessage("Fortune Blocks v" + getPlugin().getDescription().getVersion());
	}
	
	
}
