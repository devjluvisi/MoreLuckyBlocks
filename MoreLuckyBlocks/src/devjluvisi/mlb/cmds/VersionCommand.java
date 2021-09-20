package devjluvisi.mlb.cmds;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.menus.ExampleMenu;
import devjluvisi.mlb.util.BaseCommand;
import fr.dwightstudio.dsmapi.MenuView;
import net.md_5.bungee.api.ChatColor;

public class VersionCommand extends BaseCommand {

	public VersionCommand(MoreLuckyBlocks plugin) {
		super(plugin, "mlbver");
	}
	
	@Override
	public void execute() {
		getPlayer().sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + ChatColor.STRIKETHROUGH.toString() + "--------------------------------");
		
		getPlayer().sendMessage(ChatColor.GOLD + "MoreLuckyBlocks " + ChatColor.GRAY + "(" + ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "v" + getPlugin().getDescription().getVersion() + ChatColor.GRAY + ")");
		getPlayer().sendMessage(ChatColor.GRAY + "API Version: " + ChatColor.RED + getPlugin().getDescription().getAPIVersion());
		getPlayer().sendMessage(ChatColor.GRAY + "Server Version: " + ChatColor.RED + getPlugin().getServer().getVersion().substring(getPlugin().getServer().getVersion().indexOf('(')));
		getPlayer().sendMessage(ChatColor.GRAY + "Author: " + ChatColor.RED + getPlugin().getDescription().getAuthors());
		getPlayer().sendMessage("");
		getPlayer().sendMessage(ChatColor.BLUE.toString() + ChatColor.UNDERLINE + getPlugin().getDescription().getWebsite());
		
		getPlayer().sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + ChatColor.STRIKETHROUGH.toString() + "--------------------------------");
		MenuView view = new ExampleMenu().open(getPlayer(), 0);
	}
	
	
}
