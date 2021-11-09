package devjluvisi.mlb.events;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import devjluvisi.mlb.blocks.drops.LuckyBlockCommand;
import devjluvisi.mlb.blocks.drops.LuckyBlockPotionEffect;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.admin.EditDropMenu;
import devjluvisi.mlb.util.config.files.messages.Message;
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
                e.getPlayer().sendMessage(Message.CANCELLED.get());
                return;
            }
            if (!command.startsWith("/")) {
                p.sendMessage(Message.A1.get());
                return;
            }


            p.sendMessage(Message.A2.format(command, plugin.getLuckyBlocks().get(b.getBlockIndex()).getInternalName()));
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
            p.sendMessage(Message.A3.get());

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
                p.sendMessage(Message.A4.format(message.toUpperCase()));
                p.sendMessage(Message.TYPE_EXIT.get());
                return;
            }
            luckyBlockDrop.getPotionEffects()
                    .add(new LuckyBlockPotionEffect(PotionEffectType.getByName(message), -1, -1));

            p.sendMessage(Message.A5.format(message.toUpperCase(), "Unset", "Unset"));
            b.setAddPotionEffectStage(2);
            p.sendMessage(Message.A6.get());
            return;
        } else if (b.getAddPotionEffectStage() == 2) {

            int amplifier = -1;
            try {
                amplifier = Integer.parseInt(message);
                if ((amplifier > 255) || (amplifier < 0)) {
                    throw new NumberFormatException();
                }

            } catch (final NumberFormatException exc) {
                p.sendMessage(Message.A8.get());
                return;
            }
            luckyBlockDrop.getPotionEffects().get(luckyBlockDrop.getPotionEffects().size() - 1).setAmplifier(amplifier);

            p.sendMessage(Message.A5.format(luckyBlockDrop.getPotionEffects()
                    .get(luckyBlockDrop.getPotionEffects().size() - 1).getType().getName(), amplifier, "Unset"));

            b.setAddPotionEffectStage(3);
            p.sendMessage(Message.A7.get());

            return;
        } else if (b.getAddPotionEffectStage() == 3) {

            int duration = -1;
            try {
                duration = Integer.parseInt(message);

                if (duration <= 0) {
                    throw new NumberFormatException();
                }
            } catch (final NumberFormatException exc) {
                p.sendMessage(Message.A9.get());
                return;
            }
            luckyBlockDrop.getPotionEffects().get(luckyBlockDrop.getPotionEffects().size() - 1).setDuration(duration);

            p.sendMessage(Message.A5.format(luckyBlockDrop.getPotionEffects()
                    .get(luckyBlockDrop.getPotionEffects().size() - 1).getType().getName(), luckyBlockDrop.getPotionEffects()
                    .get(luckyBlockDrop.getPotionEffects().size() - 1).getAmplifier(), duration));

            p.sendMessage("");
            p.sendMessage(Message.A10.get());
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
