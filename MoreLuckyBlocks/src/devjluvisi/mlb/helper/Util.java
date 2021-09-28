package devjluvisi.mlb.helper;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;


public class Util {

	public static boolean isSameDisplayName(String n1, String n2) {
		return (ChatColor.stripColor(n1).equals(ChatColor.stripColor(n2)));
	}

	public static String asNormalColoredString(String str) {
		if(str == null || str.isBlank() || str.isEmpty()) return str;
		for (ChatColor v : ChatColor.values()) {
			str = str.replaceAll(v.toString(), "&" + v.getChar());
		}
		return str;
	}

	public static List<String> asNormalColoredString(List<String> lore) {

		List<String> loreCopy = new LinkedList<>();
		for (String s : lore) {
			if(s == null || s.isEmpty()) continue;
			for (ChatColor v : ChatColor.values()) {
				s = s.replaceAll(v.toString(), "&" + v.getChar());
			}
			loreCopy.add(s);
		}
		return loreCopy;
	}
	
	public static String makeInternal(String unformatted) {
		unformatted = ChatColor.translateAlternateColorCodes('&', unformatted);
		unformatted = ChatColor.stripColor(unformatted);
		unformatted = StringUtils.trim(unformatted);
		unformatted = StringUtils.replace(unformatted, " ", "_");
		unformatted = StringUtils.lowerCase(unformatted);
		return unformatted;
	}

}
