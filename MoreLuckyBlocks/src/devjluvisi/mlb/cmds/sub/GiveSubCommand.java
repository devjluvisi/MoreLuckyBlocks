package devjluvisi.mlb.cmds.sub;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.cmds.MainCommand;
import devjluvisi.mlb.helper.LuckyBlockHelper;
import devjluvisi.mlb.util.SubCommand;
import net.md_5.bungee.api.ChatColor;

public class GiveSubCommand extends SubCommand {

	public GiveSubCommand(MainCommand cmd) {
		super(cmd, Arrays.asList("give"), "mlb.give", "/mlb give", true);
	}

	@Override
	public void execute() {
		if(args.length != 5) {
			sender.sendMessage(plugin.getMessagesYaml().getString("incorrect-usage").replaceAll("%usage%", "/mlb give <player> <lb name> <luck> <amount>"));
			return;
		}
		Player p = Bukkit.getPlayerExact(args[1]);
		if(p == null) {
			sender.sendMessage(ChatColor.RED + "Could not find " + ChatColor.GRAY + args[1] + ChatColor.RED + " are you sure they are online?");
			return;
		}
		if(!LuckyBlockHelper.doesExist(plugin.getBlocksYaml(), args[2].toLowerCase())) {
			sender.sendMessage(ChatColor.RED + "Lucky block does not exist.");
			return;
		}
		float luck = 0.0F;
		int amount = 1;
		try {
			luck = Float.parseFloat(args[3]);
			amount = Integer.parseInt(args[4]);
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "Invalid values for command. Please make sure all values follow the correct formatting (ex. no strings for numbers).");
		}
		
		LuckyBlock block = LuckyBlockHelper.getLuckyBlock(plugin.getBlocksYaml(), args[2].toLowerCase());
		block.setBlockLuck(luck);
		p.getInventory().addItem(block.asItem(amount));
		sender.sendMessage(ChatColor.GREEN + "Success!");
		
	}
	
	

}
