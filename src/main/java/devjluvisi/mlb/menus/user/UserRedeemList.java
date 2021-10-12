package devjluvisi.mlb.menus.user;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.menus.MenuBuilder;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuType;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class UserRedeemList extends MenuBuilder {

    public UserRedeemList(MenuManager manager) {
        super(manager, "Redeem List", PageType.CHEST_PLUS);
    }

    @Override
    public ItemStack[][] getContent(ItemStack[][] content) {
        return content;
    }

    @Override
    public MenuType type() {
        return MenuType.USER_REDEEM_LIST;
    }

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {

    }
}
