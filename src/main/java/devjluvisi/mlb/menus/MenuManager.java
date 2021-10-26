package devjluvisi.mlb.menus;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.api.gui.MenuView;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumSet;
import java.util.Objects;

// One menu for different parts of every GUI
public class MenuManager {

    private static final EnumSet<MenuType> menus = EnumSet.allOf(MenuType.class);
    private final MoreLuckyBlocks plugin;
    private final Deque<MenuType> traverseHistory;
    private MenuResource menuResource;
    private Player p;


    public MenuManager(MoreLuckyBlocks plugin) {
        this.plugin = plugin;
        this.menuResource = new MenuResource();
        this.traverseHistory = new ArrayDeque<>();
    }

    public MenuManager withData(MenuResource src) {
        this.menuResource = src;
        return this;
    }

    public void silentOpen(Player p, MenuType type) {
        this.p = p;
        Validate.isTrue(type != MenuType.EMPTY && Objects.nonNull(type), "Cannot open invalid MenuType.");
        traverseHistory.removeFirst();
        traverseHistory.removeFirst();
        get(type).open(p);
    }

    public MenuBuilder get(MenuType type) {
        if (type == MenuType.EMPTY) {
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

    public boolean isIndirectMenu() {
        return this.traverseHistory.size() > 1;
    }

    public void regress(MenuView view) {
        if (!regress()) {
            view.close();
        }
    }

    public boolean regress() {
        Validate.notNull(p, "Player was null when regress() function was called.");
        // Remove the last element.
        traverseHistory.removeFirst();
        if (traverseHistory.isEmpty()) {
            return false;
        }
        MenuType type = this.traverseHistory.pop();
        open(p, type);
        return true;
    }

    public void open(Player p, MenuType type) {
        this.p = p;
        Validate.isTrue(type != MenuType.EMPTY && Objects.nonNull(type), "Cannot open invalid MenuType.");
        open(p, get(type));
    }

    public void open(Player p, MenuBuilder menu) {
        this.p = p;
        Validate.isTrue(menu.type() != MenuType.EMPTY && Objects.nonNull(menu.type()), "Cannot open invalid MenuType.");
        addTraceback(menu.type());
        menu.open(p);

        //Bukkit.broadcastMessage(this.traverseHistory.toString());
    }

    private void addTraceback(MenuType type) {
        if (traverseHistory.contains(type)) {
            return;
        }
        traverseHistory.push(type);
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