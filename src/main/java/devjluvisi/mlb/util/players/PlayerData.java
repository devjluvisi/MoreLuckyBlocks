package devjluvisi.mlb.util.players;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.PluginConstants;
import devjluvisi.mlb.util.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

/**
 * Class which manages players including individual player luck, how many lucky
 * blocks broken, how many lucky blocks placed, and the average drop rarity that
 * the user gets from a drop on a lucky block (how lucky they are)
 *
 * @author jacob
 */
public class PlayerData {

    private UUID UUID;
    private float luck;
    private int luckyBlocksBroken;
    private int luckyBlocksPlaced;
    private float averageDropRarity;

    public PlayerData() {
        this.UUID = null;
        this.luck = PluginConstants.DEFAULT_PLAYER_LUCK;
    }

    public PlayerData(OfflinePlayer p) {
        if (p == null) {
            this.UUID = null;
            return;
        }
        this.UUID = p.getUniqueId();
        this.luck = PluginConstants.DEFAULT_PLAYER_LUCK;
    }

    public PlayerData(Player p) {
        this.UUID = p.getUniqueId();
        this.luck = PluginConstants.DEFAULT_PLAYER_LUCK;
    }

    public PlayerData(UUID uuid) {
        this.UUID = uuid;
        this.luck = PluginConstants.DEFAULT_BLOCK_LUCK;
    }

    public PlayerData withLuck(float luck) {
        this.luck = luck;
        return this;
    }

    public PlayerData fill(MoreLuckyBlocks plugin) {
        PlayerData p = plugin.getPlayerManager().getPlayer(Objects.requireNonNull(plugin.getServer().getPlayer(UUID)).getName());
        this.luck = p.luck;
        this.averageDropRarity = p.averageDropRarity;
        this.luckyBlocksBroken = p.luckyBlocksBroken;
        this.luckyBlocksPlaced = p.luckyBlocksPlaced;
        return this;
    }

    public final float getLuck() {
        return this.luck;
    }

    public final void setLuck(float luck) {
        this.luck = luck;
    }

    public final UUID getUUID() {
        return this.UUID;
    }

    public final void setUUID(UUID uUID) {
        this.UUID = uUID;
    }

    public int getLuckyBlocksBroken() {
        return luckyBlocksBroken;
    }

    public void setLuckyBlocksBroken(int luckyBlocksBroken) {
        this.luckyBlocksBroken = luckyBlocksBroken;
    }

    public int getLuckyBlocksPlaced() {
        return luckyBlocksPlaced;
    }

    public void setLuckyBlocksPlaced(int luckyBlocksPlaced) {
        this.luckyBlocksPlaced = luckyBlocksPlaced;
    }

    public float getAverageDropRarity() {
        return averageDropRarity;
    }

    public void setAverageDropRarity(float averageDropRarity) {
        this.averageDropRarity = averageDropRarity;
    }

    public OfflinePlayer getOfflinePlayer() {
        if (this.isOnline()) {
            return null;
        }
        return Bukkit.getOfflinePlayer(this.UUID);
    }

    public boolean isOnline() {
        if (this.isNull()) {
            return true;
        }
        return Objects.isNull(Bukkit.getPlayer(this.UUID));
    }

    public boolean isNull() {
        return Objects.isNull(this.UUID);
    }

    /**
     * Save the player to the "players.yml" resource file.
     *
     * @param playersYaml The config file to save to.
     */
    public void save(ConfigManager playersYaml, boolean advancedData) {
        if (this.isNull()) {
            Bukkit.getLogger().severe("Could not set luck of this player as their UUID is not present.");
            return;
        }

        playersYaml.getConfig().set("players." + this.UUID + ".luck", String.valueOf(this.luck));
        if (this.isOnline()) {
            playersYaml.getConfig().set("players." + this.UUID + ".name", this.getPlayer().getName());
        }else{
            playersYaml.getConfig().set("players." + this.UUID + ".name", Bukkit.getOfflinePlayer(this.UUID).getName());
        }
        if(advancedData) {
            playersYaml.getConfig().set("players." + this.UUID + ".blocks-broken", String.valueOf(this.luckyBlocksBroken));
            playersYaml.getConfig().set("players." + this.UUID + ".blocks-placed", String.valueOf(this.luckyBlocksPlaced));
            playersYaml.getConfig().set("players." + this.UUID + ".average-drop-rarity", String.valueOf(this.averageDropRarity));
        }


        playersYaml.save();
        playersYaml.reload();
    }

    /**
     * @return Bukkit representation of the player.
     */
    public Player getPlayer() {
        if (!this.isOnline()) {
            return null;
        }
        return Bukkit.getPlayer(this.UUID);
    }

    @Override
    public int hashCode() {
        return this.UUID.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlayerData)) {
            return false;
        }
        return obj.hashCode() == this.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PlayerData{");
        sb.append("UUID=").append(UUID);
        sb.append(", luck=").append(luck);
        sb.append(", luckyBlocksBroken=").append(luckyBlocksBroken);
        sb.append(", luckyBlocksPlaced=").append(luckyBlocksPlaced);
        sb.append(", averageDropRarity=").append(averageDropRarity);
        sb.append('}');
        return sb.toString();
    }
}
