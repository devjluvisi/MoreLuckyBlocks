package devjluvisi.mlb.menus.admin;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import devjluvisi.mlb.menus.MenuBuilder;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.util.MenuItem;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public class DropsMenu extends MenuBuilder {

    private static final byte CHEST_ROW_SIZE = 18;
    /**
     * Index to start from when displaying drops.
     */
    private int beginIndex;
    private LuckyBlock lb;

    public DropsMenu(MenuManager manager) {
        super(manager, "");
        this.beginIndex = 0;
    }

    @Override
    public ItemStack[][] getContent(ItemStack[][] content) {
        this.lb = manager.getMenuData().getLuckyBlock();
        setMenuName("Viewing: " + lb.getInternalName());

        ItemStack noDropSet = new MenuItem(Material.GRAY_STAINED_GLASS_PANE, "&7You have no drop set for this slot.").asItem();
        Arrays.fill(content[0], noDropSet);
        Arrays.fill(content[1], noDropSet);

        int row = 0;
        int col = 0;

        for (int i = beginIndex; i < lb.getDroppableItems().size(); i++) {
            if (col == 9) {
                col = 0;
                row++;
            }
            content[row][col] = new MenuItem().with(Material.GREEN_STAINED_GLASS_PANE).with("&aDrop: " + i)
                    .addLine("&bYou have a drop set for this item!")
                    .addLine("&7&lTotal Drops: &d" + lb.getDroppableItems().get(i).getLoot().size())
                    .addLine("&7&lRarity: &d" + lb.getDroppableItems().get(i).getRarity())
                    .addLine("\n")
                    .addLine("&7&oClick to configure drop contents.").asItem();


            if (row == 2) break;

            col++;
        }
        Arrays.fill(content[2], MenuItem.redPlaceholder().asItem());

        content[2][0] = new MenuItem().of(MenuItem.SpecialItem.EXIT_BUTTON).asItem();
        content[2][2] = new MenuItem().of(MenuItem.SpecialItem.EDIT_ATTRIBUTES).asItem();
        content[2][3] = new MenuItem().of(MenuItem.SpecialItem.ADD_NEW_DROP).asItem();
        content[2][4] = new MenuItem().of(MenuItem.SpecialItem.REMOVE_ALL_DROPS).asItem();
        content[2][5] = new MenuItem().of(MenuItem.SpecialItem.DELETE_LUCKY_BLOCK).asItem();

        final int maxPages = lb.getDroppableItems().size()
                / CHEST_ROW_SIZE + 1;
        final int currPage = (this.beginIndex / CHEST_ROW_SIZE) + 1;
        content[2][7] = new MenuItem().with(Material.FEATHER).with("&ePrevious Page &8(&6" + currPage + "&e/&6" + maxPages + "&8)").addLine("&7Go back to the previous page.").asItem();
        content[2][8] = new MenuItem().with(Material.ARROW).with("&eNext Page &8(&6" + currPage + "&e/&6" + maxPages + "&8)").addLine("&7Go back to the next page.").asItem();
        return content;
    }

    @Override
    public MenuType type() {
        return MenuType.LIST_DROPS;
    }

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
        if (itemStack == null) return;
        if (ChatColor.stripColor(Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName()).startsWith("Drop: ")) {
            final int dropIndex = Integer
                    .parseInt(ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()).split(": ")[1]);
            manager.setMenuData(manager.getMenuData().with(lb.getDroppableItems().get(dropIndex)));
            manager.open(view.getPlayer(), MenuType.LIST_LOOT);
            return;
        }
        if (new MenuItem().of(MenuItem.SpecialItem.ADD_NEW_DROP).equals(itemStack)) {
            lb.getDroppableItems().add(new LuckyBlockDrop());
            manager.setMenuData(manager.getMenuData().with(lb.getDroppableItems().get(lb.getDroppableItems().size() - 1)));
            manager.open(view.getPlayer(), MenuType.EDIT_LOOT);
            return;
        }
        if (new MenuItem().of(MenuItem.SpecialItem.REMOVE_ALL_DROPS).equals(itemStack)) {
            manager.setMenuData(manager.getMenuData().with(lb));
            manager.open(manager.getPlayer(), new ConfirmMenu(manager).request(ConfirmMenu.ConfirmAction.REMOVE_ALL_DROPS).returnTo(type()));
            return;
        }
        if (new MenuItem().of(MenuItem.SpecialItem.DELETE_LUCKY_BLOCK).equals(itemStack)) {
            manager.setMenuData(manager.getMenuData().with(lb));
            manager.open(manager.getPlayer(), new ConfirmMenu(manager).request(ConfirmMenu.ConfirmAction.REMOVE_LUCKY_BLOCK).returnTo(type()));
            return;
        }
        if (new MenuItem(MenuItem.SpecialItem.EDIT_ATTRIBUTES).equals(itemStack)) {
            manager.open(manager.getPlayer(), MenuType.EDIT_LUCKY_BLOCK_ATTRIBUTES);
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
            manager.regress(view);
        }
    }

}
