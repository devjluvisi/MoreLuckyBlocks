package devjluvisi.mlb.cmds.admin;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import devjluvisi.mlb.util.Range;
import devjluvisi.mlb.util.SubCommand;
import io.netty.util.internal.StringUtil;
import net.md_5.bungee.api.ChatColor;

public class ItemCommand implements SubCommand {

	@Override
	public String getName() {
		return "item";
	}

	@Override
	public String getDescription() {
		return "Useful tools for editing item attributes like name and lore.";
	}

	@Override
	public String getSyntax() {
		return "/mlb item <name|lore> <values>\nSeperate lore by commas for each line.";
	}

	@Override
	public String getPermission() {
		return "mlb.admin.item";
	}

	@Override
	public boolean isAllowConsole() {
		return false;
	}

	@Override
	public Range getArgumentRange() {
		return new Range(3, Integer.MAX_VALUE);
	}

	@Override
	public ExecutionResult perform(CommandSender sender, String[] args) {
		Player p = (Player)sender;
		if(p.getInventory().getItemInMainHand() == null || p.getInventory().getItemInMainHand().getType().isAir()) {
			p.sendMessage(ChatColor.RED + "You must hold an item to set.");
			return ExecutionResult.PASSED;
		}
		ItemStack i = p.getInventory().getItemInMainHand();
		ItemMeta meta = i.getItemMeta();
		
		if(args[1].equalsIgnoreCase("name")) {
			StringBuilder argStr = new StringBuilder();
			for(int j = 2; j < args.length; j++) {
				argStr.append(args[j] + " ");
			}
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', argStr.toString().trim()));
			p.sendMessage(ChatColor.AQUA + "Name has been set.");
		}else if(args[1].equalsIgnoreCase("lore")) {
			List<String> lore = new LinkedList<String>();
			StringBuilder argStr = new StringBuilder();
			for(int j = 2; j < args.length; j++) {
				argStr.append(args[j] + " ");
			}
			String[] splitString = StringUtils.split(argStr.toString(), ",");
			for(int j = 0; j < splitString.length; j++) {
				lore.add(ChatColor.translateAlternateColorCodes('&', splitString[j].trim()));
			}
			meta.setLore(lore);
			p.sendMessage(ChatColor.AQUA + "Lore has been set.");
		}else {
			return ExecutionResult.BAD_ARGUMENT_TYPE;
		}
		i.setItemMeta(meta);
		return ExecutionResult.PASSED;
	}

}
