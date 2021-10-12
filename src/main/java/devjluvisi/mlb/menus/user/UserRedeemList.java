package devjluvisi.mlb.menus.user;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.menus.MenuBuilder;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuResource;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.util.MenuItem;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public class UserRedeemList extends MenuBuilder {

    public UserRedeemList(MenuManager manager) {
        super(manager, "Redeem List", PageType.CHEST_PLUS);
    }

    @Override
    public ItemStack[][] getContent(ItemStack[][] content) {
        Arrays.fill(content[0], MenuItem.blackPlaceholder().asItem());
        Arrays.fill(content[3], MenuItem.blackPlaceholder().asItem());
        content[1][0] = content[2][0] = content[1][8] = content[2][8] = MenuItem.blackPlaceholder().asItem();

        int lbIndex = 0;
        for (int i = 1; i < 3; i++) {
            for (int j = 1; j < 8; j++) {
                if (lbIndex == manager.getPlugin().getLuckyBlocks().size()) {
                    break;
                }
                content[i][j] = manager.getPlugin().getLuckyBlocks().get(lbIndex).asItem(manager.getPlugin(), 1);
                lbIndex++;
            }
        }

        return content;
    }

    @Override
    public MenuType type() {
        return MenuType.USER_REDEEM_LIST;
    }

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
        if (itemStack == null) return;
        if (!itemStack.hasItemMeta()) return;
        // search lucky block list for any lucky block who has an item name that matches the name of the item the user clicked on.
        LuckyBlock lb = manager.getPlugin().getLuckyBlocks().stream().filter(e -> e.getName().equals(Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName())).findFirst().orElse(null);
        if (lb == null) {
            return;
        }
        if (isPlayerSlot(slot)) return;
        manager.setMenuData(new MenuResource().with(lb));
        manager.open(manager.getPlayer(), MenuType.USER_REDEEM);
    }
}
