package devjluvisi.mlb.cmds.sub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitScheduler;

import devjluvisi.mlb.cmds.MainCommand;
import devjluvisi.mlb.util.SubCommand;
import net.md_5.bungee.api.ChatColor;

public class CreateSubCommand extends SubCommand implements Listener {
	
	public CreateSubCommand(MainCommand cmd) {
		super(cmd, Arrays.asList("admin", "create"), "mlb.admin.create", "/mlb admin create", false);
	}
	
	/**
	 * A hash set of player UUIDs made for admins who are creating lucky blocks using the
	 * /mlb admin create command!
	 */
	private static ArrayList<UUID> adminCreateLB = new ArrayList<UUID>();
	private int taskId;
	
	private enum AWAITING_FOR {
		LUCKY_INTERNAL_NAME, ITEM_NAME, ITEM_LORE, BLOCK_TYPE, BREAK_PERMISSION, DEFAULT_LUCK
	}
	private AWAITING_FOR currentProcess = AWAITING_FOR.LUCKY_INTERNAL_NAME;

	@Override
	public void execute() {
		if(adminCreateLB.contains(p.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "You are already creating a lucky block! Type \"exit\" in the chat to leave.");
			return;
		}
		adminCreateLB.add(p.getUniqueId());
		p.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "********************************************");
		p.sendMessage("");
		p.sendMessage(ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + "You have entered Creator Mode!");
		p.sendMessage(ChatColor.GRAY + "To create a new lucky block, type the answers to the questions in chat as they appear!");
		p.sendMessage("");
		p.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "********************************************");
		
		BukkitScheduler scheduler = plugin.getServer().getScheduler();
         scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
            	if(!adminCreateLB.contains(p.getUniqueId())) return;
            	p.sendMessage(ChatColor.YELLOW.toString() + "Type \"exit\" in the chat to leave.");
            }
        }, 60L);
         taskId = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
            	if(!adminCreateLB.contains(p.getUniqueId())) {
            		scheduler.cancelTask(taskId);
            		return;
            	}
            	switch(currentProcess) {
            	case LUCKY_INTERNAL_NAME:
            		 p.sendMessage("");
                     p.sendMessage(ChatColor.LIGHT_PURPLE + "Please enter the internal name you would like your lucky block to have.\nThis is not the name of the item. It is the \"internal\" name which is used by config, commands, etc.");
                     p.sendMessage(ChatColor.GRAY + "Ex: /mlb give <player> <internal-name> ...");
                     p.sendMessage("");
                break;
				default:
					break;
            	}
               
            }
        }, 120L, 150L);
	}
	
	@EventHandler
	public void playerTalk(AsyncPlayerChatEvent e) {
		if(!adminCreateLB.contains(e.getPlayer().getUniqueId())) {
			return;
		}
		if(e.getMessage().equalsIgnoreCase("exit")) {
			adminCreateLB.remove(e.getPlayer().getUniqueId());
			e.getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "You have exited the lucky block creator mode.");
			e.setCancelled(true);
			return;
		}
		switch(currentProcess) {
    	case LUCKY_INTERNAL_NAME:
    		 e.getPlayer().sendMessage(ChatColor.GRAY + "You set the name to: " + ChatColor.YELLOW + e.getMessage());
    		 
        break;
		default:
			break;
    	}
		
		e.setCancelled(true);
	}
}
