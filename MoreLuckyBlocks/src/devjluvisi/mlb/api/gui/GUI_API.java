package devjluvisi.mlb.api.gui;

import org.bukkit.plugin.java.JavaPlugin;

import devjluvisi.mlb.MoreLuckyBlocks;

public final class GUI_API {
	static private MoreLuckyBlocks INSTANCE = JavaPlugin.getPlugin(MoreLuckyBlocks.class);

	public static MoreLuckyBlocks getInstance() {
		return INSTANCE;
	}
}
