package devjluvisi.mlb.menus;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.api.gui.Menu;
import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.SimpleMenu;
import devjluvisi.mlb.api.gui.pages.Page;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.menus.util.MenuItem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public abstract class MenuBuilder extends SimpleMenu {

    protected MenuManager manager;

    private String name;
    private final PageType pageType;

    public MenuBuilder(MenuManager manager) {
        this.name = StringUtils.EMPTY;
        this.pageType = PageType.CHEST;
        this.manager = manager;
    }

    public MenuBuilder(MenuManager manager, String name) {
        this.manager = manager;
        this.name = name;
        this.pageType = PageType.CHEST;
    }

    public MenuBuilder(MenuManager manager, String name, PageType pageType) {
        this.manager = manager;
        this.name = name;
        this.pageType = pageType;
    }

    public boolean isPlayerSlot(int rawSlot) {
        return !((this.pageType.getSize() - rawSlot) > 0);
    }

    public void setMenuName(String str) {
        this.name = str;
    }


    @Override
    public boolean equals(Object obj) {
       return ((MenuBuilder)obj).type() == type();
    }

    @Override
    public String getName() {
        return name;
    }


    @Override
    public ItemStack[] getContent() {
        final ItemStack[][] content = getContent(this.getPageType().getBlank2DArray());
        return this.getPageType().flatten(content);
    }

    @Override
    public PageType getPageType() {
        return pageType;
    }

    public abstract ItemStack[][] getContent(ItemStack[][] content);

    public abstract MenuType type();

    @Override
    public int hashCode() {
        return type().hashCode();
    }

}
