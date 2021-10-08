package devjluvisi.mlb.menus.admin;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import devjluvisi.mlb.menus.MenuBuilder;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.util.MenuItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Random;

public class ConfirmMenu extends MenuBuilder  {

    private MenuType returnType;
    private ConfirmAction action;
    private LuckyBlock lb;
    private LuckyBlockDrop lbDrop;

    public enum ConfirmAction {
        REMOVE_LUCKY_BLOCK, REMOVE_ALL_DROPS, DELETE_DROP
    }

    public ConfirmMenu(MenuManager manager) {
        super(manager, "Confirm Action");
    }

    public ConfirmMenu request(ConfirmAction action) {
        this.action = action;
        return this;
    }

    public ConfirmMenu returnTo(MenuType type) {
        this.returnType = type;
        return this;
    }

    public void init() {
        try {
            this.lb = manager.getMenuData().getLuckyBlock();
        } catch (NullPointerException | IllegalArgumentException ignored) {
        }
        try {
            this.lbDrop = manager.getMenuData().getDrop();
        } catch (NullPointerException | IllegalArgumentException ignored) {
        }

    }


    @Override
    public ItemStack[][] getContent(ItemStack[][] content) {
        init();
        Arrays.fill(content[0], MenuItem.blackPlaceholder().asItem());
        Arrays.fill(content[1], MenuItem.blackPlaceholder().asItem());
        Arrays.fill(content[2], MenuItem.blackPlaceholder().asItem());

        content[1][2] = new MenuItem().with(Material.GREEN_TERRACOTTA).with("&2&lConfirm")
                .addLine("&7Press to confirm this action.")
                .addLine("&8" + action.name()).asItem();
        content[1][6] = new MenuItem().with(Material.RED_TERRACOTTA).with("&c&lCancel")
                .addLine("&7Press to cancel this action.")
                .addLine("&8" + action.name()).asItem();

        return content;
    }

    public void goBack() {
        if(returnType == MenuType.EMPTY) {
            manager.getPlayer().getOpenInventory().close();
            return;
        }
        manager.open(manager.getPlayer(), returnType);
    }

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
        if ((itemStack == null) || itemStack.getType().equals(Material.GRAY_STAINED_GLASS_PANE)
                || this.isPlayerSlot(slot)) {
            return;
        }
        if(itemStack.getType() == Material.RED_TERRACOTTA) {
            goBack();
            return;
        }
        if(action == ConfirmAction.DELETE_DROP) {
            lb.removeDrop(lbDrop);
            manager.getPlayer().sendMessage(ChatColor.GRAY + "Deleted the drop.");
            if(returnType == MenuType.EMPTY) {
                view.close();
                return;
            }
            manager.open(manager.getPlayer(), MenuType.LIST_DROPS);
        }
        if(action == ConfirmAction.REMOVE_ALL_DROPS) {
            lb.getDroppableItems().clear();
            manager.getPlayer().sendMessage(ChatColor.GRAY + "Deleted All Drops for " + lb.getInternalName() + ".");
            if(returnType == MenuType.EMPTY) {
                view.close();
                return;
            }
            manager.open(manager.getPlayer(), MenuType.LIST_DROPS);
        }
        if(action == ConfirmAction.REMOVE_LUCKY_BLOCK) {
            manager.getPlugin().getLuckyBlocks().remove(lb);
            manager.getPlayer().sendMessage(ChatColor.GRAY + "Deleted the lucky block.");
            view.close();
        }

    }

    @Override
    public MenuType type() {
        return MenuType.CONFIRM;
    }
}
