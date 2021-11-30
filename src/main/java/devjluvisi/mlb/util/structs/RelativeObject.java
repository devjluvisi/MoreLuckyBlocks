package devjluvisi.mlb.util.structs;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

import java.util.Arrays;

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
    private int x;
    private int y;
    private int z;

    private Material material;
    private EntityType entity;

    public RelativeObject() {
        super();
        strBuild = new StringBuilder();
    }

    /**
     * Create a relative object as a material, not an entity.
     *
     * @param x Relative X
     * @param y Relative Y
     * @param z Relative Z
     */
    public RelativeObject(String name, int x, int y, int z) {
        super();
        this.name = name;
        if(Material.getMaterial(name) != null) {
            this.material = Material.getMaterial(name);
            this.entity = null;
        }else{
            this.entity = EntityType.valueOf(name);
            this.material = null;
        }
        this.x = x;
        this.y = y;
        this.z = z;
        strBuild = new StringBuilder();
    }

    public final Material getMaterial() {
        return material;
    }

    public final void setMaterial(Material m) {
        this.material = m;
    }

    public final EntityType getEntity() { return entity; }

    public final void setEntity(EntityType e) { this.entity = e; }

    public final int getX() {
        return x;
    }

    public final void setX(int x) {
        this.x = x;
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
        if(entity != null) {
            if(w.getName().equals(DropStructure.getDefaultName())) {
                return;
            }
            w.spawnEntity(new Location(w, x, y, z), entity);
            return;
        }
        w.getBlockAt(x, y, z).setType(material);
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
        if (Arrays.stream(EntityType.values()).noneMatch(e-> e.name().equals(split[0]))) {
            this.material = Material.getMaterial(split[0]);
            this.entity = null;
        }else{
            this.entity = EntityType.valueOf(split[0]);
            this.material = null;
        }
        this.name = split[0];

        this.x = Integer.parseInt(split[1]);
        this.y = Integer.parseInt(split[2]);
        this.z = Integer.parseInt(split[3]);
        return this;
    }

    @Override
    public String toString() {
        strBuild.append("RelativeObject [m=");
        strBuild.append(name);
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
