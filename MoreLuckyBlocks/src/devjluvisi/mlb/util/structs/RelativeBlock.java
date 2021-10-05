package devjluvisi.mlb.util.structs;

import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import io.netty.util.internal.StringUtil;

public class RelativeBlock {
	
	private Material m;
	private int x;
	private int y;
	private int z;

	private StringBuilder strBuild;
	
	public RelativeBlock() {
		super();
		strBuild = new StringBuilder();
	}
	
	public RelativeBlock(Material m, int x, int y, int z) {
		super();
		this.m = m;
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
	
	public void setOffset(Location blockLocation) {
		this.y -= 64;
		this.x += blockLocation.getX();
		this.y += blockLocation.getY();
		this.z += blockLocation.getZ();
	}
	
	/**
	 * Places a block at the specified location if the block dosen't exist.
	 */
	public void place(World w) {
		w.getBlockAt(x, y, z).setType(m);
	}
	
	/**
	 * Removes the block at a specific location if it exists.
	 */
	public void remove(World w) {
		w.getBlockAt(x, y, z).setType(Material.AIR);
	}
	
	public String serialize() {
		strBuild.append("[");
		strBuild.append(m.name()).append(",");
		strBuild.append(x).append(",");
		strBuild.append(y).append(",");
		strBuild.append(z);
		strBuild.append("]");
		return strBuild.toString();
	}
	
	public RelativeBlock deserialize(String arg) {
		arg = arg.replace("[", StringUtils.EMPTY);
		arg = arg.replace("]", StringUtils.EMPTY);
		String split[] = arg.split(",");
		this.m = Material.getMaterial(split[0]);
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
