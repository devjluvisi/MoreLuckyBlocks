package devjluvisi.mlb.util.players;

import devjluvisi.mlb.MoreLuckyBlocks;

import java.util.Objects;
import java.util.UUID;

/**
 * <h2>PlayerManager</h2>
 * Manages all of the players on the server by using {@link PlayerData} to get
 * data for each player such as luck.
 */
public record PlayerManager(MoreLuckyBlocks plugin) {

    /**
     * Update a players luck.
     *
     * @param uuid    UUID of player to update.
     * @param newLuck New luck value to set.
     */
    public void update(UUID uuid, float newLuck) {
        this.plugin.getPlayersYaml().getConfig().set("players." + uuid.toString() + ".name",
                Objects.requireNonNull(this.plugin.getServer().getPlayer(uuid)).getName());
        this.updateOffline(uuid, newLuck);
        this.save();
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

        if (this.plugin.getPlayersYaml().getConfig().getConfigurationSection("players") == null) {
            return new PlayerData();
        }
        for (final String playerUUIDs : Objects.requireNonNull(this.plugin.getPlayersYaml().getConfig().getConfigurationSection("players"))
                .getKeys(false)) {
            if ((this.plugin.getPlayersYaml().getConfig().get("players." + playerUUIDs + ".name") != null)
                    && ((String) Objects.requireNonNull(this.plugin.getPlayersYaml().getConfig().get("players." + playerUUIDs + ".name")))
                    .equalsIgnoreCase(name)) {
                return new PlayerData(UUID.fromString(playerUUIDs)).withLuck(Float.parseFloat(String
                        .valueOf(this.plugin.getPlayersYaml().getConfig().get("players." + playerUUIDs + ".luck"))));
            }
        }
        return new PlayerData();
    }

}
