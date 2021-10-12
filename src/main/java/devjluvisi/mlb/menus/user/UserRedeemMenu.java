package devjluvisi.mlb.menus.user;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.menus.MenuBuilder;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuType;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class UserRedeemMenu extends MenuBuilder {

    private LuckyBlock lb;


    public UserRedeemMenu(MenuManager manager) {
        super(manager, "Redeem Menu", PageType.DOUBLE_CHEST);
    }

    @Override
    public ItemStack[][] getContent(ItemStack[][] content) {
        lb = manager.getMenuData().getLuckyBlock();
        setMenuName("Redeem for: " + lb.getInternalName());

        return content;
    }

    @Override
    public MenuType type() {
        return MenuType.USER_REDEEM;
    }

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {

    }
}
