package devjluvisi.mlb.menus.admin;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import devjluvisi.mlb.helper.Util;
import devjluvisi.mlb.menus.MenuBuilder;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.util.MenuItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class EditStructureMenu extends MenuBuilder {

    private static final byte EXIT_BUTTON = 16;
    private static final byte ADD_EDIT_STRUCTURE = 12;
    private static final byte DELETE_STRUCTURE = 14;
    private LuckyBlock lb;
    private LuckyBlockDrop lbDrop;

    public EditStructureMenu(MenuManager manager) {
        super(manager, "Structure for Drop #" + manager.getMenuData().getLuckyBlock().getDroppableItems().indexOf(manager.getMenuData().getDrop()), PageType.CHEST);
        this.lb = manager.getMenuData().getLuckyBlock();
        this.lbDrop = manager.getMenuData().getDrop();
    }

    @Override
    public ItemStack[][] getContent(ItemStack[][] content) {
        for (int i = 0; i < getPageType().getRow(); i++) {
            Arrays.fill(content[i], MenuItem.whitePlaceholder().asItem());
        }

        content[1][1] = new MenuItem().with(Material.OAK_SIGN).with("&3Structure &7#" + lb.getDroppableItems().indexOf(lbDrop))
                .addAllLine(Util.descriptionToLore(ChatColor.GRAY + "Click on a GUI option to configure the structure for this drop. Editing a structure will teleport you to the structure world.").toArray(String[]::new)).asItem();

        if (lbDrop.hasStructure()) {
            content[1][3] = new MenuItem(MenuItem.SpecialItem.EDIT_STRUCTURE).asItem();
        } else {
            content[1][3] = new MenuItem(MenuItem.SpecialItem.NEW_STRUCTURE).asItem();
        }

        if (lbDrop.hasStructure()) {
            content[1][5] = new MenuItem(MenuItem.SpecialItem.DELETE_STRUCTURE).asItem();
        } else {
            content[1][5] = new MenuItem(MenuItem.SpecialItem.DELETE_STRUCTURE)
                    .addLine("\n")
                    .addLine("&cAdd a structure before deleting one.").asItem();
        }
        content[1][7] = new MenuItem(MenuItem.SpecialItem.EXIT_BUTTON).asItem();
        return content;
    }

    @Override
    public MenuType type() {
        return MenuType.EDIT_STRUCTURE;
    }

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
        switch (slot) {
            //TODO: Add to confirm menu.
            case EXIT_BUTTON -> {
                manager.regress(view);
            }
            case ADD_EDIT_STRUCTURE -> {
                if (manager.getPlugin().getServerDropStructure().hasEditingPlayer()) {
                    manager.getPlayer().sendMessage(ChatColor.RED + "There is currently a player editing a structure.");
                    return;
                }
                view.close();
                manager.getPlugin().getServerDropStructure().update(lb, lbDrop, manager.getPlayer());
            }
            case DELETE_STRUCTURE -> {
                if (!lbDrop.hasStructure()) {
                    return;
                }
                lbDrop.setStructure(null);
            }
        }
    }

}
