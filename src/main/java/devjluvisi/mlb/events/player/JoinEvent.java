package devjluvisi.mlb.events.player;

import devjluvisi.mlb.MoreLuckyBlocks;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

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
        new BukkitRunnable() {
            @Override
            public void run() {
                e.getPlayer().sendMessage(ChatColor.GOLD.toString() + ChatColor.ITALIC + "Running MoreLuckyBlocks version " + plugin.getDescription().getVersion());
                if (plugin.getAudit().getMap().size() > 50000) {
                    e.getPlayer().sendMessage(ChatColor.RED + "[WARNING] Over 50,000 unopened lucky blocks exist on your server. Lag may be present due to large data requirements. /mlb reset to remove lucky block player data.");
                }
            }
        }.runTaskLaterAsynchronously(plugin, 20L);
    }
}
