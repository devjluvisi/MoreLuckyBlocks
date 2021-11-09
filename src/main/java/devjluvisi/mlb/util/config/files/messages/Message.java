package devjluvisi.mlb.util.config.files.messages;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.helper.Util;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.EnumSet;
import java.util.Objects;

public enum Message {

    NO_PERMISSION("&cYou do not have permission to perform this command.", 1);

    // Singleton Access
    private static MoreLuckyBlocks plugin;

    private final String defaultValue;
    private final int id;

    Message(String defaultValue, int id) {
        this.defaultValue = defaultValue;
        this.id = id;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public static String get(int id) {
        return plugin.getMessagesManager().getPrefix() + Util.toColor((String) Objects.requireNonNull(plugin.getMessagesManager().getConfig().get(String.valueOf(id))));
    }

    public String get() {
        if (plugin.getMessagesManager().getConfig().get(String.valueOf(this.id)) == null) {
            plugin.getServer().getLogger().severe("Displayed unknown message of id: " + this.id);
            return plugin.getMessagesManager().getPrefix() + Util.toColor(defaultValue);
        }
        return plugin.getMessagesManager().getPrefix() + Util.toColor((String) Objects.requireNonNull(plugin.getMessagesManager().getConfig().get(String.valueOf(this.id))));
    }

    public static void loadDefaults(YamlConfiguration cfg) {
        EnumSet<Message> messages = EnumSet.allOf(Message.class);
        for(Message m : messages) {
            if(cfg.get(String.valueOf(m.id)) != null) {
                continue;
            }
            cfg.set(String.valueOf(m.id), m.defaultValue);
        }
    }

    public int getId() {
        return this.id;
    }

    public static void register(MoreLuckyBlocks pl) {
        if(plugin != null) {
            return;
        }
        plugin = pl;
    }

    @Override
    public String toString() {
        return "Message" + " [" + this.name() + "] -> {" + "defaultValue='" + defaultValue + '\'' +
                ", id=" + id +
                '}';
    }
}
