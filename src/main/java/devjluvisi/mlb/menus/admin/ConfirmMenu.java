package devjluvisi.mlb.menus.admin;

import devjluvisi.mlb.api.gui.MenuView;
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

public class ConfirmMenu extends MenuBuilder {

    private MenuType returnType;
    private ConfirmAction action;
    private LuckyBlock lb;
    private LuckyBlockDrop lbDrop;

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

    @Override
    public ItemStack[][] getContent(ItemStack[][] content) {
        init();
        Arrays.fill(content[0], MenuItem.blackPlaceholder().asItem());
        Arrays.fill(content[1], MenuItem.blackPlaceholder().asItem());
        Arrays.fill(content[2], MenuItem.blackPlaceholder().asItem());

        MenuItem confirm = new MenuItem().with(Material.GREEN_TERRACOTTA).with("&2&lConfirm")
                .addLine("&7Press to confirm this action.")
                .addLine("&d&o" + action.name());

        MenuItem cancel = new MenuItem().with(Material.RED_TERRACOTTA).with("&c&lCancel")
                .addLine("&7Press to cancel this action.")
                .addLine("&d&o" + action.name());

        if (lb != null) {
            confirm.addLine("\n");
            cancel.addLine("\n");

            confirm.addLine("&rLucky Block &8→ &e" + lb.getInternalName());
            cancel.addLine("&rLucky Block &8→ &e" + lb.getInternalName());
        }
        if (lbDrop != null) {
            assert lb != null;
            confirm.addLine("&rDrop &8→ &7#&e" + lb.getDroppableItems().indexOf(lbDrop));
            cancel.addLine("&rDrop &8→ &7#&e" + lb.getDroppableItems().indexOf(lbDrop));
        }

        content[1][2] = confirm.asItem();
        content[1][6] = cancel.asItem();

        return content;
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
    public MenuType type() {
        return MenuType.CONFIRM;
    }

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
        if ((itemStack == null) || itemStack.equals(MenuItem.blackPlaceholder().asItem()) || itemStack.equals(MenuItem.redPlaceholder().asItem()) ||
                this.isPlayerSlot(slot)) {
            return;
        }
        if (itemStack.getType() == Material.RED_TERRACOTTA) {
            goBack(view);
            return;
        }
        if (action == ConfirmAction.DELETE_DROP) {
            lb.removeDrop(lbDrop);
            manager.getPlayer().sendMessage(ChatColor.GRAY + "Deleted the drop.");
            if (returnType == MenuType.EMPTY) {
                view.close();
                return;
            }
            manager.silentOpen(manager.getPlayer(), MenuType.LIST_DROPS);
        }
        if (action == ConfirmAction.REMOVE_ALL_DROPS) {
            lb.getDroppableItems().clear();
            manager.getPlayer().sendMessage(ChatColor.GRAY + "Deleted All Drops for " + lb.getInternalName() + ".");
            manager.regress(view);
        }
        if (action == ConfirmAction.DELETE_BLOCK_DATA_SPECIFIC) {
            manager.getPlugin().getAudit().removeAll(lb);
            manager.getPlayer().sendMessage(ChatColor.DARK_RED + "Deleted all player data for lucky blocks of type " + lb.getInternalName());
            view.close();
        }
        if (action == ConfirmAction.DELETE_BLOCK_DATA_ALL) {
            manager.getPlugin().getAudit().removeAll();
            manager.getPlayer().sendMessage(ChatColor.DARK_RED + "Deleted all player data for lucky blocks of type all.");
            view.close();
        }
        if (action == ConfirmAction.DISABLE_PLUGIN) {
            manager.getPlayer().sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "MoreLuckyBlocks will disable in 5 seconds. All data will be saved.");
            manager.getPlugin().getServer().getScheduler().runTaskLater(manager.getPlugin(), () -> manager.getPlugin().getServer().getPluginManager().disablePlugin(manager.getPlugin()), 20L * 5L);
            view.close();
        }
        if (action == ConfirmAction.REMOVE_LUCKY_BLOCK) {
            manager.getPlugin().getLuckyBlocks().remove(lb);
            manager.getPlayer().sendMessage(ChatColor.GRAY + "Deleted the lucky block.");
            view.close();
        }
        if (action == ConfirmAction.DELETE_EXCHANGE) {
            manager.getPlugin().getExchangesManager().removeExchange(lb.getInternalName());
            manager.getPlayer().sendMessage(ChatColor.GRAY + "Deleted the exchange for " + lb.getInternalName() + ".");
            view.close();
        }

    }

    public void goBack(MenuView view) {
        manager.regress(view);
    }

    public enum ConfirmAction {
        REMOVE_LUCKY_BLOCK, REMOVE_ALL_DROPS, DELETE_DROP, DELETE_BLOCK_DATA_ALL, DELETE_BLOCK_DATA_SPECIFIC,
        DISABLE_PLUGIN, DELETE_EXCHANGE
    }

}
