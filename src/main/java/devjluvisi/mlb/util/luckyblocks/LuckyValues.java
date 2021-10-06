package devjluvisi.mlb.util.luckyblocks;

import devjluvisi.mlb.PluginConstants;
import org.bukkit.Material;

import java.util.Objects;

/**
 * Values a placed lucky block has.
 * Values include: block material, hash code, and block luck at the time of placement.
 */
public class LuckyValues {
    // Ensure that the block material is the same
    private Material blockMaterial;
    private int luckyBlockHash;
    private float blockLuck;
    // Maybe: Add a UUID for who placed the block.

    public LuckyValues() {
        super();
        this.blockMaterial = Material.AIR;
        this.luckyBlockHash = -1;
        this.blockLuck = PluginConstants.DEFAULT_BLOCK_LUCK;
    }

    public LuckyValues(Material blockMaterial, int luckyBlockHash, float blockLuck) {
        super();
        this.blockMaterial = blockMaterial;
        this.luckyBlockHash = luckyBlockHash;
        this.blockLuck = blockLuck;
    }

    public LuckyValues(int luckyBlockHash, float blockLuck) {
        super();
        this.luckyBlockHash = luckyBlockHash;
        this.blockLuck = blockLuck;
        this.blockMaterial = Material.AIR;
    }

    public final Material getBlockMaterial() {
        return this.blockMaterial;
    }

    public final void setBlockMaterial(Material blockMaterial) {
        this.blockMaterial = blockMaterial;
    }

    public final int getLuckyBlockHash() {
        return this.luckyBlockHash;
    }

    public final void setLuckyBlockHash(int luckyBlockHash) {
        this.luckyBlockHash = luckyBlockHash;
    }

    public final float getBlockLuck() {
        return this.blockLuck;
    }

    public final void setBlockLuck(float blockLuck) {
        this.blockLuck = blockLuck;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.blockLuck, this.blockMaterial, this.luckyBlockHash);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (this.getClass() != obj.getClass())) {
            return false;
        }
        final LuckyValues other = (LuckyValues) obj;
        return (Float.floatToIntBits(this.blockLuck) == Float.floatToIntBits(other.blockLuck))
                && (this.blockMaterial == other.blockMaterial) && (this.luckyBlockHash == other.luckyBlockHash);
    }

    @Override
    public String toString() {
        return "[" + this.blockMaterial.name().toUpperCase() + ", " + this.luckyBlockHash + ", " + this.blockLuck + "]";
    }

}
