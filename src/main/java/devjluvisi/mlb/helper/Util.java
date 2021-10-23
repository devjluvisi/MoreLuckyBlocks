package devjluvisi.mlb.helper;

import devjluvisi.mlb.PluginConstants;
import devjluvisi.mlb.util.Range;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Class which contains static utility methods.
 */
public final class Util {

    private Util() {}

    public static boolean isSameDisplayName(String n1, String n2) {
        return (ChatColor.stripColor(n1).equals(ChatColor.stripColor(n2)));
    }

    /**
     * Convert a string colored with {@link ChatColor} back to a "normal" string with & codes.
     *
     * @param str Colored string.
     * @return String colored with "&" symbol.
     */
    public static String asNormalColoredString(String str) {
        if ((str == null) || str.isBlank() || str.isEmpty()) {
            return str;
        }
        for (final ChatColor v : ChatColor.values()) {
            str = str.replaceAll(v.toString(), "&" + v.getChar());
        }
        return str;
    }

    /**
     * Convert a list colored with {@link ChatColor} back to a "normal" string with & codes.
     *
     * @param lore Colored lore list.
     * @return A lore with the normal string colors.
     */
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

    /**
     * Color a list with {@link ChatColor}
     *
     * @param lore The lore to color.
     * @return Colored list.
     */
    public static List<String> listToColor(List<String> lore) {

        final List<String> loreCopy = new LinkedList<>();
        for (final String s : lore) {
            if ((s == null) || s.isEmpty()) {
                continue;
            }
            loreCopy.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return loreCopy;
    }

    /**
     * Converts any string to a string which follows the rules
     * for internal names in config. (for lucky blocks)
     *
     * @param unformatted Unformatted string.
     * @return A formatted string.
     */
    public static String makeInternal(String unformatted) {
        unformatted = ChatColor.translateAlternateColorCodes('&', unformatted);
        unformatted = ChatColor.stripColor(unformatted);
        unformatted = StringUtils.trim(unformatted);
        unformatted = StringUtils.lowerCase(unformatted);

        final String allowedChars = "abcdefghijklmnopqrstuvwxyz0123456789_";

        unformatted = StringUtils.replace(unformatted, " ", "_");

        for (char c : unformatted.toCharArray()) {
            if (!allowedChars.contains(String.valueOf(c))) {
                unformatted = StringUtils.replace(unformatted, String.valueOf(c), StringUtils.EMPTY);
            }
        }
        // Remove underscores at the end of the lucky block name.
        try {
            for(int i = unformatted.length(); i > 0; i--) {
                if(unformatted.charAt(i-1) == '_') {
                    unformatted = unformatted.substring(0, i-1);
                }else{
                    break;
                }
            }
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }

        return unformatted;
    }

    /**
     * Returns if the string passed is a valid number and is within a range.
     *
     * @param str   The string to test.
     * @param range The range of numbers the number can be in.
     * @return If the string is a number and within range.
     */
    public static boolean isNumber(String str, Range range) {
        return NumberUtils.isNumber(str) && range.isInRange(NumberUtils.toDouble(str));
    }

    public static double toNumber(String str) {
        if (!isNumber(str)) {
            throw new NumberFormatException("Type not of number.");
        }
        return NumberUtils.toDouble(str);
    }

    /**
     * Returns if the string passed is a valid number. Should always be used over
     * try/catch, .parse, etc.
     *
     * @param str String to test.
     * @return If the string is a number.
     */
    public static boolean isNumber(String str) {
        return NumberUtils.isNumber(str);
    }

    /**
     * Converts a string "desc" to a proper lore (list of strings).
     * The method splits up the string every 18 characters.
     *
     * Maintains Chat Colors throughout the lore.
     *
     * @see {@link devjluvisi.mlb.PluginConstants}
     * @param desc The string to convert to lore.
     * @return List of strings.
     */
    public static List<String> descriptionToLore(String desc) {
        String lastCode = "";
        ArrayList<String> lore = new ArrayList<>();
        StringBuilder str = new StringBuilder();
        int iterations = 0;
        int index = 0;
        while (iterations != desc.length()) {
            if ((index >= PluginConstants.SPLIT_LORE_LINE_THRESHOLD && desc.charAt(iterations) == ' ') || desc.charAt(iterations) == '\n') {
                if (!ChatColor.getLastColors(str.toString()).isEmpty()) {
                    lastCode = ChatColor.getLastColors(str.toString());
                }
                lore.add(lastCode + str);
                str.setLength(0);
                index = 0;
            } else {
                str.append(desc.charAt(iterations));
                index++;
            }
            iterations++;
        }
        // Add any remainder characters at the end.
        if (!str.isEmpty()) {
            lore.add(lastCode + str);
        }
        return lore;
    }

    /**
     * Returns an ItemStack as a string.
     * If the item has a display name, it returns the display name.
     * If not, it returns the type of the item.
     * @param item The item to get a name for.
     * @return The item as a name.
     */
    public static String getItemAsString(ItemStack item) {
        if(item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasDisplayName()) {
            return item.getItemMeta().getDisplayName();
        }
        return WordUtils.capitalize(StringUtils.lowerCase(StringUtils.replace(item.getType().name(), "_", " ")));
    }

    public static String toColor(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

}
