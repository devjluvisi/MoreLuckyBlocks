package devjluvisi.mlb.blocks;

import org.bukkit.Bukkit;

public class LuckyBlockCommand {
	
	private String command;

	public LuckyBlockCommand(String command) {
		super();
		this.command = command;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
	
	public void runCommand() {
		Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
	}

	@Override
	public String toString() {
		return "LuckyBlockCommand [command=" + command + "]";
	}
	
	
	

}
