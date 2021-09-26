package devjluvisi.mlb.events;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.potion.PotionEffectType;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.drops.LuckyBlockCommand;
import devjluvisi.mlb.blocks.drops.LuckyBlockPotionEffect;
import devjluvisi.mlb.menus.BasePage;
import devjluvisi.mlb.menus.LuckyMenu;
import devjluvisi.mlb.menus.LuckyMenu.View;
import devjluvisi.mlb.menus.pages.EditDrop;

public class EditDropInChatEvent implements Listener {

	private MoreLuckyBlocks plugin;

	public EditDropInChatEvent(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void commandEvent(PlayerCommandPreprocessEvent e) {
		if (!plugin.getPlayersEditingDrop().containsKey(e.getPlayer().getUniqueId())) {
			return;
		}

		EditDrop b = (EditDrop) plugin.getPlayersEditingDrop().get(e.getPlayer().getUniqueId())
				.getPage(View.EDIT_DROP.toInt());

		if (b.getAddPotionEffectStage() == 0) {
			e.setCancelled(true);

			if (ChatColor.stripColor(e.getMessage()).equalsIgnoreCase("/exit")) {
				plugin.getPlayersEditingDrop().get(e.getPlayer().getUniqueId()).open(e.getPlayer(), View.EDIT_DROP);
				plugin.getPlayersEditingDrop().remove(e.getPlayer().getUniqueId());
				e.getPlayer().sendMessage(ChatColor.GREEN + "You have cancelled your command action.");
				return;
			}
			if (!e.getMessage().startsWith("/")) {
				e.getPlayer().sendMessage(ChatColor.RED + "Command to add must start with a \"/\"");
				return;
			}

			e.getPlayer().sendMessage(ChatColor.GREEN + "Added command " + ChatColor.YELLOW + e.getMessage()
					+ ChatColor.GREEN + " to the lucky block you selected.");
			plugin.getLuckyBlocks().get(b.getBlockIndex()).getDroppableItems().get(b.getDropIndex()).getCommands()
					.add(new LuckyBlockCommand(e.getMessage()));

			plugin.getPlayersEditingDrop().get(e.getPlayer().getUniqueId()).open(e.getPlayer(), View.EDIT_DROP);
			plugin.getPlayersEditingDrop().remove(e.getPlayer().getUniqueId());
			return;
		}
		return;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void chatEvent(AsyncPlayerChatEvent e) {
		if (!plugin.getPlayersEditingDrop().containsKey(e.getPlayer().getUniqueId())) {
			return;
		}

		EditDrop b = (EditDrop) plugin.getPlayersEditingDrop().get(e.getPlayer().getUniqueId())
				.getPage(View.EDIT_DROP.toInt());

		Bukkit.getConsoleSender().sendMessage(b.getAddPotionEffectStage() + "");
		if (b.getAddPotionEffectStage() == 0) {
			return;
		}
		
		if (e.getMessage().equalsIgnoreCase("exit")) {
			e.setCancelled(true);
			if (plugin.getLuckyBlocks().get(b.getBlockIndex()).getDroppableItems().get(b.getDropIndex())
					.getPotionEffects().size() != 0 && b.getAddPotionEffectStage() != 1) {
				plugin.getLuckyBlocks().get(b.getBlockIndex()).getDroppableItems().get(b.getDropIndex()).getPotionEffects().remove(plugin.getLuckyBlocks().get(b.getBlockIndex()).getDroppableItems().get(b.getDropIndex())
					.getPotionEffects().size()-1);
			}
			e.getPlayer().sendMessage(ChatColor.GRAY + "You have stopped adding a potion effect.");
			
			Bukkit.getScheduler().runTask(plugin, () -> {

				plugin.getPlayersEditingDrop().get(e.getPlayer().getUniqueId()).open(e.getPlayer(), View.EDIT_DROP);
				plugin.getPlayersEditingDrop().remove(e.getPlayer().getUniqueId());

				});
			
			
			
			return;
		}

		e.setCancelled(true);

		if (b.getAddPotionEffectStage() == 1) {
			if (PotionEffectType.getByName(e.getMessage().toUpperCase()) == null) {
				e.getPlayer().sendMessage(ChatColor.DARK_RED + "Unknown potion effect type. Try again.");
				e.getPlayer().sendMessage(ChatColor.GRAY + "Type \"exit\" to exit.");
				return;
			}
			plugin.getLuckyBlocks().get(b.getBlockIndex()).getDroppableItems().get(b.getDropIndex()).getPotionEffects()
					.add(new LuckyBlockPotionEffect(PotionEffectType.getByName(e.getMessage()), -1, -1));

			e.getPlayer().sendMessage(
					ChatColor.GRAY + "Potion Effect Type: " + ChatColor.YELLOW + e.getMessage().toUpperCase());
			e.getPlayer().sendMessage(ChatColor.GRAY + "Amplifier: " + ChatColor.YELLOW + "Unset");
			e.getPlayer().sendMessage(ChatColor.GRAY + "Duration: " + ChatColor.YELLOW + "Unset");
			b.setAddPotionEffectStage(2);
			e.getPlayer().sendMessage(ChatColor.GOLD + "(2/3) > " + ChatColor.YELLOW + "Please enter the amplifier of the potion. (0-255)");
			return;
		} else if (b.getAddPotionEffectStage() == 2) {
			
			ArrayList<LuckyBlockPotionEffect> effects = plugin.getLuckyBlocks().get(b.getBlockIndex())
					.getDroppableItems().get(b.getDropIndex()).getPotionEffects();
			int amplifier = -1;
			try {
				amplifier = Integer.parseInt(e.getMessage());
				if (amplifier > 255 || amplifier < 0) {
					throw new NumberFormatException();
				}

			} catch (NumberFormatException exc) {
				e.getPlayer().sendMessage(
						ChatColor.RED + "Please enter a positive integer (0-255) for potion effect duration.");
				return;
			}
			effects.get(effects.size() - 1).setAmplifier(amplifier);

			e.getPlayer().sendMessage(ChatColor.GRAY + "Potion Effect Type: " + ChatColor.YELLOW
					+ effects.get(effects.size() - 1).getType().getName());
			e.getPlayer().sendMessage(ChatColor.GRAY + "Amplifier: " + ChatColor.YELLOW + amplifier);
			e.getPlayer().sendMessage(ChatColor.GRAY + "Duration: " + ChatColor.YELLOW + "Unset");
			b.setAddPotionEffectStage(3);
			e.getPlayer().sendMessage(ChatColor.GOLD + "(3/3) > " + ChatColor.YELLOW + "Please enter the duration of the potion. (0-32768)");
			
			return;
		} else if (b.getAddPotionEffectStage() == 3) {
			ArrayList<LuckyBlockPotionEffect> effects = plugin.getLuckyBlocks().get(b.getBlockIndex())
					.getDroppableItems().get(b.getDropIndex()).getPotionEffects();
			int duration = -1;
			try {
				duration = Integer.parseInt(e.getMessage());

				if (duration <= 0) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException exc) {
				e.getPlayer().sendMessage(
						ChatColor.RED + "Please enter a positive integer (0-32768) for potion effect duration.");
				return;
			}
			effects.get(effects.size() - 1).setDuration(duration);

			e.getPlayer().sendMessage(ChatColor.GRAY + "Potion Effect Type: " + ChatColor.YELLOW
					+ effects.get(effects.size() - 1).getType().getName());
			e.getPlayer().sendMessage(
					ChatColor.GRAY + "Amplifier" + ChatColor.YELLOW + effects.get(effects.size() - 1).getAmplifier());
			e.getPlayer().sendMessage(ChatColor.GRAY + "Duration: " + ChatColor.YELLOW + duration);

			e.getPlayer().sendMessage("");
			e.getPlayer().sendMessage(ChatColor.GREEN + "You successfully added a potion effect to this drop!");
			Bukkit.getScheduler().runTask(plugin, () -> {
				plugin.getPlayersEditingDrop().get(e.getPlayer().getUniqueId()).open(e.getPlayer(), View.EDIT_DROP);
				plugin.getPlayersEditingDrop().remove(e.getPlayer().getUniqueId());
				
				

				});
			b.setAddPotionEffectStage(0);
			e.getPlayer().sendMessage("");
		}
	}

}
