package devjluvisi.mlb.blocks.drops;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class LuckyBlockCommand implements LootProperty {

	private String command;

	public LuckyBlockCommand(String command) {
		super();
		this.command = command;
	}

	public String getCommand() {
		return this.command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	@Override
	public String toString() {
		return "LuckyBlockCommand [command=" + this.command + "]";
	}

	@Override
	public ItemStack asItem() {
		final ItemStack i = new ItemStack(Material.OAK_SIGN);
		final ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Execute Command");
		meta.setLore(Arrays.asList(ChatColor.GRAY + "The following command will", ChatColor.GRAY + "be executed:",
				ChatColor.GOLD + this.command));
		i.setItemMeta(meta);
		return i;
	}

	@Override
	public boolean isValid() {
		if (!this.command.contains("/")) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return this.command.toLowerCase().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LuckyBlockCommand)) {
			return false;
		}
		return ((LuckyBlockCommand) obj).hashCode() == this.hashCode();
	}

}
