package devjluvisi.mlb.menus;

import devjluvisi.mlb.api.gui.SimpleMenu;
import devjluvisi.mlb.api.gui.pages.PageType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.inventory.ItemStack;

public abstract class MenuBuilder extends SimpleMenu {

    private final PageType pageType;
    protected MenuManager manager;
    private String name;

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

    @Override
    public int hashCode() {
        return type().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return ((MenuBuilder) obj).type() == type();
    }

    public abstract MenuType type();

}
