package devjluvisi.mlb.api.items.itemdata.util;

import org.bukkit.ChatColor;

import java.nio.charset.StandardCharsets;

public class HiddenStringUtil {

    // String constants. TODO Change them to something unique to avoid conflict with
    // other plugins!
    private static final String SEQUENCE_HEADER = "" + ChatColor.RESET + ChatColor.UNDERLINE + ChatColor.RESET;
    private static final String SEQUENCE_FOOTER = "" + ChatColor.RESET + ChatColor.ITALIC + ChatColor.RESET;

    public static String encodeString(String hiddenString) {
        return quote(stringToColors(hiddenString));
    }

    /**
     * Internal stuff.
     */
    private static String quote(String input) {
        if (input == null) {
            return null;
        }
        return SEQUENCE_HEADER + input + SEQUENCE_FOOTER;
    }

    private static String stringToColors(String normal) {
        if (normal == null) {
            return null;
        }

        final byte[] bytes = normal.getBytes(StandardCharsets.UTF_8);
        final char[] chars = new char[bytes.length * 4];

        for (int i = 0; i < bytes.length; i++) {
            final char[] hex = byteToHex(bytes[i]);
            final int index = i * 4;
            chars[index] = ChatColor.COLOR_CHAR;
            chars[index + 1] = hex[0];
            chars[index + 2] = ChatColor.COLOR_CHAR;
            chars[index + 3] = hex[1];
        }

        return new String(chars);
    }

    private static char[] byteToHex(byte b) {
        final int unsignedByte = b - Byte.MIN_VALUE;
        return new char[] { unsignedIntToHex((unsignedByte >> 4) & 0xf), unsignedIntToHex(unsignedByte & 0xf) };
    }

    private static char unsignedIntToHex(int i) {
        if ((i >= 0) && (i <= 9)) {
            return (char) (i + 48);
        } else if ((i >= 10) && (i <= 15)) {
            return (char) (i + 87);
        }
        throw new IllegalArgumentException("Invalid hex int: out of range");
    }

    public static boolean hasHiddenString(String input) {
        if (input == null) {
            return false;
        }
        return input.contains(SEQUENCE_HEADER) && input.contains(SEQUENCE_FOOTER);
    }

    public static String extractHiddenString(String input) {
        return colorsToString(extract(input));
    }

    private static String colorsToString(String colors) {
        if (colors == null) {
            return null;
        }

        colors = colors.toLowerCase().replace(String.valueOf(ChatColor.COLOR_CHAR), "");

        if ((colors.length() % 2) != 0) {
            colors = colors.substring(0, (colors.length() / 2) * 2);
        }

        final char[] chars = colors.toCharArray();
        final byte[] bytes = new byte[chars.length / 2];

        for (int i = 0; i < chars.length; i += 2) {
            bytes[i / 2] = hexToByte(chars[i], chars[i + 1]);
        }

        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static String extract(String input) {
        if (input == null) {
            return null;
        }

        final int start = input.indexOf(SEQUENCE_HEADER);
        final int end = input.indexOf(SEQUENCE_FOOTER);

        if ((start < 0) || (end < 0)) {
            return null;
        }

        return input.substring(start + SEQUENCE_HEADER.length(), end);
    }

    private static byte hexToByte(char hex1, char hex0) {
        return (byte) (((hexToUnsignedInt(hex1) << 4) | hexToUnsignedInt(hex0)) + Byte.MIN_VALUE);
    }

    private static int hexToUnsignedInt(char c) {
        if ((c >= '0') && (c <= '9')) {
            return c - 48;
        } else if ((c >= 'a') && (c <= 'f')) {
            return c - 87;
        }
        throw new IllegalArgumentException("Invalid hex char: out of range");
    }

    public static String replaceHiddenString(String input, String hiddenString) {
        if (input == null) {
            return null;
        }

        final int start = input.indexOf(SEQUENCE_HEADER);
        final int end = input.indexOf(SEQUENCE_FOOTER);

        if ((start < 0) || (end < 0)) {
            return null;
        }

        return input.substring(0, start + SEQUENCE_HEADER.length()) + stringToColors(hiddenString)
                + input.substring(end);
    }

    public static String removeHiddenString(String input) {
        return input.replaceAll(SEQUENCE_HEADER + ".*" + SEQUENCE_FOOTER, "");
    }
}
