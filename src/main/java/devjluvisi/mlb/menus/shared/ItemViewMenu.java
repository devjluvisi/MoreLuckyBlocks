package devjluvisi.mlb.menus.shared;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.helper.Util;
import devjluvisi.mlb.menus.MenuBuilder;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.exceptions.MenuInvalidException;
import devjluvisi.mlb.menus.util.MenuItem;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ItemViewMenu extends MenuBuilder {

    private static final byte EXIT_SLOT = 7;
    private ItemStack item;

    public ItemViewMenu(MenuManager manager) throws MenuInvalidException {
        super(null);
        throw new MenuInvalidException("Cannot create this type of menu with the parameters provided!");
    }

    public ItemViewMenu(MenuManager manager, ItemStack item) {
        super(manager, "Viewing Item: " + Util.getItemAsString(item), PageType.DISPENSER);
        this.item = item;
    }

    @Override
    public ItemStack[][] getContent(ItemStack[][] content) {
        for (int i = 0; i < getPageType().getRow(); i++) {
            Arrays.fill(content[i], MenuItem.blackPlaceholder().asItem());
        }
        content[1][1] = item;
        if (manager.isIndirectMenu()) {
            content[2][1] = new MenuItem(MenuItem.SpecialItem.EXIT_BUTTON).asItem();
        }
        return content;
    }

    @Override
    public MenuType type() {
        return MenuType.VIEW_ITEM;
    }

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
        if (slot == EXIT_SLOT && manager.isIndirectMenu()) {
            manager.regress();
        }
    }
}
