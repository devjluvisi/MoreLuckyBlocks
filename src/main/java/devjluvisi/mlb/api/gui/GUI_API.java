package devjluvisi.mlb.api.gui;

import devjluvisi.mlb.MoreLuckyBlocks;
import org.bukkit.plugin.java.JavaPlugin;

public final class GUI_API {

    static private final MoreLuckyBlocks INSTANCE = JavaPlugin.getPlugin(MoreLuckyBlocks.class);

    public static MoreLuckyBlocks getInstance() {
        return INSTANCE;
    }

}
