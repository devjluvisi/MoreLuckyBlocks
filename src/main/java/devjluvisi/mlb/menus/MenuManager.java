package devjluvisi.mlb.menus;

import devjluvisi.mlb.MoreLuckyBlocks;
import org.bukkit.entity.Player;

import java.util.EnumSet;
import java.util.Objects;

// One menu for different parts of every GUI
public class MenuManager {

    private final MoreLuckyBlocks plugin;
    private final EnumSet<MenuType> menus;
    private MenuResource menuResource;
    private Player p;

    public MenuManager(MoreLuckyBlocks plugin) {
        this.plugin = plugin;
        this.menuResource = new MenuResource();
        menus = EnumSet.allOf(MenuType.class);
    }

    public MenuManager withData(MenuResource src) {
        this.menuResource = src;
        return this;
    }

    public MenuBuilder get(MenuType type) {
        if(type == MenuType.EMPTY) {
            return null;
        }
        try {
            return Objects.requireNonNull(
                    menus.stream()
                            .filter(e -> e.equals(type))
                            .findFirst()
                            .orElse(null))
                    .getMenuClass()
                    .getConstructor(MenuManager.class).newInstance(this);
        } catch (Exception e) {
            plugin.getServer().getLogger().severe("[CRITICAL] Could not parse menu requested via reflection. [type=" + type.name() + "]");
        }
        return null;
    }

    public void open(Player p, MenuType type) {
        this.p = p;
        get(type).open(p);
    }

    public void open(Player p, MenuBuilder menu) {
        this.p = p;
        menu.open(p);
    }

    public MenuResource getMenuData() {
        return menuResource;
    }

    public void setMenuData(MenuResource src) {
        this.menuResource = src;
    }

    public Player getPlayer() {
        return this.p;
    }

    public MoreLuckyBlocks getPlugin() {
        return this.plugin;
    }


}