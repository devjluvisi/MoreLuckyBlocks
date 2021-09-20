package devjluvisi.mlb.cmds.sub;

import java.util.Arrays;

import org.bukkit.Material;

import devjluvisi.mlb.cmds.MainCommand;
import devjluvisi.mlb.helper.LuckyBlockHelper;
import devjluvisi.mlb.menus.LuckyBlockListMenu;
import devjluvisi.mlb.util.SubCommand;
import fr.dwightstudio.dsmapi.MenuView;
import net.md_5.bungee.api.ChatColor;

public class ListSubCommand extends SubCommand {

	public ListSubCommand(MainCommand cmd) {
		super(cmd, Arrays.asList("list"), "mlb.list", "/mlb list", false);
	}

	@Override
	public void execute() {
		MenuView view = new LuckyBlockListMenu(plugin.getBlocksYaml()).open(p, 0);
	}
	
	

}
