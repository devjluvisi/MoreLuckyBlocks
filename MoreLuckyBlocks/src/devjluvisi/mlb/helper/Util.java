package devjluvisi.mlb.helper;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.math.NumberUtils;

import devjluvisi.mlb.util.Range;

public class Util {

	public static boolean isSameDisplayName(String n1, String n2) {
		return (ChatColor.stripColor(n1).equals(ChatColor.stripColor(n2)));
	}

	public static String asNormalColoredString(String str) {
		if ((str == null) || str.isBlank() || str.isEmpty()) {
			return str;
		}
		for (final ChatColor v : ChatColor.values()) {
			str = str.replaceAll(v.toString(), "&" + v.getChar());
		}
		return str;
	}

	public static List<String> asNormalColoredString(List<String> lore) {

		final List<String> loreCopy = new LinkedList<>();
		for (String s : lore) {
			if ((s == null) || s.isEmpty()) {
				continue;
			}
			for (final ChatColor v : ChatColor.values()) {
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
		return StringUtils.lowerCase(unformatted);
	}

	/**
	 * Returns if the string passed is a valid number. Should always be used over
	 * try/catch, .parse, etc.
	 *
	 * @param str String to test.
	 * @return If the string is a number.
	 */
	public static boolean isNumber(String str) {
		return NumberUtils.isCreatable(str);
	}

	/**
	 * Returns if the string passed is a valid number and is within a range.
	 *
	 * @param str   The string to test.
	 * @param range The range of numbers the number can be in.
	 * @return If the string is a number and within range.
	 */
	public static boolean isNumber(String str, Range range) {
		return NumberUtils.isCreatable(str) && range.isInRange(NumberUtils.toDouble(str));
	}

	public static double toNumber(String str) {
		if (!isNumber(str)) {
			throw new NumberFormatException("Type not of number.");
		}
		return NumberUtils.toDouble(str);
	}

}
