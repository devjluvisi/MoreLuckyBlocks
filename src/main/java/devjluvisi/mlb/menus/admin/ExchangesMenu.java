package devjluvisi.mlb.menus.admin;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.menus.MenuBuilder;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.util.MenuItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;

public class ExchangesMenu extends MenuBuilder {

    private static final byte MAX_EXCHANGEABLE_ITEMS = 24;
    private LuckyBlock lb;
    private List<ItemStack> items;

    public ExchangesMenu(MenuManager manager) {
        super(manager, "Exchanges", PageType.CHEST_PLUS);
        items = new LinkedList<>();
    }

    @Override
    public ItemStack[][] getContent(ItemStack[][] content) {
        setMenuName("Exchange for: " + manager.getMenuData().getLuckyBlock().getInternalName());
        this.lb = manager.getMenuData().getLuckyBlock();
        if (items.isEmpty()) {
            items = manager.getPlugin().getExchangesManager().getItems(lb.getInternalName());
        }
        int itemIndex = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 2; j < 8; j++) {
                if (itemIndex == items.size()) break;

                content[i][j] = items.get(itemIndex);
                itemIndex++;
            }
        }
        content[0][1] = MenuItem.redPlaceholder().asItem();
        content[1][1] = MenuItem.redPlaceholder().asItem();
        content[2][1] = MenuItem.redPlaceholder().asItem();
        content[3][1] = MenuItem.redPlaceholder().asItem();

        content[0][0] = new MenuItem().with(Material.BOOK).with("&eExchange")
                .addLine("&7Editing exchange for " + lb.getInternalName())
                .addLine("&9Shift+RC to add items to the exchange.")
                .addLine("&3Shift+LC to remove items from the exchange.")
                .addLine("Up to " + MAX_EXCHANGEABLE_ITEMS + " items are allowed.")
                .addLine("&7Make sure to save your progress.")
                .addLine("&7Exchanges can be redeemed with &2/mlb redeem").asItem();
        content[1][0] = new MenuItem().of(MenuItem.SpecialItem.SAVE_EXCHANGE).asItem();
        content[2][0] = new MenuItem().of(MenuItem.SpecialItem.DELETE_EXCHANGE).asItem();
        content[3][0] = new MenuItem().of(MenuItem.SpecialItem.EXIT_BUTTON).asItem();
        content[0][8] = MenuItem.blackPlaceholder().asItem();
        content[1][8] = MenuItem.blackPlaceholder().asItem();
        content[2][8] = MenuItem.blackPlaceholder().asItem();
        content[3][8] = MenuItem.blackPlaceholder().asItem();
        return content;
    }

    @Override
    public MenuType type() {
        return MenuType.VIEW_EXCHANGE;
    }

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
        if (itemStack == null) return;
        if (itemStack.equals(new MenuItem().of(MenuItem.SpecialItem.SAVE_EXCHANGE).asItem()) && !isPlayerSlot(slot)) {
            if (items.isEmpty()) {
                return;
            }
            if (!manager.getPlugin().getExchangesManager().hasExchange(lb.getInternalName())) {
                manager.getPlayer().sendMessage(ChatColor.GREEN + "Created a new exchange for " + ChatColor.YELLOW + lb.getInternalName());
            }
            manager.getPlugin().getExchangesManager().removeExchange(lb.getInternalName());
            manager.getPlugin().getExchangesManager().addExchange(lb.getInternalName(), items);
            manager.getPlayer().sendMessage(ChatColor.GREEN + "Saved.");
            return;
        }
        if (itemStack.equals(new MenuItem().of(MenuItem.SpecialItem.DELETE_EXCHANGE).asItem()) && !isPlayerSlot(slot)) {
            manager.open(manager.getPlayer(), new ConfirmMenu(manager).returnTo(MenuType.VIEW_EXCHANGE).request(ConfirmMenu.ConfirmAction.DELETE_EXCHANGE));
            return;
        }
        if (itemStack.equals(new MenuItem().of(MenuItem.SpecialItem.EXIT_BUTTON).asItem()) && !isPlayerSlot(slot)) {
            manager.regress(view);
            return;
        }
        if (isPlayerSlot(slot)) {
            if (clickType == ClickType.SHIFT_LEFT) {
                if (items.size() == MAX_EXCHANGEABLE_ITEMS) {
                    return;
                }
                items.add(itemStack);
                view.reopen();
            }

        } else if (clickType == ClickType.SHIFT_LEFT && !itemStack.getType().equals(MenuItem.redPlaceholder().asItem().getType())) {
            if (items.size() == 1 && manager.getPlugin().getExchangesManager().hasExchange(lb.getInternalName())) {
                manager.getPlayer().sendMessage(ChatColor.RED + "You must have at least one item to exchange. To delete the exchange click the \"Delete\" button.");
                return;
            }
            items.remove(itemStack);
            view.reopen();
        }
    }
}
