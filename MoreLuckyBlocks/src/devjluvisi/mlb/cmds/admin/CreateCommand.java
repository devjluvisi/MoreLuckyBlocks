package devjluvisi.mlb.cmds.admin;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.helper.LuckyBlockHelper;
import devjluvisi.mlb.helper.Util;
import devjluvisi.mlb.util.Range;
import devjluvisi.mlb.util.SubCommand;
import net.md_5.bungee.api.ChatColor;

public class CreateCommand implements SubCommand {
	
	private MoreLuckyBlocks plugin;
	public CreateCommand(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getName() {
		return "create";
	}

	@Override
	public String getDescription() {
		return "Creates a lucky block with the item in your hand.";
	}

	@Override
	public String getSyntax() {
		return "/mlb create <?break-permission> <?default-luck>";
	}

	@Override
	public String getPermission() {
		return "mlb.admin.create";
	}

	@Override
	public boolean isAllowConsole() {
		return false;
	}

	@Override
	public Range getArgumentRange() {
		return new Range(1, 3);
	}

	@Override
	public ExecutionResult perform(CommandSender sender, String[] args) {
		Player p = (Player)sender;
		ItemStack item = p.getInventory().getItemInMainHand();
		if(item == null || !item.getType().isBlock()) {
			p.sendMessage(ChatColor.RED + "You must hold a block in your hand to make a lucky block.");
			return ExecutionResult.PASSED;
		}
		if(!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
			p.sendMessage(ChatColor.RED + "Please name your item in order to make it a lucky block.\n(Change its display name)");
			return ExecutionResult.PASSED;
		}
		float luck;
		LuckyBlock luckyBlock = new LuckyBlock();
		if(!LuckyBlockHelper.unique(plugin.getLuckyBlocks(), item.getItemMeta().getDisplayName())) {
			p.sendMessage(ChatColor.RED + "There is already a lucky block with this name.\nPlease change the name of your lucky block.");
			return ExecutionResult.PASSED;
		}
		if(args.length >= 1) {
			luckyBlock.setInternalName(Util.makeInternal(item.getItemMeta().getDisplayName()));
			luckyBlock.setName(item.getItemMeta().getDisplayName());
			luckyBlock.setBlockMaterial(item.getType());
			luckyBlock.setLore(item.getItemMeta().getLore() != null ? item.getItemMeta().getLore() : new ArrayList<String>());
			luckyBlock.setDefaultBlockLuck(0.0F);
		}
		if(args.length >= 2) {
			luckyBlock.setBreakPermission(StringUtils.lowerCase(args[1]));
		}
		if(args.length >= 3) {
			try {
				luck = Float.parseFloat(args[2]);
			} catch (NumberFormatException e) {
				p.sendMessage(ChatColor.RED + "You must use a whole or decimal number to set the default block luck!");
				return ExecutionResult.PASSED;
			}
			luckyBlock.setDefaultBlockLuck(luck);
		}
		plugin.getLuckyBlocks().add(luckyBlock);
		p.sendMessage(ChatColor.DARK_GREEN + "You have added a new lucky block.");
		p.sendMessage(ChatColor.GRAY + "Internal Name" + ChatColor.DARK_GRAY + " -> " + ChatColor.RED + luckyBlock.getInternalName());
		p.sendMessage(ChatColor.GRAY + "Item Name" + ChatColor.DARK_GRAY + " -> " + ChatColor.RED + luckyBlock.getName());
		p.sendMessage(ChatColor.GRAY + "Block Type" + ChatColor.DARK_GRAY + " -> " + ChatColor.RED + luckyBlock.getBlockMaterial());
		p.sendMessage(ChatColor.GRAY + "Lore Lines" + ChatColor.DARK_GRAY + " -> " + ChatColor.RED  + luckyBlock.getLore().size());
		p.sendMessage(ChatColor.GRAY + "Default Luck" + ChatColor.DARK_GRAY + " -> " + ChatColor.RED + luckyBlock.getDefaultBlockLuck());
		p.sendMessage(ChatColor.GRAY + "Break Permission" + ChatColor.DARK_GRAY + " -> " + ChatColor.RED.toString() + (luckyBlock.getBreakPermission() == "" ? "None" : luckyBlock.getBreakPermission()));
		//TODO: Add cool mob sound
		p.sendMessage(ChatColor.LIGHT_PURPLE + "Type \"/mlb list\" in order to edit the drops and view information about your lucky block.\nAttributes can also be edited in config.");
		return ExecutionResult.PASSED;
	}

}
