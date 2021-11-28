package devjluvisi.mlb.events.player;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.helper.Util;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.StringUtil;

public class JoinEvent implements Listener {

    private final MoreLuckyBlocks plugin;

    public JoinEvent(MoreLuckyBlocks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void joinPlayerEvent(PlayerJoinEvent e) {
        if (!e.getPlayer().hasPermission("mlb.admin")) {
            return;
        }
        if (plugin.getSettingsManager().isFirstBoot()) {
            final String newLine = "\n";
            TextComponent firstBootMessage = new TextComponent();
            TextComponent githubPage = new TextComponent(ChatColor.GOLD.toString() + ChatColor.BOLD + "GitHub");
            TextComponent wikiPage = new TextComponent(ChatColor.GOLD.toString() + ChatColor.BOLD + "Wiki");
            TextComponent spigotPage = new TextComponent(ChatColor.GOLD.toString() + ChatColor.BOLD + "Spigot");

            githubPage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to open.")));
            wikiPage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to open.")));
            spigotPage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to open.")));

            for(int i = 0; i < 20; i++) {

                if(i % 2 == 0) {
                    firstBootMessage.addExtra(Util.toColor("&6✪"));
                }else{
                    firstBootMessage.addExtra(Util.toColor("&e✪"));
                }

            }
            firstBootMessage.addExtra(newLine);
            firstBootMessage.addExtra(Util.toColor("&7Welcome to MoreLuckyBlocks"));
            firstBootMessage.addExtra(newLine);
            firstBootMessage.addExtra(Util.toColor("&7Ver: &6" + plugin.getDescription().getVersion()));
            firstBootMessage.addExtra(newLine);
            firstBootMessage.addExtra(newLine);
            firstBootMessage.addExtra(ChatColor.DARK_GRAY + "★ ");
            firstBootMessage.addExtra(githubPage);
            firstBootMessage.addExtra(ChatColor.DARK_GRAY + " ★ ");
            firstBootMessage.addExtra(wikiPage);
            firstBootMessage.addExtra(ChatColor.DARK_GRAY + " ★ ");
            firstBootMessage.addExtra(spigotPage);
            firstBootMessage.addExtra(ChatColor.DARK_GRAY + " ★");
            firstBootMessage.addExtra(newLine);
            firstBootMessage.addExtra(newLine);
            firstBootMessage.addExtra(Util.toColor("&7&oUse /mlb help to view commands."));
            firstBootMessage.addExtra(newLine);
            for(int i = 0; i < 20; i++) {

                if(i % 2 == 0) {
                    firstBootMessage.addExtra(Util.toColor("&6✪"));
                }else{
                    firstBootMessage.addExtra(Util.toColor("&e✪"));
                }

            }
            e.getPlayer().spigot().sendMessage(firstBootMessage);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                e.getPlayer().sendMessage(ChatColor.GOLD.toString() + ChatColor.ITALIC + "Running MoreLuckyBlocks version " + plugin.getDescription().getVersion());
                if (plugin.getAudit().getMap().size() > plugin.getSettingsManager().getWarningThreshold()) {
                    e.getPlayer().sendMessage(ChatColor.RED + "[WARNING] Over " + plugin.getSettingsManager().getWarningThreshold() + " unopened lucky blocks exist on your server. Lag may be present due to large data requirements. /mlb reset to remove lucky block player data.");
                }
            }
        }.runTaskLaterAsynchronously(plugin, 20L);
    }

}
