package devjluvisi.mlb.cmds.admin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.helper.Util;
import devjluvisi.mlb.util.Range;
import devjluvisi.mlb.util.structs.DropStructure;
import net.md_5.bungee.api.ChatColor;

public class StructureCommand implements SubCommand {
	
	private MoreLuckyBlocks plugin;
	
	public StructureCommand(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getName() {
		return "struct";
	}

	@Override
	public String getDescription() {
		return "Configure a structure for a lucky block.";
	}

	@Override
	public String getSyntax() {
		return "/mlb struct";
	}

	@Override
	public String getPermission() {
		return "mlb.admin.struct";
	}

	@Override
	public boolean isAllowConsole() {
		return false;
	}

	@Override
	public Range getArgumentRange() {
		return new Range(1, Integer.MAX_VALUE);
	}

	@Override
	public ExecutionResult perform(CommandSender sender, String[] args) {
		Player p = (Player)sender;
		if(args.length == 1) {
			sender.sendMessage("");
			sender.sendMessage(ChatColor.DARK_GRAY + "-- " + ChatColor.GRAY + "LuckyBlock Structures" + ChatColor.DARK_GRAY + " --");
			sender.sendMessage(ChatColor.GREEN + "What are Structures?");
			sender.sendMessage(ChatColor.GOLD + 
					"Structures are ways to physically modify the world for a specific lucky block drop.\n"
					+ "Structures allow lucky block creators to define what they want to happen when a lucky block breaks outside of just dropping items or applying potions.\n"
					+ "With the use of these structures, you can make blocks get placed or spawn mobs relative to a lucky block.");
			sender.sendMessage(ChatColor.GREEN + "How do I make one?");
			sender.sendMessage(ChatColor.GOLD + "After executing the command to make a structure, you will be teleported to a new world where you will place blocks and spawn mobs relative to a lucky block. When you have finished, you exit and the blocks/mobs you spawned are saved for that drop.");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "/mlb struct help " + ChatColor.GRAY + "for a list of commands.");
			sender.sendMessage("");
			return ExecutionResult.PASSED;
		}
		if(args.length == 2) {
			if(args[1].equalsIgnoreCase("help")) {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.GRAY + "/mlb struct edit <lucky-block-name> <drop-number>");
				sender.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC + "Edit the structure of a specific drop.");
				sender.sendMessage(ChatColor.GRAY + "/mlb struct has <lucky-block-name> <drop-number>");
				sender.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC + "Check if a lucky block drop has a structure defined with it.");
				sender.sendMessage(ChatColor.GRAY + "/mlb struct reset <lucky-block-name> <drop-number>");
				sender.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC + "Delete the structure for a drop if it has one.");
				sender.sendMessage("");
			}
			return ExecutionResult.PASSED;
		}
		if(args.length == 4) {
			String internalName = Util.makeInternal(args[2]);
			int dropIndex = (int)Util.toNumber(args[3]);
			if(!plugin.getLuckyBlocks().contains(internalName)) {
				sender.sendMessage(ChatColor.RED + "Lucky block " + args[2] + " does not exist.");
				return ExecutionResult.PASSED;
			}
			LuckyBlock lb = plugin.getLuckyBlocks().get(internalName);
			
			if(!(new Range(0, lb.getDroppableItems().size()).isInRange(dropIndex))) {
				sender.sendMessage(ChatColor.RED + "Request drop index of " + dropIndex + " is out of range for lucky block " + lb.getInternalName());
				return ExecutionResult.PASSED;
			}
			LuckyBlockDrop selectedDrop = lb.getDroppableItems().get(dropIndex);
			
			if(args[1].equalsIgnoreCase("edit")) {
				if(plugin.getServerDropStructure().hasEditingPlayer()) {
					p.sendMessage(ChatColor.RED + "There is already a player editing a lucky block structure.");
					return ExecutionResult.PASSED;
				}
				plugin.getServerDropStructure().update(lb, selectedDrop, p);
				plugin.getServerDropStructure().teleport(p);
				//p.sendMessage("Teleported");
			} else if(args[1].equalsIgnoreCase("has")) {
				if(!selectedDrop.hasStructure()) {
					sender.sendMessage(ChatColor.BLUE + "Drop does not have a drop structure.");
				}else {
					sender.sendMessage(ChatColor.BLUE + "Drop has a structure.");
				}
			} else if(args[1].equalsIgnoreCase("reset")) {
				
			}
			return ExecutionResult.PASSED;
		}
		p.performCommand("mlb struct help");
		return ExecutionResult.PASSED;
	}
	

}
