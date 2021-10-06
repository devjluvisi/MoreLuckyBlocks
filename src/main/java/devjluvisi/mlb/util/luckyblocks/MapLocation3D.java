package devjluvisi.mlb.util.luckyblocks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Objects;
import java.util.UUID;

/**
 * Simple 3D Map Location on a Minecraft World for Lucky Blocks. Uses only X,Y,Z
 * coords and the world to store information.
 *
 * @author jacob
 */
public final class MapLocation3D {

    private double x;
    private double y;
    private double z;
    private UUID world;

    public MapLocation3D() {
        super();
        this.x = -0.0D;
        this.y = -0.0D;
        this.z = -0.0D;
        this.world = null;
    }

    public MapLocation3D(Location worldLocation) {
        super();
        this.x = worldLocation.getX();
        this.y = worldLocation.getY();
        this.z = worldLocation.getZ();
        this.world = Objects.requireNonNull(worldLocation.getWorld()).getUID();
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public UUID getWorld() {
        return this.world;
    }

    public void setWorld(UUID world) {
        this.world = world;
    }

    public World getBukkitWorld() {
        return Bukkit.getServer().getWorld(this.world);
    }

    public Block getBlock() {
        return new Location(this.getBukkitWorld(), this.x, this.y, this.z).getBlock();
    }

    public boolean equals(Location location) {
        return (location.getX() == this.x) && (location.getY() == this.y) && (location.getZ() == this.z)
                && (Objects.requireNonNull(location.getWorld()).getUID() == this.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y, this.z, this.world);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final MapLocation3D general)) {
            return false;
        }
        return (this.x == general.x) && (this.y == general.y) && (this.z == general.z)
                && (this.world.hashCode() == general.world.hashCode());
    }

    @Override
    public String toString() {
        return "[" + this.x + ", " + this.y + ", " + this.z + ", " + this.getBukkitWorld().getName() + "]";
    }
}
