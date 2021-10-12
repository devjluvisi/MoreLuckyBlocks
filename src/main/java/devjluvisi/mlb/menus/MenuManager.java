package devjluvisi.mlb.menus;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.menus.admin.*;
import devjluvisi.mlb.menus.user.*;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

// One menu for different parts of every GUI
public class MenuManager {

    private final MoreLuckyBlocks plugin;
    private Set<MenuBuilder> menus;
    private MenuResource menuResource;
    private Player p;


    public MenuManager(MoreLuckyBlocks plugin) {
        this.plugin = plugin;
        this.menuResource = new MenuResource();
        this.menus = new HashSet<>();
        menus = Set.of(new ListMenu(this), new DropsMenu(this), new LootMenu(this), new EditDropMenu(this), new ConfirmMenu(this), new ChangeRarityMenu(this), new ExchangesMenu(this),
                new UserListMenu(this), new UserListDrops(this), new UserListLoot(this), new UserRedeemList(this), new UserRedeemMenu(this));

    }

    public MenuManager withData(MenuResource src) {
        this.menuResource = src;
        //menus = Set.of(new ListMenu(this), new DropsMenu(this), new LootMenu(this), new EditDropMenu(this), new ConfirmMenu(this), new ChangeRarityMenu(this));

        return this;
    }

    public MenuBuilder get(MenuType type) {

        for (MenuBuilder menu : menus) {
            if (menu.type() == type) return menu;
        }
        return get(MenuType.EMPTY);
    }

    public void open(Player p, MenuType type) {
        this.p = p;
        for (MenuBuilder menu : menus) {
            if (menu.type().equals(type)) {
                menu.open(p);
                return;
            }
        }
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