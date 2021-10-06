package devjluvisi.mlb.events.players;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.PluginConstants;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinLuckEvent implements Listener {
    private final MoreLuckyBlocks plugin;

    public JoinLuckEvent(MoreLuckyBlocks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        // Replace the player name if the player has changed their name sicne last
        // login.s
        if (this.plugin.getPlayersYaml().getConfig().getConfigurationSection("players") != null) {
            for (final String playerUUIDs : this.plugin.getPlayersYaml().getConfig().getConfigurationSection("players")
                    .getKeys(false)) {
                if (playerUUIDs.equalsIgnoreCase(e.getPlayer().getUniqueId().toString())
                        && !((String) this.plugin.getPlayersYaml().getConfig().get("players." + playerUUIDs + ".name"))
                        .equalsIgnoreCase(e.getPlayer().getUniqueId().toString())) {
                    this.plugin.getPlayersYaml().getConfig().set("players." + playerUUIDs + ".name",
                            e.getPlayer().getName());
                }
            }

        } else {
            this.plugin.getPlayersYaml().getConfig().set("players." + e.getPlayer().getUniqueId() + ".name",
                    e.getPlayer().getName());
            this.plugin.getPlayersYaml().getConfig().set("players." + e.getPlayer().getUniqueId() + ".luck",
                    String.valueOf(PluginConstants.DEFAULT_PLAYER_LUCK));
        }
        this.plugin.getPlayerManager().save();
    }
}
