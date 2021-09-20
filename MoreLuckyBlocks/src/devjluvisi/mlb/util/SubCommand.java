package devjluvisi.mlb.util;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.MainCommand;
import net.md_5.bungee.api.ChatColor;

public abstract class SubCommand {

	// Arguments needed to run command excluding user-enterable. Ex /mlb run
	private MainCommand cmd;
	private List<String> baseArguments;
	private String permission;
	private String usage;
	private boolean consoleExecute;
	
	protected MoreLuckyBlocks plugin;
	protected String[] args;
	protected Player p;
	protected CommandSender sender;
	
	public SubCommand(MainCommand cmd, List<String> baseArguments, String permission, String usage, boolean consoleExecute) {
		super();
		this.cmd = cmd;
		this.baseArguments = baseArguments;
		this.permission = permission;
		this.usage = usage;
		this.consoleExecute = consoleExecute;
	}
	
	public boolean validate() {
		
		if(this.baseArguments.size() > cmd.length()) {
			return false;
		}
		for(int i = 0; i < this.baseArguments.size(); i++) {
			if(!this.baseArguments.get(i).equalsIgnoreCase(cmd.getArg(i))) {
				return false;
			}
		}
		if(!cmd.getSender().hasPermission(permission)) {
			cmd.getSender().sendMessage(cmd.getPlugin().getMessagesYaml().getString("no-permission-msg"));
			return false;
		}

		if(this.cmd.getPlayer() == null && this.consoleExecute == false) {
			cmd.getSender().sendMessage(ChatColor.RED + cmd.getPlugin().getMessagesYaml().getString("must-be-player-msg"));
			return false;
		}
		// We know the user is good to use the command so we update the protected values.
		this.plugin = cmd.getPlugin();
		this.p = cmd.getPlayer();
		this.sender = cmd.getSender();
		this.args = cmd.getArguments();
		this.execute();
		return true;
	}
	
	public abstract void execute();
	
	public List<String> getBaseArguments() {
		return baseArguments;
	}
	public void setBaseArguments(List<String> baseArguments) {
		this.baseArguments = baseArguments;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public String getUsage() {
		return usage;
	}
	public void setUsage(String usage) {
		this.usage = usage;
	}
	public boolean isConsoleExecute() {
		return consoleExecute;
	}
	public void setConsoleExecute(boolean consoleExecute) {
		this.consoleExecute = consoleExecute;
	}
	
	
	
	

}
