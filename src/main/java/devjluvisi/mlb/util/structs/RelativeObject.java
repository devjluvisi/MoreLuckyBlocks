package devjluvisi.mlb.util.structs;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

/**
 * Represents an object that can be created when a user
 * breaks a lucky block and achieves a special drop.
 * <p>
 * RelativeObject objects are managed in drop structures and are serialized to the
 * structures.yml configuration file.
 * <p>
 * RelativeObjects are "relative" as they represent a location usually close to (0, 0) as built in
 * the structure world "mlb-world". When a drop is achieved, the relative objects will create themselves
 * according to the position of the lucky block.
 *
 * @see DropStructure
 */
public class RelativeObject {

    private final StringBuilder strBuild;
    private String name;
    private Material m;
    private EntityType t;
    private int x;
    private int y;
    private int z;

    public RelativeObject() {
        super();
        strBuild = new StringBuilder();
    }

    /**
     * Create a relative object as a material, not an entity.
     *
     * @param m The type of material.
     * @param x Relative X
     * @param y Relative Y
     * @param z Relative Z
     */
    public RelativeObject(Material m, int x, int y, int z) {
        super();
        this.name = m.name();
        this.m = m;
        this.x = x;
        this.y = y;
        this.z = z;
        strBuild = new StringBuilder();
    }

    /**
     * Create a relative object as an entity, not a material.
     *
     * @param type The type of the entity.
     * @param x    Relative X
     * @param y    Relative Y
     * @param z    Relative Z
     */
    public RelativeObject(EntityType type, int x, int y, int z) {
        super();
        this.name = type.name();
        this.t = type;
        this.x = x;
        this.y = y;
        this.z = z;
        strBuild = new StringBuilder();
    }

    public final Material getMaterial() {
        return m;
    }

    public final void setMaterial(Material m) {
        this.m = m;
    }

    public final int getX() {
        return x;
    }

    public final void setX(int x) {
        this.x = x;
    }

    public final EntityType getEntity() {
        return t;
    }

    public final void setEntity(EntityType t) {
        this.t = t;
    }

    public final int getY() {
        return y;
    }

    public final void setY(int y) {
        this.y = y;
    }

    public final int getZ() {
        return z;
    }

    public final void setZ(int z) {
        this.z = z;
    }

    /**
     * Sets the offset of the object based on the position of
     * a corresponding blockLocation.
     *
     * @param blockLocation The location of the block to offset with.
     */
    public void setOffset(Location blockLocation) {
        this.y -= 64;
        this.x += blockLocation.getX();
        this.y += blockLocation.getY();
        this.z += blockLocation.getZ();
    }

    /**
     * Places an object at on a world.
     *
     * @param w The world to place the object in.
     */
    public void place(World w) {
        if (t == null) {
            w.getBlockAt(x, y, z).setType(m);
        } else {
            Entity wEntity = w.spawnEntity(w.getBlockAt(x, y + 2, z).getLocation(), t);
            //e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30000, 127));
            wEntity.setInvulnerable(true);
            wEntity.setSilent(true);
            wEntity.setRotation(0, 0);

        }
    }

    /**
     * Removes the object at a specified world.
     * Only applicable for Blocks.
     *
     * @param w The world to remove the object at.
     */
    public void remove(World w) {
        w.getBlockAt(x, y, z).setType(Material.AIR);
    }

    /**
     * Convert the object into a serializable string to be
     * saved in a resource file.
     *
     * @return Serialized {@link RelativeObject}
     */
    public String serialize() {
        strBuild.append("[");
        strBuild.append(name).append(",");
        strBuild.append(x).append(",");
        strBuild.append(y).append(",");
        strBuild.append(z);
        strBuild.append("]");
        return strBuild.toString();
    }

    /**
     * Convert a serialized string into a {@link RelativeObject}.
     *
     * @param arg The string to convert from (parse).
     * @return The {@link RelativeObject} created from the serialized string.
     */
    public RelativeObject deserialize(String arg) {
        arg = arg.replace("[", StringUtils.EMPTY);
        arg = arg.replace("]", StringUtils.EMPTY);
        String[] split = arg.split(",");
        if (Material.getMaterial(split[0]) == null) {
            this.t = EntityType.valueOf(split[0]);
        } else {
            this.m = Material.getMaterial(split[0]);
        }
        this.name = split[0];

        this.x = Integer.parseInt(split[1]);
        this.y = Integer.parseInt(split[2]);
        this.z = Integer.parseInt(split[3]);
        return this;
    }

    @Override
    public String toString() {
        strBuild.append("RelativeBlock [m=");
        strBuild.append(m);
        strBuild.append(", x=");
        strBuild.append(x);
        strBuild.append(", y=");
        strBuild.append(y);
        strBuild.append(", z=");
        strBuild.append(z);
        strBuild.append("]");
        return strBuild.toString();
    }

}
