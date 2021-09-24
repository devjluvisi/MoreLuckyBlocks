package devjluvisi.mlb.helper;

import net.md_5.bungee.api.ChatColor;

public class MenuUtils {
	
	public static boolean isSameDisplayName(String n1, String n2) {
		return (ChatColor.stripColor(n1).equals(ChatColor.stripColor(n2)));
	}

}
