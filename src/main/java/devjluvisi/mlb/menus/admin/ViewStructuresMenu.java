package devjluvisi.mlb.menus.admin;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.menus.MenuBuilder;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.util.MenuItem;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public class ViewStructuresMenu extends MenuBuilder {

    private static final byte DROP_VIEW_AMOUNT = 14;
    private static final byte NEXT_PAGE = 34;
    private static final byte PREVIOUS_PAGE = 33;
    private LuckyBlock lb;
    private int dropIndex;
    public ViewStructuresMenu(MenuManager manager) {
        super(manager, "Edit Structures for: " + manager.getMenuData().getLuckyBlock().getInternalName(), PageType.CHEST_PLUS_PLUS);
        this.dropIndex = 0;
        this.lb = manager.getMenuData().getLuckyBlock();
    }

    @Override
    public ItemStack[][] getContent(ItemStack[][] content) {

        for (int i = 0; i < getPageType().getRow(); i++) {
            Arrays.fill(content[i], MenuItem.blackPlaceholder().asItem());
        }

        int index = dropIndex;
        for (int i = 1; i < 3; i++) {
            for (int j = 1; j < 8; j++) {
                if (lb.getDroppableItems().size() <= index) {
                    content[i][j] = new MenuItem().with(Material.GRAY_STAINED_GLASS_PANE).with("&7No Drop")
                            .addLine("&cYou do not have a drop")
                            .addLine("&cfor this slot.")
                            .asItem();
                } else {
                    content[i][j] = new MenuItem().with(Material.GREEN_STAINED_GLASS_PANE).with("&7Drop: &d" + index)
                            .addLine("&7Click to edit the")
                            .addLine("&7structure for drop #" + index + ".").asItem();
                }

                index++;
            }
        }


        content[3][2] = content[3][3] = content[3][4] = content[3][5] = MenuItem.whitePlaceholder().asItem();
        content[3][1] = new MenuItem().with(Material.OAK_SIGN).with("Structure Menu").asItem();

        content[3][7] = new MenuItem().with(Material.ARROW).with("&fNext Page")
                .addLine("&7Go to page &6" + (dropIndex + 1) + ".").asItem();
        if (lb.getDroppableItems().size() <= dropIndex + DROP_VIEW_AMOUNT) {
            content[3][7] = new MenuItem().with(Material.ARROW).with("&fNext Page")
                    .addLine("&7You are on the last page.").asItem();
        } else {
            content[3][7] = new MenuItem().with(Material.ARROW).with("&fNext Page")
                    .addLine("&7Go to page &6" + (((dropIndex + DROP_VIEW_AMOUNT) / DROP_VIEW_AMOUNT) + 1) + ".").asItem();
        }
        if (dropIndex == 0) {
            content[3][6] = new MenuItem().with(Material.FEATHER).with("&fPrevious Page")
                    .addLine("&7You are on the first page.").asItem();
        } else {
            content[3][6] = new MenuItem().with(Material.FEATHER).with("&fPrevious Page")
                    .addLine("&7Go to page &6" + (((dropIndex - DROP_VIEW_AMOUNT) / DROP_VIEW_AMOUNT) + 1) + ".").asItem();
        }


        return content;
    }

    @Override
    public MenuType type() {
        return MenuType.STRUCTURE;
    }

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
        if(itemStack == null) {
            return;
        }
        switch (slot) {
            case NEXT_PAGE -> {
                if (lb.getDroppableItems().size() <= dropIndex + DROP_VIEW_AMOUNT) {
                    return;
                }
                dropIndex += DROP_VIEW_AMOUNT;
                view.reopen();
            }
            case PREVIOUS_PAGE -> {
                if (dropIndex == 0) {
                    return;
                }
                dropIndex -= DROP_VIEW_AMOUNT;
                view.reopen();
            }
            default -> {
                if(!(itemStack.hasItemMeta() && Objects.requireNonNull(itemStack.getItemMeta()).hasDisplayName())) {
                    return;
                }
                String name = ChatColor.stripColor(itemStack.getItemMeta().getDisplayName());
                int index = NumberUtils.toInt(StringUtils.split(name, "Drop: ")[0], -1);
                if(index == -1) {
                    return;
                }
                manager.setMenuData(manager.getMenuData().with(lb.getDroppableItems().get(index)));

                manager.open(manager.getPlayer(), MenuType.EDIT_STRUCTURE);
            }
        }
    }
}
