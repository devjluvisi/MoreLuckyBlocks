package devjluvisi.mlb.menus;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.api.gui.Menu;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.menus.admin.*;
import devjluvisi.mlb.menus.exceptions.MenuInvalidException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

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
        menus = Set.of(new ListMenu(this), new DropsMenu(this), new LootMenu(this), new EditDropMenu(this), new ConfirmMenu(this), new ChangeRarityMenu(this));

    }

    public MenuManager withData(MenuResource src) {
        this.menuResource = src;
        //menus = Set.of(new ListMenu(this), new DropsMenu(this), new LootMenu(this), new EditDropMenu(this), new ConfirmMenu(this), new ChangeRarityMenu(this));

        return this;
    }

    public MenuBuilder get(MenuType type) {

        for(MenuBuilder menu : menus) {
            if(menu.type() == type) return menu;
        }
        return get(MenuType.EMPTY);
    }

    public void open(Player p, MenuType type) {
        this.p = p;
        for(MenuBuilder menu : menus) {
            if(menu.type().equals(type)) {
                menu.open(p);
                return;
            }
        }
    }

    public void open(Player p, MenuBuilder menu) {
        this.p = p;
        menu.open(p);
    }

    public void setMenuData(MenuResource src) {
        this.menuResource = src;
    }

    public MenuResource getMenuData() {
        return menuResource;
    }

    public Player getPlayer() {
        return this.p;
    }

    public MoreLuckyBlocks getPlugin() {
        return this.plugin;
    }


}