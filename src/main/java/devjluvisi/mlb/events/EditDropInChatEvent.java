package devjluvisi.mlb.events;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import devjluvisi.mlb.blocks.drops.LuckyBlockCommand;
import devjluvisi.mlb.blocks.drops.LuckyBlockPotionEffect;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.admin.EditDropMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.potion.PotionEffectType;

public record EditDropInChatEvent(MoreLuckyBlocks plugin) implements Listener {

    // TODO: Test to ensure that it works.
    @EventHandler(priority = EventPriority.HIGHEST)
    public void commandEvent(PlayerCommandPreprocessEvent e) {

        if (!this.plugin.getPlayersEditingDrop().containsKey(e.getPlayer().getUniqueId())) {
            return;
        }

        final MenuManager manager = new MenuManager(plugin);
        final EditDropMenu b = plugin.getPlayersEditingDrop().get(e.getPlayer().getUniqueId());

        final Player p = e.getPlayer();
        final String command = ChatColor.stripColor(e.getMessage());

        if (b.getAddPotionEffectStage() == 0) {
            e.setCancelled(true);

            if (command.equalsIgnoreCase("/exit")) {

                for (final LuckyBlock lb : this.plugin.getLuckyBlocks()) {
                    lb.clean();
                }
                manager.setMenuData(b.getResource());
                manager.open(p, MenuType.EDIT_LOOT);

                this.plugin.getPlayersEditingDrop().remove(p.getUniqueId());
                e.getPlayer().sendMessage(ChatColor.GREEN + "You have cancelled your command action.");
                return;
            }
            if (!command.startsWith("/")) {
                p.sendMessage(ChatColor.RED + "Command to add must start with a \"/\"");
                return;
            }

            p.sendMessage(ChatColor.GREEN + "Added command " + ChatColor.YELLOW + command + ChatColor.GREEN
                    + " to the lucky block you selected.");
            this.plugin.getLuckyBlocks().get(b.getBlockIndex()).getDroppableItems().get(b.getDropIndex()).getCommands()
                    .add(new LuckyBlockCommand(command));
            manager.setMenuData(b.getResource());
            manager.open(p, MenuType.EDIT_LOOT);
            this.plugin.getPlayersEditingDrop().remove(p.getUniqueId());
            return;
        }
        return;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void chatEvent(AsyncPlayerChatEvent e) {
        if (!this.plugin.getPlayersEditingDrop().containsKey(e.getPlayer().getUniqueId())) {
            return;
        }

        final EditDropMenu b = this.plugin.getPlayersEditingDrop().get(e.getPlayer().getUniqueId());
        final MenuManager manager = new MenuManager(plugin);
        final Player p = e.getPlayer();
        final String message = ChatColor.stripColor(e.getMessage());
        final LuckyBlockDrop luckyBlockDrop = this.plugin.getLuckyBlocks().get(b.getBlockIndex()).getDroppableItems()
                .get(b.getDropIndex());

        if (b.getAddPotionEffectStage() == 0) {
            return;
        }

        if (message.equalsIgnoreCase("exit")) {
            e.setCancelled(true);
            if ((luckyBlockDrop.getPotionEffects().size() != 0) && (b.getAddPotionEffectStage() != 1)) {
                luckyBlockDrop.getPotionEffects().remove(luckyBlockDrop.getPotionEffects().size() - 1);
            }
            p.sendMessage(ChatColor.GRAY + "You have stopped adding a potion effect.");

            for (final LuckyBlock lb : this.plugin.getLuckyBlocks()) {
                lb.clean();
            }

            Bukkit.getScheduler().runTask(this.plugin, () -> {
                manager.setMenuData(b.getResource());
                manager.open(p, MenuType.EDIT_LOOT);
                this.plugin.getPlayersEditingDrop().remove(p.getUniqueId());

            });

            return;
        }

        e.setCancelled(true);

        if (b.getAddPotionEffectStage() == 1) {
            if (PotionEffectType.getByName(message.toUpperCase()) == null) {
                p.sendMessage(ChatColor.DARK_RED + "Unknown potion effect type. Try again.");
                p.sendMessage(ChatColor.GRAY + "Type \"exit\" to exit.");
                return;
            }
            luckyBlockDrop.getPotionEffects()
                    .add(new LuckyBlockPotionEffect(PotionEffectType.getByName(message), -1, -1));

            p.sendMessage(ChatColor.GRAY + "Potion Effect Type: " + ChatColor.YELLOW + message.toUpperCase());
            p.sendMessage(ChatColor.GRAY + "Amplifier: " + ChatColor.YELLOW + "Unset");
            p.sendMessage(ChatColor.GRAY + "Duration: " + ChatColor.YELLOW + "Unset");
            b.setAddPotionEffectStage(2);
            p.sendMessage(ChatColor.GOLD + "(2/3) > " + ChatColor.YELLOW
                    + "Please enter the amplifier of the potion. (0-255)");
            return;
        } else if (b.getAddPotionEffectStage() == 2) {

            int amplifier = -1;
            try {
                amplifier = Integer.parseInt(message);
                if ((amplifier > 255) || (amplifier < 0)) {
                    throw new NumberFormatException();
                }

            } catch (final NumberFormatException exc) {
                p.sendMessage(ChatColor.RED + "Please enter a positive integer (0-255) for potion effect duration.");
                return;
            }
            luckyBlockDrop.getPotionEffects().get(luckyBlockDrop.getPotionEffects().size() - 1).setAmplifier(amplifier);

            p.sendMessage(ChatColor.GRAY + "Potion Effect Type: " + ChatColor.YELLOW + luckyBlockDrop.getPotionEffects()
                    .get(luckyBlockDrop.getPotionEffects().size() - 1).getType().getName());
            p.sendMessage(ChatColor.GRAY + "Amplifier: " + ChatColor.YELLOW + amplifier);
            p.sendMessage(ChatColor.GRAY + "Duration: " + ChatColor.YELLOW + "Unset");
            b.setAddPotionEffectStage(3);
            p.sendMessage(ChatColor.GOLD + "(3/3) > " + ChatColor.YELLOW
                    + "Please enter the duration of the potion. (0-32768)");

            return;
        } else if (b.getAddPotionEffectStage() == 3) {

            int duration = -1;
            try {
                duration = Integer.parseInt(message);

                if (duration <= 0) {
                    throw new NumberFormatException();
                }
            } catch (final NumberFormatException exc) {
                p.sendMessage(ChatColor.RED + "Please enter a positive integer (0-32768) for potion effect duration.");
                return;
            }
            luckyBlockDrop.getPotionEffects().get(luckyBlockDrop.getPotionEffects().size() - 1).setDuration(duration);

            p.sendMessage(ChatColor.GRAY + "Potion Effect Type: " + ChatColor.YELLOW + luckyBlockDrop.getPotionEffects()
                    .get(luckyBlockDrop.getPotionEffects().size() - 1).getType().getName());
            p.sendMessage(ChatColor.GRAY + "Amplifier: " + ChatColor.YELLOW + luckyBlockDrop.getPotionEffects()
                    .get(luckyBlockDrop.getPotionEffects().size() - 1).getAmplifier());
            p.sendMessage(ChatColor.GRAY + "Duration: " + ChatColor.YELLOW + duration);

            p.sendMessage("");
            p.sendMessage(ChatColor.GREEN + "You successfully added a potion effect to this drop!");
            Bukkit.getScheduler().runTask(this.plugin, () -> {
                manager.setMenuData(
                        b.getResource()
                );
                manager.open(p, MenuType.EDIT_LOOT);
                this.plugin.getPlayersEditingDrop().remove(p.getUniqueId());

            });
            b.setAddPotionEffectStage(0);
            p.sendMessage("");
        }
    }


}
