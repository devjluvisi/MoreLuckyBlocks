package devjluvisi.mlb.menus.user;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.menus.MenuBuilder;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.util.MenuItem;
import devjluvisi.mlb.util.config.files.messages.Message;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public class UserListDrops extends MenuBuilder {

    private static final byte CHEST_ROW_SIZE = 18;
    /**
     * Index to start from when displaying drops.
     */
    private int beginIndex;
    private LuckyBlock lb;

    public UserListDrops(MenuManager manager) {
        super(manager, "");
        this.beginIndex = 0;
    }

    @Override
    public ItemStack[][] getContent(ItemStack[][] content) {
        this.lb = manager.getMenuData().getLuckyBlock();
        setMenuName(Message.VIEWING_GUI_TITLE.format(lb.getInternalName()));

        ItemStack emptyItem = new MenuItem().with(Material.GRAY_STAINED_GLASS_PANE).with(Message.EMPTY.get()).asItem();
        Arrays.fill(content[0], emptyItem);
        Arrays.fill(content[1], emptyItem);

        int row = 0;
        int col = 0;

        for (int i = beginIndex; i < lb.getDroppableItems().size(); i++) {
            if (col == 9) {
                col = 0;
                row++;
            }
            content[row][col] = new MenuItem().with(Material.GREEN_STAINED_GLASS_PANE).with(Message.DROP_TITLE.get() + i)
                    .addLine(Message.LB_TOTAL_LOOT.format(lb.getDroppableItems().get(i).getLoot().size()))
                    .addLine(Message.LB_DROP_RARITY.format(lb.getDroppableItems().get(i).getRarity()))
                    .addLine("\n")
                    .addLine(Message.LB_VIEW_CONTENTS.get()).asItem();

            if (row == 2) break;

            col++;
        }
        Arrays.fill(content[2], MenuItem.redPlaceholder().asItem());

        content[2][0] = new MenuItem().of(MenuItem.SpecialItem.EXIT_BUTTON).asItem();

        final int maxPages = lb.getDroppableItems().size()
                / CHEST_ROW_SIZE + 1;

        final int currPage = (this.beginIndex / CHEST_ROW_SIZE) + 1;

        content[2][7] = new MenuItem().with(Material.FEATHER).with(Message.PREVIOUS_PAGE_TITLE.format(currPage, maxPages)).addLine(Message.PREVIOUS_PAGE_LORE.get()).asItem();
        content[2][8] = new MenuItem().with(Material.ARROW).with(Message.NEXT_PAGE_TITLE.format(currPage, maxPages)).addLine(Message.NEXT_PAGE_LORE.get()).asItem();
        return content;
    }

    @Override
    public MenuType type() {
        return MenuType.USER_LIST_DROPS;
    }

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
        if (itemStack == null) return;
        if (ChatColor.stripColor(Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName()).startsWith(ChatColor.stripColor(Message.DROP_TITLE.get()))) {
            final int dropIndex = Integer
                    .parseInt((itemStack.getItemMeta().getDisplayName()).split(Message.DROP_TITLE.get())[1]);
            manager.setMenuData(manager.getMenuData().with(lb.getDroppableItems().get(dropIndex)));
            manager.open(view.getPlayer(), MenuType.USER_LIST_LOOT);
            return;
        }
        // Back Page Button
        if (itemStack.getType().equals(Material.FEATHER)) {
            if ((this.beginIndex - CHEST_ROW_SIZE) <= 0) {
                this.beginIndex = 0;
            } else {
                this.beginIndex -= CHEST_ROW_SIZE;
            }

            view.reopen();
        }
        // Forward Page Button
        if (itemStack.getType().equals(Material.ARROW)) {
            final int maxPages = (lb.getDroppableItems().size()
                    / CHEST_ROW_SIZE) + 1;
            final int currPage = (this.beginIndex / CHEST_ROW_SIZE) + 1;
            if (currPage == maxPages) {
                return;
            }
            this.beginIndex += CHEST_ROW_SIZE;
            view.reopen();
            return;
        }

        // Exit
        if (new MenuItem().of(MenuItem.SpecialItem.EXIT_BUTTON).equals(itemStack)) {
            manager.open(view.getPlayer(), MenuType.USER_LIST_LUCKY_BLOCKS);
        }
    }

}
