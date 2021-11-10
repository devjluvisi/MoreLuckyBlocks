package devjluvisi.mlb.util.players;

import devjluvisi.mlb.MoreLuckyBlocks;
import org.bukkit.Bukkit;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;

/**
 * <h2>PlayerManager</h2>
 * Manages all of the players on the server by using {@link PlayerData} to get
 * data for each player such as luck.
 */
public class PlayerManager {


    private MoreLuckyBlocks plugin;
    private final HashMap<UUID, ArrayList<Float>> playerLuckMap;


    public PlayerManager(MoreLuckyBlocks plugin) {
        this.plugin = plugin;
        this.playerLuckMap = new HashMap<>();
    }


    public Map<UUID, ArrayList<Float>> getPlayerLuckMap() {
        return playerLuckMap;
    }

    /**
     * Update a players luck.
     *
     * @param uuid    UUID of player to update.
     * @param newLuck New luck value to set.
     */
    public void update(UUID uuid, float newLuck) {
        //player.save(plugin.getPlayersYaml(), plugin.getSettingsManager().isExtraPlayerData());
        this.updateOffline(uuid, newLuck);
        this.save();
    }

    public void incrementBlockBreak(UUID u) {
        if(!plugin.getSettingsManager().isExtraPlayerData()) {
            return;
        }
        PlayerData player = new PlayerData(u).fill(plugin);
        player.setLuckyBlocksBroken(player.getLuckyBlocksBroken()+1);
        player.save(plugin.getPlayersYaml(), true);
        Bukkit.getPlayer(u).sendMessage("finished: " + player.toString());
    }

    public void incrementBlockPlaced(UUID u) {
        if(!plugin.getSettingsManager().isExtraPlayerData()) {
            return;
        }
        PlayerData player = new PlayerData(u).fill(plugin);
        player.setLuckyBlocksPlaced(player.getLuckyBlocksPlaced()+1);
        player.save(plugin.getPlayersYaml(), true);
    }

    /**
     * Update a player offline.
     *
     * @param uuid UUID of the player.
     * @param luck Luck of the player.
     */
    public void updateOffline(UUID uuid, float luck) {
        this.plugin.getPlayersYaml().getConfig().set("players." + uuid.toString() + ".luck", String.valueOf(luck));
        this.save();
    }

    /**
     * Save config and reload it.
     */
    public void save() {
        if(plugin.getSettingsManager().isExtraPlayerData()) {
            for(UUID u: playerLuckMap.keySet()) {
                if(!playerLuckMap.containsKey(u)) {
                    continue;
                }
                PlayerData player = new PlayerData(u).fill(plugin);
                float average = 0.0F;
                for(float i: playerLuckMap.get(u)) {
                    average += i;
                }
                average /= playerLuckMap.get(u).size();
                if(player.getAverageDropRarity() == 0) {
                    player.setAverageDropRarity(average);
                }else{
                    player.setAverageDropRarity((player.getAverageDropRarity() + average) / 2);
                }

                player.save(plugin.getPlayersYaml(), true);
            }
        }

        this.plugin.getPlayersYaml().save();
        this.plugin.getPlayersYaml().reload();
    }

    /**
     * Get a player from the resource file.
     *
     * @param name Name of the player.
     * @return PlayerData reference of the player.
     */
    public PlayerData getPlayer(String name) {
        PlayerData player = new PlayerData();

        if (this.plugin.getPlayersYaml().getConfig().getConfigurationSection("players") == null) {
            return player;
        }

        for (final String playerUUIDs : Objects.requireNonNull(this.plugin.getPlayersYaml().getConfig().getConfigurationSection("players"))
                .getKeys(false)) {
            if ((this.plugin.getPlayersYaml().getConfig().get("players." + playerUUIDs + ".name") != null)
                    && ((String) Objects.requireNonNull(this.plugin.getPlayersYaml().getConfig().get("players." + playerUUIDs + ".name")))
                    .equalsIgnoreCase(name)) {
                // Set attributes
                player.setUUID(UUID.fromString(playerUUIDs));
                player.setLuck(Float.parseFloat(String
                        .valueOf(this.plugin.getPlayersYaml().getConfig().get("players." + playerUUIDs + ".luck"))));
                if(plugin.getSettingsManager().isExtraPlayerData()) {
                    player.setAverageDropRarity(Float.parseFloat(String
                            .valueOf(this.plugin.getPlayersYaml().getConfig().get("players." + playerUUIDs + ".average-drop-rarity"))));
                    player.setLuckyBlocksBroken(Integer.parseInt(String
                            .valueOf(this.plugin.getPlayersYaml().getConfig().get("players." + playerUUIDs + ".blocks-broken"))));
                    player.setLuckyBlocksPlaced(Integer.parseInt(String
                            .valueOf(this.plugin.getPlayersYaml().getConfig().get("players." + playerUUIDs + ".blocks-placed"))));
                }

            }
        }
        return player;
    }

}
